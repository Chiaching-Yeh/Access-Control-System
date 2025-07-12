## DockerFile 說明

### **第一階段：建構 Angular 專案**

- 使用 Node.js 22（Alpine 版）作為建構環境，輕量、效能佳
- WORKDIR 設定為 /usr/src/app，所有動作皆在此資料夾內進行
- 安裝 bash，支援部分 Angular CLI 工具的執行
- 複製 package.json 與 package-lock.json，並安裝相依套件
- 複製專案原始碼（會排除 .dockerignore 中列出的檔案）
- 清除 Angular 快取，避免 build 時出錯
- 透過 npx ng build 依據傳入的環境變數（如 production）編譯 Angular 專案
- 根據 template.js 產生環境變數檔 env.js，供 Angular 靜態頁面使用

### **第二階段：Nginx 伺服器提供 Angular 靜態網頁**

- 使用 Nginx stable-alpine 作為執行環境，體積小、安全性高
- 安裝並設定時區為台北，確保記錄與時間顯示正確
- 從第一階段複製編譯好的 Angular 靜態頁面至 Nginx 預設網站根目錄 /usr/share/nginx/html
- 將 index.csr.html 改名為 index.html，作為單頁應用的入口頁面
- 套用自定的 nginx.conf，支援 Angular 的路由配置
- 開放 port 80，並設定 Nginx 為前景模式運行（daemon off）

