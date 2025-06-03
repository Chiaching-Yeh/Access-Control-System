# 門禁系統裝置端模擬器（Card / QR 模式）

本專案提供一個以 Python 撰寫的模擬裝置端腳本，可模擬「刷卡」或「QR 掃碼」進行門禁驗證的場景。透過 MQTT 通訊協定將授權請求送至伺服器，並接收伺服器授權回應。

## 環境需求

- Python 3.x
- 安裝 `paho-mqtt` 函式庫

## 使用方式
### 1. 啟動 MQTT Broker
請先確認你的開發環境中已啟動 MQTT broker，例如使用 [Mosquitto](https://mosquitto.org/)：
```bash
mosquitto
```

## 執行模擬裝置
### 1. 模擬刷卡模式
```bash
python device_simulator.py --mode card --cardId 123456789
```

### 2. 模擬 QR 掃碼模式
```bash
python device_simulator.py --mode qr --uuid 9876-ABCD
```
### 3. 參數說明
| 參數名稱         | 說明                        | 範例值         |
| ------------ | ------------------------- | ----------- |
| `--mode`     | 模擬模式（必填）`card` 或 `qr`     | `card`      |
| `--cardId`   | 卡片 ID，僅 `card` 模式使用       | `123456789` |
| `--uuid`     | QR code UUID，僅 `qr` 模式使用  | `9876-ABCD` |
| `--deviceId` | 裝置編號（選填，預設為 `device-001`） | `gate-1`    |


## 程式邏輯簡介
### 1. 裝置發送授權請求 
根據模式產生對應的 MQTT Topic 與 Payload，發送至伺服器。

| 模式 | Request Topic     | Response Topic            | Payload 範例                             |
| -- | ----------------- | ------------------------- | -------------------------------------- |
| 卡片 | `door/request`    | `door/response/{cardId}`  | `cardId:123456789,deviceId:device-001` |
| QR | `door/request/qr` | `door/response/qr/{uuid}` | `uuid:9876-ABCD,deviceId:device-001`   |


### 2. 裝置訂閱伺服器授權回應
裝置會訂閱個別的回應 topic，主機端應該回傳以下授權訊息之一：
- grant：授權通過，模擬開門
- 其他文字：授權失敗，拒絕進入

## MQTT架構簡述
本模擬器採用 MQTT 的 Pub/Sub 架構：

* 裝置端（此腳本）
    * Publish：
      * door/request（卡片模式）
      * door/request/qr（QR 模式）

    * Subscribe：
      * door/response/{cardId}
      * door/response/qr/{uuid}

* 伺服器端（例如 Java + Spring Boot）
    * Subscribe
      * door/request
      * door/request/qr

    * Publish：
      * 授權結果至對應的回應 topic
    
## 測試
``` text
    # 回應授權成功
    mosquitto_pub -t "door/response/123456789" -m "grant"
```