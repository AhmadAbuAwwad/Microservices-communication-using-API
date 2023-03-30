package com.example.JWT.controllers;

import com.example.JWT.dto.DepartmentDTO;
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
@RequestMapping("/api/secured")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/departmentAllData")
    public ResponseEntity<List<DepartmentDTO>> departmentsData(HttpServletRequest request) {
        ParameterizedTypeReference<List<DepartmentDTO>> typeReference = new ParameterizedTypeReference<>() {
        };
        List<DepartmentDTO> departmentDTOS = departmentService.getList(request, "http://localhost:8081/api/departments/getAll", typeReference);

        return ResponseEntity.ok(departmentDTOS);
    }

    @GetMapping("/departmentData")
    public ResponseEntity<DepartmentDTO> userAndDepartmentData(HttpServletRequest request) throws ExecutionException, InterruptedException {

        ParameterizedTypeReference<DepartmentDTO> typeReference = new ParameterizedTypeReference<>() {
        };

        //
        Mono<DepartmentDTO> departmentDTOS = departmentService.getResponse(request, "http://localhost:8081/api/departments/getById/" + 1L, typeReference);
        DepartmentDTO departmentDTO = departmentDTOS.block();
        System.out.println(departmentDTO);

        DepartmentDTO departmentDto = departmentService.getDepartmentDto(request, 1L);
        if (departmentDto.getDepartmentAddress() == null)
            throw new RuntimeException();   //  No value present Exception
        return ResponseEntity.ok(departmentDto);
    }

    @PostMapping("/createDepartment")
    public ResponseEntity<DepartmentDTO> createDepartment(HttpServletRequest request, @RequestBody DepartmentDTO departmentDTO) throws ExecutionException, InterruptedException {
        DepartmentDTO departmentDto = departmentService.createDepartmentDto(request, departmentDTO);
        if (departmentDto.getDepartmentAddress() == null)
            throw new RuntimeException();   //  No value present Exception
        return ResponseEntity.ok(departmentDto);
    }

    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(HttpServletRequest request, @PathVariable long id, @RequestBody DepartmentDTO departmentDTO) throws ExecutionException, InterruptedException {
        DepartmentDTO departmentDto = departmentService.updateDepartmentDto(request, id, departmentDTO);
        if (departmentDto.getDepartmentAddress() == null)
            throw new RuntimeException();   //  No value present Exception
        return ResponseEntity.ok(departmentDto);
    }

    @DeleteMapping("/deleteDepartmentById/{id}")
    public ResponseEntity<String> deleteDepartmentById(HttpServletRequest request, @PathVariable("id") long departmentId) throws ExecutionException, InterruptedException {
        departmentService.deleteDepartmentById(request, departmentId);
        return ResponseEntity.ok("Department was deleted successfully");
    }

}
