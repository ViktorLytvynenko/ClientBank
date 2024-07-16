package org.example.clientbank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.dto.CustomerDTO;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;
import org.example.clientbank.enums.status.CustomerStatus;
import org.example.clientbank.model.CreateAccountByIdModel;
import org.example.clientbank.model.ResponseMessage;
import org.example.clientbank.service.CustomerServiceImpl;
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
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        log.info("Trying to update customer");
        Optional<Customer> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
        }

        CustomerStatus status = customerService.updateCustomer(customerOptional.get(), customerDTO);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Customer updated successfully.");
            case NOTHING_TO_UPDATE -> ResponseEntity.ok(CustomerStatus.NOTHING_TO_UPDATE.getMessage());
            case CUSTOMER_NOT_FOUND -> ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
            default -> ResponseEntity.badRequest().body(CustomerStatus.UNEXPECTED.getMessage());
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        log.info("Trying to delete customer by id");

        boolean deleted = customerService.deleteById(id);

        if (deleted) {
            return ResponseEntity.ok("Customer deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
        }
    }

    @PostMapping("/create_account_by_id")
    public ResponseEntity<String> createAccountByCustomerId(@RequestBody CreateAccountByIdModel createAccountByIdModel) {
        log.info("Trying to create account by customer id");
        Currency currency = Currency.valueOf(createAccountByIdModel.currency());
        boolean created = customerService.createAccountByCustomerId(createAccountByIdModel.id(), currency);

        if (created) {
            return ResponseEntity.ok("Account created successfully.");
        } else {
            return ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
        }
    }

    @DeleteMapping("/delete_account_by_id")
    public ResponseEntity<String> deleteAccountByCustomerId(@RequestParam long id, @RequestParam String accountNumber) {
        log.info("Trying to delete account by id");
        CustomerStatus status = customerService.deleteAccountByCustomerId(id, accountNumber);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Account was successfully deleted.");
            case CUSTOMER_NOT_FOUND -> ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
            case CARD_NOT_FOUND -> ResponseEntity.badRequest().body(CustomerStatus.CARD_NOT_FOUND.getMessage());
            default -> ResponseEntity.badRequest().body("Unexpected error occurred.");
        };
    }

    @DeleteMapping("/delete_accounts_by_id")
    public ResponseEntity<String> deleteAccountsByCustomerId(@RequestParam long id) {
        log.info("Trying to delete all accounts by customer id");
        boolean deleted = customerService.deleteAccountsByCustomerId(id);
        if (deleted) {
            return ResponseEntity.ok("Accounts deleted successfully for customer with id: " + id);
        } else {
            return ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
        }
    }
}
