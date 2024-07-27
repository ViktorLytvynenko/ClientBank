package org.example.clientbank.employer.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.ResponseMessage;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.EmployerMapper;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.ResponseEmployerDto;
import org.example.clientbank.employer.service.EmployerServiceImpl;
import org.example.clientbank.employer.status.EmployerStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/v1/employers")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://client-bank-front-end.vercel.app"
}, allowedHeaders = "*")
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
    public ResponseEntity<Employer> getCustomerById(@PathVariable long id) {
        log.info("Trying to get employer by id");
        Optional<Employer> employerOptional = employerService.getEmployerById(id);

        return employerOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmployer(@RequestBody RequestEmployerDto requestEmployerDto) {
        log.info("Trying to create new employer");
        Employer employer = EmployerMapper.INSTANCE.employerDtoToEmployer(requestEmployerDto);
        try {
            Employer createdEmployer = employerService.createEmployer(employer);
            return ResponseEntity.ok(EmployerMapper.INSTANCE.employerToEmployerDto(createdEmployer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create employer: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateEmployer(@PathVariable Long id, @Valid @RequestBody RequestEmployerDto requestEmployerDto) {
        log.info("Trying to update employer");
        Optional<Employer> employerOptional = employerService.getEmployerById(id);
        if (employerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
        }

        EmployerStatus status = employerService.updateEmployer(employerOptional.get(), requestEmployerDto);

        return switch (status) {
            case SUCCESS -> ResponseEntity.ok(new ResponseMessage("Employer updated successfully."));
            case NOTHING_TO_UPDATE ->
                    ResponseEntity.ok(new ResponseMessage(EmployerStatus.NOTHING_TO_UPDATE.getMessage()));
            case EMPLOYER_NOT_FOUND ->
                    ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
            default -> ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.UNEXPECTED.getMessage()));
        };
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteById(@PathVariable long id) {
        log.info("Trying to delete employer by id: {}", id);

        try {
            employerService.deleteById(id);
            return ResponseEntity.ok(new ResponseMessage("Employer deleted successfully."));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
        }
    }
}

