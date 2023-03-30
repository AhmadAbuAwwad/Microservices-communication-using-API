package com.example.departments.service.implementaion;

import com.example.departments.model.Department;
import com.example.departments.repository.DepartmentRepository;
import com.example.departments.service.DepartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(long id, Department department) {
        Department newDepartment = departmentRepository.findById(id).get();
        if (newDepartment == null) {
            newDepartment = new Department();
        }
        newDepartment.setDepartmentCode(department.getDepartmentCode());
        newDepartment.setDepartmentAddress(department.getDepartmentAddress());
        newDepartment.setDepartmentName(department.getDepartmentName());
        newDepartment.setId(id);
        return departmentRepository.save(newDepartment);
    }

    @Override
    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).get();
    }


    @Override
    public void deleteDepartmentById(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
}
