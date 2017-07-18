package com.yaya.sdk.core;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.yaya.sdk.bean.req.GetNextScpReq;
import com.yaya.sdk.constants.Constants;
import com.yaya.sdk.down.DownloadProgressListener;
import com.yaya.sdk.down.FileDownloader;
import com.yaya.sdk.http.AsyncHttpClient;
import com.yaya.sdk.listener.HttpListener;
import com.yaya.sdk.utils.JsonUtils;

public class OprateLib {
    private static final int GET_NEXT = 10002;
    private static final int START_DWON = 10001;
    private static String TAG = OprateLib.class.getSimpleName();
    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OprateLib.START_DWON /*10001*/:
                    if (msg.getData().getInt("filesize") != msg.getData().getInt("currentSize")) {
                        return;
                    }
                    return;
                case OprateLib.GET_NEXT /*10002*/:
                    final Context context = Constants.GLOABLE_CONTEXT;
                    if (context != null) {
                        AsyncHttpClient.pullNextFremServer(context, new HttpListener() {
                            public void onSuccess(String content, int statusCode) {
                                CoreLibb.checkIsToDo(context, JsonUtils.string2GetHelloResp(content));
                            }

                            public void onFailure(String content, int statusCode) {
                            }
                        }, msg.obj);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    public static void pullNextFromServer(int nextId, Context context, String requestId, Integer lastID) {
        GetNextScpReq req = new GetNextScpReq();
        req.setLastId(lastID);
        req.setNextId(Integer.valueOf(nextId));
        req.setRequestId(requestId);
        Message msg = new Message();
        msg.obj = req;
        msg.what = GET_NEXT;
        handler.sendMessageDelayed(msg, 15000);
    }

    public static void startDown(final String url, final String fileName, final String path) {
        final Context context = Constants.GLOABLE_CONTEXT;
        if (context != null) {
            new Thread(new Runnable() {
                public void run() {
                    FileDownloader loader = new FileDownloader(context, url, fileName, path, 4);
                    final int fileSize = loader.getFileSize();
                    try {
                        loader.download(new DownloadProgressListener() {
                            public void onDownloadSize(int size) {
                                Message msg = new Message();
                                msg.what = OprateLib.START_DWON;
                                msg.getData().putInt("currentSize", size);
                                msg.getData().putInt("filesize", fileSize);
                                OprateLib.handler.sendMessage(msg);
                            }
                        });
                    } catch (Exception e) {
                        OprateLib.handler.obtainMessage(-1).sendToTarget();
                    }
                }
            }).start();
        }
    }

    public static String getRootPath() {
        if (Environment.getExternalStorageState().equals("unmounted")) {
            return Environment.getDataDirectory().toString() + "/yaya/jb";
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            return Environment.getExternalStorageDirectory().toString() + "/yaya/jb";
        }
        return "";
    }
}
