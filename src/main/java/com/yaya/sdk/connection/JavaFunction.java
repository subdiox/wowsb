package com.yaya.sdk.connection;

public abstract class JavaFunction {
    protected YayaLib L;

    public abstract int execute() throws YayaException;

    public JavaFunction(YayaLib L) {
        this.L = L;
    }

    public YayaObject getParam(int idx) {
        return this.L.getYayaObject(idx);
    }

    public void register(String name) throws YayaException {
        synchronized (this.L) {
            this.L.pushJavaFunction(this);
            this.L.setGlobal(name);
        }
    }
}
