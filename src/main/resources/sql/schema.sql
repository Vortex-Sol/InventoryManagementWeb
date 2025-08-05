-- === TABLES ===

CREATE TABLE Employee (
                          ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                          Username VARCHAR(50) NOT NULL,
                          Password VARCHAR(100) NOT NULL,
                          Name VARCHAR(50) NOT NULL,
                          Surname VARCHAR(50) NOT NULL,
                          DoB DATE NOT NULL,
                          Email VARCHAR(250) NOT NULL,
                          Phone VARCHAR(250) NOT NULL,
                          Start_Date DATE NOT NULL,
                          End_Date DATE,
                          Boss_ID BIGINT,
                          Warehouse_ID BIGINT NOT NULL
);

CREATE TABLE Job (
                     ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                     Name VARCHAR(50) NOT NULL,
                     Description VARCHAR(500) NOT NULL
);

CREATE TABLE Employee_Job (
                              Employee_ID BIGINT NOT NULL,
                              Job_ID BIGINT NOT NULL,
                              PRIMARY KEY (Employee_ID, Job_ID)
);

CREATE TABLE Login_Audit (
                             ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                             Username VARCHAR(50) NOT NULL,
                             IP_Address VARCHAR(100) NOT NULL,
                             Login_Time TIMESTAMP NOT NULL,
                             Success_Failure BOOLEAN NOT NULL,
                             employee_id BIGINT NOT NULL,
                             CONSTRAINT fk_audit_employee
                                 FOREIGN KEY (employee_id)
                                     REFERENCES Employee(ID)
);

CREATE TABLE Employee_Login_Audit (
                                      Login_Audit_ID BIGINT NOT NULL,
                                      Employee_ID BIGINT NOT NULL,
                                      PRIMARY KEY (Login_Audit_ID, Employee_ID)
);

CREATE TABLE Warehouse (
                           ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                           Address VARCHAR(250) NOT NULL
);

CREATE TABLE Item (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Description VARCHAR(200) NOT NULL,
    Barcode BIGINT NOT NULL,
    Sku VARCHAR(50),
    Price DECIMAL(10,2) NOT NULL
);

CREATE TABLE Sale (
                      ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                      Sale_Time TIMESTAMP NOT NULL,
                      Salesman_ID BIGINT NOT NULL
);

CREATE TABLE Item_Sale (
                           Item_ID BIGINT NOT NULL,
                           Sale_ID BIGINT NOT NULL,
                           Quantity_Sold INT NOT NULL,
                           PRIMARY KEY (Sale_ID, Item_ID)
);

CREATE TABLE Warehouse_Items (
                                 Warehouse_ID BIGINT NOT NULL,
                                 Item_ID BIGINT NOT NULL,
                                 Quantity_in_Stock INT NOT NULL,
                                 PRIMARY KEY (Warehouse_ID, Item_ID)
);

CREATE TABLE Report (
                        ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                        Type VARCHAR(50) NOT NULL,
                        Employee_ID_Created BIGINT NOT NULL,
                        Data CLOB NOT NULL,
                        Created_At_Warehouse_ID BIGINT NOT NULL
);

CREATE TABLE Receipt (
                         ID BIGINT AUTO_INCREMENT PRIMARY KEY,
                         Sale_ID BIGINT NOT NULL,
                         Total_Amount DECIMAL(10, 3) NOT NULL,
                         Created_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         Payment_Method VARCHAR(50) NOT NULL
);

-- === FOREIGN KEYS ===

ALTER TABLE Employee ADD FOREIGN KEY (Boss_ID) REFERENCES Employee(ID);
ALTER TABLE Employee ADD FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Employee_Job ADD FOREIGN KEY (Employee_ID) REFERENCES Employee(ID);
ALTER TABLE Employee_Job ADD FOREIGN KEY (Job_ID) REFERENCES Job(ID);
ALTER TABLE Sale ADD FOREIGN KEY (Salesman_ID) REFERENCES Employee(ID);
ALTER TABLE Item_Sale ADD FOREIGN KEY (Item_ID) REFERENCES Item(ID);
ALTER TABLE Item_Sale ADD FOREIGN KEY (Sale_ID) REFERENCES Sale(ID);
ALTER TABLE Warehouse_Items ADD FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Warehouse_Items ADD FOREIGN KEY (Item_ID) REFERENCES Item(ID);
ALTER TABLE Report ADD FOREIGN KEY (Employee_ID_Created) REFERENCES Employee(ID);
ALTER TABLE Report ADD FOREIGN KEY (Created_At_Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Receipt ADD FOREIGN KEY (Sale_ID) REFERENCES Sale(ID)
