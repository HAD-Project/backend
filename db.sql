create table user (
    id int primary key auto_increment,
    name varchar(255),
    gender varchar(20),
    mobile char(10),
    email varchar(255),
    username varchar(255),
    password varchar(255),
    type int
);

create table admin (
    id int primary key auto_increment,
    foreign key id references user(id)
);

create table doctor (
    id int primary key auto_increment,
    qualification varchar(255),
    dept_id int,
    foreign key id references user(id),
    foreign key dept_id references department(id)
);

create table receptionist (
    id int primary key auto_increment,
    qualification varchar(255),
    foreign key id references user(id)
);

create table department (
    id int primary key auto_increment,
    name varchar(255)
);

create table patient (
    id int primary key auto_increment,
    name varchar(255),
    abha_id varchar(14),
    gender varchar(20),
    dob date,
    mobile char(10)
);

create table treats (
    id int primary key auto_increment,
    doctor_id int,
    patient_id int,
    foreign key doctor_id references doctor(id),
    foreign key patient id references patient(id)
);

create table appointment (
    id int primary key auto_increment,
    appt_date date,
    appt_time time,
    remarks varchar(255),
    stay_type int
    status varchar(20)
    receptionist_id int,
    patient_id,
    doctor_id,
    foreign key receptionist_id references receptionist(id)
    foreign key patient_id references patient(id),
    foreign key doctor_id references doctor(id)
);

create table record (
    id int primary key auto_increment,
    patient_id int,
    doctor_id,
    appointment_id int,
    file_path varchar(255),
    foreign key patient_id references patient(id),
    foreign key doctor_id references doctor(id),
    foreign key appointment_id references appointment(id)
);

