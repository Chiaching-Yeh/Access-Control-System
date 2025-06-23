# 門禁系統簡介

> 本專案為門禁控制系統模擬平台，以前後端分離架構整合 MQTT 即時通訊、Redis 快取、PostgreSQL 資料庫、Spring Boot 及 Angular 前端，
並支援 Docker 化部署、自動化 CI/CD、GCP 雲端上線，另設計 Python CLI 工具模擬硬體設備（如刷卡機、QR Code 閘門）進行全流程測試，
並實作 SSL 憑證以確保安全性。

---

## 架構說明

### 後端
- #### MQTT 通訊服務
    作為 MQTT broker/客戶端，提供即時的設備通訊機制，實作即時事件推播（如開門指令、授權結果）。

- ##### Redis 快取與一次性授權管理
    用於存儲一次性 QR Code 驗證資訊，確保臨時授權憑證能安全、即時、單次生效（QR 掃碼開門）。

- ##### PostgreSQL 資料庫
    存儲門禁卡、設備、刷卡紀錄、用戶資訊等關鍵數據。

- ##### Spring Boot
    提供模組化、易於擴展的後端驗證服務，並整合Spring security管理session，後續可做為管理系統之擴充。

---

### 前端
- #### Angular SPA 框架
    所有頁面渲染於前端瀏覽器處理，大幅減輕後端 server 負擔，優於傳統 Spring MVC。

- #### 即時資料推播機制
    利用 WebSocket 建立前端與後端的長連線，實現即時監控。
前端訂閱刷卡紀錄事件，一有新紀錄就自動撈取資料庫最新資料，避免使用API輪詢造成伺服器負荷。

---

### 系統設置

- #### Nginx 反向代理於 VM 部署
    統一入口作為 API Gateway 管理所有對外請求，依照服務與路徑將流量導向對應容器。

- #### DevOps 與 CI/CD（Docker + GitHub Actions + GCP）
  1. 以 Docker 容器化所有核心組件（前端、後端、MQTT、Redis、資料庫等），確保跨平台一致性部屬。
  2. 建構基本的 CI/CD 自動化流程
     - ✅ CI：GitHub Actions 在 GitHub Runner 上自動建置專案、打包成 Docker 映像，並push至 GCP Artifact Registry
     - ✅ CD：透過 SSH 登入 GCP VM，自動從 Artifact Registry 拉取最新映像，並使用 Docker Compose 重啟服務
     - [詳細說明](./.github/workflows/README.md)

- #### SSL 憑證（VM 配置）
    購買網域並在 VM 層完成 SSL 憑證簽發（如 Let's Encrypt），全站流量皆加密，保障門禁資料與人員資訊安全。
    - [詳細說明](./infra-docs/README.md)

### 測試腳本

- #### Python CLI 測試腳本
    提供 Python CLI 工具並支援 MQTT 通訊協定，模擬裝置端刷卡、QR Code 掃碼兩種情境。
    - [詳細說明](./simulator/README.md)

## springBoot 功能模組
| 模組名稱 | 說明 |
|----------|------|
| `AuthService` | 接收刷卡卡號並查詢是否授權，含 Redis 快取 |
| `QrCodeVerifyService` | 驗證 QR code 是否有效，成功則回傳授權結果 |
| `AccessRecordService` | 寫入刷卡紀錄：卡號、裝置、時間|
| `MqttAccessControlService` | 透過 MQTT 接收裝置訊息並自動處理授權邏輯 |
---

### VM 環境建置指令

#### 1. 更新套件與系統

```text
sudo apt update && sudo apt upgrade -y
```

#### 2. 安裝 Google Cloud CLI

```text
sudo apt install apt-transport-https ca-certificates gnupg curl -y
echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | \
sudo tee /etc/apt/sources.list.d/google-cloud-sdk.list
curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | \
sudo gpg --dearmor -o /usr/share/keyrings/cloud.google.gpg
sudo apt update && sudo apt install google-cloud-cli -y
```

#### 3. 安裝 Docker 與 Docker Compose
```text
sudo apt install docker.io docker-compose -y
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER  # 讓當前用戶可執行 docker（登出再登入生效）
```

#### 4. 安裝 Git（已知你是 2.39.5，此為保險處理）

```text
sudo apt install git -y
git --version
```

#### 5. 安裝 Python CLI 環境（搭配模擬器用）

```text
sudo apt install python3 python3-pip -y
pip3 install --upgrade pip
```

#### 6. 建立 SSH 金鑰（CI/CD runner 用）

```text
ssh-keygen -t ed25519 -f ~/.ssh/id_ed25519 -C "github-runner" -N ""
```

#### 7. 顯示公鑰（可貼到 GitHub Deploy Key、或 VM metadata）

```text
cat ~/.ssh/id_ed25519.pub
```

#### 8. PostgreSQL Docker Compose 注意事項

```text
請確保你的 docker-compose.yml 中掛載正確：
./DB:/docker-entrypoint-initdb.d
在首次啟動 container 且 data 資料夾為空時，自動執行裡面的 .sql檔案
```

#### 9. 查看特定端口的使用情況工具，用於網路設置偵錯

```text
sudo apt install lsof -y
```

#### 10. 重啟 VM 後驗證 docker、git、gcloud、python 是否正常

```text
docker --version
docker-compose --version
gcloud --version
git --version
python3 --version
pip3 --version
```