CREATE TABLE accessRecord (
    recordUid UUID PRIMARY KEY,
    cardId varchar(50) NOT NULL,
    accessTime timestamp NOT NULL,
    reason text NULL,
    deviceId varchar(50) NOT NULL,
    successful bool NOT NULL
);

CREATE INDEX idx_cardId ON accessRecord(cardId);
CREATE INDEX idx_accessTime ON accessRecord(accessTime DESC);

