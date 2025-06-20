package org.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
public class AccessRecord {

    UUID recordUid; // 紀錄UID
    String cardId; // 讀到的卡號
    LocalDateTime accessTime; // 刷卡時間
    boolean successful;
    String reason; // 失敗原因
    String deviceId; // 裝置編號（模擬裝置即可）

}
