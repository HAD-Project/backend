Delete from patients;
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
-- INSERT INTO patients (patient_id, dob, abha_id, gender, mobile_no, name, abha_address, link_token) VALUES (1, "2001-08-15", "91-2211-7286-2445", "M", "9324846868", "Shrutik Sanjay Mali", "shrutik2001@sbx", "eyJhbGciOiJSUzUxMiJ9.eyJoaXBJZCI6IkhBRF8wMSIsInN1YiI6InNocnV0aWsxNUBzYngiLCJhYmhhTnVtYmVyIjpudWxsLCJleHAiOjE3Mjg1MDY2OTQsImlhdCI6MTcxMjczODY5NCwidHJhbnNhY3Rpb25JZCI6Ijk5YjVlNjlkLTQ1OWQtNDMwMy1hZDU5LTI0ZmMwYTAyNWE2OSIsImFiaGFBZGRyZXNzIjoic2hydXRpazE1QHNieCJ9.5GMeYHM_R12RyQAlj2i28fiPdSQ5tjX6aOQ3plheZrwJ3U0f5cLEK4Ajj-yKhh5rYMXedf8tPM1reWz0cL9vLlg4jrxiy-AeNxcAQRgIZJ3Bjor-eKkBxsmluOShExbfabeINjHOczq7VxsDr0Msb6wpoSi20FcPMx_I56SikiRoWsN09xhYSqiwtKjqMINMp4iQRa2tvA9deEEOuDNiZugRNDqbJVcszkXvaPdm8jJXFbIcqbXjnurytUcjlxg5o4SBN-ddbnNnwaD29bcDgUki3w0k4bH71m2Lm-WZvuUOhXY_pWuTTR7zBHLikv8kvAOTJm2__8LwlSGAOhht6A");
-- INSERT INTO patients_treated_by(treated_by_doctor_id, treats_patient_id) values (1, 1);