-- Warehouses
INSERT INTO Warehouse (ID, Address) VALUES
                                   (0, 'TBD'),
                                   (1, '123 Main St, Berlin'),
                                   (2, '456 Market Ave, Munich');

-- Jobs
INSERT INTO Job (Name, Description) VALUES
                                        ('TBD', 'To Be Determined'),
                                        ('Salesman', 'Handles customer purchases.'),
                                        ('Manager', 'Oversees operations.'),
                                        ('Stocker', 'Manages inventory.');

-- Employees
INSERT INTO Employee (Username, Password, Name, Surname, DoB, Email, Phone, Start_Date, End_Date, Boss_ID, Warehouse_ID) VALUES
                                                                                                                                   ('admin', '$2a$10$Jj5/CMDhocYIWQX4r.93H.rfCkbsiQ3twLJZFd5osi9RyJe09VH7G', 'John', 'Doe', '1990-01-15', 'jdoe@example.com', '555-1111', '2020-01-01', NULL, NULL, 1),
                                                                                                                                   ('asmith', 'hashpass456', 'Alice', 'Smith', '1985-06-20', 'asmith@example.com', '555-2222', '2021-05-01', NULL, NULL, 2),
                                                                                                                                   ('bwayne', 'hashpass789', 'Bruce', 'Wayne', '1980-03-30', 'bwayne@example.com', '555-3333', '2019-11-15', NULL, 1, 1),
                                                                                                                                   ('ckent', 'hashpass999', 'Clark', 'Kent', '1992-07-10', 'ckent@example.com', '555-4444', '2023-02-01', NULL, 2, 2);

-- Note: Get the auto-generated employee IDs using SELECTs if needed in Java code.

-- Employee Jobs (assumes IDs inserted in order)
INSERT INTO Employee_Job (Employee_ID, Job_ID) VALUES
                                                   (1, 2),
                                                   (2, 1),
                                                   (3, 3),
                                                   (4, 1);

-- Items
INSERT INTO Item (Name, Description, Price, Barcode) VALUES
                                                     ('Laptop', 'Gaming laptop with 16GB RAM', 1200.50, 415515516),
                                                     ('Mouse', 'Wireless ergonomic mouse', 25.99, 512656616),
                                                     ('Keyboard', 'Mechanical RGB keyboard', 75.25, 6126261969),
                                                     ('Monitor', '27\" 4K monitor', 299.99, 525196596);

-- Warehouse Items (assumes item and warehouse IDs inserted in order)
INSERT INTO Warehouse_Items (Warehouse_ID, Item_ID, Quantity_in_Stock) VALUES
                                                                           (1, 1, 5),
                                                                           (1, 2, 20),
                                                                           (2, 3, 10),
                                                                           (2, 4, 7);

-- Sales
INSERT INTO Sale (Sale_Time, Salesman_ID) VALUES
                                              ('2025-07-20 10:00:00', 2),
                                              ('2025-07-21 15:30:00', 4),
                                              ('2025-07-22 09:15:00', 1);

-- Item Sales
INSERT INTO Item_Sale (Item_ID, Sale_ID, Quantity_Sold) VALUES
                                                            (1, 1, 1),
                                                            (2, 1, 2),
                                                            (3, 2, 1),
                                                            (4, 3, 1);
