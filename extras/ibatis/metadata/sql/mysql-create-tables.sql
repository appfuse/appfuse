drop table if exists user_role;
drop table if exists app_user;
drop table if exists user_cookie;
drop table if exists role;
create table user_role (
   role_name varchar(255) not null,
   username varchar(255) not null,
   primary key (username, role_name)
);
create table app_user (
   username varchar(20) not null,
   updated datetime not null,
   password varchar(255),
   first_name varchar(50),
   last_name varchar(50),
   address varchar(150),
   city varchar(50),
   province varchar(100),
   country varchar(100),
   postal_code varchar(15),
   email varchar(255),
   phone_number varchar(255),
   website varchar(255),
   password_hint varchar(255),
   primary key (username)
);
create table user_cookie (
   id bigint not null,
   username varchar(30) not null,
   cookie_id varchar(100) not null,
   date_created datetime,
   primary key (id)
);
create table role (
   name varchar(20) not null,
   updated datetime not null,
   description varchar(255),
   primary key (name)
);
alter table user_role add index FK143BF46A14048CB4 (role_name), add constraint FK143BF46A14048CB4 foreign key (role_name) references role (name);
alter table user_role add index FK143BF46AF02988D6 (username), add constraint FK143BF46AF02988D6 foreign key (username) references app_user (username);
create index user_cookie_username_cookie_id on user_cookie (username, cookie_id);