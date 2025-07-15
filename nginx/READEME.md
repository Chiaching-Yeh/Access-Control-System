當使用者透過 HTTP（port 80）存取 charlielab.online 時，會被自動 301 重導到 HTTPS（port 443），保障連線安全。
當使用者透過 HTTPS 存取（port 443）時： 掛載 Let's Encrypt 憑證來支援 TLS 加密、 根據 URL 路徑將流量分流到對應的內部服務：


``` text
server {
    listen 443 ssl;

    server_name charlielab.online;
    ✔ 當使用者輸入的網址是 charlielab.online 時，才會套用這段設定區塊的規則



    ssl_certificate /etc/letsencrypt/live/charlielab.online/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/charlielab.online/privkey.pem;
    include /etc/letsencrypt/options-ssl-nginx.conf;
    ssl_dhparam /etc/letsencrypt/ssl-dhparams.pem;

    ✔  當 VM 監聽 443 port 時，針對 443 port 設定 TLS 憑證

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    ✔  當 URL 是 / 開頭（例如 /index.html），反向代理到內部 8081 port 的服務，通常是提供靜態頁面的 Nginx（可能是 Angular 應用程式）

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    location /ws/ {
        proxy_pass http://localhost:8080;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
    }

    ✔ /api/ 與 /ws/ 的請求轉發到 8080 port 的 Spring Boot 應用程式
}

server {

    listen 80;
    
    server_name charlielab.online;

    ✔ 當使用者輸入的網址是 charlielab.online 時，才會套用這段設定區塊的規則

    return 301 https://$host$request_uri;

    ✔ 「當有 HTTP 請求（port 80）打到這台機器時，要將 http 強制轉成 https，確保網站只透過 TLS 傳輸」

}

```