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
    public static String ADD_PRODUCT = "add_products.php?";
    public static String GET_QUANTITY = "get_quantity.php?";
    public static String GET_PRODUCT = "get_product.php?";
    public static String ADD_CUSTOMER = "customer_register.php?";
    public static String REMOVE_CUSTOMER = "delete_customer.php?";
    public static String VIEW_CUSTOMER = "get_customer.php?";
    public static String GET_VENDOR = "get_vendor_profile.php?";
    public static String EDIT_VENDOR = "edit_vendor.php?";
    public static String ADD_VENDOR = "vendor_register?";
    public static String VIEW_VENDOR = "get_vendor.php?";
    public static String REMOVE_VENDOR = "delete_vendor.php?";
    public static String GET_VENDOR_COMPANY = "get_vendor.php?";
    public static String REMOVE_PURCHASE = "remove_purchase.php?";
    public static String ADD_PURCHASE = "add_purchase.php?";
    public static String ADD_PURCHASE_TOTAL = "purchase_register.php?";
}
