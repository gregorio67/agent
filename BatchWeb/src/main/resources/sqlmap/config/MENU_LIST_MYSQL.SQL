DROP TABLE MENU_LIST;
CREATE TABLE MENU_LIST (
   MENU_ID        		BIGINT(10) NOT NULL,
   UPPER_MENU_ID    	BIGINT(10) NOT NULL,
   MENU_NAME      		VARCHAR(100) NOT NULL,
   MENU_NAME_EN     	VARCHAR(100) NOT NULL,
   MENU_ACTION      	VARCHAR(100),
   MENU_DESCRIPTION   	VARCHAR(200),
   USE_YN               VARCHAR(1),
   REG_USER             VARCHAR(50),
   REG_DATE             TIMESTAMP,
   constraint MENU_LIST_UN unique (MENU_ID)     
 );


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

INSERT INTO MENU_LIST VALUES(600, -1, '소스 관리','Source ManageMent', '', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(601, 600, '소스 검색', 'Source Search', '/source/sourceSearch.do', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(602, 600, '배포 소스 검색', 'Deploy Source Search', '/source/deploySearch.do', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(603, 600, '배포 승인', 'Deploy Approve', '/source/deployApprove.do', '', 'Y', '', ''); 
INSERT INTO MENU_LIST VALUES(604, 600, '소스 배포', 'Deploy Source', '/source/deploySource.do', '', 'Y', '', ''); 

commit;


SELECT * FROM MENU_LIST;