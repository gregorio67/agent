DROP TABLE MENU_LIST;
CREATE TABLE MENU_LIST (
   MENU_ID        NUMBER(10) NOT NULL,
   UPPER_MENU_ID    NUMBER(10) NOT NULL,
   MENU_NAME      VARCHAR2(100) NOT NULL,
   MENU_NAME_EN      VARCHAR2(100) NOT NULL,
   MENU_ACTION      VARCHAR2(100),
   MENU_DESCRIPTION   VARCHAR2(200),
   USE_YN               VARCHAR2(1),
   REG_USER             VARCHAR2(50),
   REG_DATE             TIMESTAMP  
 );

 ALTER TABLE MENU_LIST
 DROP CONSTRAINT PK_MENU_LIST ;
 
 ALTER TABLE MENU_LIST
  ADD CONSTRAINT PK_MENU_LIST PRIMARY KEY (MENU_ID);
  

TRUNCATE TABLE MENU_LIST;
INSERT INTO MENU_LIST VALUES(100, -1, '작업관리','Job Management', '', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(101, 100, '작업등록', 'Job Registratoin', '/job/jobschedule.do', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(102, 100, '작업승인','Job Approve', '/job/jobapprove.do', '', 'Y', '', ''); 

INSERT INTO MENU_LIST VALUES(200, -1, '작업결과', 'Job Result', '/job/jobresult.do', '', 'Y', '', ''); 

INSERT INTO MENU_LIST VALUES(300, -1, '사용자 관리','User Management', '', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(301, 300, '사용자 등록', 'User Registratoin', '/job/userregistration.do', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(302, 300, '사용자 승인', 'User Approve', '/job/userapprove.do', '', 'Y', '', ''); 

INSERT INTO MENU_LIST VALUES(400, -1, '메뉴 관리','Menu Management', '', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(401, 400, '메뉴승인', 'Menu Registratoin', '/job/menuregistration.do', '', 'Y', '', ''); 


INSERT INTO MENU_LIST VALUES(500, -1, '시스템 모니터링','System Monitoring', '', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(501, 500, '모니터링', 'Monitoring', '/system/monitoring.do', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(502, 500, 'Thread Dump', 'Thread Dump', '/system/threadDump,do', '', 'Y', '', ''); 

commit;


SELECT * FROM MENU_LIST;