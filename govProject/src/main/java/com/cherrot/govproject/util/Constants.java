/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cherrot.govproject.util;

/**
 *
 * @author cherrot
 */
public class Constants {

    /**
     * Default number of items per page. This value may be changed at runtime
     * (not a final value)
     */
    public static int DEFAULT_PAGE_SIZE = 15;
    /**
     * Session Attributes
     */
    public static final String USER_CONTEXT = "sessionUser";
    public static final String ERROR_MSG_KEY = "errorMsg";
    public static final String SUCCESS_MSG_KEY = "successMsg";
    public static final String LOGIN_TO_URL = "toUrl";
    public static final String FILTERED_REQUEST = "@@session_context_filtered_request";
    public static final int TOP_LEVEL_CATEGORY_COUNT = 7;
    /**
     * User Level:待审核用户
     */
    public static final int USER_PENDING = 0x1;
    /**
     * User Level：普通用户
     */
    public static final int USER_NORMAL = 0x2;
    /**
     * User Level：文联用户
     */
    public static final int USER_WENLIAN = 0x4 | 0x8 | 0x10;
    /**
     * User Level：宣传部管理员
     */
    public static final int USER_XUANCHUANBU = 0x20 | 0x40 | 0x80;
}
