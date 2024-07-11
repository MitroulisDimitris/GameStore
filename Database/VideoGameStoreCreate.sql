create table Games(
    gid number not null,
    title varchar2(40) not null,
    desciption varchar2(1000) not null,
    dateReleased date not null,
    url varchar2(100) not null,
    rating number not null,
    discSpace number not null, 
    cpu varchar2(100) not null,
    os varchar2(100) not null,
    ram varchar2(20) not null,
    gpu varchar2(100) not null,
    discounted number not null,
    videoUrl varchar2(100),
    creator varchar2(50) not null,
    Price number not null,
    category varchar2(20),
    profileurl varchar(50),
Primary Key(gid));


CREATE SEQUENCE mid
MINVALUE 1
START WITH 10
INCREMENT BY 1
CACHE 10;



  CREATE TABLE MEMBER
   (	MID NUMBER NOT NULL  , 
	FNAME VARCHAR2(20 BYTE) NOT NULL  , 
	LNAME VARCHAR2(20 BYTE) NOT NULL  , 
	CNAME VARCHAR2(40 BYTE), 
	CREDIDNUM NUMBER(16,0), 
	EXPDATE DATE, 
	CVV NUMBER(3,0), 
	EMAIL VARCHAR2(30 BYTE) NOT NULL  , 
	PASSWORD VARCHAR2(20 BYTE) NOT NULL  , 
    USERNAME VARCHAR2(20 BYTE) NOT NULL  , 
	TIER NUMBER, 
	DATEJOINED DATE, 
	IMGURL VARCHAR2(200 BYTE), 
    PRIMARY KEY ("MID"),
    UNIQUE(username,email));



CREATE SEQUENCE rents_increment
MINVALUE 1
START WITH 10
INCREMENT BY 1
CACHE 10;


CREATE TABLE RENTS(
   	RENTID NUMBER NOT NULL , 
	MID NUMBER NOT NULL , 
	GID NUMBER NOT NULL , 
	 RENTDATE DATE NOT NULL , 
	 PRIMARY KEY (rentid,MID, GID, RENTDATE));



