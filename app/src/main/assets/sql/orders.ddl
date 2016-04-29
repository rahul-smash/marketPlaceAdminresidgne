CREATE TABLE IF NOT EXISTS orders (
order_id TEXT UNIQUE PRIMARY KEY NOT NULL,
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
total_amount INTEGER DEFAULT (0)
);
