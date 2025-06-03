FROM python:3.10-slim
# 使用 Python 官方的 3.10 slim 版本 作為基礎映像：
# slim 表示精簡版本，只保留最基本執行環境
# 適合執行 CLI、小型服務，體積比 python:3.10 小約一半
WORKDIR /usr/src/app
# 設定 container 內的預設工作目錄為 /usr/src/app
# 之後的 COPY, RUN, CMD 等操作都會以這裡為根目錄
# 若該目錄不存在會自動建立

COPY requirements.txt .
# 只複製 requirements.txt（Python 套件清單）進 container
# 這樣做的好處是：如果套件沒變，Docker 可以利用快取，不必重跑 pip install，加快 build。
# COPY <來源路徑> <目的地路徑>

RUN pip install -r requirements.txt
# 安裝 requirements.txt 裡面列出的所有 Python 相依套件
# pip install 可重用快取，加速 build

COPY . .
# 把整個 Python 專案目錄複製到 container 的 /usr/src/app
# 內含腳本 device_mqtt.py

CMD ["python", "device_mqtt.py"]
# 設定 container 預設啟動時要執行的程式

