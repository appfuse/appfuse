-- drop the existing database
drop database appfuse;

-- create the test user
create user test password 'test';

-- create the database
create database appfuse owner test;
