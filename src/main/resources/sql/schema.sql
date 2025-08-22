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
    CONSTRAINT fk_login_audit_employee
        FOREIGN KEY (employee_id)
            REFERENCES Employee(ID)
);

CREATE TABLE Logout_Audit (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) NOT NULL,
    IP_Address VARCHAR(100) NOT NULL,
    Logout_Time TIMESTAMP NOT NULL,
    Logout_Reason VARCHAR(50) NOT NULL,
    employee_id BIGINT NOT NULL,
    CONSTRAINT fk_logout_audit_employee
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
    Phone VARCHAR(20) NOT NULL,
    Email Varchar(50) NOT NULL,
    Address VARCHAR(250) NOT NULL
);

CREATE TABLE Category(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(20) NOT NULL
);

CREATE TABLE Tax_Rate
(
    ID              BIGINT AUTO_INCREMENT PRIMARY KEY,
    Country         VARCHAR(50) NOT NULL,
    Standard_Rate DECIMAL(10, 3) NOT NULL,
    Reduced_Rate DECIMAL(10, 3),
    Super_Reduced_Rate DECIMAL(10, 3),
    None_Rate DECIMAL(10, 3),
    Other_Rate DECIMAL(10, 3)
);

CREATE TABLE Settings(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Warehouse_id BIGINT NOT NULL,
    Alert_When_Stock_Is_Low BOOLEAN NOT NULL,
    Auto_Generate_Report BOOLEAN NOT NULL,
    Auto_Generate_Report_Time TIME,
    CHECK (
        Auto_Generate_Report = FALSE OR Auto_Generate_Report_Time IS NOT NULL
    ),
    Notify_Minimum_Cash_Discrepancy DECIMAL(10, 2) NOT NULL,
    Destroy_Refund_Data_After_N_Days SMALLINT NOT NULL,
    Cash_Count_Start_Time TIME NOT NULL,
    Cash_Count_End_Time TIME NOT NULL,
    Auto_Generate_Inventory_Report_Time TIME NOT NULL,
    Tax_Rate_id BIGINT

);

CREATE TABLE Settings_Change_Audit(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Setting_ID BIGINT NOT NULL,
    Warehouse_ID BIGINT NOT NULL,
    Admin_ID BIGINT NOT NULL,
    Changed_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Settings_Changed VARCHAR(2000) NOT NULL
);

CREATE TABLE Item (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(50) NOT NULL,
    Description VARCHAR(200) NOT NULL,
    Barcode BIGINT NOT NULL,
    Price DECIMAL(10,2) NOT NULL,
    Category_ID BIGINT NOT NULL
);

CREATE TABLE Item_Change_Log(
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    Item_ID BIGINT NOT NULL,
    Stocker_ID BIGINT NOT NULL,
    Warehouse_ID BIGINT NOT NULL,
    Changed_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    Data_Changed VARCHAR(2000) NOT NULL
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
    SKU VARCHAR(50) NOT NULL,
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
     Total_Amount DECIMAL(10, 2) NOT NULL,
     Created_At TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     Payment_Method VARCHAR(50) NOT NULL,
     Amount_Received DECIMAL(10, 2),
     Change_Given DECIMAL(10, 2),
     Is_Cancelled BOOLEAN NOT NULL DEFAULT FALSE,
     Cancelled_At TIMESTAMP,
     Cancelled_By BIGINT
);



-- === FOREIGN KEYS ===

ALTER TABLE Employee ADD FOREIGN KEY (Boss_ID) REFERENCES Employee(ID);
ALTER TABLE Employee ADD FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Employee_Job ADD FOREIGN KEY (Employee_ID) REFERENCES Employee(ID);
ALTER TABLE Employee_Job ADD FOREIGN KEY (Job_ID) REFERENCES Job(ID);
ALTER TABLE Sale ADD FOREIGN KEY (Salesman_ID) REFERENCES Employee(ID);
ALTER TABLE Item ADD FOREIGN KEY (Category_ID) REFERENCES Category(ID);
ALTER TABLE Item_Sale ADD FOREIGN KEY (Item_ID) REFERENCES Item(ID);
ALTER TABLE Item_Sale ADD FOREIGN KEY (Sale_ID) REFERENCES Sale(ID);
ALTER TABLE Warehouse_Items ADD FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Warehouse_Items ADD FOREIGN KEY (Item_ID) REFERENCES Item(ID);
ALTER TABLE Report ADD FOREIGN KEY (Employee_ID_Created) REFERENCES Employee(ID);
ALTER TABLE Report ADD FOREIGN KEY (Created_At_Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Receipt ADD FOREIGN KEY (Sale_ID) REFERENCES Sale(ID);
ALTER TABLE Receipt ADD FOREIGN KEY (Cancelled_By) REFERENCES Employee(ID);
ALTER TABLE Settings ADD FOREIGN KEY (Warehouse_id) REFERENCES Warehouse(ID);
ALTER TABLE Settings ADD FOREIGN KEY (Tax_Rate_id) REFERENCES Tax_Rate(ID)ON DELETE SET NULL;
ALTER TABLE Settings_Change_Audit ADD FOREIGN KEY (Setting_ID) REFERENCES Settings(ID);
ALTER TABLE Settings_Change_Audit ADD FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(ID);
ALTER TABLE Settings_Change_Audit ADD FOREIGN KEY (Admin_ID) REFERENCES Employee(ID);
ALTER TABLE Item_Change_Log ADD FOREIGN KEY (Item_ID) REFERENCES Item(ID);
ALTER TABLE Item_Change_Log ADD FOREIGN KEY (Stocker_ID) REFERENCES Employee(ID);
ALTER TABLE Item_Change_Log ADD FOREIGN KEY (Warehouse_ID) REFERENCES Warehouse(ID);
