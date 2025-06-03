package org.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class AccessRecord {

    Long recordUid; // 紀錄UID
    String cardId; // 讀到的卡號
    LocalDateTime accessTime; // 刷卡時間
    boolean isSuccessful;
    String reason; // 失敗原因
    String deviceId; // 裝置編號（模擬裝置即可）

}
