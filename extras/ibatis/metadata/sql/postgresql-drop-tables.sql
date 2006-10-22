-- Tried to do test for existence before dropping with the following script, 
-- but it didn't work: http://www.thescripts.com/forum/thread173559.html

DROP TABLE user_role;
DROP TABLE app_user;
DROP TABLE "role";

DROP SEQUENCE user_role_id;
DROP SEQUENCE app_user_seq;
DROP SEQUENCE role_seq;