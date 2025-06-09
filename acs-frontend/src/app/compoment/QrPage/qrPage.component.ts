import { Component,Input, OnInit,Output,EventEmitter ,NgModule  } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { WebSocketService } from '../../service/webSocketService';
import { EnvironmentService } from '../../service/environmentService';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  standalone: true,
  selector: 'app-view',
  templateUrl: './qrPage.component.html',
  styleUrl: './qrPage.component.scss',
  imports: [CommonModule, FormsModule],
})
export class QrPageComponent implements OnInit {

  // 使用者輸入的員編（在 HTML 綁定）
  userId: string = '';
  // 從後端取得的 QRCode（base64 圖片）
  qrCode: string = '';
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
    private webSoket: WebSocketService,
    private envService: EnvironmentService
  ) {
    console.log(this.envService.API_URL);
    }


  // 使用者按下按鈕，產生 QRCode
  generateQrCode() {
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
          this.qrCode = response.qrCodeBase64;
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
    this.messageSub = this.webSoket.messages$.subscribe(data => {
      if (data) {
        this.accessRecords.unshift(data);
        this.isRequesting = false;
      }
    });

    // 訂閱錯誤狀態
    this.errorSub = this.webSoket.connectionError$.subscribe(err => {
      this.connectionError = err;
      if (err) {
        this.isRequesting = false; // 顯示錯誤後結束 loading
      }
    });

    // 當連線正常時就向後端索取資料
    this.connectedSub  = this.webSoket.connected$.subscribe((isConnected) => {
      if (isConnected) {
        this.webSoket.sendMessage('/app/request-records', '');
      }
    });


  }

  ngOnDestroy(): void {
    this.errorSub?.unsubscribe();
    this.connectedSub?.unsubscribe();
    this.messageSub?.unsubscribe();
  }

}
