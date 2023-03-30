package com.example.departments;

import com.example.departments.controller.DepartmentController;
import com.example.departments.service.DepartmentService;
import com.example.departments.service.implementaion.DepartmentServiceImpl;
import com.example.departments.service.implementaion.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = DepartmentController.class)
class DepartmentsApplicationTests {


    @Autowired
    private DepartmentController departmentController;

    @Test
    void contextLoads() {
        assertThat(departmentController).isNotNull();

        assertThat(departmentController.getAll().getBody())
                .extracting("departmentName").contains("IT");

        assertThat(departmentController.getDepartmentById(1l)
                .getBody()).extracting("departmentName").isEqualTo("IT");
    }

//    @Autowired
//    private MockMvc mockMvc;
//
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private DepartmentService departmentService;
//
//    @Mock
//    private DepartmentServiceImpl departmentServiceImpl;
//
//    @Test
//    void contextLoads() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/departments/getById/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        ;
//    }

}
