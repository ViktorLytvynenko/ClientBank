package org.example.clientbank.customer.api;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.api.dto.AccountMapper;
import org.example.clientbank.account.api.dto.ResponseAccountDto;
import org.example.clientbank.customer.api.dto.*;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.customer.model.CreateAccountByIdModel;
import org.example.clientbank.ResponseMessage;
import org.example.clientbank.customer.service.CustomerServiceImpl;
import org.example.clientbank.employer.status.EmployerStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://client-bank-front-end.vercel.app"
}, allowedHeaders = "*")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @GetMapping("/all_data")
//    @JsonView(View.Admin.class)
    public ResponseEntity<List<ResponseCustomerAllDataDto>> findAllDataAboutCustomers() {
        log.info("Trying to get all data about customers");
        List<ResponseCustomerAllDataDto> customers = customerService.findAll()
                .stream().map(CustomerMapper.INSTANCE::customerToCustomerAllDataDto).toList();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping
    @JsonView(View.Admin.class)
    public ResponseEntity<List<ResponseCustomerDto>> findAll() {
        log.info("Trying to get all customers");
        List<ResponseCustomerDto> customers = customerService.findAll().stream()
                .map(CustomerMapper.INSTANCE::customerToCustomerDto).toList();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/shortened")
    @JsonView(View.Base.class)
    public ResponseEntity<List<ResponseCustomerDto>> findAllShortened() {
        log.info("Trying to get all customers");
        List<ResponseCustomerDto> customers = customerService.findAll().stream()
                .map(CustomerMapper.INSTANCE::customerToCustomerDto).toList();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/filter")
    @JsonView(View.Admin.class)
    public ResponseEntity<List<ResponseCustomerDto>> findAllFiltered(@RequestParam(defaultValue = "0") int startPage,
                                                                     @RequestParam(defaultValue = "10") int perPage,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("Trying to get all customers with parameters");
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(startPage, perPage, Sort.by(direction, sortBy));

        List<ResponseCustomerDto> customers = customerService.findAllFiltered(pageable).stream()
                .map(CustomerMapper.INSTANCE::customerToCustomerDto).toList();
        if (customers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customers);
        }
    }

    @GetMapping("/customer/{id}")
    @JsonView(View.Admin.class)
    public ResponseEntity<?> getCustomerById(@PathVariable long id) {
        log.info("Trying to get customer by id");
        Optional<ResponseCustomerDto> customerOptional = customerService.getCustomerById(id)
                .map(CustomerMapper.INSTANCE::customerToCustomerDto);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
        }
        return ResponseEntity.ok(customerOptional.get());
    }

    @PostMapping("/create")
    @JsonView(View.Admin.class)
    public ResponseEntity<?> createCustomer(@Valid @RequestBody RequestCustomerDto requestCustomerDto) {
        log.info("Trying to create new customer");
        Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(requestCustomerDto);
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.ok(CustomerMapper.INSTANCE.customerToCustomerDto(createdCustomer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create customer: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateCustomer(@PathVariable Long id, @Valid @RequestBody RequestCustomerDto requestCustomerDto) {
        log.info("Trying to update customer");

        CustomerStatus status = customerService.updateCustomer(id, requestCustomerDto);

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
        log.info("Trying to delete customer by id: {}", id);

        try {
            customerService.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Customer deleted successfully."));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        }
    }

    @PostMapping("/create_account_by_id")
    public ResponseEntity<?> createAccountByCustomerId(@Valid @RequestBody CreateAccountByIdModel createAccountByIdModel) {
        log.info("Trying to create account by customer id");
        Currency currency = Currency.valueOf(createAccountByIdModel.currency());
        Account createdAccount = customerService.createAccountByCustomerId(createAccountByIdModel.id(), currency);

        if (createdAccount == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));

        } else {
            ResponseAccountDto responseAccountDto = AccountMapper.INSTANCE.accountToAccountDto(createdAccount);
            return ResponseEntity.ok(responseAccountDto);
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
            default -> ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.UNEXPECTED.getMessage()));
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

    @PutMapping("/customer/add_employer")
    public ResponseEntity<ResponseMessage> addEmployerToCustomer(@RequestParam long customerId,
                                                   @RequestParam long employerId) {
        log.info("Trying to connect customer and employer");
        Enum<?> status = customerService.addEmployerToCustomer(customerId, employerId);


        if (status == CustomerStatus.SUCCESS) {
            return ResponseEntity.ok(new ResponseMessage(CustomerStatus.SUCCESS.getMessage()));
        } else if (status == CustomerStatus.CUSTOMER_NOT_FOUND) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        } else if (status == EmployerStatus.EMPLOYER_NOT_FOUND) {
            return ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
        } else if (status == CustomerStatus.NOTHING_TO_UPDATE) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.NOTHING_TO_UPDATE.getMessage()));
        }

        return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.UNEXPECTED.getMessage()));
    }

    @PutMapping("/customer/remove_employer")
    public ResponseEntity<ResponseMessage> removeEmployerFromCustomer(
            @RequestParam long customerId,
            @RequestParam long employerId) {

        log.info("Trying to disconnect employer from customer");
        Enum<?> status = customerService.removeEmployerFromCustomer(customerId, employerId);

        if (status == CustomerStatus.SUCCESS) {
            return ResponseEntity.ok(new ResponseMessage(CustomerStatus.SUCCESS.getMessage()));
        } else if (status == CustomerStatus.CUSTOMER_NOT_FOUND) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        } else if (status == EmployerStatus.EMPLOYER_NOT_FOUND) {
            return ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
        } else if (status == CustomerStatus.NOTHING_TO_UPDATE) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.NOTHING_TO_UPDATE.getMessage()));
        }

        return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.UNEXPECTED.getMessage()));
    }
}
