import argparse
import paho.mqtt.client as mqtt
import time
import os
import uuid
from datetime import datetime
from dotenv import load_dotenv

load_dotenv()

MQTT_HOST = os.environ.get("MQTT_HOST", "localhost")
MQTT_PORT = 1883
DEVICE_ID = "device-001"

def on_connect(client, userdata, flags, rc):
    print("[裝置端] 已連線至 MQTT broker")
    topic = userdata['response_topic']
    client.subscribe(topic)
    print(f"[裝置端] 訂閱回應 topic: {topic}")

    # 確保在訂閱完成後再發送
    if 'request_topic' in userdata and 'payload' in userdata:
        print(f"[裝置端] 發送資料至 topic {userdata['request_topic']} → {userdata['payload']}")
        client.publish(userdata['request_topic'], userdata['payload'])

def on_message(client, userdata, msg):
    result = msg.payload.decode()
    now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    print(f"[裝置端] 收到授權結果: {result}")

    if result.startswith("grant"):
        print(f"[{now}] 開門成功（模擬）")
    elif result.startswith("deny"):
        parts = result.split("deny", 1)
        reason = parts[1].strip(" -") if len(parts) > 1 else ""
        if reason:
            print(f"[{now}] 拒絕進入（模擬）原因：{reason}")
        else:
            print(f"[{now}] 拒絕進入（模擬）")
    else:
        print(f"[{now}] 收到未知授權狀態")

def main():
    parser = argparse.ArgumentParser(description="模擬門禁設備（卡片或 QR 掃碼）")
    parser.add_argument('--mode', choices=['card', 'qr'], required=True, help="選擇模擬模式：card 或 qr")
    parser.add_argument('--cardId', help="僅 card 模式需輸入卡號")
    parser.add_argument('--deviceId', default=DEVICE_ID, help="設備 ID（預設為 device-001）")
    args = parser.parse_args()

    if args.mode == 'qr':
        args.deviceId = "device-002"
        request_topic = None
        payload = None
        response_topic = f"door/response/qr/{args.deviceId}"
    else:
        if not args.cardId:
            parser.error("刷卡模式必須提供 --cardId")
        request_topic = "door/request/card"
        response_topic = f"door/response/card/{args.deviceId}"
        payload = f"cardId:{args.cardId},deviceId:{args.deviceId}"

    client_id = f"simulator-{args.deviceId}-{uuid.uuid4()}"
    print(f"[裝置端] 啟動模式: {args.mode}，設備ID: {args.deviceId}，ClientId: {client_id}")

    # 建立 client，並把 request_topic/payload/response_topic 傳入
    client = mqtt.Client(client_id=client_id, userdata={
        'request_topic': request_topic,
        'response_topic': response_topic,
        'payload': payload
    })

    client.on_connect = on_connect
    client.on_message = on_message

    client.connect(MQTT_HOST, MQTT_PORT, 60)
    client.loop_start()

    if args.mode == 'card':
        time.sleep(10)  # 等待回應
    else:
        print(f"[裝置端] 等待授權回應（QR 模式），訂閱 topic: {response_topic}")
        time.sleep(300)

    client.loop_stop()
    client.disconnect()

if __name__ == "__main__":
    main()
