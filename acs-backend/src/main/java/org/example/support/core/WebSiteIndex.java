package org.example.support.core;

import java.util.Arrays;

public class WebSiteIndex {

    /***
     * 模組滿版12中得到分配的值
     * @param areaType
     * @param index
     * @return
     */
    public static Integer getDistribution(Long areaType, Integer index) {

        String temp_areaType = "";
        if (areaType == 1) {
            temp_areaType = "12";
        } else if (areaType == 2) {
            temp_areaType = "8/4";
        } else if (areaType == 3) {
            temp_areaType = "7/5";
        } else if (areaType == 4) {
            temp_areaType = "6/6";
        } else if (areaType == 5) {
            temp_areaType = "4/4/4";
        } else if (areaType == 6) {
            temp_areaType = "5/7";
        } else if (areaType == 7) {
            temp_areaType = "4/8";
        } else if (areaType == 8) {
            temp_areaType = "3/3/3/3";
        } else if (areaType == 9) {
            temp_areaType = "3/9";
        } else if (areaType == 10) {
            temp_areaType = "9/3";
        } else if (areaType == 11) {
            temp_areaType = "6/3/3";
        } else if (areaType == 12) {
            temp_areaType = "3/3/6";
        }

        int[] a = Arrays.stream(temp_areaType.split("/")).mapToInt(Integer::parseInt).toArray();
        if (index + 1 <= a.length) {
            return a[index];
        } else {
            System.out.println("分割模組分配值錯誤,先以滿版呈現,出錯的地方:" + areaType + "的第{" + (index + 1) + "}個");
            return 12;
        }

    }

    /****
     * 得到模組view頁的檔案路徑
     * @param serno
     * @param detailNo
     * @return
     */
    public static String getModelEnName(String serno, Long detailNo) {
        detailNo = (detailNo != null ? detailNo : 1);
        if ("2020071701".equals(serno)) {
            // 相簿/圖片輪播
            // 1 圖+文字/2 圖
            return "album/album";
        } else if ("2020071702".equals(serno)) {
            // 頁籤模組
            // 1 條列式/2 強調第一則/3 圖文列表一/4 圖文列表二
            return "tab/tab";
        } else if ("2020071703".equals(serno)) {
            // 時間軸
            return "calender/calender";
        } else if ("2020071704".equals(serno)) {
            // ICON列表
            // 1 橫式(輪播) >A/ 2 上下交錯(輪播) >B/ 3 蜂巢式(無輪播) >D/ 4 整齊排列(無輪播) >C
            if (detailNo == 1) {
                return "slider_icon_A/slider_icon_A";
            } else if (detailNo == 2) {
                return "slider_icon_B/slider_icon_B";
            } else if (detailNo == 3) {
                return "slider_icon_D/slider_icon_D";
            } else if (detailNo == 4) {
                return "slider_icon_C/slider_icon_C";
            } else {
                return "slider_icon_A/slider_icon_A";
            }

        } else if ("2020071705".equals(serno)) {
            // 條列式列表
            return "link_list/link_list";      
        } else if ("2020071706".equals(serno)) {
            // 卡片式列表
            // 1 圖+標題+內文 >A/ 2 標題+內文>B/3 圖+標題
            if (detailNo == 1) {
                return "card_list_A/card_list_A";
            } else if (detailNo == 2) {
                return "card_list_B/card_list_B";
            } else if (detailNo == 3) {
                return "card_list_C/card_list_C";
            } else {
                return "card_list_A/card_list_A";
            }

        } else if ("2020071707".equals(serno)) {
            // 圖片連結
            return "link_pic/link_pic";
        } else if ("2020071708".equals(serno)) {
            // 圖文連結
            // 1 上圖下文 >A/ 2 左圖右文 >B
            if (detailNo == 1) {
                return "link_txt_A/link_txt_A";
            } else if (detailNo == 2) {
                return "link_txt_B/link_txt_B";
            } else {
                return "link_txt_A/link_txt_A";
            }
        } else if ("2020071709".equals(serno)) {
            // 資訊圖像化
            // 1 上圖下文/ 2 左圖右文/ 3 右圖左文
            if (detailNo == 1) {
                return "chart/chart_middle";
            } else if (detailNo == 2) {
                return "chart/chart_left";
            } else if (detailNo == 3) {
                return "chart/chart_right";
            } else {
                return "chart/chart_left";
            }
        } else if ("2020071710".equals(serno)) {
            // 風琴式模組
            return "collapse/collapse";
        } else if ("2020071711".equals(serno)) {
            // 小圖輪播
            // 1 圖+文/ 2 圖
            return "slider_pic/slider_pic";
        } else if ("2020071712".equals(serno)) {
            // 1 自訂內容
            // 2 客製化功能
            return "customize/customize";
        } else {
            // 1 自訂內容
            // 2 客製化功能
            return "customize/customize";
        }
    }

}
