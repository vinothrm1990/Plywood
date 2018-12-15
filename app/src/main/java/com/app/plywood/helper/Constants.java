package com.app.plywood.helper;

import android.content.SharedPreferences;

public class Constants {

    //sharedPreference
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    //base URL
    public static String BASE_URL = "http://pandiyanadu.in/err/";
    //page URL
    public static String LOGIN = "login.php?";
    public static String GET_COMPANY = "get_company.php?";
    public static String GET_CUSTOMER = "get_customer_profile.php?";
    public static String EDIT_CUSTOMER = "edit_customer.php?";
    public static String ADD_INVOICE = "add_invoice.php?";
    public static String REMOVE_INVOICE = "remove_invoice.php?";
    public static String ADD_PRODUCT = "add_product.php?";
}
