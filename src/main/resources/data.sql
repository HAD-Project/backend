DELETE FROM receptionists;
DELETE FROM doctors;
DELETE FROM admins;
DELETE FROM departments;
DELETE FROM token;
DELETE FROM users;
insert into users (users.user_id,active, email, gender, name, password, phone, role, username) VALUES (1,true,'admin@mail.com','Male','Admin','$2a$12$/mMT1pLQxljL6PspA2I1DOfmfX8uz/amBOb0JIWdLusvxiuPlM/5i',1111111111,'ADMIN','admin');
insert into users (users.user_id,active, email, gender, name, password, phone, role, username) VALUES (2,true,'doctor@mail.com','Male','Doctor','$2a$12$cwWk8R5N4Rc.Dgr1I1jjNOtF7d/Q87WwCEBMKm/fWs7qhrNkEUC/K',2222222222,'DOCTOR','doctor');
insert into users (users.user_id,active, email, gender, name, password, phone, role, username) VALUES (3,true,'receptionist@mail.com','Male','Receptionist','$2a$12$6ySCFybL4i6ukKXsFQ.Is.wRqLemEA3T4tnpqKcYYNrNUlVsGAZ6i',3333333333,'RECEPTIONIST','receptionist');
INSERT INTO departments (DEPT_ID, NAME) VALUES (1,'SOME DEPARTMENT');
insert into admins (admins.ADMIN_ID, admins.USER_ID) values (1,1);
insert into doctors (doctors.doctor_id,qualifications, department, user_id) VALUES (1,'MBBS',1,2);
INSERT INTO receptionists (receptionists.receptionist_id,qualifications, user_id) VALUES (1,'B.COM',3);
INSERT INTO patients (patient_id, dob, abha_id, gender, mobile_no, name) VALUES (1, "2001-08-15", "91-2211-7286-2445", "M", "9324846868", "Shrutik")