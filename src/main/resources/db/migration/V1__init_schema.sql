-- ============================================================
-- V1__init_schema.sql
-- Procurement System — Full Schema Initialization
-- ============================================================

-- 1. DEPARTMENTS
CREATE TABLE departments (
                             id               BIGSERIAL    PRIMARY KEY,
                             name             VARCHAR(100) NOT NULL,
                             cost_center_code VARCHAR(50)  UNIQUE NOT NULL,
                             created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. USERS
CREATE TABLE users (
                       id              BIGSERIAL    PRIMARY KEY,
                       department_id   BIGINT       REFERENCES departments(id),          -- Nullable untuk Vendor external
                       email           VARCHAR(150) UNIQUE NOT NULL,
                       password_hash   VARCHAR(255) NOT NULL,
                       full_name       VARCHAR(150) NOT NULL,
                       role            VARCHAR(50)  NOT NULL,                            -- 'REQUESTER','MANAGER','FINANCE','PROCUREMENT','WAREHOUSE','VENDOR'
                       is_active       BOOLEAN      DEFAULT TRUE,
                       created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. DEPARTMENT BUDGETS (Concurrency Lock via reserved_amount)
CREATE TABLE department_budgets (
                                    id               BIGSERIAL      PRIMARY KEY,
                                    department_id    BIGINT         NOT NULL REFERENCES departments(id),
                                    fiscal_year      INT            NOT NULL,
                                    allocated_amount DECIMAL(15, 2) NOT NULL,
                                    reserved_amount  DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                                    spent_amount     DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                                    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT uq_dept_fiscal    UNIQUE (department_id, fiscal_year),
                                    CONSTRAINT chk_budget_balance CHECK (reserved_amount + spent_amount <= allocated_amount)
);

-- 4. VENDORS
CREATE TABLE vendors (
                         id            BIGSERIAL    PRIMARY KEY,
                         user_id       BIGINT       UNIQUE REFERENCES users(id),           -- Akun login vendor
                         company_name  VARCHAR(150) NOT NULL,
                         tax_id_npwp   VARCHAR(50)  UNIQUE NOT NULL,
                         contact_email VARCHAR(100) NOT NULL,
                         is_active     BOOLEAN      DEFAULT TRUE,
                         created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. VENDOR CATALOG ITEMS
CREATE TABLE vendor_catalog_items (
                                      id              BIGSERIAL      PRIMARY KEY,
                                      vendor_id       BIGINT         NOT NULL REFERENCES vendors(id),
                                      sku             VARCHAR(60)    NOT NULL,
                                      name            VARCHAR(200)   NOT NULL,
                                      category        VARCHAR(100)   NOT NULL,
                                      unit            VARCHAR(30)    NOT NULL,                          -- 'PCS', 'BOX', 'UNIT'
                                      unit_price      DECIMAL(15, 2) NOT NULL,
                                      stock_available INT            NOT NULL DEFAULT 0,
                                      is_active       BOOLEAN        DEFAULT TRUE,
                                      CONSTRAINT uq_vendor_sku UNIQUE (vendor_id, sku)
);

-- 6. PURCHASE REQUISITIONS (PR)
CREATE TABLE purchase_requisitions (
                                       id               BIGSERIAL      PRIMARY KEY,
                                       pr_number        VARCHAR(60)    UNIQUE NOT NULL,                  -- Format: PR-YYYYMM-XXXXX
                                       requester_id     BIGINT         NOT NULL REFERENCES users(id),
                                       department_id    BIGINT         NOT NULL REFERENCES departments(id),
                                       total_amount     DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
                                       status           VARCHAR(50)    NOT NULL,                         -- 'DRAFT','SUBMITTED','PENDING_FINANCE','APPROVED','REJECTED'
                                       rejection_reason TEXT,
                                       created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 7. PR ITEMS
CREATE TABLE pr_items (
                          id              BIGSERIAL      PRIMARY KEY,
                          pr_id           BIGINT         NOT NULL REFERENCES purchase_requisitions(id) ON DELETE CASCADE,
                          catalog_item_id BIGINT         NOT NULL REFERENCES vendor_catalog_items(id),
                          quantity        INT            NOT NULL CHECK (quantity > 0),
                          unit_price      DECIMAL(15, 2) NOT NULL,
                          subtotal        DECIMAL(15, 2) NOT NULL
);

-- 8. PURCHASE ORDERS (PO)
CREATE TABLE purchase_orders (
                                 id           BIGSERIAL      PRIMARY KEY,
                                 po_number    VARCHAR(60)    UNIQUE NOT NULL,                      -- Format: PO-YYYYMM-XXXXX
                                 pr_id        BIGINT         UNIQUE NOT NULL REFERENCES purchase_requisitions(id),
                                 vendor_id    BIGINT         NOT NULL REFERENCES vendors(id),
                                 total_amount DECIMAL(15, 2) NOT NULL,
                                 status       VARCHAR(50)    NOT NULL,                             -- 'ISSUED','ACCEPTED','DELIVERING','COMPLETED','CANCELLED'
                                 issued_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 9. PO ITEMS
CREATE TABLE po_items (
                          id              BIGSERIAL      PRIMARY KEY,
                          po_id           BIGINT         NOT NULL REFERENCES purchase_orders(id) ON DELETE CASCADE,
                          catalog_item_id BIGINT         NOT NULL REFERENCES vendor_catalog_items(id),
                          quantity        INT            NOT NULL CHECK (quantity > 0),
                          unit_price      DECIMAL(15, 2) NOT NULL,
                          subtotal        DECIMAL(15, 2) NOT NULL
);

-- 10. GOODS RECEIPTS
CREATE TABLE goods_receipts (
                                id             BIGSERIAL   PRIMARY KEY,
                                receipt_number VARCHAR(60) UNIQUE NOT NULL,                       -- Format: GR-YYYYMM-XXXXX
                                po_id          BIGINT      NOT NULL REFERENCES purchase_orders(id),
                                received_by    BIGINT      NOT NULL REFERENCES users(id),
                                received_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 11. GOODS RECEIPT ITEMS
CREATE TABLE goods_receipt_items (
                                     id                BIGSERIAL   PRIMARY KEY,
                                     receipt_id        BIGINT      NOT NULL REFERENCES goods_receipts(id) ON DELETE CASCADE,
                                     po_item_id        BIGINT      NOT NULL REFERENCES po_items(id),
                                     quantity_received INT         NOT NULL CHECK (quantity_received >= 0),
                                     condition         VARCHAR(50) NOT NULL                            -- 'GOOD', 'DAMAGED'
);

-- 12. VENDOR INVOICES (3-Way Matching)
CREATE TABLE vendor_invoices (
                                 id               BIGSERIAL      PRIMARY KEY,
                                 invoice_number   VARCHAR(100)   UNIQUE NOT NULL,
                                 po_id            BIGINT         NOT NULL REFERENCES purchase_orders(id),
                                 billed_amount    DECIMAL(15, 2) NOT NULL,
                                 match_status     VARCHAR(50)    NOT NULL,                         -- 'PENDING_MATCH','MATCHED','DISCREPANCY_HOLD'
                                 is_paid          BOOLEAN        DEFAULT FALSE,
                                 discrepancy_note TEXT,
                                 created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 13. AUDIT LOGS (Immutable History)
CREATE TABLE audit_logs (
                            id             BIGSERIAL   PRIMARY KEY,
                            entity_name    VARCHAR(50) NOT NULL,
                            entity_id      BIGINT      NOT NULL,
                            actor_id       BIGINT      NOT NULL,
                            action         VARCHAR(50) NOT NULL,                              -- 'SUBMIT','APPROVE_MANAGER','REJECT','THREE_WAY_PASS'
                            previous_state JSONB,
                            new_state      JSONB,
                            created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- INDEXES — optimasi query umum
-- ============================================================
CREATE INDEX idx_users_department    ON users(department_id);
CREATE INDEX idx_users_email         ON users(email);
CREATE INDEX idx_dept_budget_dept    ON department_budgets(department_id);
CREATE INDEX idx_vendor_catalog_vend ON vendor_catalog_items(vendor_id);
CREATE INDEX idx_pr_requester        ON purchase_requisitions(requester_id);
CREATE INDEX idx_pr_department       ON purchase_requisitions(department_id);
CREATE INDEX idx_pr_status           ON purchase_requisitions(status);
CREATE INDEX idx_po_vendor           ON purchase_orders(vendor_id);
CREATE INDEX idx_po_status           ON purchase_orders(status);
CREATE INDEX idx_gr_po               ON goods_receipts(po_id);
CREATE INDEX idx_invoice_po          ON vendor_invoices(po_id);
CREATE INDEX idx_invoice_match       ON vendor_invoices(match_status);
CREATE INDEX idx_audit_entity        ON audit_logs(entity_name, entity_id);
CREATE INDEX idx_audit_actor         ON audit_logs(actor_id);