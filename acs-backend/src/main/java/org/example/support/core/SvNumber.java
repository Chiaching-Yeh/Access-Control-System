package org.example.support.core;

import java.text.DecimalFormat;

public class SvNumber {
    protected static String errorMsg = "";

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static String format(int var0, String var1) {
        try {
            DecimalFormat var2 = new DecimalFormat(var1);
            return var2.format((long)var0);
        } catch (Exception var3) {
            errorMsg = "SvNumber.format error: " + var3.toString();
            return null;
        }
    }

    public static String format(double var0, String var2) {
        try {
            DecimalFormat var3 = new DecimalFormat(var2);
            return var3.format(var0);
        } catch (Exception var4) {
            errorMsg = "SvNumber.format error: " + var4.toString();
            return null;
        }
    }

    public static String format(long var0, String var1) {
        try {
            DecimalFormat var2 = new DecimalFormat(var1);
            return var2.format(var0);
        } catch (Exception var3) {
            errorMsg = "SvNumber.format error: " + var3.toString();
            return null;
        }
    }

    public static String strChineseYear(String var0) {
        return strNumAdd(var0, -1911);
    }

    public static int chineseYear(String var0) {
        return numAdd(var0, -1911);
    }

    public static String strNumAdd(String var0, int var1) {
        try {
            int var2 = numAdd(var0, var1);
            return Integer.toString(var2);
        } catch (Exception var3) {
            errorMsg = "SvNumber.strNumAdd error: " + var3.toString();
            return null;
        }
    }

    public static int numAdd(String var0, int var1) throws NumberFormatException {
        int var2 = Integer.parseInt(var0);
        var2 += var1;
        errorMsg = "";
        return var2;
    }

    public static String cn2s(String var0) {
        try {
            StringBuffer var1 = new StringBuffer();

            for(int var3 = 0; var3 < var0.length(); ++var3) {
                int var2;
                if ((var2 = cw2n(var0.charAt(var3))) < 0) {
                    return null;
                }

                var1.append(var2);
            }

            return var1.toString();
        } catch (Exception var4) {
            errorMsg = "SvNumber.cn2s error: " + var4.toString();
            return null;
        }
    }

    public static long cn2l(String var0) {
        try {
            long var1 = 0L;
            long var3 = 0L;
            boolean var7 = false;

            for(int var9 = 0; var9 < var0.length(); ++var9) {
                int var8;
                if ((var8 = cw2n(var0.charAt(var9))) < 0) {
                    return -1L;
                }

                long var5;
                if (var9 + 1 < var0.length() && (var5 = cu2n(var0.charAt(var9 + 1))) >= 0L) {
                    var3 = (long)var8 * var5;
                    ++var9;
                } else {
                    var3 = (long)var8;
                }

                var1 += var3;
            }

            return var1;
        } catch (Exception var10) {
            errorMsg = "SvNumber.cn2l error: " + var10.toString();
            return 0L;
        }
    }

    protected static int cw2n(char var0) {
        char[] var1 = new char[]{'零', '０', '0', '○', 'O', 'Ｏ', '壹', '一', '1', '１', '貳', '二', '2', '２', '參', '三', '3', '３', '肆', '四', '4', '４', '伍', '五', '5', '５', '陸', '六', '6', '６', '柒', '七', '7', '７', '捌', '八', '8', '８', '玖', '九', '9', '９'};
        int[] var2 = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9};

        for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var0 == var1[var3]) {
                return var2[var3];
            }
        }

        return -1;
    }

    protected static long cu2n(char var0) {
        char[] var1 = new char[]{'拾', '佰', '仟', '萬', '億', '兆', '十', '百', '千'};
        long[] var2 = new long[]{10L, 100L, 1000L, 10000L, 100000000L, 1000000000000L, 10L, 100L, 1000L};

        for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var0 == var1[var3]) {
                return var2[var3];
            }
        }

        return -1L;
    }

    public static void main(String[] var0) {
        System.out.println(chineseYear("2002"));
    }
}