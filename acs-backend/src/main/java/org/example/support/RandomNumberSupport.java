package org.example.support;

import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base32;

public final class RandomNumberSupport {

	public static String getPwd() {
		SecureRandom r = new SecureRandom();
		String pwd = "";
		int j = 0;// 計數器，紀錄迴圈執行次數
		do {
			int chartype;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < 12; i++) {
				chartype = (int) ((r.nextDouble() * 7) % 3);

				if (chartype == 1) { // 放數字
					sb.append((int) (r.nextDouble() * 10));
				} else if (chartype == 2) { // 放大寫英文
					sb.append((char) (((r.nextDouble() * 26) + 65)));
				} else {// 放小寫英文
					sb.append(((char) ((r.nextDouble() * 26) + 97)));
				}
			}
			pwd = sb.toString();
			j++;
			if(j > 10) {
				// 迴圈執行超過10 次 跳出
				break;
			}
		} while (!checkpwd(pwd, j));

		return pwd;
	}

	public static boolean checkpwd(String pwd, int i) {
		boolean ckUpperCase = false, ckLowerCase = false, ckNum = false;
		char[] ca = pwd.toCharArray();
		for (char c : ca) {
			if (Character.isUpperCase(c)) {
				ckUpperCase = true;
			} else if (Character.isLowerCase(c)) {
				ckLowerCase = true;
			} else if (Character.isDigit(c)) {
				ckNum = true;
			}
		}
		if (ckUpperCase && ckLowerCase && ckNum) {
			System.out.println("迴圈執行次數:" + i);
			return true;
		} else {
			return false;
		}
	}
	
	public static String generateSecretKey() {
	    SecureRandom random = new SecureRandom();
	    byte[] bytes = new byte[20];
	    random.nextBytes(bytes);
	    Base32 base32 = new Base32();
	    return base32.encodeToString(bytes);
	}

    private RandomNumberSupport() {
        super();
    }

}
