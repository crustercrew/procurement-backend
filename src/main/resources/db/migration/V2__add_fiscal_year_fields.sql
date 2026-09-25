-- Add fiscal year tracking fields to purchase_requisitions
-- 1 PR = 1 fiscal year, with support for prior-year budget requests
ALTER TABLE purchase_requisitions ADD COLUMN target_fiscal_year INT NOT NULL DEFAULT EXTRACT(YEAR FROM CURRENT_TIMESTAMP)::INT;
ALTER TABLE purchase_requisitions ADD COLUMN justification_note TEXT;