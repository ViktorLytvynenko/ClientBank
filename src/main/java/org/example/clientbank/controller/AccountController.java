package org.example.clientbank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.entity.Account;
import org.example.clientbank.enums.status.AccountStatus;
import org.example.clientbank.service.AccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001"
}, allowedHeaders = "*")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountService;

    @PostMapping("/add_funds")
    public ResponseEntity<String> addFunds(@RequestParam String number, @RequestParam double sum) {
        log.info("Trying to add funds");
        Optional<Account> accountOptional = accountService.getAccountByAccountNumber(number);
        if (accountOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Account not found.");
        }
        accountService.addFunds(number, sum);
        return ResponseEntity.ok("Funds add successfully.");
    }

    @PostMapping("/withdraw_funds")
    public ResponseEntity<String> withdrawFunds(@RequestParam String number, @RequestParam double sum) {
        log.info("Trying to withdraw funds");
        AccountStatus status = accountService.withdrawFunds(number, sum);
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds withdrawn successfully.");
            case INSUFFICIENT_FUNDS -> ResponseEntity.badRequest().body("Insufficient funds.");
            case ACCOUNT_NOT_FOUND -> ResponseEntity.badRequest().body("Account not found.");
            default -> ResponseEntity.badRequest().body("An unexpected error occurred.");
        };
    }

    @PostMapping("/send_funds")
    public ResponseEntity<String> sendFunds(@RequestParam String numberFrom,
                                            @RequestParam String numberTo,
                                            @RequestParam double sum) {
        log.info("Trying to send funds");
        AccountStatus status = accountService.sendFunds(numberFrom, numberTo, sum);
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds sent successfully.");
            case ACCOUNT_FROM_NOT_FOUND -> ResponseEntity.badRequest().body("Account from not found.");
            case ACCOUNT_TO_NOT_FOUND -> ResponseEntity.badRequest().body("Account to not found.");
            case INSUFFICIENT_FUNDS -> ResponseEntity.badRequest().body("Insufficient funds.");
            default -> ResponseEntity.badRequest().body("An unexpected error occurred.");
        };
    }
}
