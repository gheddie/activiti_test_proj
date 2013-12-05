---------------------------------------------------------------------------------------
---groups
---------------------------------------------------------------------------------------

--accountancy
insert into act_id_group (id_, rev_, name_, type_) values ('accountancy', 1, 'Accountancy', 'assignment');

--admin
insert into act_id_group (id_, rev_, name_, type_) values ('admin', 1, 'Admin', 'security-role');

--hr

insert into act_id_group (id_, rev_, name_, type_) values ('humanResources', 1, 'HumanResources', 'assignment');

---------------------------------------------------------------------------------------
---users
---------------------------------------------------------------------------------------

---accountancy users
insert into act_id_user (id_, rev_, first_, last_, email_, pwd_, picture_id_) values ('sarah', 5, 'Sarah', 'The Accountant', 'sarah.accountant@test.com', 'sarah1234', NULL);
insert into act_id_user (id_, rev_, first_, last_, email_, pwd_, picture_id_) values ('elsie', 5, 'Elsie', 'The Accountant', 'elsie.accountant@test.com', 'elsie1234', NULL);

---admin users
insert into act_id_user (id_, rev_, first_, last_, email_, pwd_, picture_id_) values ('ted', 5, 'Ted', 'The Admin', 'ted.admin@test.com', 'admin1234', NULL);

---hr users
insert into act_id_user (id_, rev_, first_, last_, email_, pwd_, picture_id_) values ('jenna', 5, 'Jenna', 'The HR Girl', 'jenna.hr@test.com', 'jenna1234', NULL);

---------------------------------------------------------------------------------------
---memberships
---------------------------------------------------------------------------------------

--sarah.accountant
insert into act_id_membership (user_id_, group_id_) values ('sarah', 'accountancy');

--sarah.accountant
insert into act_id_membership (user_id_, group_id_) values ('elsie', 'accountancy');

--ted.admin
insert into act_id_membership (user_id_, group_id_) values ('ted', 'admin');

--jenna.hr
insert into act_id_membership (user_id_, group_id_) values ('jenna', 'humanResources');