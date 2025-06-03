CREATE TABLE accessRecord (
    recordUid     BIGSERIAL PRIMARY KEY,       -- 紀錄UID 使用 BIGSERIAL 作為主鍵，讓 PostgreSQL 自動產生唯一值。
    cardId        VARCHAR(50) NOT NULL,        -- 讀到的卡號
    accessTime    TIMESTAMP NOT NULL,          -- 刷卡時間
    reason         TEXT,                        -- 失敗原因，可為空
    deviceId      VARCHAR(50) NOT NULL         -- 裝置編號
);

-- 加入索引
CREATE INDEX idx_cardId ON accessRecord(cardId);
CREATE INDEX idx_deviceId ON accessRecord(deviceId);