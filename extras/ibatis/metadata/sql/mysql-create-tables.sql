drop table if exists user_role;
drop table if exists app_user;
drop table if exists role;
create table user_role (
    username varchar(20) not null,
    role_name varchar(20) not null,
    primary key (username, role_name)
);
create table app_user (
    username varchar(20) not null,
    version integer not null,
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
    account_enabled char(1),
    account_expired char(1),
    account_locked char(1),
    credentials_expired char(1),
    primary key (username)
);
create table role (
    name varchar(20) not null,
    version integer not null,
    description varchar(255),
    primary key (name)
);
alter table user_role add index FK143BF46A14048CB4 (role_name), add constraint FK143BF46A14048CB4 foreign key (role_name) references role (name);
alter table user_role add index FK143BF46AF02988D6 (username), add constraint FK143BF46AF02988D6 foreign key (username) references app_user (username);
