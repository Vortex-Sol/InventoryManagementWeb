# 📦 Inventory Management Web App – Vortex Solution

A web-based inventory management system built to streamline warehouse operations and sales tracking. This project serves as a proof-of-concept (POC) with plans for future integration of barcode scanning and POS systems.

---

## Project Goals & Vision

### Version 1: Proof of Concept (Current)
- Add, remove, or sell items from inventory via web interface.

### Version 2: Barcode Integration
- Automatically generate unique SKU for new items.
- Print barcodes for tagging products.

### Version 3: Barcode Scanning
- Use USB barcode scanner to retrieve item info.
- Update inventory and finance tables upon sales or removal.

### Version 4: Enterprise POS Integration *(Future Goal)*
- Integrate with POS systems to process payments.
- Automatically reduce inventory and record sales upon payment success.

---

## Prerequisites

- **Java** 17 or higher
- **Gradle** (Wrapper included)
- **MySQL** (Database setup required >> FUTURE)

---

## Contributing
To contribute, please follow the steps below to ensure a smooth and successful submission

### Setup

1. **Clone the Repository**
Clone the repository to your local machine in your desired path (traverse to it using cd in terminal):
    ```bash
    git clone https://github.com/Vortex-Sol/InventoryManagementWeb.git
    cd InventoryManagementWeb   
    ```

2. **Create a New Branch**
Create a new branch to isolate your changes & features. Use a descriptive branch name:
    ```bash
    git checkout -b feature.name-category
    ```

Where:
   - Feature is the code you are working on
   - Category is the status of the feature; **use**: 
     - _fix_ [You are fixing a bug]
     - _patch_ [You forgot to add something] 
     - _update_ [You are creating a new feature] 
     - _temp_ [You are temporarily introducing a feature -> to be removed later]
     
Example branch name:
```
git checkout -b report.system-update
```
    
> **Tip:** Avoid making changes on the main branch explicitly

### Submitting Changes
1. **Make Your Changes**
   - Make your edits or additions to the codebase
   - Ensure your code is **formatted properly** and follows standard style guides
   - **Write test**, if applicable, for any new functionality
   - Verify everything works correctly before commiting

2. **Fetch Newest  Main**
    - Make sure you are on the newest main
    - Do ```git fetch origin main```
    - To resolve any merge conflicts >> contact the team
   
3. **Commit Your Changes**
Use clear, descriptive commit messages
    ```bash
    git add .
    git commit -m "Update: implemented Report System" -m "I created the appropriate Mappers, DTOs and Models. We are missing a service class"
    ```
4. **Push to Your Fork**
Push changes to your forked repository:
    ```bash
    git push origin report.system-update
    ```

5. **Open a Pull Request (PR)**
- Go to the original repository on GitHub
- Click "Compare & Pull Request" next to your recently pushed branch
- Title should be relevant feature issue
  - for e.g., **[3B] Report System**
- Provide a detailed description of your changes and what they do
  - for e.g., _The report system creates reports based on period or time for employees, receipts, inventory or all. A report includes...._ 
- Submit the pull request for review
  - assign 2 team members as reviewers to review your changes
  - assign yourself as an assignee
  - assign a label to the pull request

6. **Respond to Review Feedback**
- Be responsive to any comments, questions or change requests from the team members
- Push follow-up commits to the same branch; the PR will update automatically
- Finally merge to main once everyone approve

---

## Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Vortex-Sol/InventoryManagementWeb.git
   cd InventoryManagementWeb
   ```
   
2. **Build the Application**
    ```bash
    ./gradlew build
    ```
    
3. **Run the App**
    ```bash
    ./gradlew bootRun
    ```
    and access locally

---

## System Users

| Role     | Responsibilities                                                  |
|----------|-------------------------------------------------------------------|
| Salesman | Sell items, view sales history                                    |
| Stocker  | Add/write-off/update/delete items                                 |
| Manager  | Same as Salesman & Stocker, has access to report & receipt system |
| Admin    | Has IT settings system                                            |

---

## Technology Stack

| Layer      | Technology                                   |
|------------|----------------------------------------------|
| Frontend   | HTML, CSS, Bootstrap 5, JavaScript           |
| Templating | Thymeleaf (Server-side rendering)            |
| Backend    | Java Spring Boot (MVC)                       |
| Security   | Spring Security with JWT, Email Verification |
| ORM        | Spring Data JPA (Hibernate)                  |
| Database   | H2 (Development) & MySQL (Production)        |
| Testing    | JUnit, Mockito, Spring Test                  |

---

## Core Functionalities
Core Functionalities are tied to a user's warehouse. That is, user can only overlook and operate within his Warehouse bounds.

### Common to All Users
- Login / Logout

### Stocker
- Add Item
- Delete Item
- Edit Item
- Write-off (damaged/lost)
- Manual Stock Adjustments

### Salesman
- Sell Item
- Search by ID/Name/Barcode
- View Own Sales History

### Manager
- Salesman & Stocker Functionalities
- Capable of checking All employee data
- Employee, Sales & Inventory Reports
- Export to Google Sheets (optional)
- Dashboard with charts/statistics (optional)

### Admin
- Manages warehouse contact details
- Add/edits/removes employees
- He can set settings regarding stock, reports, cash count times and more

---

## API Endpoints

### Admin - [AdminController](src/main/java/vortex/imwp/controllers/AdminController.java)
| Method | Endpoint             | Description                      |
|--------|----------------------|----------------------------------|
| GET    | `/api/admin`         | Retrieves admin dashboard        |
| GET    | `/api/register`      | Loads Employee registration page |
| POST   | `/api/register`      | Creates Employee                 |

### Authentication - [AuthController](src/main/java/vortex/imwp/controllers/AuthController.java)
| Method | Endpoint        | Description             |
|--------|-----------------|-------------------------|
| GET    | `/auth/login`   | Retrieves login         |
| POST   | `/auth/logout`  | Logout and revoke token |

### Home - [HomeController](src/main/java/vortex/imwp/controllers/HomeController.java)
| Method | Endpoint    | Description                            |
|--------|-------------|----------------------------------------|
| GET    | `/api/home` | Redirects user to his appropriate home |

### Inventory - [InventoryController](src/main/java/vortex/imwp/controllers/InventoryController.java)

| Method | Endpoint              | Description                 |
|--------|-----------------------|-----------------------------|
| GET    | `/api/items`          | List all items (filterable) |
| GET    | `/api/items/{id}`     | Get item details            |
| POST   | `/api/items/add`      | Create new item             |
| GET    | `/api/items/search`   | Finds list of items         |
| PUT    | `/api/items/{id}`     | Update item                 |
| DELETE | `/api/items/{id}`     | Delete item                 |
| GET    | `/api/items/checkout` | Retrieves checkout page     |

### Warehouse - [WarehouseController](src/main/java/vortex/imwp/controllers/WarehouseController.java)
| Method | Endpoint         | Description                                                               |
|--------|------------------|---------------------------------------------------------------------------|
| GET    | `/api/warehouse` | Retrieves stocker's warehouse information on items, quantity with filters |

### Sales - [SaleController](src/main/java/vortex/imwp/controllers/SaleController.java)
| Method | Endpoint             | Description             |
|--------|----------------------|-------------------------|
| GET    | `/api/sales`         | List sales (filterable) |
| GET    | `/api/sales/{id}`    | Sale details            |
| POST   | `/api/sales`         | Record a new sale       |

### Employees - [EmployeeController](src/main/java/vortex/imwp/controllers/EmployeeController.java)
| Method | Endpoint              | Description               |
|--------|-----------------------|---------------------------|
| GET    | `/api/employees`      | Returns all employee      |
| GET    | `/api/employees/{id}` | Returns specific employee |
| POST   | `/api/employees`      | Create user               |
| PUT    | `/api/employees/{id}` | Update user               |
| DELETE | `/api/employees/{id}` | Delete user               |

### Receipts - [ReceiptController](src/main/java/vortex/imwp/controllers/ReceiptController)
| Method | Endpoint                           | Description                                                                                                                                |
|--------|------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------|
| GET    | `/api/receipt/checkout/{saleId}`   | Show checkout page for a given sale, including item totals and total amount.                                                               |
| GET    | `/api/receipt/confirm/{receiptId}` | View a confirmed receipt and its JSON representation.                                                                                      |
| GET    | `/api/receipt/{saleId}/add-items`  | Display page to add items to a sale, showing available items, stock, and warehouses.                                                       |
| GET    | `/api/receipt/start-checkout`      | Create a new sale for the authenticated user and redirect to the add-items page.                                                           |
| POST   | `/api/receipt/checkout`            | Process checkout for a sale with specified payment method and optional amount received; redirects to receipt confirmation or shows errors. |
| POST   | `/api/receipt/addItem-form`        | Add an item to a sale via item ID or barcode, with quantity and warehouse; redirects back to add-items page with possible errors.          |
| POST   | `/api/receipt/cancel/{receiptId}`  | Cancel a receipt; redirects to receipt confirmation page with success or error message.                                                    |

### Reports - [ReportController](src/main/java/vortex/imwp/controllers/ReportController.java)
| Method | Endpoint                        | Description                                                                               |
|--------|---------------------------------|-------------------------------------------------------------------------------------------|
| GET    | `/api/reports/employees/today`  | Generate employee activity report for today.                                              |
| GET    | `/api/reports/employees/period` | Generate employee activity report for a specific period (`start` and `end` query params). |
| GET    | `/api/reports/inventory`        | Generate inventory report for the authenticated user.                                     |
| GET    | `/api/reports/inventory/today`  | Generate inventory report for today.                                                      |
| GET    | `/api/reports/inventory/period` | Generate inventory report for a specific period (`start` and `end` query params).         | 
| GET    | `/api/reports/sales`            | Generate sales report; optionally filter by period using `start` and `end` query params.  |
| GET    | `/api/reports/sales/today`      | Generate sales report for today.                                                          |
| GET    | `/api/reports/sales/period`     | Generate sales report for a specific period (`start` and `end` query params).             |

### Settings - [SettingsController](src/main/java/vortex/imwp/controllers/SettingsController.java)
| Method | Endpoint        | Description                                                 |
|--------|-----------------|-------------------------------------------------------------|
| GET    | `/api/settings` | Retrieve current settings for the authenticated admin user. |
| POST   | `/api/settings` | Update settings for the authenticated admin user.           |


---

## Database

### Diagrams

- Entity Relationship Diagram
![ER Diagram](docs/er-diagram.png)
- Use Case Diagram
![UC Diagram](docs/uc-diagram.png)
- Class Diagram
![Class Diagram](docs/class-diagram.png)

---

### Database Schema (SQL)

> Full SQL setup for H2 is available under [schema.sql](src/main/resources/sql/schema.sql).

---

## Systems

### Audit System

### Notification System (Future)

### Report System
We can a few report types:
- salesman
- inventory
- receipts
- general (all 3)

Report can be either generated based on period, e.g. from 15th to 20th FEB, or based on Today.
For employees, it should provide employee login audits, (and logouts but we didn't implement a registree for logouts, frontend wise we will leave that to konrad), sales an employee was responsible for.
Sales should present data of totals profits based on period and product. It should also contain information on refunds (we dont have refund entity yet, igmore this) and canceled/failed transactions.
Inventory should present data about new stock/quantity of products that came, and current stock.

### Receipt System

### Setting System
The setting system should enable to toggle on/off or set the following settings:

1. Alert When a Stock is Low (True / False) >> if true the notification system (we need to build a notification system but this is for the future) sends email
2. Auto Generate a Report: (True / False)
3. Set Auto Generate a Report Time (if 2. is set to True)
4. Set Minimum cash discrepancy (the minimum amount of money that does not add up: expected money in cash register vs actual amount)
5. Set the number of days a refund is possible
6. At what time counting cash must start
7. At what time does counting cash (at the end of day) must start
8. At what time should the system generate an inventory report

---

## Future Features
- Dashboard with KPIs and charts
- Barcode printing and scanning
- POS integration
- Google Sheets export
- Enhanced role permissions

---
