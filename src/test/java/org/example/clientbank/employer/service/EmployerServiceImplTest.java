package org.example.clientbank.employer.service;

import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.RequestPatchEmployerDto;
import org.example.clientbank.employer.db.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerServiceImplTest {

    @Mock
    private EmployerRepository employerRepository;

    @InjectMocks
    private EmployerServiceImpl employerServiceImpl;

    private final long googleId = 1L;
    private final long amazonId = 2L;
    private Employer google;
    private Employer amazon;

    @BeforeEach
    void setUp() {
        google = new Employer("Google", "USA, California");
        google.setId(googleId);

        amazon = new Employer("Amazon", "USA, Nevada");
        amazon.setId(amazonId);
    }


    @Test
    void findAll() {
        List<Employer> expectedEmployers = List.of(google, amazon);
        when(employerRepository.findAll()).thenReturn(expectedEmployers);

        List<Employer> actualEmployers = employerServiceImpl.findAll();

        assertEquals(expectedEmployers, actualEmployers);
    }

    @Test
    void findAllFiltered() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Employer> expectedEmployers = List.of(google, amazon);
        Page<Employer> employerPage = new PageImpl<>(expectedEmployers, pageable, expectedEmployers.size());

        when(employerRepository.findAll(pageable)).thenReturn(employerPage);
        Page<Employer> result = employerServiceImpl.findAllFiltered(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(expectedEmployers, result.getContent());
    }

    @Test
    void getEmployerById() {
        when(employerRepository.findById(googleId)).thenReturn(Optional.of(google));

        Optional<Employer> actualEmployer = employerServiceImpl.getEmployerById(googleId);

        assertTrue(actualEmployer.isPresent());
        assertEquals(google, actualEmployer.get());
    }

    @Test
    void createEmployer() {
        when(employerRepository.save(google)).thenReturn(google);
        Employer savedEmployer = employerServiceImpl.createEmployer(google);

        assertEquals(google, savedEmployer);
    }

    @Test
    void deleteById() {
        when(employerRepository.existsById(googleId)).thenReturn(true);
        employerServiceImpl.deleteById(googleId);

        verify(employerRepository, times(1)).deleteById(googleId);
    }

    @Test
    void updateEmployer() {
        when(employerRepository.findById(googleId)).thenReturn(Optional.of(google));

        RequestEmployerDto requestEmployerDto = new RequestEmployerDto("Bosch", "Germany");

        Optional<Employer> updatedEmployerOptional = employerServiceImpl.updateEmployer(googleId, requestEmployerDto);
        assertTrue(updatedEmployerOptional.isPresent());

        Employer updatedEmployer = updatedEmployerOptional.get();

        assertNotEquals(google.getName(), "Google");
        assertEquals(google.getName(), updatedEmployer.getName());

        assertNotEquals(google.getAddress(), "USA, California");
        assertEquals(google.getAddress(), updatedEmployer.getAddress());
    }

    @Test
    void patchEmployer() throws IllegalAccessException {
        when(employerRepository.findById(googleId)).thenReturn(Optional.of(google));

        RequestPatchEmployerDto requestPatchEmployerDto = new RequestPatchEmployerDto("Bosch", "Germany");

        Optional<Employer> updatedEmployerOptional = employerServiceImpl.patchEmployer(googleId, requestPatchEmployerDto);
        assertTrue(updatedEmployerOptional.isPresent());

        Employer updatedEmployer = updatedEmployerOptional.get();

        assertNotEquals(google.getName(), "Google");
        assertEquals(google.getName(), updatedEmployer.getName());

        assertNotEquals(google.getAddress(), "USA, California");
        assertEquals(google.getAddress(), updatedEmployer.getAddress());
    }
}