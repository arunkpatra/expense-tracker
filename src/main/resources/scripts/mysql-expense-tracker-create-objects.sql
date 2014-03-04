--
-- CREATE TABLES
--
create table et_audit (id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
createdby varchar(50), 
createddate datetime, 
recorddata varchar(512));
--
-- Users
create table users (id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
username varchar(50) NOT NULL UNIQUE, 
password varchar(50) NOT NULL, 
firstname varchar(16),
lastname varchar(16),
middleinit varchar(1),
emailid varchar(32),
createddate datetime, 
lastupdateddate datetime, 
lastmodifiedby varchar(50),
createdby varchar(50),
pwdchangeneeded smallint NOT NULL,
creditcardnumber varchar(16),
phonenumber varchar(32),
enabled smallint NOT NULL);
-- Roles
create table roles (id bigint  NOT NULL AUTO_INCREMENT PRIMARY KEY,
role varchar(50) NOT NULL UNIQUE);
--
-- authorities
create table authorities (id bigint  NOT NULL AUTO_INCREMENT PRIMARY KEY,
username varchar(50) NOT NULL,
authority varchar(50) NOT NULL,
grantedby varchar(50),
user_id bigint NOT NULL,  
constraint fk_authorities_roles foreign key(authority) references roles(role),
constraint fk_authorities_grantedby foreign key(grantedby) references users(username),
constraint fk_authorities_users_id foreign key(user_id) references users(id),
constraint fk_authorities_users_name foreign key(username) references users(username));
--
--
--
-- Settlement table
create table et_settlement (id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY, 
cyclestartdate datetime NOT NULL,
cycleenddate datetime NOT NULL,
createddate datetime, 
closeddate datetime, 
createdby varchar(50),
accountmanager varchar(50),
volume float NOT NULL,
settlementcompleted smallint NOT NULL,
constraint fk_settlement_accountmanager foreign key(accountmanager) references users(username),
constraint fk_settlement_createdby foreign key(createdby) references users(username));
alter table et_settlement AUTO_INCREMENT = 1000;
--
-- Reports Table
create table et_reports (id bigint  NOT NULL AUTO_INCREMENT PRIMARY KEY, 
createddate datetime, 
reportcontent mediumblob,
reporttype varchar(16) NOT NULL,
reportname varchar(32) NOT NULL,
settlementid bigint NOT NULL,
constraint fk_reports_settlementid foreign key(settlementid) references et_settlement(id));
alter table et_reports AUTO_INCREMENT = 1000;
--
-- user settlement
create table et_user_settlement (id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY, 
setteleddate datetime, 
username varchar(50) NOT NULL,
settlement_id bigint NOT NULL,
amount float NOT NULL,
usershare float NOT NULL,
userpaid float NOT NULL,
settlementcompleted smallint NOT NULL,
constraint fk_u_settlement_user foreign key(username) references users(username),
constraint fk_u_settlement_settlement foreign key(settlement_id) references et_settlement(id));
alter table et_user_settlement AUTO_INCREMENT = 1000;
--
-- expense
create table et_expense (id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY, 
paidby varchar(50) NOT NULL, 
settlement_id bigint,
amount float, 
date datetime, 
createdby varchar(50),
lastmodifiedby varchar(50),
createddate datetime, 
lastmodifieddate datetime, 
description varchar(64), 
category varchar(32),
constraint fk_expense_settlement foreign key(settlement_id) references et_settlement(id),
constraint fk_exp_createdby_users foreign key(createdby) references users(username),
constraint fk_exp_lastmodifiedby_users foreign key(lastmodifiedby) references users(username),
constraint fk_expense_users foreign key(paidby) references users(username));
--
-- user_expense
create table et_user_expense(id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY, 
username varchar(50) NOT NULL,
discountpercent float NOT NULL,
amount float NOT NULL,
expense_id bigint NOT NULL, 
constraint fk_u_expense_users foreign key(username) references users(username), 
constraint fk_u_expense_expense foreign key(expense_id) references et_expense(id));
--
--
-- Payments Table
create table et_payments(id bigint AUTO_INCREMENT NOT NULL PRIMARY KEY, 
paymentdate datetime NOT NULL,
amount float NOT NULL,
username varchar(50) NOT NULL,
constraint fk_payments_users foreign key(username) references users(username));
