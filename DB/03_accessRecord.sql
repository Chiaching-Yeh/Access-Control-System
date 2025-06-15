CREATE TABLE accessRecord (
	recordUid bigserial NOT NULL, -- 紀錄UID 使用 BIGSERIAL 作為主鍵，讓 PostgreSQL 自動產生唯一值。
	cardId varchar(50) NOT NULL,
	accessTime timestamp NOT NULL,
	reason text NULL,
	deviceId varchar(50) NOT NULL,
	isSuccessful bool NOT NULL,
	CONSTRAINT accessRecord_pkey PRIMARY KEY (recordUid)
);

