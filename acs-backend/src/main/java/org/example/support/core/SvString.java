package org.example.support.core;

import java.util.ArrayList;

public class SvString {
    protected static String errorMsg = "";

    public static String getErrorMsg() {
        return errorMsg;
    }

    public static String left(String var0, int var1) {
        int var2 = var0.indexOf(var1);
        return var2 < 0 ? var0 : var0.substring(0, var2);
    }

    public static String left(String var0, String var1) {
        int var2 = var0.indexOf(var1);
        return var2 < 0 ? var0 : var0.substring(0, var2);
    }

    public static String lastLeft(String var0, int var1) {
        int var2 = var0.lastIndexOf(var1);
        return var2 < 0 ? var0 : var0.substring(0, var2);
    }

    public static String lastLeft(String var0, String var1) {
        int var2 = var0.lastIndexOf(var1);
        return var2 < 0 ? var0 : var0.substring(0, var2);
    }

    public static String right(String var0, int var1) {
        int var2 = var0.indexOf(var1);
        return var2 < 0 ? var0 : var0.substring(var2 + 1);
    }

    public static String right(String var0, String var1) {
        int var2 = var0.indexOf(var1);
        return var2 < 0 ? var0 : var0.substring(var2 + var1.length());
    }

    public static String lastRight(String var0, int var1) {
        int var2 = var0.lastIndexOf(var1);
        return var2 < 0 ? var0 : var0.substring(var2 + 1);
    }

    public static String lastRight(String var0, String var1) {
        int var2 = var0.lastIndexOf(var1);
        return var2 < 0 ? var0 : var0.substring(var2 + var1.length());
    }

    public static String at(String var0, int var1, int var2) {
        try {
            int var3 = 0;

            int var4;
            int var5;
            for(var5 = 0; (var4 = var0.indexOf(var1, var3)) != -1; var3 = var4 + 1) {
                if (var5++ == var2) {
                    return var0.substring(var3, var4);
                }
            }

            if (var5 == var2) {
                return var0.substring(var3);
            }
        } catch (Exception var6) {
            errorMsg = var6.getMessage();
        }

        return null;
    }

    public static String at(String var0, String var1, int var2) {
        try {
            int var3 = 0;

            int var4;
            int var5;
            for(var5 = 0; (var4 = var0.indexOf(var1, var3)) != -1; var3 = var4 + var1.length()) {
                if (var5++ == var2) {
                    return var0.substring(var3, var4);
                }
            }

            if (var5 == var2) {
                return var0.substring(var3);
            }
        } catch (Exception var6) {
            errorMsg = var6.getMessage();
        }

        return null;
    }

    public static String lastAt(String var0, int var1, int var2) {
        try {
            int var4 = var0.length() - 1;

            int var3;
            int var5;
            for(var5 = 0; (var3 = var0.lastIndexOf(var1, var4)) != -1; var4 = var3 - 1) {
                if (var5++ == var2) {
                    return var0.substring(var3 + 1, var4 + 1);
                }
            }

            if (var5 == var2) {
                return var0.substring(0, var4 + 1);
            }
        } catch (Exception var6) {
            errorMsg = var6.getMessage();
        }

        return null;
    }

    public static String lastAt(String var0, String var1, int var2) {
        try {
            int var4 = var0.length() - 1;

            int var3;
            int var5;
            for(var5 = 0; (var3 = var0.lastIndexOf(var1, var4)) != -1; var4 = var3 - 1) {
                if (var5++ == var2) {
                    return var0.substring(var3 + var1.length(), var4 + 1);
                }
            }

            if (var5 == var2) {
                return var0.substring(0, var4 + 1);
            }
        } catch (Exception var6) {
            errorMsg = var6.getMessage();
        }

        return null;
    }

    public static String first(String var0, int var1) {
        return at(var0, var1, 0);
    }

    public static String first(String var0, String var1) {
        return at(var0, var1, 0);
    }

    public static String last(String var0, int var1) {
        return lastAt(var0, var1, 0);
    }

    public static String last(String var0, String var1) {
        return lastAt(var0, var1, 0);
    }

    public static String delAt(String var0, int var1, int var2) {
        try {
            int var3 = 0;

            int var4;
            int var5;
            for(var5 = 0; (var4 = var0.indexOf(var1, var3)) != -1; var3 = var4 + 1) {
                if (var5++ == var2) {
                    return var0.substring(0, var3) + var0.substring(var4 + 1);
                }
            }

            if (var5 == 0) {
                return "";
            }

            if (var5 == var2) {
                return var0.substring(0, var3 - 1);
            }
        } catch (Exception var6) {
            errorMsg = var6.getMessage();
        }

        return null;
    }

    public static String replace(String var0, String var1, String var2) {
        try {
            StringBuffer var3 = new StringBuffer("");
            int var4 = var1.length();

            int var5;
            int var6;
            for(var5 = 0; (var6 = var0.indexOf(var1, var5)) >= 0; var5 = var6 + var4) {
                var3.append(var0.substring(var5, var6) + var2);
            }

            var3.append(var0.substring(var5));
            return var3.toString();
        } catch (Exception var7) {
            errorMsg = var7.getMessage();
            return null;
        }
    }

    public static String[] split(String var0, int var1) {
        try {
            int var2 = -1;

            int var3;
            for(var3 = 0; (var2 = var0.indexOf(var1, var2 + 1)) != -1; ++var3) {
            }

            String[] var4 = new String[var3 + 1];
            int var5 = 0;

            for(int var7 = 0; var7 < var3; ++var7) {
                int var6 = var0.indexOf(var1, var5);
                var4[var7] = var0.substring(var5, var6);
                var5 = var6 + 1;
            }

            var4[var3] = var0.substring(var5);
            return var4;
        } catch (Exception var8) {
            errorMsg = var8.getMessage();
            return null;
        }
    }

    public static String combine(String[] var0, int var1) {
        try {
            if (var0 == null) {
                return null;
            } else {
                int var2 = var0.length;
                StringBuffer var3 = new StringBuffer();

                for(int var4 = 0; var4 < var2; ++var4) {
                    var3.append(var0[var4] + (char)var1);
                }

                return var3.substring(0, var3.length() - 1);
            }
        } catch (Exception var5) {
            errorMsg = var5.getMessage();
            return null;
        }
    }

    public static String[] split(String var0, String var1) {
        try {
            int var2 = -1;

            int var3;
            for(var3 = 0; (var2 = var0.indexOf(var1, var2 + 1)) != -1; ++var3) {
            }

            String[] var4 = new String[var3 + 1];
            int var5 = var1.length();
            int var6 = 0;

            for(int var8 = 0; var8 < var3; ++var8) {
                int var7 = var0.indexOf(var1, var6);
                var4[var8] = var0.substring(var6, var7);
                var6 = var7 + var5;
            }

            var4[var3] = var0.substring(var6);
            return var4;
        } catch (Exception var9) {
            errorMsg = var9.getMessage();
            return null;
        }
    }

    public static String combine(String[] var0, String var1) {
        try {
            if (var0 == null) {
                return null;
            } else {
                int var2 = var0.length;
                StringBuffer var3 = new StringBuffer();

                for(int var4 = 0; var4 < var2; ++var4) {
                    var3.append(var0[var4] + var1);
                }

                return var3.substring(0, var3.length() - var1.length());
            }
        } catch (Exception var5) {
            errorMsg = var5.getMessage();
            return null;
        }
    }

    public static String[] splits(String var0, int[] var1) {
        try {
            ArrayList var2 = new ArrayList();
            String var3 = "";
            int var4 = 0;

            for(int var5 = 0; var5 < var0.length(); ++var5) {
                for(int var6 = 0; var6 < var1.length; ++var6) {
                    if (var0.charAt(var5) == var1[var6]) {
                        var3 = var0.substring(var4, var5);
                        var2.add(var3);
                        var4 = var5 + 1;
                        break;
                    }
                }
            }

            var3 = var0.substring(var4);
            var2.add(var3);
            String[] var9 = new String[var2.size()];

            for(int var7 = 0; var7 < var9.length; ++var7) {
                var9[var7] = (String)var2.get(var7);
            }

            return var9;
        } catch (Exception var8) {
            errorMsg = var8.getMessage();
            return null;
        }
    }

    public static String[] splits(String var0, String[] var1) {
        try {
            ArrayList var2 = new ArrayList();
            String var3 = "";
            int var4 = 0;
            boolean var6 = false;

            for(int var5 = 0; var5 < var0.length(); ++var5) {
                for(int var7 = 0; var7 < var1.length; ++var7) {
                    int var10 = var1[var7].length();
                    if (var0.substring(var5, var5 + var10).equals(var1[var7])) {
                        var3 = var0.substring(var4, var5);
                        var3 = var3.trim();
                        if (!var3.equals("")) {
                            var2.add(var3);
                        }

                        var4 = var5 + var10;
                        var5 += var10 - 1;
                        break;
                    }
                }
            }

            var3 = var0.substring(var4);
            var3 = var3.trim();
            if (!var3.equals("")) {
                var2.add(var3);
            }

            String[] var11 = new String[var2.size()];

            for(int var8 = 0; var8 < var11.length; ++var8) {
                var11[var8] = (String)var2.get(var8);
            }

            return var11;
        } catch (Exception var9) {
            errorMsg = var9.getMessage();
            return null;
        }
    }

    public static String[] split(String var0, int var1, int var2) {
        try {
            ArrayList var3 = new ArrayList();
            int var4 = 0;
            boolean var5 = false;
            int var6 = 0;

            for(int var7 = 0; var7 < var0.length(); ++var7) {
                if (var0.charAt(var7) == var1) {
                    ++var6;
                    if (var6 == 1) {
                        var4 = var7;
                    }
                } else if (var0.charAt(var7) == var2) {
                    --var6;
                    if (var6 == 0) {
                        var3.add(var0.substring(var4 + 1, var7));
                    }
                }
            }

            if (var3.size() <= 0) {
                errorMsg = "沒有符合的子字串";
                return null;
            } else {
                String[] var10 = new String[var3.size()];

                for(int var8 = 0; var8 < var10.length; ++var8) {
                    var10[var8] = (String)var3.get(var8);
                }

                return var10;
            }
        } catch (Exception var9) {
            errorMsg = var9.getMessage();
            return null;
        }
    }

    public static String[] split(String var0, String var1, String var2) {
        try {
            ArrayList var3 = new ArrayList();
            int var4 = -1;

            int var9;
            for(boolean var5 = false; (var4 = var0.indexOf(var1, var4 + 1)) >= 0; var4 = var9 + var2.length()) {
                var9 = var4 + var1.length();
                if ((var9 = var0.indexOf(var2, var9 + 1)) >= 0) {
                    var3.add(var0.substring(var4 + var1.length(), var9));
                }
            }

            if (var3.size() <= 0) {
                errorMsg = "沒有符合的子字串";
                return null;
            } else {
                String[] var6 = new String[var3.size()];

                for(int var7 = 0; var7 < var6.length; ++var7) {
                    var6[var7] = (String)var3.get(var7);
                }

                return var6;
            }
        } catch (Exception var8) {
            errorMsg = var8.getMessage();
            return null;
        }
    }

    public static String[] splitNoCut(String var0, int var1, int var2) {
        try {
            ArrayList var3 = new ArrayList();
            int var4 = 0;
            boolean var5 = false;
            int var6 = 0;

            for(int var7 = 0; var7 < var0.length(); ++var7) {
                if (var0.charAt(var7) == var1) {
                    ++var6;
                    if (var6 == 1) {
                        var4 = var7;
                    }
                } else if (var0.charAt(var7) == var2) {
                    --var6;
                    if (var6 == 0) {
                        var3.add(var0.substring(var4, var7 + 1));
                    }
                }
            }

            if (var3.size() <= 0) {
                errorMsg = "沒有符合的子字串";
                return null;
            } else {
                String[] var10 = new String[var3.size()];

                for(int var8 = 0; var8 < var10.length; ++var8) {
                    var10[var8] = (String)var3.get(var8);
                }

                return var10;
            }
        } catch (Exception var9) {
            errorMsg = var9.getMessage();
            return null;
        }
    }

    public static String[] splitNoCut(String var0, String var1, String var2) {
        try {
            ArrayList var3 = new ArrayList();
            int var4 = -1;

            int var9;
            for(boolean var5 = false; (var4 = var0.indexOf(var1, var4 + 1)) >= 0; var4 = var9 + var2.length()) {
                var9 = var4 + var1.length();
                if ((var9 = var0.indexOf(var2, var9 + 1)) >= 0) {
                    var3.add(var0.substring(var4, var9 + var2.length()));
                }
            }

            if (var3.size() <= 0) {
                errorMsg = "沒有符合的子字串";
                return null;
            } else {
                String[] var6 = new String[var3.size()];

                for(int var7 = 0; var7 < var6.length; ++var7) {
                    var6[var7] = (String)var3.get(var7);
                }

                return var6;
            }
        } catch (Exception var8) {
            errorMsg = var8.getMessage();
            return null;
        }
    }

    public static String between(String var0, int var1, int var2) {
        try {
            byte var3 = 0;
            boolean var4 = false;
            int var7;
            if ((var7 = var0.indexOf(var1, var3)) >= 0) {
                int var6 = var7 + 1;
                if ((var7 = var0.indexOf(var2, var6)) >= 0) {
                    return var0.substring(var6, var7);
                }
            }
        } catch (Exception var5) {
            errorMsg = var5.getMessage();
        }

        return null;
    }

    public static String between(String var0, String var1, String var2) {
        try {
            byte var3 = 0;
            boolean var4 = false;
            int var7;
            if ((var7 = var0.indexOf(var1, var3)) >= 0) {
                int var6 = var7 + var1.length();
                if ((var7 = var0.indexOf(var2, var6)) >= 0) {
                    return var0.substring(var6, var7);
                }
            }
        } catch (Exception var5) {
            errorMsg = var5.getMessage();
        }

        return null;
    }

    public static String[] parse(String var0, String var1) {
        return parse(var0, var1, 32, 63);
    }

    public static String[] parse(String var0, String var1, int var2) {
        return parse(var0, var1, var2, 63);
    }

    public static String[] parse(String var0, String var1, int var2, int var3) {
        try {
            String[] var4 = split(var1, var2);
            ArrayList var5 = new ArrayList();
            int var6 = 0;
            int var7 = 0;
            boolean var8 = false;

            for(int var9 = 0; var9 < var4.length; ++var9) {
                if (var4[var9].equals(String.valueOf((char)var3))) {
                    var6 = var7;
                    var8 = true;
                } else {
                    if ((var7 = var0.indexOf(var4[var9], var7)) < 0) {
                        errorMsg = "來源字串的格式不符";
                        return null;
                    }

                    if (var8) {
                        var5.add(var0.substring(var6, var7).trim());
                        var8 = false;
                    }

                    var7 += var4[var9].length();
                }
            }

            if (var8) {
                var5.add(var0.substring(var6).trim());
            }

            if (var5.size() <= 0) {
                errorMsg = "沒有符合的子字串";
                return null;
            } else {
                String[] var12 = new String[var5.size()];

                for(int var10 = 0; var10 < var12.length; ++var10) {
                    var12[var10] = (String)var5.get(var10);
                }

                return var12;
            }
        } catch (Exception var11) {
            errorMsg = var11.getMessage();
            return null;
        }
    }

    public static String[] parseIgnoreCase(String var0, String var1) {
        return parseIgnoreCase(var0, var1, 32, 63);
    }

    public static String[] parseIgnoreCase(String var0, String var1, int var2) {
        return parseIgnoreCase(var0, var1, var2, 63);
    }

    public static String[] parseIgnoreCase(String var0, String var1, int var2, int var3) {
        try {
            String var4 = var0.toLowerCase();
            String var5 = var1.toLowerCase();
            String[] var6 = split(var5, var2);
            ArrayList var7 = new ArrayList();
            int var8 = 0;
            int var9 = 0;
            boolean var10 = false;

            for(int var11 = 0; var11 < var6.length; ++var11) {
                if (var6[var11].equals(String.valueOf((char)var3))) {
                    var8 = var9;
                    var10 = true;
                } else {
                    if ((var9 = var4.indexOf(var6[var11], var9)) < 0) {
                        errorMsg = "來源字串的格式不符";
                        return null;
                    }

                    if (var10) {
                        var7.add(var0.substring(var8, var9).trim());
                        var10 = false;
                    }

                    var9 += var6[var11].length();
                }
            }

            if (var10) {
                var7.add(var0.substring(var8).trim());
            }

            if (var7.size() <= 0) {
                errorMsg = "沒有符合的子字串";
                return null;
            } else {
                String[] var14 = new String[var7.size()];

                for(int var12 = 0; var12 < var14.length; ++var12) {
                    var14[var12] = (String)var7.get(var12);
                }

                return var14;
            }
        } catch (Exception var13) {
            errorMsg = var13.getMessage();
            return null;
        }
    }

    public static String format(String var0, String[] var1) {
        return format(var0, var1, 63);
    }

    public static String format(String var0, String[] var1, int var2) {
        try {
            StringBuffer var3 = new StringBuffer("");
            int var4 = 0;
            int var5 = 0;

            for(int var6 = 0; var6 < var0.length(); ++var6) {
                if (var0.charAt(var6) == var2) {
                    var3.append(var0.substring(var4, var6) + var1[var5++]);
                    var4 = var6 + 1;
                }
            }

            var3.append(var0.substring(var4));
            return var3.toString();
        } catch (Exception var7) {
            errorMsg = var7.getMessage();
            return null;
        }
    }

    public static String format(String var0, String var1) {
        StringBuffer var2 = new StringBuffer();
        int var3 = var0.length();
        int var4 = var1.length();
        if (var3 <= var4) {
            int var5 = var4 - var3;
            var2.append(var1.substring(0, var5));
        }

        var2.append(var0);
        return var2.toString();
    }

    public static boolean contain(String[] var0, String var1) {
        try {
            for(int var2 = 0; var2 < var0.length; ++var2) {
                if (var0[var2].equals(var1)) {
                    return true;
                }
            }
        } catch (Exception var3) {
            errorMsg = "contain: " + var3.toString();
        }

        return false;
    }

    public static boolean containIgnoreCase(String[] var0, String var1) {
        try {
            for(int var2 = 0; var2 < var0.length; ++var2) {
                if (var0[var2].equalsIgnoreCase(var1)) {
                    return true;
                }
            }
        } catch (Exception var3) {
            errorMsg = "containIgnoreCase: " + var3.toString();
        }

        return false;
    }

    public static String ignoreStart(String var0, String var1) {
        try {
            int var2 = var0.indexOf(var1);
            if (var2 != 0) {
                errorMsg = "字串的開頭沒有指定的字串";
                return null;
            } else {
                return var0.substring(var1.length());
            }
        } catch (Exception var3) {
            errorMsg = var3.getMessage();
            return null;
        }
    }

    public static String ignoreChar(String var0, int var1) {
        try {
            StringBuffer var2 = new StringBuffer("");
            int var3 = 0;

            for(int var4 = 0; var4 < var0.length(); ++var4) {
                if (var0.charAt(var4) == var1) {
                    var2.append(var0.substring(var3, var4));
                    var3 = var4 + 1;
                }
            }

            var2.append(var0.substring(var3));
            return var2.toString();
        } catch (Exception var5) {
            errorMsg = var5.getMessage();
            return null;
        }
    }

    public static String digitStart(String var0) {
        try {
            int var1;
            for(var1 = 0; var1 < var0.length() && Character.isDigit(var0.charAt(var1)); ++var1) {
            }

            return var0.substring(0, var1);
        } catch (Exception var2) {
            errorMsg = var2.getMessage();
            return null;
        }
    }

    public static String letterStart(String var0) {
        try {
            int var1;
            for(var1 = 0; var1 < var0.length() && Character.isLetter(var0.charAt(var1)); ++var1) {
            }

            return var0.substring(0, var1 - 1);
        } catch (Exception var2) {
            errorMsg = var2.getMessage();
            return null;
        }
    }

    public static boolean isDigit(String var0) {
        try {
            for(int var1 = 0; var1 < var0.length(); ++var1) {
                if (!Character.isDigit(var0.charAt(var1))) {
                    return false;
                }
            }

            return true;
        } catch (Exception var2) {
            errorMsg = var2.getMessage();
            return false;
        }
    }

    public static boolean isLetter(String var0) {
        try {
            for(int var1 = 0; var1 < var0.length(); ++var1) {
                if (!Character.isLetter(var0.charAt(var1))) {
                    return false;
                }
            }

            return true;
        } catch (Exception var2) {
            errorMsg = var2.getMessage();
            return false;
        }
    }

    public static boolean isLetterOrDigit(String var0) {
        try {
            for(int var1 = 0; var1 < var0.length(); ++var1) {
                if (!Character.isLetterOrDigit(var0.charAt(var1))) {
                    return false;
                }
            }

            return true;
        } catch (Exception var2) {
            errorMsg = var2.getMessage();
            return false;
        }
    }

    public static void main(String[] var0) {
        String var1 = format("214567", "0v00");
        System.out.println(var1);
    }
}