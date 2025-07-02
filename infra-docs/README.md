# GCP VM + Nginx + Certbot HTTPS 設定教學（適用 Debian 12）

## ✅ 教學目標

建立一個 GCP VM，在上面安裝 Nginx，配置自訂 domain 的 HTTP 服務，並使用 Certbot 自動申請與安裝 Let's Encrypt 憑證，開啟 HTTPS，作為反向代理proxy GateWay。

```text

    [使用者瀏覽器] ──▶ 443 (HTTPS)
           │
           ▼
    ┌──────────────┐
    │ VM Host Nginx│ 🔐 TLS + Reverse Proxy
    └──────┬───────┘
           │
        轉發至VM容器內部 http://localhost:8081
           │
           ▼
    ┌──────────────────────────┐
    │ Container (acs-frontend) │ Nginx serve /usr/share/nginx/html
    └──────────────────────────┘

```



---
1. [🔧 建立 GCP VM（Ubuntu/Debian）]
2. [🌐 綁定 Domain DNS 到 VM IP]
3. [🛠 GCP VM 安裝 Nginx]
4. [📁 編寫 /etc/nginx/sites-available/<customized domain>]
5. [🔗 ln -s 建立 sites-enabled/ 連結]
6. [✅ nginx -t 檢查設定語法]
7. [🚀 systemctl reload nginx 重啟服務]
8. [🌐 VM 防火牆放行 HTTP (port 80)]
9. [🔐 使用 Certbot 執行 HTTPS 憑證申請]
10. [⚙️ Certbot 自動修改 nginx.conf 為 TLS 版]
11. [🌍 網站成功支援 HTTPS！]
12. [🕒 開啟 certbot 自動續期機制 (systemd)]
---

## ✅ 1. 設定目的與環境解釋

### 🔍 為什麼要這樣設定（目的與意義）

1. **使用 Nginx 作為 Web 反向代理（Reverse Proxy）**：可將前端靜態資源與後端服務分開部署，並實作轉址、路由、Header 調整等行為，提升安全性與維護彈性。
2. **透過 Certbot 自動取得 Let's Encrypt 憑證**：達成 HTTPS 安全傳輸，避免瀏覽器跳出「不安全」警告，並提升 SEO 分數。
3. **使用 GCP VM 佈建自訂 Domain 的 Web 服務**：結合 DNS、Firewall、VM、Nginx 等組件進行完整 DevOps 演練。
4. **預設先使用 HTTP，確保 Certbot 順利進行 ACME Challenge 驗證**：因為 HTTPS 配置前若沒有有效憑證，Nginx 啟動會失敗，因此流程中採分段式配置策略。

---

## ✅ 2. Nginx 設定結構與角色說明

### 📁 `/etc/nginx/sites-available/`

- 儲存所有「可用」站台設定檔。
- 相當於草稿區，不會直接生效。

### 🔗 `/etc/nginx/sites-enabled/`

- 會被 Nginx 實際讀取的啟用區。
- 使用 `ln -s` 建立 symbolic link 啟用對應的設定。

### 📝 `default`

- 系統預設啟用的站台設定。
- 不建議直接修改，保留可避免覆蓋掉預設行為。

### 🔄 常見工作流程

```text
sudo nano /etc/nginx/sites-available/<customized domain>
sudo ln -s /etc/nginx/sites-available/<customized domain> /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

---

### 🔗 為什麼要設定 symbolic link（ln -s）？
✅ 目的：啟用指定站台設定檔
Nginx 預設只會讀取 /etc/nginx/sites-enabled/ 裡的設定。
我們會將「真正寫的設定檔」放在 /etc/nginx/sites-available/，然後用 ln -s 做連結到 sites-enabled，代表「我要啟用這個站台」。

✅ 優點與實務意義
- 集中管理
    - 所有站台設定檔都放在 sites-available/，容易維護
- 避免誤刪
    - 停用某站台只要移除連結，而不會誤刪設定檔原始內容
- 支援多站台
    - 可同時維護多個 domain 的設定，只開啟想啟用的那幾個
- 方便測試/切換
    - 可快速啟用/關閉不同設定，不需修改原始檔案

🔧 指令範例：
```text
sudo ln -s /etc/nginx/sites-available/<customized domain> /etc/nginx/sites-enabled/
如果想要「停用」這個站台，只需刪除 link：
```

```text

sudo rm /etc/nginx/sites-enabled/<customized domain>
✅ 總結
使用 ln -s 的好處不是技術上「一定要」，而是讓 Nginx 的站台設定更有組織、易於維運、支援多個 domain，並能配合 Certbot 自動化機制安全地作業。這也是 Ubuntu/Debian 上 Nginx 經典慣例。
```
---

## 3. 建立 HTTP 站台設定（初期先不加 TLS）

```text
sudo nano /etc/nginx/sites-available/<customized domain>
```

```nginx
server {
    listen 80;
    server_name <customized domain> www.<customized domain>;

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

啟用設定：

```text
sudo ln -s /etc/nginx/sites-available/<customized domain> /etc/nginx/sites-enabled/
sudo nginx -t && sudo systemctl reload nginx
```

---

## ✅ 4. 開通 GCP VM 防火牆：允許 HTTP 流量

```text
gcloud compute instances add-tags acs-instance-spot --zone=us-central1-a --tags=http-server
```

確認 `default-allow-http` firewall rule 允許 `http-server` tag：

```text
gcloud compute firewall-rules update default-allow-http --target-tags=http-server
```

---

## ✅ 5. 安裝 Certbot 與申請憑證（自動設定 Nginx）

```text
sudo apt update
sudo apt install certbot python3-certbot-nginx
```

執行自動申請與設定：

```text
sudo certbot --nginx -d <customized domain> -d www.<customized domain>
```

Certbot 將：

- 檢查是否可透過 HTTP 驗證 `.well-known` 路徑
- 自動將 Nginx 升級成 `listen 443 ssl` 設定並引入憑證

---

## ✅ 6. HTTPS 設定範例（成功後應為）

```nginx
server {
    listen 443 ssl;
    server_name <customized domain> www.<customized domain>;

    ssl_certificate /etc/letsencrypt/live/<customized domain>/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/<customized domain>/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    location / {
        proxy_pass http://localhost:8081;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}

server {
    listen 80;
    server_name <customized domain> www.<customized domain>;
    return 301 https://$host$request_uri;
}
```

---

## ✅ 7. 自動更新憑證設定（內建 systemd timer）

Certbot 安裝時會自動設定 `/lib/systemd/system/certbot.timer`：

```text
systemctl list-timers | grep certbot
```

也可手動測試續期：

```text
sudo certbot renew --dry-run
```

---

## ✅ 8. 容器內部Nginx設定

### 🔸 支援 SPA router fallback（Angular）

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```

### 🔸 將 /api 代理到 Spring Boot

```nginx
location /api/ {
    proxy_pass http://localhost:8080;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
}
```

---



