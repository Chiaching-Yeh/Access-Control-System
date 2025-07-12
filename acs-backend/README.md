## DockerFile 說明

### 第一階段：建構 Spring Boot 專案（Build Stage）

- 使用 Temurin JDK 21（Alpine）作為基礎映像，輕量、效能佳，適合用來進行 Maven 打包

- 指定工作目錄為 /usr/src/app

- 安裝 Maven Wrapper 所需檔案

- 複製專案原始碼包含 acs-common 與 acs-backend 的 multi-module 原始碼以及最上層的 pom.xml（管理模組依賴）

- 賦予 Maven Wrapper 執行權限，使用 chmod +x mvnw 讓它可以被正確執行

- 建置 backend 模組（自動包含 acs-common）

  #### 使用 Maven Wrapper：
    1. pl：只建 acs-backend 模組
    2. am：同時建構其所依賴的 acs-common
    3. DskipTests：略過測試，加快建置速度
  

- 將打包好的 JAR 檔重新命名為 app.jar 統一命名便於後續部署

### **第二階段：執行 Spring Boot 應用（Run Stage）**

- 使用乾淨的 Temurin JDK 21（Alpine）映像，不含 Maven、原始碼與建置工具，安全、體積小
- 設定工作目錄為 /app，所有後續操作都相對於此資料夾進行
- 安裝時區套件與設定為台北時間，確保日誌與系統行為與台灣時間一致
- 複製第一階段產生的 app.jar 取得已編譯好的 JAR 並放到 /app 資料夾中
- 宣告容器對外開放的埠號為 8080 便於部署後 Nginx 或 Load Balancer 對接
- 啟動容器時執行 java -jar app.jar

***

## 🧠 補充觀念：
- WORKDIR 是針對「當前階段」的容器內部路徑設定。
- 多階段中，每個階段都有自己的 filesystem 與 working directory。



