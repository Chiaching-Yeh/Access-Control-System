# -------- 第一階段：Build Angular --------
FROM node:22-alpine AS build

WORKDIR /usr/src/app

RUN apk add --no-cache bash

COPY package*.json ./

# 安裝相依套件
RUN npm install

# 複製專案原始碼（排除 .dockerignore 的項目）
COPY . .

# 執行編譯
ARG API_URL=https://35.212.237.104:8080
RUN npx ng build --configuration=production --verbose

# 產生環境變數檔（供 Angular 使用）
RUN mkdir -p ./dist/acs-frontend/assets && \
    sed "s|__API_URL__|${API_URL}|g" ./src/assets/env.${BUILD_ENV}.template.js > ./dist/acs-frontend/assets/env.js

# 清除不必要檔案以減少 image 體積
RUN rm -rf node_modules \
           src \
           e2e \
           .git \
           .angular \
           .vscode \
           .npm \
           README.md \
           package-lock.json

# -------- 第二階段：Serve with Nginx --------
FROM nginx:stable-alpine

# 複製編譯後的檔案到 Nginx 對外目錄
COPY --from=build /usr/src/app/dist/acs-frontend /usr/share/nginx/html

# 複製 Nginx 自訂設定
COPY default.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
