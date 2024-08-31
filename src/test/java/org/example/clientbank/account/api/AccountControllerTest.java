package org.example.clientbank.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.api.dto.AccountMapper;
import org.example.clientbank.account.model.AddWithdrawFundsModel;
import org.example.clientbank.account.model.SendFundsModel;
import org.example.clientbank.account.service.AccountServiceImpl;
import org.example.clientbank.account.status.AccountStatus;
import org.example.clientbank.customer.Customer;
import org.example.clientbank.security.SysRole.SysRole;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static org.example.clientbank.account.enums.Currency.USD;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountService;

    @MockBean
    private AccountMapper accountMapper;

    private Account firstAccount;
    private Account secondAccount;

    private String accessToken;

    @Value("${jwt.secret.access}")
    private String secretAccess;

    @BeforeEach
    void setUp() {
        Customer johnDoe = new Customer("John Doe", "johndoe@gmail.com", 35, "qWerty", "+1234567890");
        johnDoe.setId(1L);

        firstAccount = new Account();
        firstAccount.setId(1L);
        firstAccount.setCurrency(USD);
        firstAccount.setCustomer(johnDoe);
        firstAccount.setNumber("123456789");
        firstAccount.setBalance(100.0);

        secondAccount = new Account();
        secondAccount.setId(2L);
        secondAccount.setCurrency(USD);
        secondAccount.setCustomer(johnDoe);
        secondAccount.setNumber("555555555");
        secondAccount.setBalance(75.0);

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
    void addFunds() throws Exception {
        AddWithdrawFundsModel model = new AddWithdrawFundsModel(firstAccount.getNumber(), 100.0);

        when(accountService.addFunds(firstAccount.getNumber(), 100.0)).thenReturn(firstAccount);

        String requestJson = new ObjectMapper().writeValueAsString(model);

        mockMvc.perform(post("/api/v1/accounts/add_funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(AccountStatus.ADDED_FUNDS.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.number", Matchers.is(firstAccount.getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.balance", Matchers.is(firstAccount.getBalance())));
    }

    @Test
    void withdrawFunds() throws Exception {
        AddWithdrawFundsModel model = new AddWithdrawFundsModel(firstAccount.getNumber(), 50.0);

        when(accountService.withdrawFunds(firstAccount.getNumber(), 50.0)).thenReturn(firstAccount);

        String requestJson = new ObjectMapper().writeValueAsString(model);

        mockMvc.perform(post("/api/v1/accounts/withdraw_funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(AccountStatus.WITHDRAW_FUNDS.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.number", Matchers.is(firstAccount.getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.balance", Matchers.is(firstAccount.getBalance())));
    }

    @Test
    void sendFunds() throws Exception {
        SendFundsModel model = new SendFundsModel(firstAccount.getNumber(), secondAccount.getNumber(), 50.0);

        when(accountService.sendFunds(firstAccount.getNumber(), secondAccount.getNumber(), 50.0)).thenReturn(firstAccount);

        String requestJson = new ObjectMapper().writeValueAsString(model);

        mockMvc.perform(post("/api/v1/accounts/send_funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.is(AccountStatus.SEND_FUNDS.getMessage())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.number", Matchers.is(firstAccount.getNumber())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.balance", Matchers.is(firstAccount.getBalance())));
    }
}