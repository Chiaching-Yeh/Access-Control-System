# 第 1 階段（build）
# 用 Node.js 編譯 Angular 專案，產出 dist 靜態檔

FROM node:18-alpine AS build
# 使用 Node.js 18 + Alpine 版當作建構階段（build stage）的 base image
# alpine 表示精簡、高效、小體積
# AS build 是給這個階段命名，後面會用 --from=build 來引用

WORKDIR /usr/src/app
# 設定 container 內的工作目錄為 /usr/src/app
# 之後的所有 COPY, RUN 指令都會以這個目錄為基準

COPY . .
# 將你前端專案的所有檔案（含 package.json, angular.json, src/ 等）複製到 container 內的 /usr/src/app

RUN npm install && npm run build
# 在 container 裡執行：
# npm install：安裝 Angular 專案所需的相依套件
# npm run build：執行 build，會產出在 dist/<project-name>/

# 第 2 階段
# 用 Nginx 提供靜態檔服務
FROM nginx:stable-alpine
# 建立新的階段（run stage），以官方的 Nginx + Alpine 版作為 base image
# 用來 serve 前端的靜態檔案（.html, .js, .css）
# Nginx 是最穩定、效能最佳的靜態伺服器之一

COPY --from=build /usr/src/app/dist/acs-frontend /usr/share/nginx/html
# 從前一個 build 階段中，把 dist/ 中編譯好的 Angular 靜態檔複製進 nginx 的對外根目錄 /usr/share/nginx/html

EXPOSE 80
# 宣告 container 對外提供服務的 port 是 80（Nginx 預設 port）
