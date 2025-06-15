# 門禁系統裝置端模擬器

本專案為模擬門禁控制系統，整合 MQTT 即時通訊、Redis 快取、PostgreSQL 資料庫與 Spring Boot 架構，實作設備授權與 QR Code 驗證流程。

## 環境需求

- Python 3.x
- 安裝 `paho-mqtt` 函式庫
- JDK 21
- Spring Boot 3.3
- Redis Server
- PostgreSQL
- Eclipse Paho MQTT Client

## MQTT 規則

根據模式產生對應的 MQTT Topic 與 Payload，發送至伺服器。

| 模式 | Request Topic     | Response Topic            | Payload 範例                             |
| -- | ----------------- | ------------------------- | -------------------------------------- |
| 卡片 | `door/request`    | `door/response/{cardId}`  | `cardId:123456789,deviceId:device-001` |
| QR | `door/request/qr` | `door/response/qr/{uuid}` | `uuid:9876-ABCD,deviceId:device-001`   |


## 功能模組
| 模組名稱 | 說明 |
|----------|------|
| `AuthService` | 接收刷卡卡號並查詢是否授權，含 Redis 快取 |
| `QrCodeVerifyService` | 驗證 QR code 是否有效，成功則回傳授權結果 |
| `DeviceStatusService` | 接收裝置心跳並更新在線狀態 |
| `AccessRecordService` | 寫入刷卡紀錄：卡號、裝置、時間|
| `MqttAccessControlService` | 透過 MQTT 接收裝置訊息並自動處理授權邏輯 |

## 其他說明
@PostConstruct 用於初始化 MQTT 客戶端訂閱 topic

* Redis key 規則請參考：
  * auth:card:{cardId}
  * qr:{uuid}
  * device:{deviceId}:status

[裝置端 Python 模擬]
⇅ MQTT
[Spring Boot 後端服務]
⇅ Redis 
(授權快取 / 裝置狀態 / 掃碼資訊)
⇅ PostgresSQL
(使用者與開門紀錄)

## Angular docker
這是典型的 Angular 專案 multi-stage build，會先編譯，再交給 Nginx 做靜態檔案服務

## python-simulator docker
用來建立一個 Python CLI 容器，來模擬 MQTT 發布功能


1. gcloud CLI 必須已安裝在 VM 上
2. GitHub - vm ssh key
3. vm ssh key
4. Docker version 20.10.24+dfsg1
5. gcloud --version Google Cloud SDK 522.0.0
6. git version 2.39.5
7. Docker Compose 的 PostgreSQL service 中指定整個資料夾（例如 ./DB），只要裡面是 .sql 或 .sql.gz 檔案，PostgreSQL 官方映像檔會自動執行該資料夾底下的所有 SQL 腳本。
  - 當 container 第一次啟動 且 /var/lib/postgresql/data 是空的時候：
    ✅ 它會自動執行：
    所有 .sql、.sql.gz、.sh 檔案（只限 /docker-entrypoint-initdb.d 裡的）
    所以你只要把 .sql 檔案放進那個資料夾，就會自動建立資料表，完全不需要自己額外寫 shell script
8. python cli