package org.example.clientbank.employer.service;

import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.ResponseEmployerDto;
import org.example.clientbank.employer.status.EmployerStatus;

import java.util.List;
import java.util.Optional;

public interface EmployerService {

    List<ResponseEmployerDto> findAll();

    Optional<Employer> getEmployerById(Long id);

    Employer createEmployer(Employer employer);

    void deleteById(Long id);

    EmployerStatus updateEmployer(Employer employer, RequestEmployerDto requestEmployerDto);

    boolean updateEmployerFromDTO(Employer employer, RequestEmployerDto requestEmployerDto);
}
