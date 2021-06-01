# Expense Reimbursement System (ERS)
Michael McGroary's Revature Project 1

## Description
The Expense Reimbursement System (ERS) will manage the process of reimbursing employees for expenses incurred while on company time. All employees in the company can login and submit requests for reimbursement and view their past tickets and pending requests. Finance managers can log in and view all reimbursement requests and past history for all employees in the company. Finance managers are authorized to approve and deny requests for expense reimbursement.

## Technologies Used
* JavaScript
* HTML
* CSS
* AJAX
* SQL
* Hibernate
* Java
* Selenium
* Javalin
* Mockito
* JUnit
* Amazon RDS
* Amazon EC2

## Features
* Allow users to log in and post requests for reimbursements
* Allows finance managers to log in and approve and deny reimbursements
* Only shows reimbursement requests made by a particular user while hiding the rest unless they are a finance manager
* Allow finance managers to filter the visible requests by their status (approved, denied, or pending)

## Roles and Responsibilities
* Utilized Javalin to handle endpoints for Rest API.
* Managed backend database on a MariaDB server.
* Sent and received information from the database using Hibernate.
* Tested service layer of project utilizing JUnit testing with Mockito.
* Increased security by hashing passwords through MD5 Hashing algorithm.
* Built front end website using HTML, CSS, JavaScript, and Bootstrap allowing users to create accounts, post new reimbursement requests and view their request history.
* Allowed only users designated as Finance Managers to view, approve, and deny all requests in the system.
* Hosted website on an AWS EC2 instance with database hosted on AWS RDS Instance.
* Included file uploading and downloading for receipts of purchases to help Finance Managers validate requests.

