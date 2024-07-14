package org.example.clientbank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.dto.CustomerDTO;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.enums.Currency;
import org.example.clientbank.enums.status.CustomerStatus;
import org.example.clientbank.model.CreateAccountByIdModel;
import org.example.clientbank.model.CreateAccountModel;
import org.example.clientbank.model.DeleteAccountModel;
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

    @GetMapping("/customer")
    public ResponseEntity<Customer> getCustomerByCustomer(@RequestBody Customer customer) {
        log.info("Trying to get customer by customer");
        Optional<Customer> customerOptional = customerService.getCustomerByCustomer(customer);

        return customerOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestParam String name, @RequestParam String email, @RequestParam Integer age) {
        log.info("Trying to create new customer");
        try {
            customerService.createCustomer(name, email, age);
            return ResponseEntity.ok("Customer created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create customer: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        log.info("Trying to update customer");
        Optional<Customer> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Customer not found.");
        }

        CustomerStatus status = customerService.updateCustomer(customerOptional.get(), customerDTO);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Customer updated successfully.");
            case NOTHING_TO_UPDATE -> ResponseEntity.ok("No changes detected for the customer.");
            case CUSTOMER_NOT_FOUND -> ResponseEntity.badRequest().body("Customer not found.");
            default -> ResponseEntity.badRequest().body("An unexpected error occurred.");
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        log.info("Trying to delete customer by id");

        boolean deleted = customerService.deleteById(id);

        if (deleted) {
            return ResponseEntity.ok("Customer deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByCustomer(@RequestBody Customer customer) {
        log.info("Trying to delete customer by customer");

        boolean deleted = customerService.deleteByCustomer(customer);

        if (deleted) {
            return ResponseEntity.ok("Customer deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Customer not found.");
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
            return ResponseEntity.badRequest().body("Customer not found.");
        }
    }

    @PostMapping("/create_account")
    public ResponseEntity<String> createAccountByCustomer(@RequestBody CreateAccountModel createAccountModel) {
        log.info("Trying to create account by customer");

        boolean created = customerService.createAccountByCustomer(createAccountModel.customer(), createAccountModel.currency());

        if (created) {
            return ResponseEntity.ok("Account created successfully.");
        } else {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
    }

    @DeleteMapping("/delete_account_by_id")
    public ResponseEntity<String> deleteAccountByCustomerId(@RequestParam long id, @RequestParam String accountNumber) {
        log.info("Trying to delete account by id");
        CustomerStatus status = customerService.deleteAccountByCustomerId(id, accountNumber);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Account was successfully deleted.");
            case CUSTOMER_NOT_FOUND -> ResponseEntity.badRequest().body("Customer not found.");
            case CARD_NOT_FOUND -> ResponseEntity.badRequest().body("Card not found.");
            default -> ResponseEntity.badRequest().body("Unexpected error occurred.");
        };
    }

    @DeleteMapping("/delete_account")
    public ResponseEntity<String> deleteAccountByCustomer(@RequestBody DeleteAccountModel deleteAccountModel) {
        log.info("Trying to delete account by customer");
        CustomerStatus status = customerService.deleteAccountByCustomer(deleteAccountModel.customer(), deleteAccountModel.accountNumber());

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Account was successfully deleted.");
            case CUSTOMER_NOT_FOUND -> ResponseEntity.badRequest().body("Customer not found.");
            case CARD_NOT_FOUND -> ResponseEntity.badRequest().body("Account not found.");
            default -> ResponseEntity.badRequest().body("Unexpected error occurred.");
        };
    }

    @DeleteMapping("/delete_accounts")
    public ResponseEntity<String> deleteAccountsByCustomer(@RequestBody Customer customer) {
        log.info("Trying to delete all accounts by customer");
        boolean deleted = customerService.deleteAccountsByCustomer(customer);
        if (deleted) {
            return ResponseEntity.ok("All accounts were successfully deleted.");
        } else {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
    }

    @DeleteMapping("/delete_accounts_by_id")
    public ResponseEntity<String> deleteAccountsByCustomerId(@RequestParam long id) {
        log.info("Trying to delete all accounts by customer id");
        boolean deleted = customerService.deleteAccountsByCustomerId(id);
        if (deleted) {
            return ResponseEntity.ok("Accounts deleted successfully for customer with id: " + id);
        } else {
            return ResponseEntity.badRequest().body("Customer not found.");
        }
    }
}
