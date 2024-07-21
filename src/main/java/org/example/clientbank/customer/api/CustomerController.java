package org.example.clientbank.customer.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.customer.api.dto.CustomerDTO;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.customer.model.CreateAccountByIdModel;
import org.example.clientbank.ResponseMessage;
import org.example.clientbank.customer.service.CustomerServiceImpl;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://client-bank-front-end.vercel.app"
}, allowedHeaders = "*")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        log.info("Trying to get all customers");
        List<Customer> customers = customerService.findAll();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable long id) {
        log.info("Trying to get customer by id");
        Optional<Customer> customerOptional = customerService.getCustomerById(id);

        return customerOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createCustomer(@RequestParam String name, @RequestParam String email, @RequestParam Integer age) {
        log.info("Trying to create new customer");
        try {
            customerService.createCustomer(name, email, age);
            return ResponseEntity.ok(new ResponseMessage("Customer created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Failed to create customer: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        log.info("Trying to update customer");
        Optional<Customer> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));

        }

        CustomerStatus status = customerService.updateCustomer(customerOptional.get(), customerDTO);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok(new ResponseMessage("Customer updated successfully."));
            case NOTHING_TO_UPDATE ->
                    ResponseEntity.ok(new ResponseMessage(CustomerStatus.NOTHING_TO_UPDATE.getMessage()));
            case CUSTOMER_NOT_FOUND ->
                    ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
            default -> ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.UNEXPECTED.getMessage()));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteById(@PathVariable long id) {
        log.info("Попытка удаления клиента по id: {}", id);

        try {
            customerService.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Customer deleted successfully."));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        }
    }

    @PostMapping("/create_account_by_id")
    public ResponseEntity<ResponseMessage> createAccountByCustomerId(@RequestBody CreateAccountByIdModel createAccountByIdModel) {
        log.info("Trying to create account by customer id");
        Currency currency = Currency.valueOf(createAccountByIdModel.currency());
        boolean created = customerService.createAccountByCustomerId(createAccountByIdModel.id(), currency);

        if (created) {
            return ResponseEntity.ok(new ResponseMessage("Account created successfully."));
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        }
    }

    @DeleteMapping("/delete_account_by_id")
    public ResponseEntity<ResponseMessage> deleteAccountByCustomerId(@RequestParam long id, @RequestParam String accountNumber) {
        log.info("Trying to delete account by id");
        CustomerStatus status = customerService.deleteAccountByCustomerId(id, accountNumber);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok(new ResponseMessage("Account was successfully deleted."));
            case CUSTOMER_NOT_FOUND ->
                    ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
            case CARD_NOT_FOUND ->
                    ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CARD_NOT_FOUND.getMessage()));
            default -> ResponseEntity.badRequest().body(new ResponseMessage("Unexpected error occurred."));
        };
    }

    @DeleteMapping("/delete_accounts_by_id")
    public ResponseEntity<ResponseMessage> deleteAccountsByCustomerId(@RequestParam long id) {
        log.info("Trying to delete all accounts by customer id");
        boolean deleted = customerService.deleteAccountsByCustomerId(id);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Accounts deleted successfully for customer with id: " + id));
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        }
    }
}
