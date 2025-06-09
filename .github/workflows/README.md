## GitHub 如何判斷要跑哪一個檔案？

GitHub 只要看到 .github/workflows/*.yml 裡有定義：
``
on:
  push:
    branches: [main]
``

就會在 main 分支推送時自動觸發對應的 CI/CD 流程。


## 完成後會出現在哪裡？
部署後，每次你 push 到 GitHub，就能在：
GitHub Repo 頁面 → Actions 頁籤看到執行紀錄！

## backend.yml
 - 監聽 backend/ 資料夾有變更時觸發
 - Build + Push backend Docker image 到 Artifact Registry
 - SSH 到 GCP VM，執行 docker pull 並重新部署 backend 容器


## frontend.yml
- 監聽 acs-frontend/ 有變更時觸發
- Build + Push frontend Docker image 到 Artifact Registry
- SSH 到 GCP VM，執行 docker pull 並重新部署 frontend 容器

## GitHub Actions CI/CD 工作流程說明：backend.yml
本工作流程自動化部署 Spring Boot 後端服務至 GCP VM，採用 Docker + Artifact Registry + SSH 部署架構。以下為各步驟說明：
- Checkout source
  - 使用 [actions/checkout]，將 GitHub Repo 的程式碼 checkout 到 runner（GitHub 提供的 Ubuntu 虛擬機），方便後續建置 Docker Image。
- Set up Google Cloud SDK
  - 使用 [google-github-actions/setup-gcloud]，透過 GitHub Secrets 中的 GCP 專案 ID（GCP_PROJECT_ID）與服務帳號金鑰（GCP_SA_KEY）登入 Google Cloud。
  - 這一步讓 GitHub Actions 有權限操作 GCP 的資源（如 Artifact Registry）。
- Authenticate Docker with Artifact Registry
  - 執行 gcloud auth configure-docker，將 GCP Artifact Registry 登入資訊註冊給 Docker，使後續能夠成功推送 Docker Image 到 GCP 的容器倉庫。
- Build and Push Backend Docker Image
  - 使用 docker build 建置 acs-backend/acs-frontend 資料夾中的 Dockerfile，並將其標記為目標 Artifact Registry Image。
  - 接著執行 docker push 將 Image 上傳至 GCP，供 VM 拉取使用。
- SSH and Deploy Backend/Frontend on VM
  - 使用 [appleboy/ssh-action]，透過 SSH 登入 GCP VM（使用 VM_HOST, VM_USER, VM_SSH_KEY），在 VM 上執行部署指令。
  - 該指令會先 docker pull 最新後端 Image，再透過 docker-compose 重啟 backend/frontend 服務容器，以完成更新。
