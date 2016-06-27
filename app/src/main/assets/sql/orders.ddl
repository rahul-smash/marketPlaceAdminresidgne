CREATE TABLE IF NOT EXISTS orders (
order_id TEXT UNIQUE PRIMARY KEY NOT NULL,
store_id TEXT,
user_id TEXT,
status TEXT,
customer_name TEXT,
phone TEXT,
time TEXT,
note TEXT,
discount TEXT,
total TEXT,
checkout TEXT,
shipping_charges TEXT,
coupon_code TEXT,
address TEXT,
total_amount TEXT,
items TEXT,
tax TEXT
);
