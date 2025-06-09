import argparse  # 處理指令列參數（CLI）
import paho.mqtt.client as mqtt  # MQTT 通訊協定函式庫（需 pip install）
import time  # 內建模組
import os
from dotenv import load_dotenv
# 載入 .env 檔案的變數
load_dotenv()

# MQTT broker 設定
MQTT_HOST = os.environ.get("MQTT_HOST", "localhost")
MQTT_PORT = 1883
DEVICE_ID = "device-001"  # 預設設備 ID

# MQTT 事件處理
def on_connect(client, userdata, flags, rc):
    print("[裝置端] 已連線至 MQTT broker")
    client.subscribe(userdata['response_topic'])
    print(f"[裝置端] 訂閱回應 topic: {userdata['response_topic']}")

# 當收到訊息（回應授權結果）時，會自動觸發這個函式。
# msg.payload.decode() 把收到的位元資料轉換成字串。
# 根據內容判斷是否顯示「開門」或「拒絕」。
def on_message(client, userdata, msg):
    result = msg.payload.decode()
    print(f"[裝置端] 收到授權結果: {result}")
    if result == "grant":
        print("✅ 開門成功（模擬）")
    else:
        print("❌ 拒絕進入（模擬）")


#--mode 必須指定 card 或 qr
#--cardId：卡片模式要用
#--uuid：QR 模式要用
#--deviceId：可選，預設是 "device-001"

def main():
    parser = argparse.ArgumentParser(description="模擬門禁設備（卡片或 QR 掃碼）")
    parser.add_argument('--mode', choices=['card', 'qr'], required=True, help="選擇模擬模式：card 或 qr")
    parser.add_argument('--cardId', help="刷卡模式使用的卡號")
    parser.add_argument('--uuid', help="QR 模式使用的 UUID")
    parser.add_argument('--deviceId', default=DEVICE_ID, help="設備 ID（預設為 device-001）")
    args = parser.parse_args()

    if args.mode == 'card' and not args.cardId:
        parser.error("刷卡模式必須提供 --cardId")
    if args.mode == 'qr' and not args.uuid:
        parser.error("QR 模式必須提供 --uuid")

    # 根據模式設定 topic 與 payload
    if args.mode == 'card':
        request_topic = "door/request"
        response_topic = f"door/response/{args.cardId}"
        payload = f"cardId:{args.cardId},deviceId:{args.deviceId}"
    elif args.mode == 'qr':
        request_topic = "door/request/qr"
        response_topic = f"door/response/qr/{args.uuid}"
        payload = f"uuid:{args.uuid},deviceId:{args.deviceId}"

    # 建立 MQTT client
    client = mqtt.Client(userdata={'response_topic': response_topic})
    client.on_connect = on_connect
    client.on_message = on_message

    client.connect(MQTT_HOST, MQTT_PORT, 60)
    client.loop_start()

    print(f"[裝置端] 發送資料至 topic {request_topic} → {payload}")
    client.publish(request_topic, payload)

    # 等待回應
    time.sleep(10)           # 等待最多 10 秒的回應
    client.loop_stop()       # 停止背景執行緒
    client.disconnect()      # 中斷連線

if __name__ == "__main__":
    main()
