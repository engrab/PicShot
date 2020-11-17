package com.pic.editor.constants;

import java.util.ArrayList;

public class Constants {
    public static int BORDER_WIDTH_DP = 3;
    public static String FACE = "com.facebook.katana";
    public static ArrayList<String> FORMAT_IMAGE = new ImageTypeList();
    public static String GMAIL = "com.google.android.gm";
    public static String INSTA = "com.instagram.android";
    public static String MESSEGER = "com.facebook.orca";
    public static boolean SHOW_ADS = true;
    public static String TWITTER = "com.twitter.android";
    public static String WHATSAPP = "com.whatsapp";

    static class ImageTypeList extends ArrayList<String> {
        ImageTypeList() {
            add(".PNG");
            add(".JPEG");
            add(".jpg");
            add(".png");
            add(".jpeg");
            add(".JPG");
        }
    }
}
