CREATE TABLE CUSTOMER (
		CUSTOMER_ID INTEGER NOT NULL,
		DISCOUNT_CODE CHAR(1) NOT NULL,
		ZIP VARCHAR(10) NOT NULL,
		NAME VARCHAR(30),
		ADDRESSLINE1 VARCHAR(30),
		ADDRESSLINE2 VARCHAR(30),
		CITY VARCHAR(25),
		STATE CHAR(2),
		PHONE CHAR(12),
		FAX CHAR(12),
		EMAIL VARCHAR(40),
		CREDIT_LIMIT INTEGER
	);

CREATE UNIQUE INDEX SQL130323125543660 ON CUSTOMER (CUSTOMER_ID ASC);

ALTER TABLE CUSTOMER ADD CONSTRAINT SQL130323125543660 PRIMARY KEY (CUSTOMER_ID);
