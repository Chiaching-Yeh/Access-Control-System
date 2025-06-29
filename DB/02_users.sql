CREATE TABLE IF NOT EXISTS users (
    userId varchar(50) NOT NULL,
    "name" varchar(100) NULL,
    cardId varchar(50) NULL,
    isActive bool NULL,
    updatedAt timestamp NULL,
    createdAt timestamp DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT users_pkey PRIMARY KEY (userId)
);

-- 加入索引
CREATE INDEX idx_userid ON public.users USING btree (userId);

INSERT INTO users (
    userId,
    "name",
    cardId,
    isActive,
    updatedAt
) VALUES (
    'U123',
    '測試使用者',
    '12345',
    true,
    now()
);

INSERT INTO users (
    userId,
    "name",
    cardId,
    isActive,
    updatedAt
) VALUES (
    'U456',
    '測試使用者2',
    '67890',
    false,
    now()
);