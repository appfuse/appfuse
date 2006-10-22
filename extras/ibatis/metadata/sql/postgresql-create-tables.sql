-- Tried to do test for existence before dropping with the following script, 
-- but it didn't work: http://www.thescripts.com/forum/thread173559.html

DROP TABLE user_role;
DROP TABLE app_user;
DROP TABLE "role";

DROP SEQUENCE user_role_id;
DROP SEQUENCE app_user_seq;
DROP SEQUENCE role_seq;

-- Note the start value of 3 so the 2 inserts performed by appfuse do not conflict
CREATE SEQUENCE app_user_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 3
  CACHE 1;

CREATE SEQUENCE role_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 11
  CACHE 1;

CREATE SEQUENCE user_role_id
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE app_user
(
  id int4 NOT NULL DEFAULT nextval('app_user_seq'::regclass),
  username varchar(20) NOT NULL,
  version int4 NOT NULL,
  "password" varchar(50),
  first_name varchar(50),
  last_name varchar(50),
  address varchar(150),
  city varchar(50),
  province varchar(100),
  country varchar(100),
  postal_code varchar(15),
  email varchar(50),
  phone_number varchar(20),
  website varchar(255),
  password_hint varchar(100),
  account_enabled bool,
  account_expired bool,
  account_locked bool,
  credentials_expired bool,
  CONSTRAINT app_user_pkey PRIMARY KEY (id),
  CONSTRAINT app_user_email_key UNIQUE (email),
  CONSTRAINT app_user_username_key UNIQUE (username)
)
WITHOUT OIDS;

CREATE TABLE "role"
(
  id int4 NOT NULL DEFAULT nextval('role_seq'::regclass),
  name varchar(20) NOT NULL,
  description varchar(64),
  CONSTRAINT role_pkey PRIMARY KEY (id)
)
WITHOUT OIDS;


CREATE TABLE user_role
(
  user_id int4 NOT NULL,
  role_id int4 NOT NULL,
  CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk143bf46a14048cb4 FOREIGN KEY (role_id)
      REFERENCES "role" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk143bf46af02988d6 FOREIGN KEY (user_id)
      REFERENCES app_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) 
WITHOUT OIDS;
;