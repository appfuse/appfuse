create database if not exists @dbname@;
grant all privileges on @dbname@.* to test@"%" identified by "test";
grant all privileges on @dbname@.* to test@localhost identified by "test";

-- Extra user/host privilege added to account for Fedora Core default hostname quirk.
grant all privileges on @dbname@.* to test@localhost.localdomain identified by "test";

-- You may have to explicitly define your hostname in order for things
-- to work correctly.  For example:
-- grant all privileges on @dbname@.* to test@host.domain.com identified by "test";