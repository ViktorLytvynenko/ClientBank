package org.example.clientbank.customer.api;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.api.dto.AccountMapper;
import org.example.clientbank.account.api.dto.ResponseAccountDto;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.account.status.AccountStatus;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.*;
import org.example.clientbank.customer.model.CreateAccountByIdModel;
import org.example.clientbank.customer.service.CustomerServiceImpl;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.dto.BaseResponseDto;
import org.example.clientbank.dto.ResponseMessage;
import org.example.clientbank.employer.status.EmployerStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/v1/customers")
//@CrossOrigin(origins = {
//        "http://localhost:3000",
//        "http://localhost:3001",
//        "https://client-bank-front-end.vercel.app"
//}, allowedHeaders = "*")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;

    @GetMapping("/all_data")
    @JsonView(View.Admin.class)
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
        log.info("Trying to get all customers. Short version");
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
    public ResponseEntity<Optional<ResponseCustomerDto>> getCustomerById(@PathVariable long id) {
        log.info("Trying to get customer by id");
        Optional<ResponseCustomerDto> customerOptional = customerService.getCustomerById(id)
                .map(CustomerMapper.INSTANCE::customerToCustomerDto);

        if (customerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerOptional);
    }

    @PostMapping("/create")
    @JsonView(View.Admin.class)
    public ResponseEntity<BaseResponseDto<ResponseCustomerDto>> createCustomer(@Valid @RequestBody RequestCustomerDto requestCustomerDto) {
        log.info("Trying to create new customer");

        BaseResponseDto<ResponseCustomerDto> baseResponseDto = new BaseResponseDto<>();
        Customer customer = CustomerMapper.INSTANCE.customerDtoToCustomer(requestCustomerDto);
        try {
            Customer createdCustomer = customerService.createCustomer(customer);
            baseResponseDto.setDto(CustomerMapper.INSTANCE.customerToCustomerDto(createdCustomer));
            baseResponseDto.setMessage(CustomerStatus.SUCCESS.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } catch (Exception e) {
            baseResponseDto.setMessage(CustomerStatus.UNEXPECTED.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponseDto<ResponseCustomerDto>> updateCustomer(@PathVariable Long id, @Valid @RequestBody RequestCustomerDto requestCustomerDto) {
        log.info("Trying to update customer");

        BaseResponseDto<ResponseCustomerDto> baseResponseDto = new BaseResponseDto<>();
        Optional<Customer> customerOptional = customerService.updateCustomer(id, requestCustomerDto);

        if (customerOptional.isPresent()) {
            Customer updatedCustomer = customerOptional.get();
            ResponseCustomerDto responseCustomerDto = CustomerMapper.INSTANCE.customerToCustomerDto(updatedCustomer);
            baseResponseDto.setDto(responseCustomerDto);
            baseResponseDto.setMessage(CustomerStatus.CUSTOMER_UPDATED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } else {
            baseResponseDto.setMessage(CustomerStatus.UNEXPECTED.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<BaseResponseDto<ResponseCustomerDto>> patchCustomer(@PathVariable Long id, @Valid @RequestBody RequestPatchCustomerDto requestPatchCustomerDto) throws IllegalAccessException {
        log.info("Trying to patch customer");

        BaseResponseDto<ResponseCustomerDto> baseResponseDto = new BaseResponseDto<>();
        Optional<Customer> customerOptional = customerService.patchCustomer(id, requestPatchCustomerDto);

        if (customerOptional.isPresent()) {
            Customer updatedCustomer = customerOptional.get();
            ResponseCustomerDto responseCustomerDto = CustomerMapper.INSTANCE.customerToCustomerDto(updatedCustomer);
            baseResponseDto.setDto(responseCustomerDto);
            baseResponseDto.setMessage(CustomerStatus.CUSTOMER_UPDATED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } else {
            baseResponseDto.setMessage(CustomerStatus.UNEXPECTED.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponseDto<ResponseCustomerDto>> deleteById(@PathVariable long id) {
        log.info("Trying to delete customer by id: {}", id);

        BaseResponseDto<ResponseCustomerDto> baseResponseDto = new BaseResponseDto<>();

        try {
            customerService.deleteById(id);
            baseResponseDto.setMessage(CustomerStatus.DELETED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } catch (EmptyResultDataAccessException e) {
            baseResponseDto.setMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @PostMapping("/create_account_by_id")
    public ResponseEntity<BaseResponseDto<ResponseAccountDto>> createAccountByCustomerId(@Valid @RequestBody CreateAccountByIdModel createAccountByIdModel) {
        log.info("Trying to create account by customer id");

        BaseResponseDto<ResponseAccountDto> baseResponseDto = new BaseResponseDto<>();
        Currency currency = Currency.valueOf(createAccountByIdModel.currency());
        Account createdAccount = customerService.createAccountByCustomerId(createAccountByIdModel.id(), currency);

        if (createdAccount == null) {
            baseResponseDto.setMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);

        } else {
            ResponseAccountDto responseAccountDto = AccountMapper.INSTANCE.accountToAccountDto(createdAccount);
            baseResponseDto.setDto(responseAccountDto);
            baseResponseDto.setMessage(AccountStatus.ACCOUNT_CREATED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        }
    }

    @DeleteMapping("/delete_account_by_id")
    public ResponseEntity<BaseResponseDto<ResponseCustomerDto>> deleteAccountByCustomerId(@RequestParam long id, @RequestParam String accountNumber) {
        log.info("Trying to delete account by id");

        BaseResponseDto<ResponseCustomerDto> baseResponseDto = new BaseResponseDto<>();
        CustomerStatus status = customerService.deleteAccountByCustomerId(id, accountNumber);

        return switch (status) {
            case SUCCESS -> {
                baseResponseDto.setMessage(AccountStatus.ACCOUNT_DELETED.getMessage());
                yield ResponseEntity.ok(baseResponseDto);
            }
            case CUSTOMER_NOT_FOUND -> {
                baseResponseDto.setMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
                yield ResponseEntity.badRequest().body(baseResponseDto);
            }
            case CARD_NOT_FOUND -> {
                baseResponseDto.setMessage(CustomerStatus.CARD_NOT_FOUND.getMessage());
                yield ResponseEntity.badRequest().body(baseResponseDto);
            }
            default -> {
                baseResponseDto.setMessage(CustomerStatus.UNEXPECTED.getMessage());
                yield ResponseEntity.badRequest().body(baseResponseDto);
            }
        };
    }

    @DeleteMapping("/delete_accounts_by_id")
    public ResponseEntity<BaseResponseDto<ResponseCustomerDto>> deleteAccountsByCustomerId(@RequestParam long id) {
        log.info("Trying to delete all accounts by customer id");

        BaseResponseDto<ResponseCustomerDto> baseResponseDto = new BaseResponseDto<>();
        boolean deleted = customerService.deleteAccountsByCustomerId(id);
        if (deleted) {
            baseResponseDto.setMessage(AccountStatus.ACCOUNTS_DELETED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } else {
            baseResponseDto.setMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @PutMapping("/customer/add_employer")
    public ResponseEntity<ResponseMessage> addEmployerToCustomer(@RequestParam long customerId,
                                                                 @RequestParam long employerId) {
        log.info("Trying to connect customer and employer");
        CustomerStatus status = customerService.addEmployerToCustomer(customerId, employerId);

        if (status == CustomerStatus.SUCCESS) {
            return ResponseEntity.ok(new ResponseMessage(CustomerStatus.SUCCESS.getMessage()));
        } else if (status == CustomerStatus.CUSTOMER_NOT_FOUND) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.CUSTOMER_NOT_FOUND.getMessage()));
        } else if (status == CustomerStatus.EMPLOYER_NOT_FOUND) {
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
        } else if (status == CustomerStatus.EMPLOYER_NOT_FOUND) {
            return ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
        } else if (status == CustomerStatus.NOTHING_TO_UPDATE) {
            return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.NOTHING_TO_UPDATE.getMessage()));
        }

        return ResponseEntity.badRequest().body(new ResponseMessage(CustomerStatus.UNEXPECTED.getMessage()));
    }
}
