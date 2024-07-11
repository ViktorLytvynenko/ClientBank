package org.example.clientbank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.entity.Customer;
import org.example.clientbank.model.CreateAccountByIdModel;
import org.example.clientbank.model.CreateAccountModel;
import org.example.clientbank.model.DeleteAccountModel;
import org.example.clientbank.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001"
}, allowedHeaders = "*")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        log.info("Trying to get all customers");
        return customerService.findAll();
    }

    @GetMapping("/customer/{id}")
    public Optional<Customer> getCustomerById(@PathVariable long id) {
        log.info("Trying to get customer by id");
        return customerService.getCustomerById(id);
    }

    @PostMapping("/customer")
    public Optional<Customer> getCustomerByCustomer(@RequestBody Customer customer) {
        log.info("Trying to get customer by customer");
        return customerService.getCustomerByCustomer(customer);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestParam String name, @RequestParam String email, @RequestParam Integer age) {
        log.info("Trying to create new customer");
        return customerService.createCustomer(name, email, age);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer) {
        log.info("Trying to update customer");
        return customerService.updateCustomer(customer);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteById(@PathVariable long id) {
        log.info("Trying to delete customer by id");
        return customerService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public boolean deleteByCustomer(@RequestBody Customer customer) {
        log.info("Trying to delete customer by customer");
        return customerService.deleteByCustomer(customer);
    }

    @PostMapping("/create_account_by_id")
    public ResponseEntity<String> createAccountByCustomerId(@RequestBody CreateAccountByIdModel createAccountByIdModel) {
        log.info("Trying to create account by customer id");
        return customerService.createAccountByCustomerId(createAccountByIdModel.id(), createAccountByIdModel.currency());
    }

    @PostMapping("/create_account")
    public ResponseEntity<String> createAccountByCustomer(@RequestBody CreateAccountModel createAccountModel) {
        log.info("Trying to create account by customer");
        return customerService.createAccountByCustomer(createAccountModel.customer(), createAccountModel.currency());
    }

    @DeleteMapping("/delete_account_by_id")
    public ResponseEntity<String> deleteAccountByCustomerId(@RequestParam long id, @RequestParam String accountNumber) {
        log.info("Trying to delete account by id");
        return customerService.deleteAccountByCustomerId(id, accountNumber);
    }

    @DeleteMapping("/delete_account")
    public ResponseEntity<String> deleteAccountByCustomer(@RequestBody DeleteAccountModel deleteAccountModel) {
        log.info("Trying to delete account by customer");
        return customerService.deleteAccountByCustomer(deleteAccountModel.customer(), deleteAccountModel.accountNumber());
    }

    @DeleteMapping("/delete_accounts")
    public ResponseEntity<String> deleteAccountsByCustomer(@RequestBody Customer customer) {
        log.info("Trying to delete all accounts by customer");
       return customerService.deleteAccountsByCustomer(customer);
    }

    @DeleteMapping("/delete_accounts_by_id")
    public ResponseEntity<String> deleteAccountsByCustomerId(@RequestParam long id) {
        log.info("Trying to delete all accounts by customer id");
        return customerService.deleteAccountsByCustomerId(id);
    }
}
