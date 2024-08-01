package org.example.clientbank.employer.service;

import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.RequestPatchEmployerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployerService {

    List<Employer> findAll();

    Page<Employer> findAllFiltered(Pageable pageable);

    Optional<Employer> getEmployerById(Long id);

    Employer createEmployer(Employer employer);

    void deleteById(Long id);

    Optional<Employer> updateEmployer(Long id, RequestEmployerDto requestEmployerDto);

    Optional<Employer> patchEmployer(Long id, RequestPatchEmployerDto requestPatchEmployerDto) throws IllegalAccessException;
}
