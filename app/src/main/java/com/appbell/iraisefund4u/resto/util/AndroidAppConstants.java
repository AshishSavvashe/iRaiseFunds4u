    package com.appbell.iraisefund4u.resto.util;

    public interface AndroidAppConstants {

    String SERVER_URL	 = "192.168.1.123:8080";//instafundr.com:8443 // local - 192.168.1.123:8080
    String SERVER_CONTEXT = "RestoWeb";//iweb // local - RestoWeb
    boolean isHTTPS 	= false;


    String URLPART_RequestProcessor = "AppRequestProcessor?";

    int ANIMATION_EFFECT_LEFT_TO_RIGHT = 0;
    int ANIMATION_EFFECT_RIGHT_TO_LEFT = 1;
    int ANIMATION_EFFECT_TOP_TO_DOWN = 2;
    int ANIMATION_EFFECT_DOWN_TO_TOP = 3;
    int ANIMATION_EFFECT_ZOOM_IN = 0;
    int ANIMATION_EFFECT_ZOOM_OUT = 1;


    int DIALOG_ACTION_BackKeyPress      = 1;
    int DIALOG_ACTION_Logout            = 2;
    int DIALOG_ACTION_RegisterCardMsg   = 3;


    int VOUCHER_AVIABLE4SALE  = 1;
    int VOUCHER_REDEEMED      = 2;
    int VOUCHER_COMMINGSOON   = 3;
    int VOUCHER_EXPIRED       = 4;

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;

    public static final int NOTIFICATION_ID_NewVoucherAdded = 1;
    public static final int NOTIFICATION_ID_RemaningVoucherEndOfWeek = 2;

    public static final int ALARM_REQUEST_CODE4WEEK  = 1;
    public static final int ALARM_REQUEST_CODE4MONTH = 2;

    String RedeemVoucher = "R";

    String ALLRestaurant = "Restaurant Filter";
    String ALLMonth = "All Month";

    String PACKAGE_NAME = "com.appbell.sdscbogo";

}

