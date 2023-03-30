package com.example.departments;

import com.example.departments.controller.DepartmentController;
import com.example.departments.model.Department;
import com.example.departments.service.DepartmentService;
import com.example.departments.service.implementaion.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DepartmentControllerTestAnnotations {

    @Mock
    private DepartmentService departmentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private DepartmentController departmentController;

    @Test
    public void testGetDepartmentById() {
        Department department = new Department(1L, "IT", "BZU", "IT101");
        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        ResponseEntity<Department> response = departmentController.getDepartmentById(1L);

        assertThat(response).extracting("status").isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting("departmentName").isEqualTo("IT");
    }
}