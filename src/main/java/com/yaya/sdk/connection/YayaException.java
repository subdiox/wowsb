package com.yaya.sdk.connection;

public class YayaException extends Exception {
    private static final long serialVersionUID = 1;

    public YayaException(String str) {
        super(str);
    }

    public YayaException(Exception e) {
        if (e.getCause() != null) {
            e = e.getCause();
        }
        super(e);
    }
}
