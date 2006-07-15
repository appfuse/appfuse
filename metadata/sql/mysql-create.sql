create database if not exists @DB-NAME@;
grant all privileges on @DB-NAME@.* to @DB-USERNAME@@"%" identified by "@DB-PASSWORD@";
grant all privileges on @DB-NAME@.* to @DB-USERNAME@@localhost identified by "@DB-PASSWORD@";

-- Extra user/host privilege added to account for Fedora Core default hostname quirk.
grant all privileges on @DB-NAME@.* to @DB-USERNAME@@localhost.localdomain identified by "@DB-PASSWORD@";

-- You may have to explicitly define your hostname in order for things
-- to work correctly.  For example:
-- grant all privileges on @DB-NAME@.* to @DB-USERNAME@@host.domain.com identified by "@DB-PASSWORD@";