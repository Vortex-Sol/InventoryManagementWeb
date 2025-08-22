-- Warehouses
INSERT INTO
    Warehouse (ID, Phone, Email, Address)
VALUES
    (0, '+48123456789', 'warehouse0@gmail.com', '123 Main St, Berlin'),
    (1, '+48234567891', 'warehouse1@gmail.com','456 Market Ave, Munich'),
    (2, '+48345678901', 'warehouse2@gmail.com', '789 Szczesliwicka, Warsaw');

-- Jobs
INSERT INTO
    Job (Name, Description)
VALUES
    ('TBD', 'To Be Determined'),
    ('SALESMAN', 'Handles customer purchases.'),
    ('MANAGER', 'Oversees operations.'),
    ('STOCKER', 'Manages inventory.'),
    ('ADMIN', 'Oversees Application'),
    ('SUPERADMIN', 'Superior Admin');

-- Employees
INSERT INTO
    Employee (Username, Password, Name, Surname, DoB, Email, Phone, Start_Date, End_Date, Boss_ID, Warehouse_ID)
VALUES
    ('super', '$2a$10$Jj5/CMDhocYIWQX4r.93H.rfCkbsiQ3twLJZFd5osi9RyJe09VH7G', 'Clark', 'Kent', '1985-05-02', 'super@gmail.com', '555-0000', '2019-12-30', NULL, NULL, 0),
    ('admin', '$2a$10$Jj5/CMDhocYIWQX4r.93H.rfCkbsiQ3twLJZFd5osi9RyJe09VH7G', 'John', 'Doe', '1990-01-15', 'jdoe@example.com', '555-1111', '2020-01-01', NULL, 1, 1),
    ('manager', '$2a$10$Jj5/CMDhocYIWQX4r.93H.rfCkbsiQ3twLJZFd5osi9RyJe09VH7G', 'Clark', 'Kent', '1992-07-10', 'ckent@example.com', '555-4444', '2023-02-01', NULL, 1, 0),
    ('salesman', '$2a$10$Jj5/CMDhocYIWQX4r.93H.rfCkbsiQ3twLJZFd5osi9RyJe09VH7G', 'Alice', 'Smith', '1985-06-20', 'asmith@example.com', '555-2222', '2021-05-01', NULL, 3, 2),
    ('stocker', '$2a$10$Jj5/CMDhocYIWQX4r.93H.rfCkbsiQ3twLJZFd5osi9RyJe09VH7G', 'Bruce', 'Wayne', '1980-03-30', 'bwayne@example.com', '555-3333', '2019-11-15', NULL, 3, 1);

-- Note: Get the auto-generated employee IDs using SELECTs if needed in Java code.

-- Employee Jobs (assumes IDs inserted in order)

INSERT INTO
    Employee_Job (Employee_ID, Job_ID)
VALUES
    (1, 6),
    (2, 5),
    (3, 3),
    (4, 2),
    (5, 4);

INSERT INTO
    Category (Name)
VALUES
    ('A'),
    ('B'),
    ('C'),
    ('D'),
    ('E');

INSERT INTO
    Tax_Rate (Country, Standard_Rate, Reduced_Rate, Super_Reduced_Rate, None_Rate, Other_Rate)
VALUES
    ('DENMARK', 25, null, null, null,null),
    ('IRELAND', 23, 13.5, 9, null,4.8),
    ('POLAND', 23, 8, 5, null,null),
    ('AUSTRIA', 20, 13, 10, null,null),
    ('GERMANY', 19, 9, null, null,null);

-- Items
INSERT INTO
    Item (Name, Description, Price, Barcode, Category_ID)
VALUES
    ('Laptop', 'Gaming laptop with 16GB RAM', 1200.50, 415515516, 1),
    ('Mouse', 'Wireless ergonomic mouse', 25.99, 512656616, 2),
    ('Keyboard', 'Mechanical RGB keyboard', 75.25, 6126261969, 2),
    ('Monitor', '27\" 4K monitor', 299.99, 525196596, 1);

-- Warehouse Items (assumes item and warehouse IDs inserted in order)
INSERT INTO
    Warehouse_Items (Warehouse_ID, Item_ID, Quantity_in_Stock, SKU)
VALUES
    (1, 1, 5, '001-001-AF32XA'),
    (2, 1, 5, '001-001-AF32XA'),
    (0, 1, 5, '001-001-AF32XA'),
    (1, 2, 20, '001-002-KFAM8M'),
    (2, 3, 10, '002-002-AAA12G'),
    (2, 4, 7, '002-001-DD00C4');

-- Sales
INSERT INTO
    Sale (Sale_Time, Salesman_ID)
VALUES
    ('2025-07-20 10:00:00', 2),
    ('2025-07-21 15:30:00', 4),
    ('2025-07-22 09:15:00', 1),
    ('2025-08-11 11:50:00', 1),
    ('2025-08-11 11:55:00', 1);

-- Item Sales
INSERT INTO
    Item_Sale (Item_ID, Sale_ID, Quantity_Sold)
VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 2, 1),
    (4, 3, 1),
    (2, 4, 4),
    (1, 4, 1),
    (3, 5, 2);

INSERT INTO
    Settings (Warehouse_ID, Alert_When_Stock_Is_Low, Auto_Generate_Report, Auto_Generate_Report_Time, Notify_Minimum_Cash_Discrepancy, Destroy_Refund_Data_After_N_Days, Cash_Count_Start_Time, Cash_Count_End_Time, Auto_Generate_Inventory_Report_Time, Tax_Rate_id)
VALUES
    (0, FALSE, TRUE, '00:00:00', 500, 14, '06:00:00', '23:00:00', '23:00:00', 1),
    (1, FALSE, TRUE, '00:00:00', 500, 14, '06:00:00', '23:00:00', '23:00:00', 2),
    (2, FALSE, TRUE, '00:00:00', 500, 14, '06:00:00', '23:00:00', '23:00:00', 3);

INSERT INTO
    Settings_Change_Audit (Setting_ID, Warehouse_ID, Admin_ID, Changed_At, Settings_Changed)
VALUES
    (1, 1, 1, DEFAULT,'ALL');

INSERT INTO
    Item_Change_Log (Item_ID, Stocker_ID, Warehouse_ID, Changed_At, Data_Changed)
VALUES
    (1, 5, 1, DEFAULT, 'ALL'),
    (4, 5, 1, DEFAULT, 'ALL');

INSERT INTO
    Receipt (Sale_ID, Total_Amount, Payment_Method, Amount_Received, Change_Given, Is_Cancelled, Cancelled_At, Cancelled_By)
VALUES
    (1, 25.500, 'Cash', 30.000, 4.500, FALSE, NULL, NULL),
    (2, 99.990, 'Card', 99.990, 0.000, FALSE, NULL, NULL),
    (3, 15.750, 'Cash', 20.000, 4.250, TRUE, '2025-08-10 14:32:00', 2),
    (4, 45.000, 'Mobile Payment', 50.000, 5.000, FALSE, NULL, NULL),
    (5, 120.300, 'Card', 120.300, 0.000, TRUE, '2025-08-12 09:15:00', 1);

