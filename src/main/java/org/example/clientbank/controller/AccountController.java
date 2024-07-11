package org.example.clientbank.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.service.AccountService;
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
    private final AccountService accountService;

    @PostMapping("/add_funds")
    public ResponseEntity<String> addFunds(@RequestParam String number, @RequestParam double sum) {
        log.info("Trying to add funds");
        return accountService.addFunds(number, sum);
    }

    @PostMapping("/withdraw_funds")
    public ResponseEntity<String> withdrawFunds(@RequestParam String number, @RequestParam double sum) {
        log.info("Trying to withdraw funds");
        return accountService.withdrawFunds(number, sum);
    }

    @PostMapping("/send_funds")
    public ResponseEntity<String> sendFunds(@RequestParam String numberFrom,
                                            @RequestParam String numberTo,
                                            @RequestParam double sum) {
        log.info("Trying to send funds");
        return accountService.sendFunds(numberFrom, numberTo, sum);
    }
}
