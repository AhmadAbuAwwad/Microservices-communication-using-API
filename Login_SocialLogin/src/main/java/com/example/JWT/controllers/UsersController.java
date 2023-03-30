package com.example.JWT.controllers;

import com.example.JWT.dto.DepartmentDTO;
import com.example.JWT.models.Client;
import com.example.JWT.service.implementation.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/usersAllData")
    public ResponseEntity<List<Client>> departmentsData(HttpServletRequest request) throws ExecutionException, InterruptedException {
        ParameterizedTypeReference<List<Client>> typeReference = new ParameterizedTypeReference<List<Client>>() {
        };
        List<Client> clients = departmentService.getList(request, "http://localhost:8081/api/users/getAllUsers", typeReference);

        return ResponseEntity.ok(clients);
    }

    @GetMapping("/userData")
    public ResponseEntity<Client> userAndDepartmentData(HttpServletRequest request) throws ExecutionException, InterruptedException {

        ParameterizedTypeReference<Client> typeReference = new ParameterizedTypeReference<Client>() {
        };
        Mono<Client> departmentDTOS = departmentService.getResponse(request, "http://localhost:8081/api/departments/getById/" + 1l, typeReference);
        departmentDTOS.block();

        Mono<Client> client = departmentService.getResponse(request, "http://localhost:8081/api/departments/getUserById/" + 1l, typeReference);

        return ResponseEntity.ok(client.block());
    }
}
