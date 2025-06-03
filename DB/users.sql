CREATE TABLE IF NOT EXISTS users (
    userId     VARCHAR(50),
    name        VARCHAR(100),
    cardId     VARCHAR(50),
    isActive   BOOLEAN,
    qrCode     VARCHAR(255),
    updatedAt  TIMESTAMP,
    createdAt  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (userId)
);

-- 加入索引
CREATE INDEX idx_userId ON users(userId);