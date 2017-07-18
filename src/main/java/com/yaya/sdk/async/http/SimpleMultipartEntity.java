package com.yaya.sdk.async.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

class SimpleMultipartEntity implements HttpEntity {
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    private static final byte[] CR_LF = "\r\n".getBytes();
    private static final String LOG_TAG = "SimpleMultipartEntity";
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final byte[] TRANSFER_ENCODING_BINARY = "Content-Transfer-Encoding: binary\r\n".getBytes();
    private String boundary;
    private byte[] boundaryEnd;
    private byte[] boundaryLine;
    private int bytesWritten;
    private List<FilePart> fileParts = new ArrayList();
    private boolean isRepeatable = false;
    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private ResponseHandlerInterface progressHandler;
    private int totalSize;

    private class FilePart {
        public File file;
        public byte[] header;

        public FilePart(String key, File file, String type) {
            this.header = createHeader(key, file.getName(), type);
            this.file = file;
        }

        private byte[] createHeader(String key, String filename, String type) {
            ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
            try {
                headerStream.write(SimpleMultipartEntity.this.boundaryLine);
                headerStream.write(SimpleMultipartEntity.this.createContentDisposition(key, filename));
                headerStream.write(SimpleMultipartEntity.this.createContentType(type));
                headerStream.write(SimpleMultipartEntity.TRANSFER_ENCODING_BINARY);
                headerStream.write(SimpleMultipartEntity.CR_LF);
            } catch (IOException e) {
                if (AsyncHttpClient.isDebug) {
                }
            }
            return headerStream.toByteArray();
        }

        public long getTotalLength() {
            return ((long) this.header.length) + this.file.length();
        }

        public void writeTo(OutputStream out) throws IOException {
            out.write(this.header);
            SimpleMultipartEntity.this.updateProgress(this.header.length);
            FileInputStream inputStream = new FileInputStream(this.file);
            byte[] tmp = new byte[4096];
            while (true) {
                int l = inputStream.read(tmp);
                if (l != -1) {
                    out.write(tmp, 0, l);
                    SimpleMultipartEntity.this.updateProgress(l);
                } else {
                    out.write(SimpleMultipartEntity.CR_LF);
                    SimpleMultipartEntity.this.updateProgress(SimpleMultipartEntity.CR_LF.length);
                    out.flush();
                    try {
                        inputStream.close();
                        return;
                    } catch (IOException e) {
                        if (!AsyncHttpClient.isDebug) {
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    public SimpleMultipartEntity(ResponseHandlerInterface progressHandler) {
        StringBuilder buf = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 30; i++) {
            buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = buf.toString();
        this.boundaryLine = ("--" + this.boundary + "\r\n").getBytes();
        this.boundaryEnd = ("--" + this.boundary + "--\r\n").getBytes();
        this.progressHandler = progressHandler;
    }

    public void addPart(String key, String value, String contentType) {
        try {
            this.out.write(this.boundaryLine);
            this.out.write(createContentDisposition(key));
            this.out.write(createContentType(contentType));
            this.out.write(CR_LF);
            this.out.write(value.getBytes());
            this.out.write(CR_LF);
        } catch (IOException e) {
            if (!AsyncHttpClient.isDebug) {
            }
        }
    }

    public void addPart(String key, String value) {
        addPart(key, value, "text/plain; charset=UTF-8");
    }

    public void addPart(String key, File file) {
        addPart(key, file, null);
    }

    public void addPart(String key, File file, String type) {
        if (type == null) {
            type = APPLICATION_OCTET_STREAM;
        }
        this.fileParts.add(new FilePart(key, file, type));
    }

    public void addPart(String key, String streamName, InputStream inputStream, String type) throws IOException {
        if (type == null) {
            type = APPLICATION_OCTET_STREAM;
        }
        this.out.write(this.boundaryLine);
        this.out.write(createContentDisposition(key, streamName));
        this.out.write(createContentType(type));
        this.out.write(TRANSFER_ENCODING_BINARY);
        this.out.write(CR_LF);
        byte[] tmp = new byte[4096];
        while (true) {
            int l = inputStream.read(tmp);
            if (l != -1) {
                this.out.write(tmp, 0, l);
            } else {
                this.out.write(CR_LF);
                this.out.flush();
                try {
                    inputStream.close();
                    return;
                } catch (IOException e) {
                    if (!AsyncHttpClient.isDebug) {
                        return;
                    }
                    return;
                }
            }
        }
    }

    private byte[] createContentType(String type) {
        return ("Content-Type: " + type + "\r\n").getBytes();
    }

    private byte[] createContentDisposition(String key) {
        return ("Content-Disposition: form-data; name=\"" + key + "\"\r\n").getBytes();
    }

    private byte[] createContentDisposition(String key, String fileName) {
        return ("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes();
    }

    private void updateProgress(int count) {
        this.bytesWritten += count;
        this.progressHandler.sendProgressMessage(this.bytesWritten, this.totalSize);
    }

    public long getContentLength() {
        long contentLen = (long) this.out.size();
        for (FilePart filePart : this.fileParts) {
            long len = filePart.getTotalLength();
            if (len < 0) {
                return -1;
            }
            contentLen += len;
        }
        return contentLen + ((long) this.boundaryEnd.length);
    }

    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + this.boundary);
    }

    public boolean isChunked() {
        return false;
    }

    public void setIsRepeatable(boolean isRepeatable) {
        this.isRepeatable = isRepeatable;
    }

    public boolean isRepeatable() {
        return this.isRepeatable;
    }

    public boolean isStreaming() {
        return false;
    }

    public void writeTo(OutputStream outstream) throws IOException {
        this.bytesWritten = 0;
        this.totalSize = (int) getContentLength();
        this.out.writeTo(outstream);
        updateProgress(this.out.size());
        for (FilePart filePart : this.fileParts) {
            filePart.writeTo(outstream);
        }
        outstream.write(this.boundaryEnd);
        updateProgress(this.boundaryEnd.length);
    }

    public Header getContentEncoding() {
        return null;
    }

    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
        }
    }

    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException("getContent() is not supported. Use writeTo() instead.");
    }
}
