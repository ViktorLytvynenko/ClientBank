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
        "http://localhost:3001",
        "https://client-bank-front-end.vercel.app"
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
            case ACCOUNT_NOT_FOUND -> ResponseEntity.badRequest().body(AccountStatus.ACCOUNT_NOT_FOUND.getMessage());
            default -> ResponseEntity.badRequest().body(AccountStatus.UNEXPECTED.getMessage());
        };
    }

    @PostMapping("/withdraw_funds")
    public ResponseEntity<String> withdrawFunds(@RequestBody AddWithdrawFundsModel addWithdrawFundsModel) {
        log.info("Trying to withdraw funds");

        AccountStatus status = accountService.withdrawFunds(addWithdrawFundsModel.cardNumber(), addWithdrawFundsModel.sum());
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds withdrawn successfully.");
            case INSUFFICIENT_FUNDS -> ResponseEntity.badRequest().body(AccountStatus.INSUFFICIENT_FUNDS.getMessage());
            case ACCOUNT_NOT_FOUND -> ResponseEntity.badRequest().body(AccountStatus.ACCOUNT_NOT_FOUND.getMessage());
            default -> ResponseEntity.badRequest().body(AccountStatus.UNEXPECTED.getMessage());
        };
    }

    @PostMapping("/send_funds")
    public ResponseEntity<String> sendFunds(@RequestBody SendFundsModel sendFundsModel) {
        log.info("Trying to send funds");
        AccountStatus status = accountService.sendFunds(sendFundsModel.numberFrom(), sendFundsModel.numberTo(), sendFundsModel.sum());
        return switch (status) {
            case SUCCESS -> ResponseEntity.ok("Funds sent successfully.");
            case ACCOUNT_FROM_NOT_FOUND -> ResponseEntity.badRequest().body(AccountStatus.ACCOUNT_FROM_NOT_FOUND.getMessage());
            case ACCOUNT_TO_NOT_FOUND -> ResponseEntity.badRequest().body(AccountStatus.ACCOUNT_TO_NOT_FOUND.getMessage());
            case INSUFFICIENT_FUNDS -> ResponseEntity.badRequest().body(AccountStatus.INSUFFICIENT_FUNDS.getMessage());
            default -> ResponseEntity.badRequest().body(AccountStatus.UNEXPECTED.getMessage());
        };
    }
}
