package org.example.clientbank.employer.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.customer.status.CustomerStatus;
import org.example.clientbank.dto.BaseResponseDto;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.EmployerMapper;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.RequestPatchEmployerDto;
import org.example.clientbank.employer.api.dto.ResponseEmployerDto;
import org.example.clientbank.employer.service.EmployerServiceImpl;
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
@RequestMapping("/api/v1/employers")
//@CrossOrigin(origins = {
//        "http://localhost:3000",
//        "http://localhost:3001",
//        "https://client-bank-front-end.vercel.app"
//}, allowedHeaders = "*")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerServiceImpl employerService;

    @GetMapping
    public ResponseEntity<List<ResponseEmployerDto>> findAll() {
        log.info("Trying to get all employers");
        List<ResponseEmployerDto> employers = employerService.findAll().stream()
                .map(EmployerMapper.INSTANCE::employerToEmployerDto).toList();

        if (employers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(employers);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ResponseEmployerDto>> findAllFiltered(@RequestParam(defaultValue = "0") int startPage,
                                                                     @RequestParam(defaultValue = "10") int perPage,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("Trying to get all employers with parameters");
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(startPage, perPage, Sort.by(direction, sortBy));

        List<ResponseEmployerDto> employers = employerService.findAllFiltered(pageable).stream()
                .map(EmployerMapper.INSTANCE::employerToEmployerDto).toList();

        if (employers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(employers);
        }
    }

    @GetMapping("/employer/{id}")
    public ResponseEntity<Optional<ResponseEmployerDto>> getEmployerById(@PathVariable long id) {
        log.info("Trying to get employer by id");

        Optional<Employer> employerOptional = employerService.getEmployerById(id);
        Optional<ResponseEmployerDto> employerDtoOptional = employerService.getEmployerById(id)
                .map(EmployerMapper.INSTANCE::employerToEmployerDto);

        if (employerOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employerDtoOptional);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDto<ResponseEmployerDto>> createEmployer(@RequestBody RequestEmployerDto requestEmployerDto) {
        log.info("Trying to create new employer");

        BaseResponseDto<ResponseEmployerDto> baseResponseDto = new BaseResponseDto<>();
        Employer employer = EmployerMapper.INSTANCE.employerDtoToEmployer(requestEmployerDto);

        try {
            Employer createdEmployer = employerService.createEmployer(employer);
            baseResponseDto.setDto(EmployerMapper.INSTANCE.employerToEmployerDto(createdEmployer));
            baseResponseDto.setMessage(EmployerStatus.SUCCESS.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } catch (Exception e) {
            baseResponseDto.setMessage(CustomerStatus.UNEXPECTED.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponseDto<ResponseEmployerDto>> updateEmployer(@PathVariable Long id, @Valid @RequestBody RequestEmployerDto requestEmployerDto) {
        log.info("Trying to update employer");

        BaseResponseDto<ResponseEmployerDto> baseResponseDto = new BaseResponseDto<>();
        Optional<Employer> employerOptional = employerService.updateEmployer(id, requestEmployerDto);

        if (employerOptional.isPresent()) {
            Employer updatedEmployer = employerOptional.get();
            ResponseEmployerDto responseEmployerDto = EmployerMapper.INSTANCE.employerToEmployerDto(updatedEmployer);
            baseResponseDto.setDto(responseEmployerDto);
            baseResponseDto.setMessage(EmployerStatus.EMPLOYER_UPDATED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } else {
            baseResponseDto.setMessage(EmployerStatus.UNEXPECTED.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @PatchMapping("/patch/{id}")
    public ResponseEntity<BaseResponseDto<ResponseEmployerDto>> patchCustomer(@PathVariable Long id, @Valid @RequestBody RequestPatchEmployerDto requestPatchEmployerDto) throws IllegalAccessException {
        log.info("Trying to patch employer");

        BaseResponseDto<ResponseEmployerDto> baseResponseDto = new BaseResponseDto<>();
        Optional<Employer> employerOptional = employerService.patchEmployer(id, requestPatchEmployerDto);

        if (employerOptional.isPresent()) {
            Employer updatedEmployer = employerOptional.get();
            ResponseEmployerDto responseEmployerDto = EmployerMapper.INSTANCE.employerToEmployerDto(updatedEmployer);
            baseResponseDto.setDto(responseEmployerDto);
            baseResponseDto.setMessage(EmployerStatus.EMPLOYER_UPDATED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } else {
            baseResponseDto.setMessage(EmployerStatus.UNEXPECTED.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponseDto<ResponseEmployerDto>> deleteById(@PathVariable long id) {
        log.info("Trying to delete employer by id: {}", id);

        BaseResponseDto<ResponseEmployerDto> baseResponseDto = new BaseResponseDto<>();

        try {
            employerService.deleteById(id);
            baseResponseDto.setMessage(EmployerStatus.DELETED.getMessage());
            return ResponseEntity.ok(baseResponseDto);
        } catch (EmptyResultDataAccessException e) {
            baseResponseDto.setMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage());
            return ResponseEntity.badRequest().body(baseResponseDto);
        }
    }
}
