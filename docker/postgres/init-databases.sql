-- Creates the per-service PostgreSQL databases (database-per-service).
-- Executed once by the official postgres image on first container start.
CREATE DATABASE procurement_auth_db;
CREATE DATABASE procurement_user_db;
CREATE DATABASE procurement_vendor_catalog_db;
CREATE DATABASE procurement_department_budget_db;
CREATE DATABASE procurement_requisition_db;
CREATE DATABASE procurement_po_db;
CREATE DATABASE procurement_goods_receipt_db;
CREATE DATABASE procurement_invoice_db;
CREATE DATABASE procurement_audit_db;
