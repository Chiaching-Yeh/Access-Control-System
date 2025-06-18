# -------- 第一階段：Build Angular --------
FROM node:22-alpine AS build

WORKDIR /usr/src/app
# 安裝必要工具
RUN apk add --no-cache bash

COPY package*.json ./

# 安裝相依套件
RUN npm install

# 複製專案原始碼（排除 .dockerignore 的項目）
COPY . .

# 避免 Permission denied 與 Angular cache
RUN npx ng config cli.cache.enabled false && rm -rf .angular/cache

# 執行編譯
ARG BUILD_ENV=production
ARG API_URL=https://charlielab.online/api
RUN npx ng build --configuration=$BUILD_ENV

# 產生環境變數檔（供 Angular 使用）
RUN mkdir -p ./dist/acs-frontend/browser/assets && \
    sed "s|__API_URL__|${API_URL}|g" ./src/assets/env.${BUILD_ENV}.template.js > ./dist/acs-frontend/browser/assets/env.js

RUN echo "🗂 dist/acs-frontend/browser：" && ls -al ./dist/acs-frontend/browser

# -------- 第二階段：Serve with Nginx --------
#FROM nginx:stable-alpine
#
##  複製 Angular 編譯結果到 Nginx 靜態網站根目錄
#COPY --from=build /usr/src/app/dist/acs-frontend/browser /usr/share/nginx/html
#
## 把 index.csr.html 改名成 index.html（覆蓋掉預設頁面）
#RUN mv /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html
#
## 複製 Nginx 自訂設定
#COPY default.conf /etc/nginx/conf.d/default.conf
#
#EXPOSE 80
#
#CMD ["nginx", "-g", "daemon off;"]
