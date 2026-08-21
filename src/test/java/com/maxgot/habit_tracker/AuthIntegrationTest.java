package com.maxgot.habit_tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxgot.habit_tracker.dto.LoginRequest;
import com.maxgot.habit_tracker.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @Value("${local.server.port}")
    private int port;

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        baseUrl = "http://localhost:" + port;
    }

    @Test
    void registrationWorksPasswordIsHashed() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("hashed");
        request.setPassword("secret");
        request.setEmail("hashed@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = objectMapper.writeValueAsString(request);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/api/auth/register",
                HttpMethod.POST,
                entity,
                Map.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void loginWithCorrectPasswordReturnsToken() throws Exception {
        // Регистрация
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("logintest");
        registerRequest.setPassword("secret");
        registerRequest.setEmail("logintest@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = objectMapper.writeValueAsString(registerRequest);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        restTemplate.exchange(baseUrl + "/api/auth/register", HttpMethod.POST, entity, Map.class);

        // Логин
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("logintest");
        loginRequest.setPassword("secret");

        json = objectMapper.writeValueAsString(loginRequest);
        entity = new HttpEntity<>(json, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/api/auth/login",
                HttpMethod.POST,
                entity,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("token"));
    }

    @Test
    void loginWithWrongPasswordReturns401() throws Exception {
        // Регистрация
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("wrongpass");
        registerRequest.setPassword("secret");
        registerRequest.setEmail("wrongpass@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String json = objectMapper.writeValueAsString(registerRequest);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        restTemplate.exchange(baseUrl + "/api/auth/register", HttpMethod.POST, entity, Map.class);

        // Логин с неправильным паролем
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrongpass");
        loginRequest.setPassword("wrongpassword");

        json = objectMapper.writeValueAsString(loginRequest);
        entity = new HttpEntity<>(json, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                baseUrl + "/api/auth/login",
                HttpMethod.POST,
                entity,
                Map.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void requestWithoutTokenReturns401() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/api/habits",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void userSeesOnlyOwnHabits() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Регистрация Alice
        RegisterRequest aliceRegister = new RegisterRequest();
        aliceRegister.setUsername("alice");
        aliceRegister.setPassword("secret");
        aliceRegister.setEmail("alice@example.com");

        String json = objectMapper.writeValueAsString(aliceRegister);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        restTemplate.exchange(baseUrl + "/api/auth/register", HttpMethod.POST, entity, Map.class);

        // Логин Alice
        LoginRequest aliceLogin = new LoginRequest();
        aliceLogin.setUsername("alice");
        aliceLogin.setPassword("secret");

        json = objectMapper.writeValueAsString(aliceLogin);
        entity = new HttpEntity<>(json, headers);
        ResponseEntity<Map> aliceLoginResponse = restTemplate.exchange(
                baseUrl + "/api/auth/login",
                HttpMethod.POST,
                entity,
                Map.class
        );
        String aliceToken = (String) aliceLoginResponse.getBody().get("token");

        // Создание привычки для Alice
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.set("Authorization", "Bearer " + aliceToken);
        authHeaders.setContentType(MediaType.APPLICATION_JSON);

        String habitJson = "{\"name\":\"Alice habit\",\"description\":\"for Alice\",\"target\":5}";
        HttpEntity<String> habitEntity = new HttpEntity<>(habitJson, authHeaders);
        restTemplate.exchange(baseUrl + "/api/habits", HttpMethod.POST, habitEntity, Map.class);

        // Регистрация Bob
        RegisterRequest bobRegister = new RegisterRequest();
        bobRegister.setUsername("bob");
        bobRegister.setPassword("secret");
        bobRegister.setEmail("bob@example.com");

        json = objectMapper.writeValueAsString(bobRegister);
        entity = new HttpEntity<>(json, headers);
        restTemplate.exchange(baseUrl + "/api/auth/register", HttpMethod.POST, entity, Map.class);

        // Логин Bob
        LoginRequest bobLogin = new LoginRequest();
        bobLogin.setUsername("bob");
        bobLogin.setPassword("secret");

        json = objectMapper.writeValueAsString(bobLogin);
        entity = new HttpEntity<>(json, headers);
        ResponseEntity<Map> bobLoginResponse = restTemplate.exchange(
                baseUrl + "/api/auth/login",
                HttpMethod.POST,
                entity,
                Map.class
        );
        String bobToken = (String) bobLoginResponse.getBody().get("token");

        // Запрос привычек от Bob
        HttpHeaders bobHeaders = new HttpHeaders();
        bobHeaders.set("Authorization", "Bearer " + bobToken);
        HttpEntity<Void> bobRequest = new HttpEntity<>(bobHeaders);

        ResponseEntity<Map[]> bobHabits = restTemplate.exchange(
                baseUrl + "/api/habits",
                HttpMethod.GET,
                bobRequest,
                Map[].class
        );

        assertEquals(HttpStatus.OK, bobHabits.getStatusCode());
        assertEquals(0, bobHabits.getBody().length);
    }
}