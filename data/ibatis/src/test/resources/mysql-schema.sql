drop table if exists user_role;
drop table if exists app_user;
drop table if exists role;
create table user_role (
    user_id int(8) not null,
    role_id int(8) not null,
    primary key (user_id, role_id)
);
create table app_user (
    id int(8) not null auto_increment,
    username varchar(20) not null unique,
    version integer not null,
    password varchar(50),
    first_name varchar(50),
    last_name varchar(50),
    address varchar(150),
    city varchar(50),
    province varchar(100),
    country varchar(100),
    postal_code varchar(15),
    email varchar(50) unique,
    phone_number varchar(20),
    website varchar(255),
    password_hint varchar(100),
    account_enabled char(1),
    account_expired char(1),
    account_locked char(1),
    credentials_expired char(1),
    primary key (id)
);
create table role (
    id int(8) not null auto_increment,
    name varchar(20) not null,
    description varchar(64),
    primary key (id)
);
alter table user_role add index FK143BF46A14048CB4 (role_id), add constraint FK143BF46A14048CB4 foreign key (role_id) references role (id);
alter table user_role add index FK143BF46AF02988D6 (user_id), add constraint FK143BF46AF02988D6 foreign key (user_id) references app_user (id);
