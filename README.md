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
⇅ PostgreSQL
(使用者與開門紀錄)

## Angualr docker
這是典型的 Angular 專案 multi-stage build，會先編譯，再交給 Nginx 做靜態檔案服務

## python-simulator docker
用來建立一個 Python CLI 容器，來模擬 MQTT 發布功能