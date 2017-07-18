package com.yaya.sdk.down;

import android.content.Context;
import com.yaya.sdk.async.http.AsyncHttpResponseHandler;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileDownloader {
    private static final String TAG = "FileDownloader";
    private int block;
    private Context context;
    private Map<Integer, Integer> data = new ConcurrentHashMap();
    private int downloadSize = 0;
    private String downloadUrl;
    private int fileSize = 0;
    private File saveFile;
    private DownloadThread[] threads;

    public int getThreadSize() {
        return this.threads.length;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    protected synchronized void append(int size) {
        this.downloadSize += size;
    }

    protected synchronized void update(int threadId, int pos) {
        this.data.put(Integer.valueOf(threadId), Integer.valueOf(pos));
    }

    public FileDownloader(Context context, String downloadUrl, String fileName, String path, int threadNum) {
        try {
            this.context = context;
            this.downloadUrl = downloadUrl;
            URL url = new URL(this.downloadUrl);
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            this.threads = new DownloadThread[threadNum];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Referer", downloadUrl);
            conn.setRequestProperty("Charset", AsyncHttpResponseHandler.DEFAULT_CHARSET);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                this.fileSize = conn.getContentLength();
                if (this.fileSize <= 0) {
                    throw new RuntimeException("Unkown file size ");
                }
                this.saveFile = new File(file.getAbsolutePath().toString(), fileName);
                if (this.data.size() == this.threads.length) {
                    for (int i = 0; i < this.threads.length; i++) {
                        this.downloadSize = ((Integer) this.data.get(Integer.valueOf(i + 1))).intValue() + this.downloadSize;
                    }
                    print("已经下载的长度" + this.downloadSize);
                }
                this.block = this.fileSize % this.threads.length == 0 ? this.fileSize / this.threads.length : (this.fileSize / this.threads.length) + 1;
                return;
            }
            throw new RuntimeException("server no response ");
        } catch (Exception e) {
            print(e.toString());
            throw new RuntimeException("don't connection this url");
        }
    }

    private String getFileName(HttpURLConnection conn) {
        String filename = this.downloadUrl.substring(this.downloadUrl.lastIndexOf(47) + 1);
        if (filename == null || "".equals(filename.trim())) {
            int i = 0;
            while (true) {
                String mine = conn.getHeaderField(i);
                if (mine == null) {
                    break;
                }
                if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if (m.find()) {
                        return m.group(1);
                    }
                }
                i++;
            }
            filename = UUID.randomUUID() + ".tmp";
        }
        return filename;
    }

    public int download(DownloadProgressListener listener) throws Exception {
        try {
            int i;
            RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rw");
            if (this.fileSize > 0) {
                randOut.setLength((long) this.fileSize);
            }
            randOut.close();
            URL url = new URL(this.downloadUrl);
            if (this.data.size() != this.threads.length) {
                this.data.clear();
                for (i = 0; i < this.threads.length; i++) {
                    this.data.put(Integer.valueOf(i + 1), Integer.valueOf(0));
                }
            }
            for (i = 0; i < this.threads.length; i++) {
                if (((Integer) this.data.get(Integer.valueOf(i + 1))).intValue() >= this.block || this.downloadSize >= this.fileSize) {
                    this.threads[i] = null;
                } else {
                    this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, ((Integer) this.data.get(Integer.valueOf(i + 1))).intValue(), i + 1);
                    this.threads[i].setPriority(7);
                    this.threads[i].start();
                }
            }
            boolean notFinish = true;
            while (notFinish) {
                Thread.sleep(900);
                notFinish = false;
                i = 0;
                while (i < this.threads.length) {
                    if (!(this.threads[i] == null || this.threads[i].isFinish())) {
                        notFinish = true;
                        if (this.threads[i].getDownLength() == -1) {
                            this.threads[i] = new DownloadThread(this, url, this.saveFile, this.block, ((Integer) this.data.get(Integer.valueOf(i + 1))).intValue(), i + 1);
                            this.threads[i].setPriority(7);
                            this.threads[i].start();
                        }
                    }
                    i++;
                }
                if (listener != null) {
                    listener.onDownloadSize(this.downloadSize);
                }
            }
            return this.downloadSize;
        } catch (Exception e) {
            print(e.toString());
            throw new Exception("file download fail");
        }
    }

    public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        Map<String, String> header = new LinkedHashMap();
        int i = 0;
        while (true) {
            String mine = http.getHeaderField(i);
            if (mine == null) {
                return header;
            }
            header.put(http.getHeaderFieldKey(i), mine);
            i++;
        }
    }

    public static void printResponseHeader(HttpURLConnection http) {
        for (Entry<String, String> entry : getHttpResponseHeader(http).entrySet()) {
            print("HttpHead:" + (entry.getKey() != null ? ((String) entry.getKey()) + ":" : "") + ((String) entry.getValue()));
        }
    }

    private static void print(String msg) {
    }
}
