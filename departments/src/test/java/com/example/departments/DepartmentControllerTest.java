package com.example.departments;

import com.example.departments.controller.DepartmentController;
import com.example.departments.model.Department;
import com.example.departments.repository.DepartmentRepository;
import com.example.departments.service.DepartmentService;
import com.example.departments.service.implementaion.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DepartmentControllerTest {



    @Test
    public void testGetDepartmentById() {

        DepartmentService departmentService = mock(DepartmentService.class);
        UserService userService = mock(UserService.class);
        DepartmentRepository departmentRepository = mock(DepartmentRepository.class);
        Department department = new Department(1L, "IT", "BZU", "IT101");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        DepartmentController departmentController = new DepartmentController(departmentService, userService);
        ResponseEntity<Department> response = departmentController.getDepartmentById(1L);
        assertThat(response).extracting("status").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting("departmentName").isEqualTo("IT");
    }

}
