package com.example.departments.service;


import com.example.departments.model.Department;

import java.util.List;

public interface DepartmentService {
    Department getDepartmentById(Long departmentId);

    Department saveDepartment(Department department);

    Department updateDepartment(long id, Department department);

    void deleteDepartmentById(Long departmentId);

    List<Department> getAllDepartments();
}
