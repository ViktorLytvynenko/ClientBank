package org.example.clientbank.employer.service;

import lombok.RequiredArgsConstructor;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.db.EmployerRepository;
import org.example.clientbank.employer.status.EmployerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return null;
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
        employerRepository.deleteById(id);
    }

    @Override
    public EmployerStatus updateEmployer(Employer employer, RequestEmployerDto requestEmployerDto) {
        Optional<Employer> employerOptional = getEmployerById(employer.getId());

        if (employerOptional.isPresent()) {
            Employer existingEmployer = employerOptional.get();

            boolean updated = updateEmployerFromDTO(existingEmployer, requestEmployerDto);

            if (updated) {
                employerRepository.save(existingEmployer);
                return EmployerStatus.SUCCESS;
            } else {
                return EmployerStatus.NOTHING_TO_UPDATE;
            }
        } else {
            return EmployerStatus.EMPLOYER_NOT_FOUND;
        }
    }

    @Override
    public boolean updateEmployerFromDTO(Employer employer, RequestEmployerDto requestEmployerDto) {
        if (!employer.getName().equals(requestEmployerDto.getName())
                || !employer.getAddress().equals(requestEmployerDto.getAddress())) {
            employer.setName(requestEmployerDto.getName());
            employer.setAddress(requestEmployerDto.getAddress());
            return true;
        }
        return false;
    }
}
