package org.example.clientbank.employer.service;

import lombok.RequiredArgsConstructor;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.EmployerDto;
import org.example.clientbank.employer.db.EmployerRepository;
import org.example.clientbank.employer.status.EmployerStatus;
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
    public Optional<Employer> getEmployerById(Long id) {
        return employerRepository.findById(id);
    }

    @Override
    public void createEmployer(String name, String address) {
        employerRepository.save(new Employer(name, address));
    }

    @Override
    public void deleteById(Long id) {
        employerRepository.deleteById(id);
    }

    @Override
    public EmployerStatus updateEmployer(Employer employer, EmployerDto employerDto) {
        Optional<Employer> employerOptional = getEmployerById(employer.getId());
        List<Employer> allEmployers = employerRepository.findAll();

        if (employerOptional.isPresent()) {
            Employer existingEmployer = employerOptional.get();

            boolean updated = updateEmployerFromDTO(existingEmployer, employerDto);

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
    public boolean updateEmployerFromDTO(Employer employer, EmployerDto employerDto) {
        if (!employer.getName().equals(employerDto.getName())
                || !employer.getAddress().equals(employerDto.getAddress())) {
            employer.setName(employerDto.getName());
            employer.setAddress(employerDto.getAddress());
            return true;
        }
        return false;
    }
}
