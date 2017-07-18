package com.yaya.sdk.connection;

import java.lang.reflect.Proxy;
import java.util.StringTokenizer;

public class YayaObject {
    protected YayaLib L;
    protected Integer ref;

    protected YayaObject(YayaLib L, String globalName) {
        synchronized (L) {
            this.L = L;
            L.getGlobal(globalName);
            registerValue(-1);
            L.pop(1);
        }
    }

    protected YayaObject(YayaObject parent, String name) throws YayaException {
        synchronized (parent.getYayaState()) {
            this.L = parent.getYayaState();
            if (parent.isTable() || parent.isUserdata()) {
                parent.push();
                this.L.pushString(name);
                this.L.getTable(-2);
                this.L.remove(-2);
                registerValue(-1);
                this.L.pop(1);
            } else {
                throw new YayaException("Object parent should be a table or userdata .");
            }
        }
    }

    protected YayaObject(YayaObject parent, Number name) throws YayaException {
        synchronized (parent.getYayaState()) {
            this.L = parent.getYayaState();
            if (parent.isTable() || parent.isUserdata()) {
                parent.push();
                this.L.pushNumber(name.doubleValue());
                this.L.getTable(-2);
                this.L.remove(-2);
                registerValue(-1);
                this.L.pop(1);
            } else {
                throw new YayaException("Object parent should be a table or userdata .");
            }
        }
    }

    protected YayaObject(YayaObject parent, YayaObject name) throws YayaException {
        if (parent.getYayaState() != name.getYayaState()) {
            throw new YayaException("YayaStates must be the same!");
        }
        synchronized (parent.getYayaState()) {
            if (parent.isTable() || parent.isUserdata()) {
                this.L = parent.getYayaState();
                parent.push();
                name.push();
                this.L.getTable(-2);
                this.L.remove(-2);
                registerValue(-1);
                this.L.pop(1);
            } else {
                throw new YayaException("Object parent should be a table or userdata .");
            }
        }
    }

    protected YayaObject(YayaLib L, int index) {
        synchronized (L) {
            this.L = L;
            registerValue(index);
        }
    }

    public YayaLib getYayaState() {
        return this.L;
    }

    private void registerValue(int index) {
        synchronized (this.L) {
            this.L.pushValue(index);
            this.ref = new Integer(this.L.Lref(-10000));
        }
    }

    protected void finalize() {
        try {
            synchronized (this.L) {
                if (this.L.getCPtrPeer() != 0) {
                    this.L.LunRef(-10000, this.ref.intValue());
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to release object " + this.ref);
        }
    }

    public void push() {
        this.L.rawGetI(-10000, this.ref.intValue());
    }

    public boolean isNil() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isNil(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isBoolean() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isBoolean(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isNumber() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isNumber(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isString() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isString(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isFunction() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isFunction(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isJavaObject() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isObject(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isJavaFunction() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isJavaFunction(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isTable() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isTable(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public boolean isUserdata() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.isUserdata(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public int type() {
        int type;
        synchronized (this.L) {
            push();
            type = this.L.type(-1);
            this.L.pop(1);
        }
        return type;
    }

    public boolean getBoolean() {
        boolean bool;
        synchronized (this.L) {
            push();
            bool = this.L.toBoolean(-1);
            this.L.pop(1);
        }
        return bool;
    }

    public double getNumber() {
        double db;
        synchronized (this.L) {
            push();
            db = this.L.toNumber(-1);
            this.L.pop(1);
        }
        return db;
    }

    public String getString() {
        String str;
        synchronized (this.L) {
            push();
            str = this.L.toString(-1);
            this.L.pop(1);
        }
        return str;
    }

    public Object getObject() throws YayaException {
        Object obj;
        synchronized (this.L) {
            push();
            obj = this.L.getObjectFromUserdata(-1);
            this.L.pop(1);
        }
        return obj;
    }

    public YayaObject getField(String field) throws YayaException {
        return this.L.getYayaObject(this, field);
    }

    public Object[] call(Object[] args, int nres) throws YayaException {
        Object[] res;
        synchronized (this.L) {
            if (isFunction() || isTable() || isUserdata()) {
                int i;
                int nargs;
                int top = this.L.getTop();
                push();
                if (args != null) {
                    for (Object obj : args) {
                        this.L.pushObjectValue(obj);
                    }
                } else {
                    nargs = 0;
                }
                int err = this.L.pcall(nargs, nres, 0);
                if (err != 0) {
                    String str;
                    if (this.L.isString(-1)) {
                        str = this.L.toString(-1);
                        this.L.pop(1);
                    } else {
                        str = "";
                    }
                    if (err == 1) {
                        str = "Runtime error. " + str;
                    } else if (err == 4) {
                        str = "Memory allocation error. " + str;
                    } else if (err == 5) {
                        str = "Error while running the error handler function. " + str;
                    } else {
                        str = "Yaya Error code " + err + ". " + str;
                    }
                    throw new YayaException(str);
                }
                if (nres == -1) {
                    nres = this.L.getTop() - top;
                }
                if (this.L.getTop() - top < nres) {
                    throw new YayaException("Invalid Number of Results .");
                }
                res = new Object[nres];
                for (i = nres; i > 0; i--) {
                    res[i - 1] = this.L.toJavaObject(-1);
                    this.L.pop(1);
                }
            } else {
                throw new YayaException("Invalid object. Not a function, table or userdata .");
            }
        }
        return res;
    }

    public Object call(Object[] args) throws YayaException {
        return call(args, 1)[0];
    }

    public String toString() {
        String str = null;
        synchronized (this.L) {
            try {
                if (isNil()) {
                    str = "nil";
                } else if (isBoolean()) {
                    str = String.valueOf(getBoolean());
                } else if (isNumber()) {
                    str = String.valueOf(getNumber());
                } else if (isString()) {
                    str = getString();
                } else if (isFunction()) {
                    str = "Yaya Function";
                } else if (isJavaObject()) {
                    str = getObject().toString();
                } else if (isUserdata()) {
                    str = "Userdata";
                } else if (isTable()) {
                    str = "Yaya Table";
                } else if (isJavaFunction()) {
                    str = "Java Function";
                }
            } catch (YayaException e) {
            }
        }
        return str;
    }

    public Object createProxy(String implem) throws ClassNotFoundException, YayaException {
        Object newProxyInstance;
        synchronized (this.L) {
            if (isTable()) {
                StringTokenizer st = new StringTokenizer(implem, ",");
                Class[] interfaces = new Class[st.countTokens()];
                int i = 0;
                while (st.hasMoreTokens()) {
                    interfaces[i] = Class.forName(st.nextToken());
                    i++;
                }
                newProxyInstance = Proxy.newProxyInstance(getClass().getClassLoader(), interfaces, new YayaInvocationHandler(this));
            } else {
                throw new YayaException("Invalid Object. Must be Table.");
            }
        }
        return newProxyInstance;
    }
}
