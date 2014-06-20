--
-- DATA LOAD
-- DEFAULT SYSTEM USER
-- User Id and passwords are case sensitive
-- Username : Admin
-- Password : password
insert into users (username, password, enabled, pwdchangeneeded, firstname, lastname, creditcardnumber) values 
('Admin','f6dd810b14efdd9fe7ed81a67fdd8dc6',1,0,'System','Administrator', '1234567890123456'),
('Frodo','7d3066b5917e4e637900acda05b8b89b',1,0,'Frodo','Baggins', '1234567890123456'),
('Jane','750f5d63a8185176fe8ce3cbba0929dc',1,0,'Jane','Doe', '1234567890123456'),
('Mark','cd197c3b6a9616ada59d03c4ae754084',1,0,'Mark','Twain', '1234567890123456');
--
insert into roles (role) values ('ROLE_USER'), ('ROLE_SUPERVISOR'), ('ROLE_SITE_ADMIN');
--
insert into authorities (username, authority, user_id) values 
('Admin', 'ROLE_SUPERVISOR', 1), 
('Admin', 'ROLE_SITE_ADMIN', 1),
('Frodo', 'ROLE_USER', 2),
('Frodo', 'ROLE_SUPERVISOR', 2),
('Jane', 'ROLE_USER', 3),
('Mark', 'ROLE_USER', 4);
--
insert into et_groups (groupname, groupdescription, active) values 
('Weekend Outings', 'Outings among friends', 1),
('Official Lunches', 'Official departmental lunches', 1);
--
insert into et_group_user (group_id, user_id) values 
(1, 2),
(1, 3),
(2, 2),
(2, 3),
(2, 4);
--
