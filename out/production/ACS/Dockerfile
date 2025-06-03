# 第 1 階段（build）
# 建構原始碼、打包 .jar 檔案（包含 commom module）
FROM eclipse-temurin:21-jdk-alpine AS build
# 使用 Temurin JDK 21 + Alpine 版本 作為「建構階段（build stage）」的基礎 image
# AS build 是給這個階段一個名字，讓後面可以引用
# alpine 表示是輕量版 Linux，image 小、效率高，但預設工具較少

WORKDIR /usr/src/app
# 設定工作目錄為 /usr/src/app，之後所有操作（如 COPY、RUN）都會在這個目錄下進行
# 沒有這個目錄時會自動建立
# /usr/src/app 是開源專案慣例：source code 暫存與編譯區

COPY ../commom ../commom
# 把專案外層的 commom/ 模組複製進來，因為它是 backend 專案所依賴的 module（如 model、DAO）
# 注意：
# build context 必須是 project-root 才能使用 ../commom 這樣的相對路徑
# COPY 指令是以 build.context 作為基準來看路徑的

COPY . .
# 把當前 context（也就是 acs-backend/）底下的檔案全部複製到 container 的 /usr/src/app/ 中

RUN ./mvnw clean package -DskipTests
# 在 container 中執行 Maven wrapper 指令，打包出 Spring Boot JAR 檔
# clean package: 清除舊的 build，再編譯並產出 .jar
# -DskipTests: 跳過測試（加快 build，避免測試阻擋 CI/CD）

# 第 2 階段（run）
# 載入 .jar，乾淨執行，不含多餘檔案或工具
FROM eclipse-temurin:21-jdk-alpine
# 第二階段：只用一個「乾淨的 JDK 執行環境」，不包含 Maven、原始碼等內容
# 這會大幅縮小 image 並提升安全性

WORKDIR /app
# 設定執行階段的工作目錄為 /app

COPY --from=build /usr/src/app/target/backend-0.0.1-SNAPSHOT.jar app.jar
# 從前一個階段（名為 build）複製出打包好的 JAR 檔，放到 /app 並重新命名為 app.jar
# 這行實現了「乾淨的部署階段不帶原始碼」的目標（multi-stage 的精華）

EXPOSE 8080
#宣告這個 container 會對外開放的 port 是 8080

CMD ["java", "-jar", "app.jar"]
#設定 container 的預設啟動指令，container 啟動時會執行這一行，執行 JAR 檔