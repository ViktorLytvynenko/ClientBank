package org.example.clientbank.employer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.clientbank.employer.Employer;
import org.example.clientbank.employer.api.dto.EmployerMapper;
import org.example.clientbank.employer.api.dto.RequestEmployerDto;
import org.example.clientbank.employer.api.dto.ResponseEmployerDto;
import org.example.clientbank.employer.service.EmployerServiceImpl;
import org.example.clientbank.employer.status.EmployerStatus;
import org.example.clientbank.security.SysRole.SysRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class EmployerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployerServiceImpl employerService;

    @MockBean
    private EmployerMapper employerMapper;

    private final long googleId = 1L;
    private final long amazonId = 2L;
    private Employer google;
    private Employer amazon;

    private ResponseEmployerDto responseEmployerDto;
    private RequestEmployerDto requestEmployerDto;

    private String accessToken;

    @Value("${jwt.secret.access}")
    private String secretAccess;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        google = new Employer("Google", "USA, California");
        google.setId(googleId);
        google.setCreatedDate(LocalDateTime.now());
        google.setLastModifiedDate(LocalDateTime.now());

        amazon = new Employer("Amazon", "USA, Nevada");
        amazon.setId(amazonId);
        amazon.setCreatedDate(LocalDateTime.now());
        amazon.setLastModifiedDate(LocalDateTime.now());

        responseEmployerDto = new ResponseEmployerDto(
                google.getId(),
                google.getName(),
                google.getAddress()
        );

        requestEmployerDto = new RequestEmployerDto(
                google.getName(),
                google.getAddress()
        );

        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        accessToken = Jwts.builder()
                .setSubject("admin")
                .setExpiration(accessExpiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretAccess)))
                .claim("roles", new ArrayList<SysRole>())
                .claim("userName", "admin")
                .compact();
    }

    @Test
    void findAll() throws Exception {
        List<Employer> employers = Arrays.asList(google, amazon);
        when(employerService.findAll()).thenReturn(employers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employers")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void getEmployerById() throws Exception {
        when(employerService.getEmployerById(googleId)).thenReturn(Optional.of(google));
        when(employerMapper.employerToEmployerDto(google)).thenReturn(responseEmployerDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employers/employer/{id}", googleId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(responseEmployerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(responseEmployerDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(responseEmployerDto.getAddress()));
    }

    @Test
    void createEmployer() throws Exception {
        when(employerService.createEmployer(any(Employer.class))).thenReturn(google);
        when(employerMapper.employerToEmployerDto(google)).thenReturn(responseEmployerDto);

        String requestJson = new ObjectMapper().writeValueAsString(requestEmployerDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employers/create")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.id").value(responseEmployerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.name").value(responseEmployerDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.address").value(responseEmployerDto.getAddress()));
    }

    @Test
    void updateEmployer() throws Exception {
        when(employerService.updateEmployer(googleId, requestEmployerDto)).thenReturn(Optional.of(google));
        when(employerMapper.employerToEmployerDto(google)).thenReturn(responseEmployerDto);

        String requestJson = new ObjectMapper().writeValueAsString(requestEmployerDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employers/update/{id}", googleId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.id").value(responseEmployerDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.name").value(responseEmployerDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dto.address").value(responseEmployerDto.getAddress()));
    }

    @Test
    void deleteById() throws Exception {
        when(employerService.getEmployerById(googleId)).thenReturn(Optional.of(google));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employers/delete/{id}", googleId)
                        .header("Authorization", "Bearer " + accessToken)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(EmployerStatus.DELETED.getMessage()));

        verify(employerService).deleteById(googleId);
    }
}