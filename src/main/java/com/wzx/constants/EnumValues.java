package com.wzx.constants;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 常量枚举值.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
public interface EnumValues {

    enum EnumMethodName {
        USER_LOGIN(MethodName.USER_LOGIN, "login"),
        USER_SIGNUP(MethodName.USER_SIGNUP, "signup"),
        USER_LOGOU(MethodName.USER_LOGOU, "logout"),
        UPDATE_USER(MethodName.UPDATE_USER, "updateUser"),
        DELETE_USER(MethodName.DELETE_USER, "deleteUser"),
        QUERY_BLOG(MethodName.QUERY_BLOG, "queryBlog"),
        DETAILS_BLOG(MethodName.DETAILS_BLOG, "detailBlog"),
        UPLOAD_IMAGE(MethodName.UPLOAD_FILE, "uploadFile"),
        EDIT_BLOG(MethodName.EDIT_BLOG, "editBlog"),
        DELETE_BLOG(MethodName.DELETE_BLOG, "deleteBLog"),
        UNKNOW_METHOD("未知方法","unknowMethod");

        public String methodNameCN;
        public String methodNameEN;

        public String getMethodNameCN() {
            return methodNameCN;
        }

        public void setMethodNameCN(String methodNameCN) {
            this.methodNameCN = methodNameCN;
        }

        public String getMethodNameEN() {
            return methodNameEN;
        }

        public void setMethodNameEN(String methodNameEN) {
            this.methodNameEN = methodNameEN;
        }

        EnumMethodName(String methodNameCN, String methodNameEN) {
            this.methodNameCN = methodNameCN;
            this.methodNameEN = methodNameEN;
        }

        public static String getMethodNameCNByEN(String methodEN) {
            EnumMethodName enumMethodName = Arrays.stream(EnumMethodName.values())
                    .filter(item -> item.getMethodNameEN().equals(methodEN)).findFirst().orElse(UNKNOW_METHOD);
            return enumMethodName.getMethodNameCN();
        }

        public static String getMethodNameENByCN(String methodCN) {
            EnumMethodName enumMethodName = Arrays.stream(EnumMethodName.values())
                    .filter(item -> item.getMethodNameCN().equals(methodCN)).findFirst().orElse(UNKNOW_METHOD);
            return enumMethodName.getMethodNameEN();
        }

    }
}
