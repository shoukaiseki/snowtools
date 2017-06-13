CREATE
  TABLE SHOUKAISEKI_INSERT_SISANN
  (
    "ID"          NUMBER NOT NULL ENABLE,
    "COLUMN1"     VARCHAR2(300 BYTE),
    "COLUMN2"     VARCHAR2(300 BYTE),
    "COLUMN3"     VARCHAR2(300 BYTE),
    "COLUMN4"     VARCHAR2(300 BYTE),
    "COLUMN5"     VARCHAR2(300 BYTE),
    "COLUMN6"     VARCHAR2(300 BYTE),
    "COLUMN7"     VARCHAR2(300 BYTE),
    "COLUMN8"     VARCHAR2(300 BYTE),
    "COLUMN9"     VARCHAR2(300 BYTE),
    "COLUMN10"    VARCHAR2(300 BYTE),
    "COLUMN11"    VARCHAR2(300 BYTE),
    "COLUMN12"    VARCHAR2(300 BYTE),
    "COLUMN13"    VARCHAR2(300 BYTE),
    "COLUMN14"    VARCHAR2(300 BYTE),
    "COLUMN15"    VARCHAR2(300 BYTE),
    "COLUMN16"    VARCHAR2(300 BYTE),
    "COLUMN17"    VARCHAR2(300 BYTE),
    "COLUMN18"    VARCHAR2(300 BYTE),
    "COLUMN19"    VARCHAR2(300 BYTE),
    "COLUMN20"    VARCHAR2(300 BYTE),
    "COLUMN21"    VARCHAR2(300 BYTE),
    "COLUMN22"    VARCHAR2(300 BYTE),
    "COLUMN23"    VARCHAR2(300 BYTE),
    "COLUMN24"    VARCHAR2(300 BYTE),
    "COLUMN25"    VARCHAR2(300 BYTE),
    "COLUMN26"    VARCHAR2(300 BYTE),
    "COLUMN27"    VARCHAR2(300 BYTE),
    "COLUMN28"    VARCHAR2(300 BYTE),
    "COLUMN29"    VARCHAR2(300 BYTE),
    "COLUMN30"    VARCHAR2(300 BYTE)
	);

	
	
		//創建臨時序列,用於給 CLASSSTRUCTUREUID 編號
 create sequence TESTCLASSSTRUCTUREIDSEQ minvalue 1000 maxvalue  99999999 start with 1003 increment by 1 nocache;
	
	
--資產分類表	
CREATE TABLE SHOUKAISEKI_INSERT_SISANN_FL 
(
  COLUMN1 VARCHAR2(200) 
, COLUMN2 VARCHAR2(200) 
, COLUMN3 VARCHAR2(200) 
, COLUMN4 VARCHAR2(200) 
, COLUMN5 VARCHAR2(200) 
, COLUMN6 VARCHAR2(200) 
, ID NUMBER NOT NULL 
, CONSTRAINT SHOUKAISEKI_INSERT_SISANN_PK PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);
	