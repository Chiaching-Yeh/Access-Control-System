# 第 1 階段（build）
# 用 Node.js 編譯 Angular 專案，產出 dist 靜態檔

FROM node:18-alpine AS build
# 使用 Node.js 18 + Alpine 版當作建構階段（build stage）的 base image
# alpine 表示精簡、高效、小體積
# AS build 是給這個階段命名，後面會用 --from=build 來引用

WORKDIR /usr/src/app
# 設定 container 內的工作目錄為 /usr/src/app
# 之後的所有 COPY, RUN 指令都會以這個目錄為基準

# Debug 1：檢查 build context 是否正確
RUN echo "🐞 Build Context 根目錄內容：" && ls -al && echo "==="

# 安裝必要套件（確保 ng CLI 可執行時不會缺 native 編譯元件）
RUN apk add --no-cache bash

ARG BUILD_ENV=production
ARG API_URL=https://35.212.237.104:8080
# 如果沒有在docker run 指令中提供 --build-arg BUILD_ENV=xxx，就預設用 production
# 如果你有提供，會覆蓋它

# 先安裝 Angular CLI，確保 `ng` 可執行（避免 `sh: ng: Permission denied`）
RUN npm install -g @angular/cli

# 🧪 確認 ng CLI 安裝成功且具有執行權限
RUN echo "✅ global ng path: $(which ng)" && ls -l $(which ng)

# 先複製 package.json 和 package-lock.json（利用 Docker layer cache 優化），這樣如果程式碼變更但套件沒變，就不需要重新安裝套件。
COPY package*.json ./

# 安裝相依套件
RUN npm install

# Debug 3：安裝完後確認 ng CLI 路徑與權限
RUN echo "📦 BEFORE COPY . ." && ls -l $(which ng)

# 複製 acs-frontend 目錄下的所有檔案到容器內的工作目錄 COPY <來源路徑> <目的地路徑>
COPY . .

RUN chmod +x ./node_modules/.bin/ng
RUN chmod +x /usr/local/bin/ng

# ✅ 再次確認 ng CLI 仍然是 global 安裝的版本
RUN echo "📦 ng 確認：" && which ng && ls -l $(which ng)

# Debug 4：COPY 後再次確認 ng CLI 有沒有被覆蓋掉
RUN echo "📦 AFTER COPY . ." && ls -l $(which ng)

# 確保 `ng` 指令擁有執行權限
RUN echo "✅ Angular CLI path: $(which ng)"

# 顯示編譯環境資訊
RUN echo "🛠 Building Angular app with configuration: $BUILD_ENV"

# 禁用 Angular CLI 快取，確保 `ng build` 總是重新編譯
RUN ng config cli.cache.enabled false
RUN rm -rf .angular/cache

# Debug 6：列出 src 與 assets，確保存在
RUN echo "📁 確認 src/ assets/ 存在？" && ls -al src && ls -al src/assets || echo "❌ 找不到"

# 執行 Angular build
RUN npm run build -- --configuration=$BUILD_ENV

# mkdir -p 會確保 assets/ 目錄存在，避免 sed 失敗
# 在 build 階段完成環境變數注入（減少 runtime 變數錯誤）
RUN mkdir -p ./dist/acs-frontend/assets && \
    sed "s|__API_URL__|${API_URL}|g" ./src/assets/env.${BUILD_ENV}.template.js > ./dist/acs-frontend/assets/env.js

# 第 2 階段
# 用 Nginx 提供靜態檔服務
FROM nginx:stable-alpine
# 建立新的階段（run stage），以官方的 Nginx + Alpine 版作為 base image
# 用來 serve 前端的靜態檔案（.html, .js, .css）
# Nginx 是最穩定、效能最佳的靜態伺服器之一

COPY --from=build /usr/src/app/dist/acs-frontend /usr/share/nginx/html
# 複製 Angular 靜態檔至 nginx 對應目錄
# 從前一個 build 階段中，把 dist/ 中編譯好的 Angular 靜態檔複製進 nginx 的對外根目錄 /usr/share/nginx/html

# 複製自訂 Nginx 設定與啟動腳本
#COPY ./acs-frontend/default.conf /etc/nginx/conf.d/default.conf
COPY default.conf /etc/nginx/conf.d/default.conf


EXPOSE 80
# 宣告 container 對外提供服務的 port 是 80（Nginx 預設 port）

CMD ["nginx", "-g", "daemon off;"]
