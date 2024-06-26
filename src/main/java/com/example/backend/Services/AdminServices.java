package com.example.backend.Services;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.Entities.Admins;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Entities.Doctors;
import com.example.backend.Repositories.AdminRepository;
import com.example.backend.Repositories.DoctorRepository;

@Service
public class AdminServices {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DoctorRepository doctorRepository;




//    public Admins createAdmin(AdminModel adminModel) {
//
//        Admins newAdmin = new Admins();
//
//        newAdmin.setName(adminModel.getName());
//        newAdmin.setUsername(adminModel.getUsername());
//        newAdmin.setPassword(adminModel.getPassword());
//        newAdmin.setGender(adminModel.getGender());
//
//        return adminRepository.save(newAdmin);
//
//    }

//    public Admins fetchAdmin(int id) {
//        return adminRepository.findByUserId(id);
//    }
//
//    public Admins fetchAdminByUsernameAndPassword(String username, String password) {
//        return adminRepository.findByUsernameAndPassword(username, password);
//    }

//    public Doctors createDoctor(DoctorModel doctorModel) {
//        Doctors newDoctor = new Doctors();
//        System.out.println(doctorModel.toString());
//        newDoctor.setName(doctorModel.getName());
//        newDoctor.setUsername(doctorModel.getUsername());
//        newDoctor.setPassword(doctorModel.getPassword());
//        newDoctor.setGender(doctorModel.getGender());
//        newDoctor.setQualifications(doctorModel.getQualifications());
//        newDoctor.setDepartment(departmentsServices.fetchDepartmentsByName(doctorModel.getDepartment()));
//
//        return doctorRepository.save(newDoctor);
//
//    }

    public List<Admins> getDoctors() {
        return adminRepository.findAll();
    }

//    public Doctors updateDoctorName(String username, String newName) {
//
//        Doctors doctorToBeUpdated = doctorRepository.findByUsername(username);
//        doctorToBeUpdated.setName(newName);
//
//        return doctorRepository.save(doctorToBeUpdated);
//
//    }
//
//    public Doctors updateDoctorGender(String username, String newGender) {
//        Doctors doctorToBeUpdated = doctorRepository.findByUsername(username);
//
//        doctorToBeUpdated.setGender(newGender);
//        return doctorRepository.save(doctorToBeUpdated);
//
//    }
//
//    public Doctors updateDoctorPassword(String username, String newPassword) {
//        Doctors doctorToBeUpdated = doctorRepository.findByUsername(username);
//        doctorToBeUpdated.setPassword(newPassword);
//
//        return doctorRepository.save(doctorToBeUpdated);
//    }

//    public Doctors updateDoctorQualifications(String username, String newQualifications) {
//        Doctors doctorToBeUpdated = doctorRepository.findByUsername(username);
//        doctorToBeUpdated.setQualifications(newQualifications);
//
//        return doctorRepository.save(doctorToBeUpdated);
//    }
//
//    public DoctorDeleteResponseModel deleteDoctor(String username) {
//        Doctors doctorToBeDeleted = doctorRepository.findByUsername(username);
//        DoctorDeleteResponseModel doctorDeleteResponseModel = new DoctorDeleteResponseModel();
//        try {
//            doctorRepository.delete(doctorToBeDeleted);
//            doctorDeleteResponseModel.setRequestStatus("Success");
//            doctorDeleteResponseModel.setRemarks("None");
//        } catch (Exception e) {
//            doctorDeleteResponseModel.setRequestStatus("Failure");
//            doctorDeleteResponseModel.setRemarks(e.toString());
//        }
//
//        return doctorDeleteResponseModel;
//    }

}
