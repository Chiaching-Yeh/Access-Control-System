package org.example.support;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Slf4j
public final class QrCodeSupport {

    public static String generateBase64Qr(String content) {

        try {
            int size = 300;
            // 1. 建立 QRCode 的 BitMatrix（內容、格式、尺寸）
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size);

            // 2. 將 BitMatrix 轉成 BufferedImage
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 3. 輸出成 Base64 圖片
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("生成 QR 失敗", e);
        }

    }

    private QrCodeSupport() {
        super();
    }

}
