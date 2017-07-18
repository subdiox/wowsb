package com.yunva.sdk;

public class YvPacketSdk {
    public native byte parser_get_btye(int i, byte b, int i2);

    public native int parser_get_integer(int i, byte b, int i2);

    public native long parser_get_long(int i, byte b, int i2);

    public native void parser_get_object(int i, byte b, int i2, int i3);

    public native String parser_get_string(int i, byte b, int i2);

    public native boolean parser_is_empty(int i, byte b, int i2);

    public native void parser_set_btye(int i, byte b, byte b2);

    public native void parser_set_integer(int i, byte b, int i2);

    public native void parser_set_long(int i, byte b, long j);

    public native void parser_set_object(int i, byte b, int i2);

    public native void parser_set_string(int i, byte b, String str);

    public native int sdk_recycling(int i);

    public native int yvpacket_get_parser();

    public native int yvpacket_get_parser_object(int i);
}
