package com.appsflyer.cache;

import android.content.Context;
import android.util.Log;
import com.appsflyer.AppsFlyerLib;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CacheManager {
    public static final String AF_CACHE_DIR = "AFRequestCache";
    public static final int CACHE_MAX_SIZE = 40;
    private static CacheManager ourInstance = new CacheManager();

    public static CacheManager getInstance() {
        return ourInstance;
    }

    private CacheManager() {
    }

    private File getCacheDir(Context context) {
        return new File(context.getFilesDir(), AF_CACHE_DIR);
    }

    public void init(Context context) {
        try {
            if (!getCacheDir(context).exists()) {
                getCacheDir(context).mkdir();
            }
        } catch (Exception e) {
            Log.i(AppsFlyerLib.LOG_TAG, "Could not create cache directory");
        }
    }

    public void cacheRequest(RequestCacheData data, Context context) {
        Throwable th;
        Throwable th2;
        OutputStreamWriter outputStreamWriter = null;
        OutputStreamWriter outputStreamWriter2;
        try {
            File cacheDir = getCacheDir(context);
            if (cacheDir.exists()) {
                File[] listFiles = cacheDir.listFiles();
                if (listFiles == null || listFiles.length <= 40) {
                    Log.i(AppsFlyerLib.LOG_TAG, "caching request...");
                    File file = new File(getCacheDir(context), Long.toString(System.currentTimeMillis()));
                    file.createNewFile();
                    outputStreamWriter2 = new OutputStreamWriter(new FileOutputStream(file.getPath(), true));
                    try {
                        outputStreamWriter2.write("version=");
                        outputStreamWriter2.write(data.getVersion());
                        outputStreamWriter2.write(10);
                        outputStreamWriter2.write("url=");
                        outputStreamWriter2.write(data.getRequestURL());
                        outputStreamWriter2.write(10);
                        outputStreamWriter2.write("data=");
                        outputStreamWriter2.write(data.getPostData());
                        outputStreamWriter2.write(10);
                        outputStreamWriter2.flush();
                        if (outputStreamWriter2 != null) {
                            try {
                                outputStreamWriter2.close();
                                return;
                            } catch (IOException e) {
                                return;
                            }
                        }
                        return;
                    } catch (Exception e2) {
                        outputStreamWriter = outputStreamWriter2;
                        try {
                            Log.i(AppsFlyerLib.LOG_TAG, "Could not cache request");
                            if (outputStreamWriter != null) {
                                try {
                                    outputStreamWriter.close();
                                } catch (IOException e3) {
                                    return;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            outputStreamWriter2 = outputStreamWriter;
                            th2 = th;
                            if (outputStreamWriter2 != null) {
                                try {
                                    outputStreamWriter2.close();
                                } catch (IOException e4) {
                                }
                            }
                            throw th2;
                        }
                    } catch (Throwable th4) {
                        th2 = th4;
                        if (outputStreamWriter2 != null) {
                            outputStreamWriter2.close();
                        }
                        throw th2;
                    }
                }
                Log.i(AppsFlyerLib.LOG_TAG, "reached cache limit, not caching request");
                if (outputStreamWriter != null) {
                    try {
                        outputStreamWriter.close();
                        return;
                    } catch (IOException e5) {
                        return;
                    }
                }
                return;
            }
            cacheDir.mkdir();
            if (outputStreamWriter != null) {
                try {
                    outputStreamWriter.close();
                } catch (IOException e6) {
                }
            }
        } catch (Exception e7) {
            Log.i(AppsFlyerLib.LOG_TAG, "Could not cache request");
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        } catch (Throwable th32) {
            th = th32;
            outputStreamWriter2 = outputStreamWriter;
            th2 = th;
            if (outputStreamWriter2 != null) {
                outputStreamWriter2.close();
            }
            throw th2;
        }
    }

    public List<RequestCacheData> getCachedRequests(Context context) {
        List<RequestCacheData> arrayList = new ArrayList();
        try {
            File cacheDir = getCacheDir(context);
            if (cacheDir.exists()) {
                for (File file : cacheDir.listFiles()) {
                    Log.i(AppsFlyerLib.LOG_TAG, "Found cached request" + file.getName());
                    arrayList.add(loadRequestData(file));
                }
            } else {
                cacheDir.mkdir();
            }
        } catch (Exception e) {
            Log.i(AppsFlyerLib.LOG_TAG, "Could not cache request");
        }
        return arrayList;
    }

    private RequestCacheData loadRequestData(File file) {
        FileReader fileReader;
        FileReader fileReader2;
        Throwable th;
        try {
            fileReader = new FileReader(file);
            try {
                char[] cArr = new char[((int) file.length())];
                fileReader.read(cArr);
                RequestCacheData requestCacheData = new RequestCacheData(cArr);
                requestCacheData.setCacheKey(file.getName());
                if (fileReader == null) {
                    return requestCacheData;
                }
                try {
                    fileReader.close();
                    return requestCacheData;
                } catch (IOException e) {
                    return requestCacheData;
                }
            } catch (Exception e2) {
                fileReader2 = fileReader;
                if (fileReader2 != null) {
                    try {
                        fileReader2.close();
                    } catch (IOException e3) {
                    }
                }
                return null;
            } catch (Throwable th2) {
                th = th2;
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
            fileReader2 = null;
            if (fileReader2 != null) {
                fileReader2.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            fileReader = null;
            if (fileReader != null) {
                fileReader.close();
            }
            throw th;
        }
    }

    public void deleteRequest(String cacheKey, Context context) {
        File file = new File(getCacheDir(context), cacheKey);
        Log.i(AppsFlyerLib.LOG_TAG, "Deleting " + cacheKey + " from cache");
        if (file.exists()) {
            try {
                file.delete();
            } catch (Throwable e) {
                Log.i(AppsFlyerLib.LOG_TAG, "Could not delete " + cacheKey + " from cache", e);
            }
        }
    }
}
