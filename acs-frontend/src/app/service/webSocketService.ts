import { Injectable} from '@angular/core';
import { catchError, retry, tap } from 'rxjs/operators';
import { EMPTY, Observable, Subject, BehaviorSubject } from 'rxjs';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { EnvironmentService } from './environmentService';
import SockJS from 'sockjs-client';

@Injectable({ providedIn: 'root' })
export class WebSocketService {

  private stompClient: Client;
  public messages$ = new Subject<any>();
  public connectionError$ = new BehaviorSubject<boolean>(false);
  public connected$ = new BehaviorSubject<boolean>(false);

constructor(private envService: EnvironmentService, @Inject(PLATFORM_ID) private platformId: Object) {
  if (isPlatformBrowser(this.platformId)) {
    this.stompClient = new Client({
//       webSocketFactory: () => new SockJS('http://localhost:8080/ws/access'),
      webSocketFactory: () => new SockJS(this.envService.WS_URL),
      reconnectDelay: 0,  // 設定為 0 表示 不會自動重新連線
      debug: str => console.log(str),
      onConnect: () => {
          this.stompClient.subscribe('/topic/access', (message: IMessage) => {
            const body = JSON.parse(message.body);
            this.messages$.next(body);
          });
          this.connectionError$.next(false);
          this.connected$.next(true);
      },
      // 連線被關閉
      onWebSocketClose: (event) => {
        console.error('WebSocket 被關閉', event);
          this.connectionError$.next(true);
          this.connected$.next(false);
      },

      onStompError: (frame) => {
        console.error('STOMP 協定錯誤', frame.headers['message'], frame.body);
          this.connectionError$.next(true);
          this.connected$.next(false);
      },

      onWebSocketError: (event) => {
        console.error('WebSocket 錯誤', event);
          this.connectionError$.next(true);
          this.connected$.next(false);
      },
    });
  }

    this.stompClient.activate(); // 啟動連線


  }

// 傳送訊息到後端
  sendMessage(destination: string, payload: any) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: destination,
        body: JSON.stringify(payload),
      });
    } else {
      console.warn('STOMP 尚未連線，無法送出訊息');
        this.connectionError$.next(true);
    }
  }

  // 關閉 WebSocket
  disconnect() {
    this.stompClient.deactivate();
  }

}
