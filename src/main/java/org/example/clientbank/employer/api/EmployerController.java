package org.example.clientbank.employer.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.clientbank.ResponseMessage;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.EmployerDto;
import org.example.clientbank.employer.service.EmployerServiceImpl;
import org.example.clientbank.employer.status.EmployerStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/employers")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "https://client-bank-front-end.vercel.app"
}, allowedHeaders = "*")
@RequiredArgsConstructor
public class EmployerController {
    private final EmployerServiceImpl employerService;

    @GetMapping
    public ResponseEntity<List<Employer>> findAll() {
        log.info("Trying to get all employers");
        List<Employer> employers = employerService.findAll();
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
    public ResponseEntity<ResponseMessage> createEmployer(@RequestParam String name, @RequestParam String address) {
        log.info("Trying to create new employer");
        try {
            employerService.createEmployer(name, address);
            return ResponseEntity.ok(new ResponseMessage("Employer created successfully."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Failed to create employer: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseMessage> updateEmployer(@PathVariable Long id, @Valid @RequestBody EmployerDto employerDto) {
        log.info("Trying to update employer");
        Optional<Employer> employerOptional = employerService.getEmployerById(id);
        if (employerOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseMessage(EmployerStatus.EMPLOYER_NOT_FOUND.getMessage()));
        }

        EmployerStatus status = employerService.updateEmployer(employerOptional.get(), employerDto);

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

