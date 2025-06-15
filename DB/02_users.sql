CREATE TABLE IF NOT EXISTS users (
    userId varchar(50) NOT NULL,
    "name" varchar(100) NULL,
    cardId varchar(50) NULL,
    isActive bool NULL,
    qrCode VARCHAR(255),
    updatedAt timestamp NULL,
    createdAt timestamp DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT users_pkey PRIMARY KEY (userId)
);

-- 加入索引
CREATE INDEX idx_userid ON public.users USING btree (userId);