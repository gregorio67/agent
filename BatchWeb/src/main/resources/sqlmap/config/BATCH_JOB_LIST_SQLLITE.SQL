DROP TABLE BATCH_JOB_LIST;

CREATE TABLE BATCH_JOB_LIST
(
  JOB_ID                     INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT ,     
  JOB_NAME                   VARCHAR(100) NOT NULL,
  JOB_PATH                   VARCHAR(100) NOT NULL,
  JOB_DEFAULT_PARAM          VARCHAR(100),
  JOB_EXEUCTE_PERIOD         VARCHAR(50) NOT NULL,
  JOB_EXEUCTE_PERIOD_TEXT    VARCHAR(100),
  JOB_SHELL_LOC              VARCHAR(100) NOT NULL,
  JOB_SHELL_NAME             VARCHAR(100) NOT NULL,
  JOB_AGENT_HOST             VARCHAR(50),
  JOB_AGENT_PORT             BIGINT(7),
  USE_YN                     VARCHAR(1),
  PREV_JOB_ID                BIGINT(10),
  NEXT_JOB_ID                BIGINT(10),
  REG_USER                   VARCHAR(50),
  REG_DATE                   TIMESTAMP,
  constraint JOB_LIST_UN unique (JOB_NAME)
);




SELECT * FROM USER_SEQUENCES WHERE SEQUENCE_NAME = 'SEQ_JOB_LIST';
INSERT INTO BATCH_JOB_LIST VALUES(1, 'SampleDBFileJob', '/jobs/dur/sample/SampleDB2FileJob.xml', 'outFile=file:D:/batch/result/message.txt|useFlag=Y','0 0/1 * * * ?', '', 'D:/batch', 'BatchRun.cmd', '172.6.14.252', '9090', '','','Y','','');
