import { Component,Input, OnInit,Output,EventEmitter ,NgModule  } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { WebSocketService } from '../../service/webSocketService';
import { EnvironmentService } from '../../service/environmentService';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  standalone: true,
  selector: 'app-view',
  templateUrl: './qrPage.component.html',
  styleUrl: './qrPage.component.scss',
  imports: [CommonModule, FormsModule],
  providers: [WebSocketService, EnvironmentService],
})
export class QrPageComponent implements OnInit {

  // 使用者輸入的員編（在 HTML 綁定）
  userId: string = '';
  // 從後端取得的 QRCode（base64 圖片）
  qrCode: SafeUrl | null = null;
  // 所有開門紀錄的清單
  accessRecords: any[] = [];
  // 錯誤訊息
  connectionError: boolean = false;

  isRequesting: boolean = true; // 預設為 true，表示正在連線/請求中

  private errorSub?: Subscription;
  private messageSub?: Subscription;
  private connectedSub?: Subscription;
  // 保存訂閱物件（Subscription），讓你可以在元件被銷毀時取消訂閱，避免資源浪費或記憶體洩漏。

  constructor(
    private http: HttpClient,
    private webSocket: WebSocketService,
    private envService: EnvironmentService,
    private sanitizer: DomSanitizer  
  ) {}


  // 使用者按下按鈕，產生 QRCode
  generateQrCode() {

    console.log('使用者輸入的 userId:', this.userId); 
    console.log('this.envService.API_URL', this.envService.API_URL); // ✅ 印出 userId

    if (!this.userId) {
      alert('請輸入員工編號');
      return;
    }

    // 呼叫 Java 後端的 API，並送出員編userId
    this.http.post<any>(`${this.envService.API_URL}/qr/generate`, { userId: this.userId })
      .subscribe({
        next: (response) => {
          // 後端會回傳一張 QRCode 的圖片（base64 格式）
          // 把圖片存在 qrCode 變數中，讓 HTML 顯示用
          if ('success' in response && response.success === false) {
            alert(response.message ?? '操作失敗');
          } else if ('qrCodeBase64' in response) {
            this.qrCode = this.sanitizer.bypassSecurityTrustUrl(`data:image/png;base64,${response.qrCodeBase64}`);
          } else {
            alert('未知錯誤，請稍後再試');
          }
        },
        error: (err) => {
          console.error('產生 QRCode 錯誤:', err);
          alert('產生 QRCode 失敗，請稍後再試');
        }
      });
  }

  // 當畫面載入完成元件初始化時執行
  ngOnInit(): void {

    // 訂閱推播資料
    this.messageSub = this.webSocket.messages$.subscribe(data => {
    const isValid = (record: any): boolean =>
      record.cardId && record.accessTime && record.deviceId;
      if (isValid(data)) {
         console.log('[收到推播資料]', data);
        this.accessRecords.unshift(data);
        this.accessRecords = this.accessRecords.slice(0, 10);
        this.isRequesting = false; 
      } else {
        this.accessRecords;
      }  
    });

    // 訂閱錯誤狀態
    this.errorSub = this.webSocket.connectionError$.subscribe(err => {
      this.connectionError = err;
      if (err) {
        console.error('[WebSocket 錯誤]', err);
        this.isRequesting = false; // 顯示錯誤後結束 loading
      }
    });

    // 當連線正常時就向後端索取資料
    this.connectedSub  = this.webSocket.connected$.subscribe((isConnected) => {
      if (isConnected) {
        console.log('[WebSocket 已連線] 送出初始化請求');
        try {
          this.webSocket.sendMessage('/app/request-records', '');
          this.isRequesting = false;
        } catch (e) {
          console.error('[初始化請求失敗]', e);
          this.connectionError = true;
          this.isRequesting = false;
        }
      }
    });


  }

  ngOnDestroy(): void {
    this.errorSub?.unsubscribe();
    this.connectedSub?.unsubscribe();
    this.messageSub?.unsubscribe();
    this.webSocket.disconnect(); //WebSocket 連線釋放
  }


}
