--
-- DATA LOAD
-- DEFAULT SYSTEM USER
-- User Id and passwords are case sensitive
-- Username : Admin
-- Password : password
insert into users (username, password, enabled, pwdchangeneeded, firstname, lastname, creditcardnumber) values 
('Admin','f6dd810b14efdd9fe7ed81a67fdd8dc6',1,0,'System','Administrator', '1234567890123456');
--
insert into roles (role) values ('ROLE_USER'), ('ROLE_SUPERVISOR'), ('ROLE_SITE_ADMIN');
insert into authorities (username, authority, user_id) values 
('Admin', 'ROLE_SUPERVISOR', 1), ('Admin', 'ROLE_SITE_ADMIN', 1);

