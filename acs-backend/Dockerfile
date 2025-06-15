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

## 複製 Maven Wrapper 所需檔案
COPY mvnw mvnw
COPY .mvn .mvn

# 複製整個 multi-module 專案（包含 backend 和 common）
COPY acs-common acs-common
COPY acs-backend acs-backend
COPY pom.xml pom.xml

##  給 Maven Wrapper 執行權限
RUN chmod +x mvnw

RUN echo "👀 列出目前目錄內容：" && ls -al /usr/src/app

# 建置 backend module  使用 Maven Wrapper 指定只打包 backend 模組（跳過測試）自動先建 acs-common、再建 acs-backend
RUN ./mvnw -pl acs-backend -am clean package -DskipTests

# build 階段找出正確 jar 名稱，改名成標準名稱 app.jar
RUN cp acs-backend/target/*.jar app.jar

# 第 2 階段（run）
# 載入 .jar，乾淨執行，不含多餘檔案或工具
FROM eclipse-temurin:21-jdk-alpine
# 第二階段：只用一個「乾淨的 JDK 執行環境」，不包含 Maven、原始碼等內容
# 這會大幅縮小 image 並提升安全性

WORKDIR /app
# 設定執行階段的工作目錄為 /app

# 複製打包後的 JAR
COPY --from=build /usr/src/app/app.jar app.jar
# 從前一個階段（名為 build）複製出打包好的 JAR 檔，放到 /app 並重新命名為 app.jar

EXPOSE 8080
#宣告這個 container 會對外開放的 port 是 8080

CMD ["java", "-jar", "app.jar"]
#設定 container 的預設啟動指令，container 啟動時會執行這一行，執行 JAR 檔
# modified test