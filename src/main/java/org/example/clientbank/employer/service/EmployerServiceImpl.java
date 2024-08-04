package org.example.clientbank.employer.service;

import lombok.RequiredArgsConstructor;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.RequestPatchEmployerDto;
import org.example.clientbank.employer.db.EmployerRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;

    @Override
    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    @Override
    public Page<Employer> findAllFiltered(Pageable pageable) {
        return employerRepository.findAll(pageable);
    }

    @Override
    public Optional<Employer> getEmployerById(Long id) {
        return employerRepository.findById(id);
    }

    @Override
    public Employer createEmployer(Employer employer) {
        return employerRepository.save(employer);
    }

    @Override
    public void deleteById(Long id) {
        if (!employerRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Customer not found with id: " + id, 1);
        }
        employerRepository.deleteById(id);
    }

    @Override
    public Optional<Employer> updateEmployer(Long id, RequestEmployerDto requestEmployerDto) {
        Optional<Employer> employerOptional = getEmployerById(id);

        if (employerOptional.isEmpty()) {
            return Optional.empty();
        }

        employerOptional.get().setName(requestEmployerDto.getName());
        employerOptional.get().setAddress(requestEmployerDto.getAddress());

        employerRepository.save(employerOptional.get());
        return employerOptional;
    }

    @Override
    public Optional<Employer> patchEmployer(Long id, RequestPatchEmployerDto requestPatchEmployerDto) throws IllegalAccessException {
        Optional<Employer> employerOptional = getEmployerById(id);
        if (employerOptional.isEmpty()) {
            return Optional.empty();
        }

        Field[] dtoFields = RequestPatchEmployerDto.class.getDeclaredFields();
        Field[] entityFields = Employer.class.getDeclaredFields();

        for (Field dtoField : dtoFields) {
            dtoField.setAccessible(true);
            Object value = dtoField.get(requestPatchEmployerDto);
            if (value != null) {
                String fieldName = dtoField.getName();

                for (Field entityField : entityFields) {
                    if (entityField.getName().equals(fieldName) && entityField.getType().equals(dtoField.getType())) {
                        entityField.setAccessible(true);
                        entityField.set(employerOptional.get(), value);
                        entityField.setAccessible(false);
                        break;
                    }
                }
            }
            dtoField.setAccessible(false);
        }

        employerRepository.save(employerOptional.get());
        return employerOptional;
    }
}
