# 第 1 階段（build）
# 用 Node.js 編譯 Angular 專案，產出 dist 靜態檔

FROM node:18-alpine AS build
# 使用 Node.js 18 + Alpine 版當作建構階段（build stage）的 base image
# alpine 表示精簡、高效、小體積
# AS build 是給這個階段命名，後面會用 --from=build 來引用

WORKDIR /usr/src/app
# 設定 container 內的工作目錄為 /usr/src/app
# 之後的所有 COPY, RUN 指令都會以這個目錄為基準

# 安裝必要套件（確保 ng CLI 可執行時不會缺 native 編譯元件）
RUN apk add --no-cache bash

# 先複製 package.json 和 package-lock.json（利用 Docker layer cache 優化），這樣如果程式碼變更但套件沒變，就不需要重新安裝套件。
COPY ./acs-frontend/package*.json ./

# 安裝相依套件
RUN npm install

# 複製 acs-frontend 目錄的所有檔案到容器內的工作目錄
COPY ./acs-frontend/ .

ARG BUILD_ENV=production
# 如果沒有在docker run 指令中提供 --build-arg BUILD_ENV=xxx，就預設用 production
# 如果你有提供，會覆蓋它

ENV PATH="/usr/src/app/node_modules/.bin:$PATH"
RUN chmod +x ./node_modules/.bin/ng \
    && echo "✅ Angular CLI path: $(which ng)" \
# 加入全域 CLI 路徑與權限修復（幫 ng 指令檔案加上執行權限，讓 shell 能執行它）
# 加入 ng 路徑，避免因 Alpine 的精簡版 Linux 預設 node_modules/.bin 不在 PATH 中，導致 ng 無法被 shell 找到或執行。

RUN echo "🛠 Building Angular app with configuration: $BUILD_ENV"
# 顯示編譯環境資訊

RUN npm run build -- --configuration=$BUILD_ENV
# 在 container 裡執行：
# npm install：安裝 Angular 專案所需的相依套件
# npm run build：執行 build，會產出在 dist/<project-name>/

# 第 2 階段
# 用 Nginx 提供靜態檔服務
FROM nginx:stable-alpine
# 建立新的階段（run stage），以官方的 Nginx + Alpine 版作為 base image
# 用來 serve 前端的靜態檔案（.html, .js, .css）
# Nginx 是最穩定、效能最佳的靜態伺服器之一

ARG BUILD_ENV=production


COPY --from=build /usr/src/app/dist/acs-frontend /usr/share/nginx/html
# 複製 Angular 靜態檔至 nginx 對應目錄
# 從前一個 build 階段中，把 dist/ 中編譯好的 Angular 靜態檔複製進 nginx 的對外根目錄 /usr/share/nginx/html

# 根據不同環境注入正確的 env.template.js
COPY ./acs-frontend/src/assets/env.${BUILD_ENV}.template.js /usr/share/nginx/html/assets/env.template.js

# 複製自訂 Nginx 設定與啟動腳本
COPY ./acs-frontend/default.conf /etc/nginx/conf.d/default.conf

# 複製 docker-entrypoint.sh 腳本
COPY ./acs-frontend/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 80
# 宣告 container 對外提供服務的 port 是 80（Nginx 預設 port）

# 容器啟動時會自動產出 env.js，供 index.html 載入
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]
