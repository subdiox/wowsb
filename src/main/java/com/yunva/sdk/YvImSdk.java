package com.yunva.sdk;

import com.demo.yunva.MainActivity;
import com.facebook.share.internal.ShareConstants;
import com.unity3d.player.UnityPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class YvImSdk {
    public static final int IM_RECORD_FINISHPLAY_RESP = 102404;
    public static final int IM_RECORD_STARTPLAY_REQ = 102403;
    public static final int IM_RECORD_STOPPLAY_REQ = 102405;
    public static final int IM_RECORD_STOP_REQ = 102401;
    public static final int IM_RECORD_STOP_RESP = 102402;
    public static final int IM_RECORD_STRART_REQ = 102400;
    public static final int IM_SPEECH_SETLANGUAGE_REQ = 102408;
    public static final int IM_SPEECH_START_REQ = 102406;
    public static final int IM_SPEECH_STOP_REQ = 102407;
    public static final int IM_SPEECH_STOP_RESP = 102409;
    public static final int IM_THIRD_LOGIN_REQ = 69634;
    public static final int IM_THIRD_LOGIN_RESP = 69635;
    public static final int IM_UPLOAD_FILE_RESP = 102417;
    private static final String MESSAGE_NAME_STRING = "YvSdkCallBack";
    private static final String TARGET_NAME_STRING = "YvSDK_Object";
    public static final int yv_imsdk_channel = 6;
    public static final int yv_imsdk_chat = 4;
    public static final int yv_imsdk_clound = 5;
    public static final int yv_imsdk_friend = 2;
    public static final int yv_imsdk_group = 3;
    public static final int yv_imsdk_login = 1;
    public static final int yv_imsdk_tools = 9;
    public static final int yv_imsdk_troops = 7;

    public native int YvInitSdk(long j, String str, boolean z);

    public native void YvRelease();

    public native int YvSendCmd(int i, int i2, int i3);

    public void YvImCallback(int type, int cmdid, int parser) {
        YvPacketSdk yvPacketSdk = MainActivity.getYvPacketSdk();
        Map<String, String> map = new HashMap();
        map.put(ShareConstants.MEDIA_TYPE, String.valueOf(type));
        map.put("cmdId", String.valueOf(cmdid));
        byte cmdIdIndexer;
        byte b;
        int result;
        String msg;
        if (type == 9) {
            if (cmdid == 102402) {
                cmdIdIndexer = (byte) 2;
                int time = yvPacketSdk.parser_get_integer(parser, (byte) 1, 0);
                String strFilePath = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
                map.put("time", String.valueOf(time));
                map.put("strfilepath", strFilePath);
                sendU3DMessage(map);
                b = cmdIdIndexer;
            } else if (cmdid == 102404) {
                cmdIdIndexer = (byte) 2;
                result = yvPacketSdk.parser_get_integer(parser, (byte) 1, 0);
                String describe = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
                map.put("result", String.valueOf(result));
                map.put("describe", describe);
                sendU3DMessage(map);
                b = cmdIdIndexer;
            } else if (cmdid == 102409) {
                cmdIdIndexer = (byte) 2;
                int err_id = yvPacketSdk.parser_get_integer(parser, (byte) 1, 0);
                b = (byte) (cmdIdIndexer + 1);
                String err_msg = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
                String result2 = yvPacketSdk.parser_get_string(parser, b, 0);
                map.put("err_id", String.valueOf(err_id));
                map.put("err_msg", err_msg);
                map.put("result", result2);
                sendU3DMessage(map);
            } else if (cmdid == 102417) {
                cmdIdIndexer = (byte) 2;
                result = yvPacketSdk.parser_get_integer(parser, (byte) 1, 0);
                b = (byte) (cmdIdIndexer + 1);
                msg = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
                cmdIdIndexer = (byte) (b + 1);
                String fileid = yvPacketSdk.parser_get_string(parser, b, 0);
                b = (byte) (cmdIdIndexer + 1);
                String fileurl = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
                int percent = yvPacketSdk.parser_get_integer(parser, b, 0);
                map.put("result", String.valueOf(result));
                map.put("msg", msg);
                map.put("fileid", fileid);
                map.put("fileurl", fileurl);
                map.put("percent", String.valueOf(percent));
                sendU3DMessage(map);
            }
        } else if (type == 1 && cmdid == 69635) {
            cmdIdIndexer = (byte) 2;
            result = yvPacketSdk.parser_get_integer(parser, (byte) 1, 0);
            b = (byte) (cmdIdIndexer + 1);
            msg = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
            cmdIdIndexer = (byte) (b + 1);
            int userid = yvPacketSdk.parser_get_integer(parser, b, 0);
            b = (byte) (cmdIdIndexer + 1);
            String nickName = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
            cmdIdIndexer = (byte) (b + 1);
            String iconUrl = yvPacketSdk.parser_get_string(parser, b, 0);
            b = (byte) (cmdIdIndexer + 1);
            String thirdUserId = yvPacketSdk.parser_get_string(parser, cmdIdIndexer, 0);
            cmdIdIndexer = (byte) (b + 1);
            String thirdUserName = yvPacketSdk.parser_get_string(parser, b, 0);
            map.put("result", String.valueOf(result));
            map.put("msg", msg);
            map.put("userid", String.valueOf(userid));
            map.put("nickName", nickName);
            map.put("iconUrl", iconUrl);
            map.put("thirdUserId", thirdUserId);
            map.put("thirdUserName", thirdUserName);
            sendU3DMessage(map);
            b = cmdIdIndexer;
        }
    }

    private static void sendU3DMessage(Map<String, String> hashMap) {
        String param = "";
        if (hashMap != null) {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                if (param.length() == 0) {
                    param = new StringBuilder(String.valueOf(param)).append(String.format("%s{%s}", new Object[]{key, val})).toString();
                } else {
                    param = new StringBuilder(String.valueOf(param)).append(String.format("&%s{%s}", new Object[]{key, val})).toString();
                }
            }
        }
        UnityPlayer.UnitySendMessage(TARGET_NAME_STRING, MESSAGE_NAME_STRING, param);
    }
}
