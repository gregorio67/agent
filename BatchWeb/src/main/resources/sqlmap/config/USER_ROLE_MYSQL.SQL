DROP TABLE USER_ROLE;

CREATE TABLE USER_ROLE (
   
   ROLE_ID          BIGINT(5) NOT NULL,
   MENU_ID          BIGINT(10) NOT NULL, 	
   USE_YN  			VARCHAR(1),
   REG_USER         VARCHAR(50),
   REG_DATE         DATETIME,
   constraint HOST_INFO_UN unique (ROLE_ID, MENU_ID)     
 );
 
 ALTER TABLE USER_ROLE
 ADD CONSTRAINT PK_USER_ROLE PRIMARY KEY (ROLE_ID, MENU_ID);
  

--Admin User Role
 TRUNCATE TABLE USER_ROLE;
INSERT INTO USER_ROLE VALUES(1, 100, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 101, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 102, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 200, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 300, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 301, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 302, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 400, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 401, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 500, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 501, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(1, 502, 'Y', '', NOW());


--General User Role
INSERT INTO USER_ROLE VALUES(2, 100, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(2, 101, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(2, 200, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(2, 500, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(2, 501, 'Y', '', NOW());
INSERT INTO USER_ROLE VALUES(2, 502, 'Y', '', NOW());

COMMIT;


