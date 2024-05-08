package com.example.backend;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.backend.Entities.Admins;
import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Receptionists;
import com.example.backend.Entities.Role;
import com.example.backend.Entities.Users;
import com.example.backend.Repositories.AdminRepository;
import com.example.backend.Repositories.DepartmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.Repositories.ReceptionRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Services.ABDMServices;
import com.example.backend.cryptography.CryptographyUtil;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
// @PropertySource("classpath:application-dev.properties")
@ComponentScan(basePackages = {"com.example.*"})
@EnableAsync
public class BackendApplication {

	@Autowired
	ABDMServices abdmServices;
	
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	ReceptionRepository receptionRepository;

	@Autowired
	PatientRepository patientRepository;

	@Autowired
	private CryptographyUtil cryptographyUtil;

	@Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("HAD-");
        executor.initialize();
        return executor;
    }
	
	public static void main(String[] args) {
		System.out.println("Working");
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void receiveKey() throws Exception {

		Users admin = new Users();
		admin.setUserId(1);
		admin.setActive(true);
		admin.setEmail("admin@mail.com");
		admin.setGender("Male");
		admin.setName("Admin");
		admin.setPassword("$2a$12$/mMT1pLQxljL6PspA2I1DOfmfX8uz/amBOb0JIWdLusvxiuPlM/5i");
		admin.setPhone(new BigInteger("1111111111"));
		admin.setRole(Role.ADMIN);
		admin.setUsername("admin");
		admin = userRepository.save(admin);

		Users doctor = new Users();
		doctor.setUserId(2);
		doctor.setActive(true);
		doctor.setEmail("doctor@mail.com");
		doctor.setGender("Male");
		doctor.setName("Doctor");
		doctor.setPassword("$2a$12$cwWk8R5N4Rc.Dgr1I1jjNOtF7d/Q87WwCEBMKm/fWs7qhrNkEUC/K");
		doctor.setPhone(new BigInteger("2222222222"));
		doctor.setRole(Role.DOCTOR);
		doctor.setUsername("doctor");
		doctor = userRepository.save(doctor);

		Users receptionist = new Users();
		receptionist.setUserId(3);
		receptionist.setActive(true);
		receptionist.setEmail("receptionist@mail.com");
		receptionist.setGender("Male");
		receptionist.setName("Receptionist");
		receptionist.setPassword("$2a$12$6ySCFybL4i6ukKXsFQ.Is.wRqLemEA3T4tnpqKcYYNrNUlVsGAZ6i");
		receptionist.setPhone(new BigInteger("3333333333"));
		receptionist.setRole(Role.RECEPTIONIST);
		receptionist.setUsername("receptionist");
		receptionist = userRepository.save(receptionist);

		Departments departments = new Departments();
		departments.setId(1);
		departments.setName("SOME DEPARTMENT");
		departments = departmentRepository.save(departments);

		Admins admins = new Admins();
		admins.setAdminId(1);
		admins.setUser(admin);
		adminRepository.save(admins);

		Doctors doctors = new Doctors();
		doctors.setDoctorId(1);
		doctors.setQualifications("MBBS");
		doctors.setDepartment(departments);
		doctors.setUser(doctor);
		doctors = doctorRepository.save(doctors);

		Receptionists receptionists = new Receptionists();
		receptionists.setReceptionistId(1);
		receptionists.setQualifications("B.COM");
		receptionists.setUser(receptionist);
		receptionists = receptionRepository.save(receptionists);
		

		Patients patient = new Patients();
		patient.setPatientId(1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dob = format.parse("2001-08-15");
		patient.setDob(dob);
		patient.setAbhaId("91-2211-7286-2445");
		patient.setGender("M");
		patient.setMobileNo("9324846868");
		patient.setName("Shrutik Sanjay Mali");
		patient.setAbhaAddress("shrutik2001@sbx");
		patient.setLinkToken("eyJhbGciOiJSUzUxMiJ9.eyJoaXBJZCI6IkhBRF8wMSIsInN1YiI6InNocnV0aWsyMDAxQHNieCIsImFiaGFOdW1iZXIiOm51bGwsImV4cCI6MTcyODU5NDgyMCwiaWF0IjoxNzEyODI2ODIwLCJ0cmFuc2FjdGlvbklkIjoiYTliZTBmODgtYWNmYy00NjYwLWE2MWMtZTQwNDM2ODAzOTA0IiwiYWJoYUFkZHJlc3MiOiJzaHJ1dGlrMjAwMUBzYngifQ.tlnwddHJNGMmZavT76Vfdi0m_EQSUGG4glNE9KABlyQc__ESjgyBK5vc_XismrjD1Hh1xyOqDw6exMeNpb83wnlS3G5MjRIQesVqNJDYqfSNMOhU8blmFUFeuJc8HrLTnG8wQKaQpi2p9l4NqdEHUYSepJe7jbBSC_3-jShtwdaeXjen_nfufLVo3N43qxLTPehyausvtKrZRDpnbUgRoOiExm8bsdQOv8VaFlGoWKzEc7YKVXeFeKDYKt6fSrPXvTlSxtEjsUTNDbgREOjb98PyRAmQ1oF7xQZ2HWjHeEWXMhnHUolH6DgJtR8KXiKdjg9eAD_kWW6IXEMNMHRA8A");
		List<Doctors> treatedBy = new ArrayList<>();
		treatedBy.add(doctors);
		patient.setTreatedBy(treatedBy);
		patient = patientRepository.save(patient);

		Patients patient2 = new Patients();
		patient2.setPatientId(2);
		Date dob2 = format.parse("2001-10-25");
		patient2.setDob(dob2);
		patient2.setAbhaId("91-6586-5064-7428");
		patient2.setGender("M");
		patient2.setMobileNo("9324846868");
		patient2.setName("Sridhar S Menon");
		patient2.setAbhaAddress("sridharmenon@sbx");
		patient2.setLinkToken("eyJhbGciOiJSUzUxMiJ9.eyJoaXBJZCI6IkhBRF8wMSIsInN1YiI6InNyaWRoYXJtZW5vbkBzYngiLCJhYmhhTnVtYmVyIjpudWxsLCJleHAiOjE3MzA5MTg3NjQsImlhdCI6MTcxNTE1MDc2NCwidHJhbnNhY3Rpb25JZCI6ImQyODA0MGExLThiOWMtNDRkOS04NzNkLWRiNWI5ZmJmNjY5ZSIsImFiaGFBZGRyZXNzIjoic3JpZGhhcm1lbm9uQHNieCJ9.x079loy5Ba_mqevZAkcwCDsMfiIuUNQukIQQ1qP4ztIU0NWgVbpBqZrqpAAbRaE-zjaJjdINH2PJBgt5ZwU6u6_mlhMakwY9Zl550k8e2cUzTRxsMHdyQVuZ7onXRPUgiEImznqXl32hMxEyhwAWdaN_kYiyE_7o_uOwMyBxjSUZRK7q0WpmXzoYJmIOQtRDELkxKxBa8OpG_AeGMpbLVYoQ6BjeV103YpAVBdH1uwJMOzLezJ65hZjVSNdfDGXYmCH4FUbL1B4rYcDMl6u6SDsBrxfsf3m1xvE-KSTpz579v4d4jI42rv1ZHCN-h5Pd55jOf0HNonXe1OtqbnCGHQ");
		List<Doctors> treatedBy2 = new ArrayList<>();
		treatedBy2.add(doctors);
		patient2.setTreatedBy(treatedBy2);
		patient2 = patientRepository.save(patient2);

		cryptographyUtil.generateKeySet();
		System.out.println("Generated keys: ");
		System.out.println(cryptographyUtil.getKeys());


		// try {
		// 	abdmServices.getPublicKey();
		// 	abdmServices.initiateSession();
		// 	System.out.println(abdmServices.getRsaKey());
		// } catch (Exception e) {
		// 	System.out.println(e);
		// }
	}

}
