DROP TABLE IF EXISTS ers_reimbursement;
DROP TABLE IF EXISTS ers_users;
DROP TABLE IF EXISTS ers_user_roles;
DROP TABLE IF EXISTS ers_reimbursement_type;
DROP TABLE IF EXISTS ers_reimbursement_status;



CREATE TABLE ers_user_roles (
	ers_user_role_id INT NOT NULL UNIQUE,
	user_role VARCHAR(10) NOT NULL UNIQUE,
	PRIMARY KEY (ers_user_role_id)
);

CREATE TABLE ers_reimbursement_status (
	reimb_status_id INT NOT NULL UNIQUE,
	reimb_status VARCHAR(10) NOT NULL UNIQUE,
	PRIMARY KEY (reimb_status_id)
);

CREATE TABLE ers_reimbursement_type (
	reimb_type_id INT NOT NULL UNIQUE,
	reimb_type VARCHAR(10) NOT NULL UNIQUE,
	PRIMARY KEY (reimb_type_id)
);

INSERT INTO ers_user_roles (ers_user_role_id, user_role)
	VALUES (1, "Manager"), (2, "Employee");

INSERT INTO ers_reimbursement_status (reimb_status_id, reimb_status)
	VALUES (1, "Approved"), (2, "Denied"), (3, "Pending");

INSERT INTO ers_reimbursement_type (reimb_type_id, reimb_type)
	VALUES (1, "Lodging"), (2, "Travel"), (3, "Food"), (4, "Other");




CREATE TABLE ers_users (
	ers_userID INT NOT NULL UNIQUE AUTO_INCREMENT,
	ers_username VARCHAR(50) NOT NULL UNIQUE,
	ers_password VARCHAR(50) NOT NULL,
	user_firstname VARCHAR(100) NOT NULL,
	user_lastname VARCHAR(100) NOT NULL,
	user_email VARCHAR(150) NOT NULL,
	user_roleID INT NOT NULL,
	PRIMARY KEY (ers_userID),
	FOREIGN KEY (user_roleID) REFERENCES ers_user_roles(ers_user_role_id)
);

CREATE TABLE ers_reimbursement (
	reimb_id INT NOT NULL UNIQUE AUTO_INCREMENT,
	reimb_amount DOUBLE NOT NULL,
	reimb_submitted TIMESTAMP NOT NULL,
	reimb_resolved TIMESTAMP NULL,
	reimb_description VARCHAR(250),
 	reimb_reciept LONGBLOB,
	reimb_author INT NOT NULL,
	reimb_resolver INT,
	reimb_status_id INT NOT NULL,
	reimb_type_id INT NOT NULL,
	PRIMARY KEY(reimb_id),
	FOREIGN KEY (reimb_author) REFERENCES ers_users(ers_userID),
	FOREIGN KEY (reimb_resolver) REFERENCES ers_users(ers_userID),
	FOREIGN KEY (reimb_status_id) REFERENCES ers_reimbursement_status(reimb_status_id),
	FOREIGN KEY (reimb_type_id) REFERENCES ers_reimbursement_type(reimb_type_id)
);
