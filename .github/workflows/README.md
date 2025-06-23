## GitHub Actions CI/CD 工作流程說明：

### 流程總覽
| 階段                | 說明                                           |
| ----------------- | -------------------------------------------- |
| 1. Trigger Infra  | 觸發 `infra.yml` 確保 infra 服務（DB、Redis、MQTT）已準備 |
| 2. Docker Cleanup | 清除 Runner 上的 Docker 快取避免影響建置                 |
| 3. GCP 認證與設定      | 登入 GCP 並設定 Artifact Registry 權限              |
| 4. 建置並上傳映像        | 將前端專案建置成 Docker 映像並推送至 Artifact Registry     |
| 5. SSH 遠端部署       | 透過 SSH 登入 VM，拉取映像並以 Docker Compose 更新服務      |

#### Job 1：trigger-infra
使用 benc-uk/workflow-dispatch@v1 插件，呼叫同一目錄下的 infra.yml，確保 infra 服務事先啟動。

#### Job 2：deploy
- Step 1：拉取原始碼
  - GitHub Actions Runner 從 repo 拉取目前 commit 的程式碼內容到 Runner 的本機目錄，目的是為了讓後續的 Build 步驟。


- Step 2：清除 Docker 快取
  - 避免使用到先前舊的中間層映像，保證建置環境乾淨。


- Step 3：登入 GCP 並設定 Artifact Registry 權限
  - 授權 GCP 使用者金鑰（service account json）
  - 設定 GCP 專案與 Docker Artifact Registry 權限


- Step 4：建置並上傳映像
  - 使用當前 commit SHA 當作映像 tag，可以追蹤是哪一次的程式碼變更導致某個映像的功能或錯誤。


- Step 5：SSH 登入 VM 並部署映像
  - 將該 VM 主機的 SSH 公鑰寫入 Runner 的 ~/.ssh/known_hosts，表示這個 GCP VM 是可信任的。
  - 登入 VM，若未 clone 專案，則執行 git clone
  - 檢查 infra container 是否存在，若不存在直呼叫 docker-compose-infra.yml 啟動
  - 拉取新的映像
  - 執行 Docker Compose 更新服務

## GitHub 如何判斷要跑哪一個檔案？
GitHub 只要看到 .github/workflows/*.yml 裡有定義：
```text
on:
  push:
    branches: [main]
```
就會在 main 分支推送時自動觸發對應的 CI/CD 流程。

## 完成後會出現在哪裡？
部署後，每次你 push 到 GitHub，就能在：
GitHub Repo 頁面 → Actions 頁籤看到執行紀錄！


