drop table if exists user_role;
drop table if exists app_user;
drop table if exists user_cookie;
drop table if exists role;
create table user_role (
   id BIGINT not null,
   user_id BIGINT not null,
   username VARCHAR(255) not null,
   role_name VARCHAR(255) not null,
   primary key (id)
);
create table app_user (
   id BIGINT not null,
   username VARCHAR(255) not null unique,
   password VARCHAR(255) not null,
   first_name VARCHAR(255) not null,
   last_name VARCHAR(255) not null,
   address VARCHAR(255),
   city VARCHAR(255) not null,
   country VARCHAR(100),
   email VARCHAR(255),
   phone_number VARCHAR(255),
   postal_code VARCHAR(255) not null,
   province VARCHAR(100),
   website VARCHAR(255),
   password_hint VARCHAR(255),
   primary key (id)
);
create table user_cookie (
   id BIGINT not null,
   username VARCHAR(30) not null,
   cookie_id VARCHAR(100) not null,
   date_created DATETIME not null,
   primary key (id)
);
create table role (
   name VARCHAR(255) not null,
   description VARCHAR(255),
   primary key (name)
);
