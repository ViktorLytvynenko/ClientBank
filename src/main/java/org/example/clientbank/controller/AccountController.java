package org.example.clientbank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.enums.status.AccountStatus;
import org.example.clientbank.model.AddWithdrawFundsModel;
import org.example.clientbank.model.SendFundsModel;
import org.example.clientbank.service.AccountServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> addFunds(@RequestBody AddWithdrawFundsModel addWithdrawFundsModel) {
        log.info("Trying to add funds");

        AccountStatus status = accountService.addFunds(addWithdrawFundsModel.cardNumber(), addWithdrawFundsModel.sum());
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds add successfully.");
            case ACCOUNT_NOT_FOUND -> ResponseEntity.badRequest().body("Account not found.");
            default -> ResponseEntity.badRequest().body("An unexpected error occurred.");
        };
    }

    @PostMapping("/withdraw_funds")
    public ResponseEntity<String> withdrawFunds(@RequestBody AddWithdrawFundsModel addWithdrawFundsModel) {
        log.info("Trying to withdraw funds");

        AccountStatus status = accountService.withdrawFunds(addWithdrawFundsModel.cardNumber(), addWithdrawFundsModel.sum());
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds withdrawn successfully.");
            case INSUFFICIENT_FUNDS -> ResponseEntity.badRequest().body("Insufficient funds.");
            case ACCOUNT_NOT_FOUND -> ResponseEntity.badRequest().body("Account not found.");
            default -> ResponseEntity.badRequest().body("An unexpected error occurred.");
        };
    }

    @PostMapping("/send_funds")
    public ResponseEntity<String> sendFunds(@RequestBody SendFundsModel sendFundsModel) {
        log.info("Trying to send funds");
        AccountStatus status = accountService.sendFunds(sendFundsModel.numberFrom(), sendFundsModel.numberTo(), sendFundsModel.sum());
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds sent successfully.");
            case ACCOUNT_FROM_NOT_FOUND -> ResponseEntity.badRequest().body("Account from not found.");
            case ACCOUNT_TO_NOT_FOUND -> ResponseEntity.badRequest().body("Account to not found.");
            case INSUFFICIENT_FUNDS -> ResponseEntity.badRequest().body("Insufficient funds.");
            default -> ResponseEntity.badRequest().body("An unexpected error occurred.");
        };
    }
}
