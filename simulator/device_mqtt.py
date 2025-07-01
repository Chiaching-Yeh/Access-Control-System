import logging
import argparse
import paho.mqtt.client as mqtt
import time
import os
import uuid
from datetime import datetime
from dotenv import load_dotenv

load_dotenv()

MQTT_HOST = os.environ.get("MQTT_HOST", "localhost")
USERNAME = os.environ.get("MQTT_USERNAME")
PASSWORD = os.environ.get("MQTT_PASSWORD")
MQTT_PORT = 8883
DEVICE_ID = "device-001"

# 用來追蹤是否收到授權結果
got_response = False

def on_log(client, userdata, level, buf):
    print(f"[MQTT-LOG] {buf}")

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print("[裝置端] ✅ 已成功連線至 MQTT broker")

        topic = userdata.get('response_topic')
        if topic:
            result = client.subscribe(topic)
            print(f"[裝置端] 已訂閱回應 topic: {topic}（SUB result: {result[0]}）")
        else:
            print("[裝置端] ⚠️ 無 response_topic，略過訂閱")

        # 等訂閱完成再發送請求
        if 'request_topic' in userdata and 'payload' in userdata:
            request_topic = userdata['request_topic']
            payload = userdata['payload']
            time.sleep(1)  # 等待訂閱穩定（避免還沒來得及接收回應就送出）
            print(f"[裝置端] 發送資料至 topic {request_topic} → {payload}")
            result = client.publish(request_topic, payload)
            print(f"[裝置端] Publish result: {mqtt.error_string(result.rc)}")
        else:
            print("[裝置端] ⚠️ 無 request_topic 或 payload，略過發送")
    else:
        print(f"[裝置端] ❌ 連線失敗，錯誤代碼 rc={rc} ({mqtt.connack_string(rc)})")

def on_message(client, userdata, msg):

    global got_response
    got_response = True

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

    print(f"[DEBUG] USERNAME: {USERNAME}")
    print(f"[DEBUG] PASSWORD: {PASSWORD}")

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

    logging.basicConfig(level=logging.DEBUG)

    # 建立 client，並把 request_topic/payload/response_topic 傳入
    client = mqtt.Client(client_id=client_id, userdata={
        'request_topic': request_topic,
        'response_topic': response_topic,
        'payload': payload
    })

    client.enable_logger()

    client.tls_set() # 啟用 TLS
    client.username_pw_set(USERNAME, PASSWORD)
    client.on_connect = on_connect
    client.on_message = on_message
    client.on_log = on_log

    client.connect(MQTT_HOST, MQTT_PORT, 60)
    client.loop_start()

    wait_seconds = 10 if args.mode == 'card' else 60
    for i in range(wait_seconds):
        if got_response:
            break
        print(f"[裝置端] ⏳ 等待授權回應中...（{i + 1}/{wait_seconds}秒）")
        time.sleep(1)

    if not got_response:
        print("[裝置端] ❗ 等待逾時，未收到授權結果")

    client.loop_stop()
    client.disconnect()

if __name__ == "__main__":
    main()
