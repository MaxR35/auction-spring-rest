-- =========================================
-- Script to create the AUCTION database
-- Version with separation ARTICLES / SALES
-- =========================================
USE AUCTION;

-- Drop existing tables (important order)
IF OBJECT_ID('BIDS', 'U') IS NOT NULL DROP TABLE BIDS;
IF OBJECT_ID('WITHDRAWALS', 'U') IS NOT NULL DROP TABLE WITHDRAWALS;
IF OBJECT_ID('SALES', 'U') IS NOT NULL DROP TABLE SALES;
IF OBJECT_ID('ITEMS', 'U') IS NOT NULL DROP TABLE ITEMS;
IF OBJECT_ID('USERS', 'U') IS NOT NULL DROP TABLE USERS;
IF OBJECT_ID('ADDRESSES', 'U') IS NOT NULL DROP TABLE ADDRESSES;
IF OBJECT_ID('CATEGORIES', 'U') IS NOT NULL DROP TABLE CATEGORIES;

-- =========================================
-- Table ADDRESSES (for pickup locations)
-- =========================================
CREATE TABLE ADDRESSES (
    address_id INTEGER IDENTITY(1,1) PRIMARY KEY,
    street NVARCHAR(100) NOT NULL,
    postal_code NVARCHAR(10) NOT NULL,
    city NVARCHAR(50) NOT NULL
);

-- =========================================
-- Table CATEGORIES
-- =========================================
CREATE TABLE CATEGORIES (
    category_id INTEGER IDENTITY(1,1) PRIMARY KEY,
    label NVARCHAR(30) NOT NULL
);

-- =========================================
-- Table USERS
-- =========================================
CREATE TABLE USERS (
    user_id INTEGER IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(30) NOT NULL,
    last_name NVARCHAR(30) NOT NULL,
    first_name NVARCHAR(30) NOT NULL,
	user_img NVARCHAR(255),
    email NVARCHAR(50) NOT NULL,
    phone NVARCHAR(15),
    password NVARCHAR(255) NOT NULL, -- hashed password
    credit INTEGER NOT NULL CHECK (credit >= 0),
    is_admin BIT NOT NULL DEFAULT 0,
    created_at DATETIME2 DEFAULT SYSDATETIME(),

    CONSTRAINT unique_username UNIQUE (username),
    CONSTRAINT unique_email UNIQUE (email)
);

-- =========================================
-- Table ARTICLES (item descriptions)
-- =========================================
CREATE TABLE ITEMS (
    item_id INTEGER IDENTITY(1,1) PRIMARY KEY,
    item_name NVARCHAR(30) NOT NULL,
    item_desc NVARCHAR(300) NOT NULL,
    item_img NVARCHAR(255) NOT NULL,
    category_id INTEGER NOT NULL,

    CONSTRAINT fk_items_category FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
);

-- =========================================
-- Table SALES (linked to articles)
-- =========================================
CREATE TABLE SALES (
    sale_id INTEGER IDENTITY(1,1) PRIMARY KEY,
    item_id INTEGER NOT NULL,
    seller_id INTEGER NOT NULL,
    starting_date DATETIME2 NOT NULL,
    ending_date DATETIME2 NOT NULL,
    starting_price INTEGER CHECK (starting_price >= 0),
    sale_price INTEGER CHECK (sale_price >= 0),

    CONSTRAINT fk_sale_item FOREIGN KEY (item_id) REFERENCES ITEMS(item_id),
    CONSTRAINT fk_sale_user FOREIGN KEY (seller_id) REFERENCES USERS(user_id) ON DELETE CASCADE
);

-- =========================================
-- Table WITHDRAWALS (pickup point for a sale)
-- =========================================
CREATE TABLE WITHDRAWALS (
    sale_id INTEGER PRIMARY KEY,
    address_id INTEGER NOT NULL,

    CONSTRAINT fk_withdrawal_sale FOREIGN KEY (sale_id) REFERENCES SALES(sale_id) ON DELETE CASCADE,
    CONSTRAINT fk_withdrawal_address FOREIGN KEY (address_id) REFERENCES ADDRESSES(address_id)
);

-- =========================================
-- Table BIDS
-- =========================================
CREATE TABLE BIDS (
    bid_id INTEGER IDENTITY(1,1) PRIMARY KEY,
    bid_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    bid_amount INTEGER NOT NULL CHECK (bid_amount > 0),
    sale_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,

    CONSTRAINT fk_bid_sale FOREIGN KEY (sale_id) REFERENCES SALES(sale_id) ON DELETE CASCADE,
    CONSTRAINT fk_bid_user FOREIGN KEY (user_id) REFERENCES USERS(user_id)
);

-- =========================================
-- Indexes for performance
-- =========================================
CREATE INDEX idx_items_category ON ITEMS(category_id);
CREATE INDEX idx_sales_seller ON SALES(seller_id);
CREATE INDEX idx_bids_user ON BIDS(user_id);
CREATE INDEX idx_bids_sale ON BIDS(sale_id);
