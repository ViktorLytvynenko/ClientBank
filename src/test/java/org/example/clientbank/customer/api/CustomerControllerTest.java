package org.example.clientbank.customer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.api.dto.ResponseAccountDto;
import org.example.clientbank.account.enums.Currency;
import org.example.clientbank.account.status.AccountStatus;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.customer.api.dto.*;
import org.example.clientbank.customer.model.CreateAccountByIdModel;
import org.example.clientbank.customer.service.CustomerServiceImpl;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.ResponseEmployerDto;
import org.example.clientbank.security.SysRole.SysRole;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.example.clientbank.account.enums.Currency.USD;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerServiceImpl customerService;

    @MockBean
    private CustomerMapper customerMapper;

    private final long johnDoeId = 1L;
    private final long janeDoeId = 2L;
    private final long jackDoeId = 3L;
    private Customer johnDoe;
    private Customer janeDoe;
    private Customer jackDoe;

    private ResponseCustomerDto responseJohnDoeDto;
    private ResponseCustomerAllDataDto responseJohnDoeAllDataDto;
    private ResponseAccountDto responseAccountDto;
    private ResponseEmployerDto responseEmployerDto;
    private RequestCustomerDto requestCustomerDto;
    private RequestPatchCustomerDto requestPatchCustomerDto;

    private Account accountJohnDoe;

    private final long googleId = 1L;
    private final long amazonId = 2L;
    private Employer google;
    private Employer amazon;

    private String accessToken;

    @Value("${jwt.secret.access}")
    private String secretAccess;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accountJohnDoe = new Account(USD, johnDoe);
        accountJohnDoe.setId(1L);
        accountJohnDoe.setNumber("123456789");
        accountJohnDoe.setBalance(100.0);

        google = new Employer("Google", "USA, California");
        google.setId(googleId);
        google.setCreatedDate(LocalDateTime.now());
        google.setLastModifiedDate(LocalDateTime.now());

        amazon = new Employer("Amazon", "USA, Nevada");
        amazon.setId(amazonId);
        amazon.setCreatedDate(LocalDateTime.now());
        amazon.setLastModifiedDate(LocalDateTime.now());

        johnDoe = new Customer("John Doe", "johndoe@gmail.com", 35, "qWerty", "+1234567890");
        johnDoe.setId(johnDoeId);
        johnDoe.getAccounts().add(accountJohnDoe);
        johnDoe.getEmployers().add(google);
        johnDoe.getEmployers().add(amazon);
        johnDoe.setCreatedDate(LocalDateTime.now());
        johnDoe.setLastModifiedDate(LocalDateTime.now());


        janeDoe = new Customer("Jane Doe", "janedoe@gmail.com", 31, "qWerty", "+1234555890");
        janeDoe.setId(janeDoeId);
        janeDoe.setCreatedDate(LocalDateTime.now());
        janeDoe.setLastModifiedDate(LocalDateTime.now());

        jackDoe = new Customer("Jack Doe", "jackdoe@gmail.com", 30, "qWerty", "+1235555890");
        jackDoe.setId(jackDoeId);
        jackDoe.setCreatedDate(LocalDateTime.now());
        jackDoe.setLastModifiedDate(LocalDateTime.now());

        responseJohnDoeDto = new ResponseCustomerDto(
                johnDoe.getId(),
                johnDoe.getName(),
                johnDoe.getEmail(),
                johnDoe.getAge(),
                johnDoe.getPhone(),
                johnDoe.getCreatedDate(),
                johnDoe.getLastModifiedDate()
        );

        responseAccountDto = new ResponseAccountDto(
                accountJohnDoe.getId(),
                accountJohnDoe.getNumber(),
                accountJohnDoe.getCurrency().toString(),
                accountJohnDoe.getBalance()
        );

        responseEmployerDto = new ResponseEmployerDto(
                google.getId(),
                google.getName(),
                google.getAddress()
        );

        responseJohnDoeAllDataDto = new ResponseCustomerAllDataDto(
                johnDoe.getId(),
                johnDoe.getName(),
                johnDoe.getEmail(),
                johnDoe.getAge(),
                johnDoe.getPhone(),
                johnDoe.getCreatedDate(),
                johnDoe.getLastModifiedDate(),
                johnDoe.getAccounts().stream()
                        .map(a -> new ResponseAccountDto(a.getId(), a.getNumber(), a.getCurrency().toString(), a.getBalance()))
                        .toList(),
                johnDoe.getEmployers().stream()
                        .map(e -> new ResponseEmployerDto(e.getId(), e.getName(), e.getAddress()))
                        .toList()
        );

        requestCustomerDto = new RequestCustomerDto(
                johnDoe.getName(),
                johnDoe.getEmail(),
                johnDoe.getAge(),
                johnDoe.getPhone(),
                johnDoe.getPassword()
        );

        requestPatchCustomerDto = new RequestPatchCustomerDto(
                johnDoe.getName(),
                johnDoe.getEmail(),
                johnDoe.getAge(),
                johnDoe.getPhone()
        );

        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        accessToken = Jwts.builder()
                .setSubject("admin")
                .setExpiration(accessExpiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccess)))
                .claim("roles", new ArrayList<SysRole>())
                .claim("userName", "admin")
                .compact();
    }

    @Test
    public void findAll() throws Exception {
        List<Customer> customers = Arrays.asList(johnDoe, janeDoe);
        when(customerService.findAll()).thenReturn(customers);
        mockMvc.perform(get("/api/v1/customers")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void findAllDataAboutCustomers() throws Exception {
        when(customerService.findAll()).thenReturn(Arrays.asList(johnDoe, janeDoe));
        when(customerMapper.customerToCustomerAllDataDto(johnDoe)).thenReturn(responseJohnDoeAllDataDto);

        mockMvc.perform(get("/api/v1/customers/all_data")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // Ensure there are 2 customers

                .andExpect(jsonPath("$[0].id", Matchers.is(responseJohnDoeAllDataDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", Matchers.is(responseJohnDoeAllDataDto.getName())))
                .andExpect(jsonPath("$[0].email", Matchers.is(responseJohnDoeAllDataDto.getEmail())))
                .andExpect(jsonPath("$[0].age", Matchers.is(responseJohnDoeAllDataDto.getAge())))
                .andExpect(jsonPath("$[0].phone", Matchers.is(responseJohnDoeAllDataDto.getPhone())))

                .andExpect(jsonPath("$[0].accounts", hasSize(1)))
                .andExpect(jsonPath("$[0].accounts[0].id", Matchers.is(responseAccountDto.getId().intValue())))
                .andExpect(jsonPath("$[0].accounts[0].number", Matchers.is(responseAccountDto.getNumber())))
                .andExpect(jsonPath("$[0].accounts[0].currency", Matchers.is(responseAccountDto.getCurrency())))
                .andExpect(jsonPath("$[0].accounts[0].balance", Matchers.is(responseAccountDto.getBalance())))

                .andExpect(jsonPath("$[0].employers", hasSize(2)))
                .andExpect(jsonPath("$[0].employers[0].id", Matchers.is(responseEmployerDto.getId().intValue())))
                .andExpect(jsonPath("$[0].employers[0].name", Matchers.is(responseEmployerDto.getName())))
                .andExpect(jsonPath("$[0].employers[0].address", Matchers.is(responseEmployerDto.getAddress())));
    }

    @Test
    void findAllShortened() throws Exception {
        List<Customer> customers = Arrays.asList(johnDoe, janeDoe);

        when(customerService.findAll()).thenReturn(customers);
        mockMvc.perform(get("/api/v1/customers/shortened")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void findAllFiltered() throws Exception {
        Pageable pageable = PageRequest.of(0, 3);
        List<Customer> expectedCustomers = Arrays.asList(johnDoe, janeDoe);
        Page<Customer> customerPage = new PageImpl<>(expectedCustomers, pageable, expectedCustomers.size());

        when(customerService.findAllFiltered(any(Pageable.class))).thenReturn(customerPage);

        mockMvc.perform(get("/api/v1/customers/filter?page=0&size=10")
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", Matchers.is(expectedCustomers.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].name", Matchers.is(expectedCustomers.get(0).getName())))
                .andExpect(jsonPath("$[0].email", Matchers.is(expectedCustomers.get(0).getEmail())))
                .andExpect(jsonPath("$[0].age", Matchers.is(expectedCustomers.get(0).getAge())))
                .andExpect(jsonPath("$[0].phone", Matchers.is(expectedCustomers.get(0).getPhone())))
                .andExpect(jsonPath("$[1].id", Matchers.is(expectedCustomers.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].name", Matchers.is(expectedCustomers.get(1).getName())))
                .andExpect(jsonPath("$[1].email", Matchers.is(expectedCustomers.get(1).getEmail())))
                .andExpect(jsonPath("$[1].age", Matchers.is(expectedCustomers.get(1).getAge())))
                .andExpect(jsonPath("$[1].phone", Matchers.is(expectedCustomers.get(1).getPhone())));
    }

    @Test
    void getCustomerById() throws Exception {
        when(customerService.getCustomerById(johnDoeId)).thenReturn(Optional.of(johnDoe));
        when(customerMapper.customerToCustomerDto(johnDoe)).thenReturn(responseJohnDoeDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/customer/{id}", johnDoeId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(responseJohnDoeDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(responseJohnDoeDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(responseJohnDoeDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(responseJohnDoeDto.getAge())));
    }

    @Test
    void createCustomer() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(johnDoe);
        when(customerMapper.customerToCustomerDto(johnDoe)).thenReturn(responseJohnDoeDto);

        String requestJson = new ObjectMapper().writeValueAsString(requestCustomerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/create")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(CustomerStatus.SUCCESS.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.id", Matchers.is(responseJohnDoeDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.name", Matchers.is(responseJohnDoeDto.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.email", Matchers.is(responseJohnDoeDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.age", Matchers.is(responseJohnDoeDto.getAge())));
    }

    @Test
    void updateCustomer() throws Exception {
        when(customerService.updateCustomer(johnDoeId, requestCustomerDto)).thenReturn(Optional.ofNullable(johnDoe));
        when(customerMapper.customerToCustomerDto(johnDoe)).thenReturn(responseJohnDoeDto);

        String requestJson = new ObjectMapper().writeValueAsString(requestCustomerDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/update/{id}", johnDoeId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("Customer was successfully updated.")))
                .andExpect(jsonPath("$.dto.id", Matchers.is(responseJohnDoeDto.getId().intValue())))
                .andExpect(jsonPath("$.dto.name", Matchers.is(responseJohnDoeDto.getName())))
                .andExpect(jsonPath("$.dto.email", Matchers.is(responseJohnDoeDto.getEmail())))
                .andExpect(jsonPath("$.dto.age", Matchers.is(responseJohnDoeDto.getAge())));
    }

    @Test
    void patchCustomer() throws Exception {
        when(customerService.patchCustomer(johnDoeId, requestPatchCustomerDto)).thenReturn(Optional.ofNullable(johnDoe));
        when(customerMapper.customerToCustomerDto(johnDoe)).thenReturn(responseJohnDoeDto);

        String requestJson = new ObjectMapper().writeValueAsString(requestCustomerDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/customers/patch/{id}", johnDoeId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is(CustomerStatus.CUSTOMER_UPDATED.getMessage())))
                .andExpect(jsonPath("$.dto.id", Matchers.is(responseJohnDoeDto.getId().intValue())))
                .andExpect(jsonPath("$.dto.name", Matchers.is(responseJohnDoeDto.getName())))
                .andExpect(jsonPath("$.dto.email", Matchers.is(responseJohnDoeDto.getEmail())))
                .andExpect(jsonPath("$.dto.age", Matchers.is(responseJohnDoeDto.getAge())));
    }

    @Test
    void deleteById() throws Exception {
        when(customerService.getCustomerById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/delete/{id}", johnDoeId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is(CustomerStatus.DELETED.getMessage())));

        verify(customerService).deleteById(johnDoeId);
    }

    @Test
    void createAccountByCustomerId() throws Exception {
        when(customerService.getCustomerById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        Account createdAccount = new Account();
        createdAccount.setId(5L);
        createdAccount.setCurrency(Currency.EUR);

        when(customerService.createAccountByCustomerId(johnDoeId, Currency.EUR)).thenReturn(createdAccount);

        CreateAccountByIdModel requestModel = new CreateAccountByIdModel(johnDoeId, "EUR");
        String requestJson = new ObjectMapper().writeValueAsString(requestModel);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/create_account_by_id")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dto.id").value(createdAccount.getId()))
                .andExpect(jsonPath("$.dto.currency").value("EUR"));

        verify(customerService).createAccountByCustomerId(johnDoeId, Currency.EUR);
    }

    @Test
    void deleteAccountByCustomerId() throws Exception {
        when(customerService.deleteAccountByCustomerId(johnDoeId, "123456789"))
                .thenReturn(CustomerStatus.SUCCESS);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/delete_account_by_id")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("id", String.valueOf(johnDoeId))
                        .param("accountNumber", "123456789")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AccountStatus.ACCOUNT_DELETED.getMessage()));

        verify(customerService).deleteAccountByCustomerId(johnDoeId, "123456789");
    }

    @Test
    void deleteAccountsByCustomerId() throws Exception {
        when(customerService.deleteAccountsByCustomerId(johnDoeId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/customers/delete_accounts_by_id")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("id", String.valueOf(johnDoeId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AccountStatus.ACCOUNTS_DELETED.getMessage()));

        verify(customerService).deleteAccountsByCustomerId(johnDoeId);
    }

    @Test
    void addEmployerToCustomer() throws Exception {
        when(customerService.getCustomerById(johnDoeId)).thenReturn(Optional.of(johnDoe));

        Employer facebook = new Employer("Facebook", "USA, Nevada");
        facebook.setId(3L);
        facebook.setCreatedDate(LocalDateTime.now());
        facebook.setLastModifiedDate(LocalDateTime.now());

        when(customerService.addEmployerToCustomer(johnDoeId, facebook.getId())).thenReturn(CustomerStatus.SUCCESS);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/customer/add_employer")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("customerId", String.valueOf(johnDoeId))
                        .param("employerId", String.valueOf(facebook.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(CustomerStatus.SUCCESS.getMessage()));
    }

    @Test
    void removeEmployerFromCustomer() throws Exception {
        when(customerService.getCustomerById(johnDoeId)).thenReturn(Optional.of(johnDoe));
        when(customerService.removeEmployerFromCustomer(johnDoeId, amazon.getId())).thenReturn(CustomerStatus.SUCCESS);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/customers/customer/remove_employer")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("customerId", String.valueOf(johnDoeId))
                        .param("employerId", String.valueOf(amazon.getId()))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(CustomerStatus.SUCCESS.getMessage()));
    }
}