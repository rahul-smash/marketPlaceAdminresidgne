CREATE TABLE IF NOT EXISTS customers (
id TEXT UNIQUE PRIMARY KEY NOT NULL,
store_id TEXT,
full_name TEXT,
phone TEXT,
status TEXT,
email TEXT,
area TEXT,
total_orders INTEGER DEFAULT (0),
active_orders INTEGER DEFAULT (0),
due_amount TEXT,
paid_amount TEXT
);
