//package com.example.backend.Services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.backend.Entities.Departments;
//import com.example.backend.Models.DepartmentModel;
//import com.example.backend.Repositories.DepartmentRepository;
//
//@Service
//public class DepartmentsServices {
//
//    @Autowired
//    private DepartmentRepository departmentRepository;
//
//    public Departments createDepartments(DepartmentModel departmentModel) {
//        Departments newDepartment = new Departments();
//
//        newDepartment.setName(departmentModel.getName());
//
//        return departmentRepository.save(newDepartment);
//    }
//
//    public Departments fetchDepartments(int deptId) {
//        return departmentRepository.findById(deptId);
//    }
//
//    public Departments fetchDepartmentsByName(String name) {
//        return departmentRepository.findByName(name);
//    }
//
//    public Departments updateDepartmentName(String oldName, String newname) {
//        Departments departmentToBeUpdated = fetchDepartmentsByName(oldName);
//
//        departmentToBeUpdated.setName(newname);
//
//        return departmentRepository.save(departmentToBeUpdated);
//    }
//
//}
