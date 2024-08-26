package org.example.clientbank.account.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.account.Account;
import org.example.clientbank.account.api.dto.AccountMapper;
import org.example.clientbank.account.api.dto.ResponseAccountDto;
import org.example.clientbank.account.model.AddWithdrawFundsModel;
import org.example.clientbank.account.model.SendFundsModel;
import org.example.clientbank.account.service.AccountServiceImpl;
import org.example.clientbank.account.status.AccountStatus;
import org.example.clientbank.dto.BaseResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@Log4j2
@RestController
@RequestMapping("/api/v1/accounts")
//@CrossOrigin(origins = {
//        "http://localhost:3000",
//        "http://localhost:3001",
//        "https://client-bank-front-end.vercel.app"
//}, allowedHeaders = "*")
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountService;

    @PostMapping("/add_funds")
    public ResponseEntity<BaseResponseDto<ResponseAccountDto>> addFunds(@Valid @RequestBody AddWithdrawFundsModel addWithdrawFundsModel) throws AccountNotFoundException {
        log.info("Trying to add funds");

        BaseResponseDto<ResponseAccountDto> baseResponseDto = new BaseResponseDto<>();
        Account account = accountService.addFunds(addWithdrawFundsModel.cardNumber(), addWithdrawFundsModel.sum());

        ResponseAccountDto responseAccountDto = AccountMapper.INSTANCE.accountToAccountDto(account);
        baseResponseDto.setDto(responseAccountDto);
        baseResponseDto.setMessage(AccountStatus.ADDED_FUNDS.getMessage());
        return ResponseEntity.ok(baseResponseDto);
    }

    @PostMapping("/withdraw_funds")
    public ResponseEntity<BaseResponseDto<ResponseAccountDto>> withdrawFunds(@Valid @RequestBody AddWithdrawFundsModel addWithdrawFundsModel) throws AccountNotFoundException {
        log.info("Trying to withdraw funds");

        BaseResponseDto<ResponseAccountDto> baseResponseDto = new BaseResponseDto<>();
        Account account = accountService.withdrawFunds(addWithdrawFundsModel.cardNumber(), addWithdrawFundsModel.sum());

        ResponseAccountDto responseAccountDto = AccountMapper.INSTANCE.accountToAccountDto(account);
        baseResponseDto.setDto(responseAccountDto);
        baseResponseDto.setMessage(AccountStatus.WITHDRAW_FUNDS.getMessage());
        return ResponseEntity.ok(baseResponseDto);
    }

    @PostMapping("/send_funds")
    public ResponseEntity<BaseResponseDto<ResponseAccountDto>> sendFunds(@Valid @RequestBody SendFundsModel sendFundsModel) throws AccountNotFoundException {
        log.info("Trying to send funds");

        BaseResponseDto<ResponseAccountDto> baseResponseDto = new BaseResponseDto<>();
        Account account = accountService.sendFunds(sendFundsModel.numberFrom(), sendFundsModel.numberTo(), sendFundsModel.sum());

        ResponseAccountDto responseAccountDto = AccountMapper.INSTANCE.accountToAccountDto(account);
        baseResponseDto.setDto(responseAccountDto);
        baseResponseDto.setMessage(AccountStatus.SEND_FUNDS.getMessage());
        return ResponseEntity.ok(baseResponseDto);
    }
}
