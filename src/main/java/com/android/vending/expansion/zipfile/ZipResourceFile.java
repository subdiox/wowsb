package com.android.vending.expansion.zipfile;

import android.content.res.AssetFileDescriptor;
import android.os.ParcelFileDescriptor;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourceFile {
    static final boolean LOGV = false;
    static final String LOG_TAG = "zipro";
    static final int kCDECRC = 16;
    static final int kCDECommentLen = 32;
    static final int kCDECompLen = 20;
    static final int kCDEExtraLen = 30;
    static final int kCDELen = 46;
    static final int kCDELocalOffset = 42;
    static final int kCDEMethod = 10;
    static final int kCDEModWhen = 12;
    static final int kCDENameLen = 28;
    static final int kCDESignature = 33639248;
    static final int kCDEUncompLen = 24;
    static final int kCompressDeflated = 8;
    static final int kCompressStored = 0;
    static final int kEOCDFileOffset = 16;
    static final int kEOCDLen = 22;
    static final int kEOCDNumEntries = 8;
    static final int kEOCDSignature = 101010256;
    static final int kEOCDSize = 12;
    static final int kLFHExtraLen = 28;
    static final int kLFHLen = 30;
    static final int kLFHNameLen = 26;
    static final int kLFHSignature = 67324752;
    static final int kMaxCommentLen = 65535;
    static final int kMaxEOCDSearch = 65557;
    static final int kZipEntryAdj = 10000;
    private HashMap<String, ZipEntryRO> mHashMap = new HashMap();
    ByteBuffer mLEByteBuffer = ByteBuffer.allocate(4);
    public HashMap<File, ZipFile> mZipFiles = new HashMap();

    public static final class ZipEntryRO {
        public long mCRC32;
        public long mCompressedLength;
        public final File mFile;
        public final String mFileName;
        public long mLocalHdrOffset;
        public int mMethod;
        public long mOffset = -1;
        public long mUncompressedLength;
        public long mWhenModified;
        public final String mZipFileName;

        public ZipEntryRO(String zipFileName, File file, String fileName) {
            this.mFileName = fileName;
            this.mZipFileName = zipFileName;
            this.mFile = file;
        }

        public void setOffsetFromFile(RandomAccessFile f, ByteBuffer buf) throws IOException {
            long localHdrOffset = this.mLocalHdrOffset;
            try {
                f.seek(localHdrOffset);
                f.readFully(buf.array());
                if (buf.getInt(0) != ZipResourceFile.kLFHSignature) {
                    Log.w(ZipResourceFile.LOG_TAG, "didn't find signature at start of lfh");
                    throw new IOException();
                }
                this.mOffset = ((30 + localHdrOffset) + ((long) (buf.getShort(26) & 65535))) + ((long) (buf.getShort(28) & 65535));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        public long getOffset() {
            return this.mOffset;
        }

        public boolean isUncompressed() {
            return this.mMethod == 0;
        }

        public AssetFileDescriptor getAssetFileDescriptor() {
            if (this.mMethod == 0) {
                try {
                    return new AssetFileDescriptor(ParcelFileDescriptor.open(this.mFile, 268435456), getOffset(), this.mUncompressedLength);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public String getZipFileName() {
            return this.mZipFileName;
        }

        public File getZipFile() {
            return this.mFile;
        }
    }

    private static int swapEndian(int i) {
        return ((((i & 255) << 24) + ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) << 8)) + ((16711680 & i) >>> 8)) + ((i >>> 24) & 255);
    }

    private static int swapEndian(short i) {
        return ((i & 255) << 8) | ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i) >>> 8);
    }

    public ZipResourceFile(String zipFileName) throws IOException {
        addPatchFile(zipFileName);
    }

    ZipEntryRO[] getEntriesAt(String path) {
        Vector<ZipEntryRO> zev = new Vector();
        Collection<ZipEntryRO> values = this.mHashMap.values();
        if (path == null) {
            path = "";
        }
        int length = path.length();
        for (ZipEntryRO ze : values) {
            if (ze.mFileName.startsWith(path) && -1 == ze.mFileName.indexOf(47, length)) {
                zev.add(ze);
            }
        }
        return (ZipEntryRO[]) zev.toArray(new ZipEntryRO[zev.size()]);
    }

    public ZipEntryRO[] getAllEntries() {
        Collection<ZipEntryRO> values = this.mHashMap.values();
        return (ZipEntryRO[]) values.toArray(new ZipEntryRO[values.size()]);
    }

    public AssetFileDescriptor getAssetFileDescriptor(String assetPath) {
        ZipEntryRO entry = (ZipEntryRO) this.mHashMap.get(assetPath);
        if (entry != null) {
            return entry.getAssetFileDescriptor();
        }
        return null;
    }

    public InputStream getInputStream(String assetPath) throws IOException {
        ZipEntryRO entry = (ZipEntryRO) this.mHashMap.get(assetPath);
        if (entry != null) {
            if (entry.isUncompressed()) {
                return entry.getAssetFileDescriptor().createInputStream();
            }
            ZipFile zf = (ZipFile) this.mZipFiles.get(entry.getZipFile());
            if (zf == null) {
                zf = new ZipFile(entry.getZipFile(), 1);
                this.mZipFiles.put(entry.getZipFile(), zf);
            }
            ZipEntry zi = zf.getEntry(assetPath);
            if (zi != null) {
                return zf.getInputStream(zi);
            }
        }
        return null;
    }

    private static int read4LE(RandomAccessFile f) throws EOFException, IOException {
        return swapEndian(f.readInt());
    }

    void addPatchFile(String zipFileName) throws IOException {
        File file = new File(zipFileName);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long fileLength = randomAccessFile.length();
        if (fileLength < 22) {
            throw new IOException();
        }
        long readAmount = 65557;
        if (65557 > fileLength) {
            readAmount = fileLength;
        }
        randomAccessFile.seek(0);
        int header = read4LE(randomAccessFile);
        if (header == kEOCDSignature) {
            Log.i(LOG_TAG, "Found Zip archive, but it looks empty");
            throw new IOException();
        } else if (header != kLFHSignature) {
            Log.v(LOG_TAG, "Not a Zip archive");
            throw new IOException();
        } else {
            randomAccessFile.seek(fileLength - readAmount);
            ByteBuffer bbuf = ByteBuffer.allocate((int) readAmount);
            byte[] buffer = bbuf.array();
            randomAccessFile.readFully(buffer);
            bbuf.order(ByteOrder.LITTLE_ENDIAN);
            int eocdIdx = buffer.length - 22;
            while (eocdIdx >= 0 && (buffer[eocdIdx] != (byte) 80 || bbuf.getInt(eocdIdx) != kEOCDSignature)) {
                eocdIdx--;
            }
            if (eocdIdx < 0) {
                Log.d(LOG_TAG, "Zip: EOCD not found, " + zipFileName + " is not zip");
            }
            int numEntries = bbuf.getShort(eocdIdx + 8);
            long dirSize = ((long) bbuf.getInt(eocdIdx + 12)) & 4294967295L;
            long dirOffset = ((long) bbuf.getInt(eocdIdx + 16)) & 4294967295L;
            if (dirOffset + dirSize > fileLength) {
                Log.w(LOG_TAG, "bad offsets (dir " + dirOffset + ", size " + dirSize + ", eocd " + eocdIdx + ")");
                throw new IOException();
            } else if (numEntries == 0) {
                Log.w(LOG_TAG, "empty archive?");
                throw new IOException();
            } else {
                MappedByteBuffer directoryMap = randomAccessFile.getChannel().map(MapMode.READ_ONLY, dirOffset, dirSize);
                directoryMap.order(ByteOrder.LITTLE_ENDIAN);
                byte[] tempBuf = new byte[65535];
                int currentOffset = 0;
                ByteBuffer buf = ByteBuffer.allocate(30);
                buf.order(ByteOrder.LITTLE_ENDIAN);
                for (int i = 0; i < numEntries; i++) {
                    if (directoryMap.getInt(currentOffset) != kCDESignature) {
                        Log.w(LOG_TAG, "Missed a central dir sig (at " + currentOffset + ")");
                        throw new IOException();
                    }
                    int fileNameLen = directoryMap.getShort(currentOffset + 28) & 65535;
                    int extraLen = directoryMap.getShort(currentOffset + 30) & 65535;
                    int commentLen = directoryMap.getShort(currentOffset + 32) & 65535;
                    directoryMap.position(currentOffset + 46);
                    directoryMap.get(tempBuf, 0, fileNameLen);
                    directoryMap.position(0);
                    String str = new String(tempBuf, 0, fileNameLen);
                    ZipEntryRO zipEntryRO = new ZipEntryRO(zipFileName, file, str);
                    zipEntryRO.mMethod = directoryMap.getShort(currentOffset + 10) & 65535;
                    zipEntryRO.mWhenModified = ((long) directoryMap.getInt(currentOffset + 12)) & 4294967295L;
                    zipEntryRO.mCRC32 = directoryMap.getLong(currentOffset + 16) & 4294967295L;
                    zipEntryRO.mCompressedLength = directoryMap.getLong(currentOffset + 20) & 4294967295L;
                    zipEntryRO.mUncompressedLength = directoryMap.getLong(currentOffset + 24) & 4294967295L;
                    zipEntryRO.mLocalHdrOffset = ((long) directoryMap.getInt(currentOffset + 42)) & 4294967295L;
                    buf.clear();
                    zipEntryRO.setOffsetFromFile(randomAccessFile, buf);
                    this.mHashMap.put(str, zipEntryRO);
                    currentOffset += ((fileNameLen + 46) + extraLen) + commentLen;
                }
            }
        }
    }
}
