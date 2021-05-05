-- The following is auto-populating the reimbursements into the database

INSERT INTO ers_reimbursement (
    reimb_amount,
    reimb_submitted,
    reimb_description,
    reimb_author,
    reimb_status_id,
    reimb_type_id
) VALUES (
    500,
    '2008-01-01 00:00:01',
    "Plane Ticket for Business Trip to Chicago",
    1,
    3,
    2
), (
    62.49,
    '2008-01-01 00:00:01',
    "Lunch with business partners",
    3,
    3,
    3
), (
    354,
    '2008-01-01 00:00:01',
    "Hotel room for Personal Vacation",
    2,
    3,
    1
);



INSERT INTO ers_reimbursement (
    reimb_amount,
    reimb_submitted,
    reimb_resolved,
    reimb_description,
    reimb_author,
    reimb_resolver,
    reimb_status_id,
    reimb_type_id
) VALUES (
    1024.35,
    '2008-01-01 00:00:01',
    '2009-01-01 00:00:01',
    "Travel to Vegas to take part in EVO while wearing company t-shirt as promotion",
    2,
    1,
    2,
    2
), (
    550,
    '2008-01-01 00:00:01',
    '2009-01-01 00:00:01',
    "Bought new computer for taking stock inventory",
    4,
    5,
    1,
    4
);