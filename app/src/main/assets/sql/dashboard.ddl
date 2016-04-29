CREATE TABLE IF NOT EXISTS dashboard (
id TEXT UNIQUE PRIMARY KEY NOT NULL,
due_orders INTEGER DEFAULT (0),
active_orders INTEGER DEFAULT (0),
customers INTEGER DEFAULT (0),
outstanding TEXT,
store TEXT
);
