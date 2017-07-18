package com.yaya.sdk.connection;

public class YayaLib {
    private static final String YAYAJAVA_LIB = "YvImSdk";
    private int stateId;
    private CPtr yayaState;

    private native synchronized void Yaya_open(CPtr cPtr, int i);

    private native synchronized int _LargError(CPtr cPtr, int i, String str);

    private native synchronized int _LcallMeta(CPtr cPtr, int i, String str);

    private native synchronized void _LcheckAny(CPtr cPtr, int i);

    private native synchronized int _LcheckInteger(CPtr cPtr, int i);

    private native synchronized double _LcheckNumber(CPtr cPtr, int i);

    private native synchronized void _LcheckStack(CPtr cPtr, int i, String str);

    private native synchronized String _LcheckString(CPtr cPtr, int i);

    private native synchronized void _LcheckType(CPtr cPtr, int i, int i2);

    private native synchronized int _LdoFile(CPtr cPtr, String str);

    private native synchronized int _LdoString(CPtr cPtr, String str);

    private native synchronized String _LfindTable(CPtr cPtr, int i, String str, int i2);

    private native synchronized int _LgetMetaField(CPtr cPtr, int i, String str);

    private native synchronized void _LgetMetatable(CPtr cPtr, String str);

    private native synchronized int _LgetN(CPtr cPtr, int i);

    private native synchronized String _Lgsub(CPtr cPtr, String str, String str2, String str3);

    private native synchronized int _LloadBuffer(CPtr cPtr, byte[] bArr, long j, String str);

    private native synchronized int _LloadFile(CPtr cPtr, String str);

    private native synchronized int _LloadString(CPtr cPtr, String str);

    private native synchronized int _LnewMetatable(CPtr cPtr, String str);

    private native synchronized int _LoptInteger(CPtr cPtr, int i, int i2);

    private native synchronized double _LoptNumber(CPtr cPtr, int i, double d);

    private native synchronized String _LoptString(CPtr cPtr, int i, String str);

    private native synchronized int _Lref(CPtr cPtr, int i);

    private native synchronized void _LsetN(CPtr cPtr, int i, int i2);

    private native synchronized int _Ltyperror(CPtr cPtr, int i, String str);

    private native synchronized void _LunRef(CPtr cPtr, int i, int i2);

    private native synchronized void _Lwhere(CPtr cPtr, int i);

    private native synchronized void _call(CPtr cPtr, int i, int i2);

    private native synchronized int _checkStack(CPtr cPtr, int i);

    private native synchronized void _close(CPtr cPtr);

    private native synchronized void _concat(CPtr cPtr, int i);

    private native synchronized void _createTable(CPtr cPtr, int i, int i2);

    private native synchronized int _equal(CPtr cPtr, int i, int i2);

    private native synchronized int _error(CPtr cPtr);

    private native synchronized int _gc(CPtr cPtr, int i, int i2);

    private native synchronized void _getFEnv(CPtr cPtr, int i);

    private native synchronized void _getField(CPtr cPtr, int i, String str);

    private native synchronized int _getGcCount(CPtr cPtr);

    private native synchronized void _getGlobal(CPtr cPtr, String str);

    private native synchronized int _getMetaTable(CPtr cPtr, int i);

    private native synchronized Object _getObjectFromUserdata(CPtr cPtr, int i) throws YayaException;

    private native synchronized void _getTable(CPtr cPtr, int i);

    private native synchronized int _getTop(CPtr cPtr);

    private native synchronized void _insert(CPtr cPtr, int i);

    private native synchronized int _isBoolean(CPtr cPtr, int i);

    private native synchronized int _isCFunction(CPtr cPtr, int i);

    private native synchronized int _isFunction(CPtr cPtr, int i);

    private native synchronized boolean _isJavaFunction(CPtr cPtr, int i);

    private native synchronized int _isNil(CPtr cPtr, int i);

    private native synchronized int _isNone(CPtr cPtr, int i);

    private native synchronized int _isNoneOrNil(CPtr cPtr, int i);

    private native synchronized int _isNumber(CPtr cPtr, int i);

    private native synchronized boolean _isObject(CPtr cPtr, int i);

    private native synchronized int _isString(CPtr cPtr, int i);

    private native synchronized int _isTable(CPtr cPtr, int i);

    private native synchronized int _isThread(CPtr cPtr, int i);

    private native synchronized int _isUserdata(CPtr cPtr, int i);

    private native synchronized int _lessthan(CPtr cPtr, int i, int i2);

    private native synchronized void _newTable(CPtr cPtr);

    private native synchronized CPtr _newthread(CPtr cPtr);

    private native synchronized int _next(CPtr cPtr, int i);

    private native synchronized int _objlen(CPtr cPtr, int i);

    private native synchronized CPtr _open();

    private native synchronized void _openBase(CPtr cPtr);

    private native synchronized void _openDebug(CPtr cPtr);

    private native synchronized void _openIo(CPtr cPtr);

    private native synchronized void _openLibs(CPtr cPtr);

    private native synchronized void _openMath(CPtr cPtr);

    private native synchronized void _openOs(CPtr cPtr);

    private native synchronized void _openPackage(CPtr cPtr);

    private native synchronized void _openString(CPtr cPtr);

    private native synchronized void _openTable(CPtr cPtr);

    private native synchronized int _pcall(CPtr cPtr, int i, int i2, int i3);

    private native synchronized void _pop(CPtr cPtr, int i);

    private native synchronized void _pushBoolean(CPtr cPtr, int i);

    private native synchronized void _pushInteger(CPtr cPtr, int i);

    private native synchronized void _pushJavaFunction(CPtr cPtr, JavaFunction javaFunction) throws YayaException;

    private native synchronized void _pushJavaObject(CPtr cPtr, Object obj);

    private native synchronized void _pushNil(CPtr cPtr);

    private native synchronized void _pushNumber(CPtr cPtr, double d);

    private native synchronized void _pushString(CPtr cPtr, String str);

    private native synchronized void _pushString(CPtr cPtr, byte[] bArr, int i);

    private native synchronized void _pushValue(CPtr cPtr, int i);

    private native synchronized void _rawGet(CPtr cPtr, int i);

    private native synchronized void _rawGetI(CPtr cPtr, int i, int i2);

    private native synchronized void _rawSet(CPtr cPtr, int i);

    private native synchronized void _rawSetI(CPtr cPtr, int i, int i2);

    private native synchronized int _rawequal(CPtr cPtr, int i, int i2);

    private native synchronized void _remove(CPtr cPtr, int i);

    private native synchronized void _replace(CPtr cPtr, int i);

    private native synchronized int _resume(CPtr cPtr, int i);

    private native synchronized int _setFEnv(CPtr cPtr, int i);

    private native synchronized void _setField(CPtr cPtr, int i, String str);

    private native synchronized void _setGlobal(CPtr cPtr, String str);

    private native synchronized int _setMetaTable(CPtr cPtr, int i);

    private native synchronized void _setTable(CPtr cPtr, int i);

    private native synchronized void _setTop(CPtr cPtr, int i);

    private native synchronized int _status(CPtr cPtr);

    private native synchronized int _strlen(CPtr cPtr, int i);

    private native synchronized int _toBoolean(CPtr cPtr, int i);

    private native synchronized int _toInteger(CPtr cPtr, int i);

    private native synchronized double _toNumber(CPtr cPtr, int i);

    private native synchronized String _toString(CPtr cPtr, int i);

    private native synchronized CPtr _toThread(CPtr cPtr, int i);

    private native synchronized int _type(CPtr cPtr, int i);

    private native synchronized String _typeName(CPtr cPtr, int i);

    private native synchronized void _xmove(CPtr cPtr, CPtr cPtr2, int i);

    private native synchronized int _yield(CPtr cPtr, int i);

    static {
        System.loadLibrary(YAYAJAVA_LIB);
    }

    protected YayaLib(int stateId) {
        this.yayaState = _open();
        Yaya_open(this.yayaState, stateId);
        this.stateId = stateId;
    }

    protected YayaLib(CPtr yayaState) {
        this.yayaState = yayaState;
        this.stateId = YayaStateFactory.insertYayaState(this);
        Yaya_open(yayaState, this.stateId);
    }

    public synchronized void close() {
        YayaStateFactory.removeYayaState(this.stateId);
        _close(this.yayaState);
        this.yayaState = null;
    }

    public synchronized boolean isClosed() {
        return this.yayaState == null;
    }

    public long getCPtrPeer() {
        return this.yayaState != null ? this.yayaState.getPeer() : 0;
    }

    public YayaLib newThread() {
        YayaLib l = new YayaLib(_newthread(this.yayaState));
        YayaStateFactory.insertYayaState(l);
        return l;
    }

    public int getTop() {
        return _getTop(this.yayaState);
    }

    public void setTop(int idx) {
        _setTop(this.yayaState, idx);
    }

    public void pushValue(int idx) {
        _pushValue(this.yayaState, idx);
    }

    public void remove(int idx) {
        _remove(this.yayaState, idx);
    }

    public void insert(int idx) {
        _insert(this.yayaState, idx);
    }

    public void replace(int idx) {
        _replace(this.yayaState, idx);
    }

    public int checkStack(int sz) {
        return _checkStack(this.yayaState, sz);
    }

    public void xmove(YayaLib to, int n) {
        _xmove(this.yayaState, to.yayaState, n);
    }

    public boolean isNumber(int idx) {
        return _isNumber(this.yayaState, idx) != 0;
    }

    public boolean isString(int idx) {
        return _isString(this.yayaState, idx) != 0;
    }

    public boolean isFunction(int idx) {
        return _isFunction(this.yayaState, idx) != 0;
    }

    public boolean isCFunction(int idx) {
        return _isCFunction(this.yayaState, idx) != 0;
    }

    public boolean isUserdata(int idx) {
        return _isUserdata(this.yayaState, idx) != 0;
    }

    public boolean isTable(int idx) {
        return _isTable(this.yayaState, idx) != 0;
    }

    public boolean isBoolean(int idx) {
        return _isBoolean(this.yayaState, idx) != 0;
    }

    public boolean isNil(int idx) {
        return _isNil(this.yayaState, idx) != 0;
    }

    public boolean isThread(int idx) {
        return _isThread(this.yayaState, idx) != 0;
    }

    public boolean isNone(int idx) {
        return _isNone(this.yayaState, idx) != 0;
    }

    public boolean isNoneOrNil(int idx) {
        return _isNoneOrNil(this.yayaState, idx) != 0;
    }

    public int type(int idx) {
        return _type(this.yayaState, idx);
    }

    public String typeName(int tp) {
        return _typeName(this.yayaState, tp);
    }

    public int equal(int idx1, int idx2) {
        return _equal(this.yayaState, idx1, idx2);
    }

    public int rawequal(int idx1, int idx2) {
        return _rawequal(this.yayaState, idx1, idx2);
    }

    public int lessthan(int idx1, int idx2) {
        return _lessthan(this.yayaState, idx1, idx2);
    }

    public double toNumber(int idx) {
        return _toNumber(this.yayaState, idx);
    }

    public int toInteger(int idx) {
        return _toInteger(this.yayaState, idx);
    }

    public boolean toBoolean(int idx) {
        return _toBoolean(this.yayaState, idx) != 0;
    }

    public String toString(int idx) {
        return _toString(this.yayaState, idx);
    }

    public int strLen(int idx) {
        return _strlen(this.yayaState, idx);
    }

    public int objLen(int idx) {
        return _objlen(this.yayaState, idx);
    }

    public YayaLib toThread(int idx) {
        return new YayaLib(_toThread(this.yayaState, idx));
    }

    public void pushNil() {
        _pushNil(this.yayaState);
    }

    public void pushNumber(double db) {
        _pushNumber(this.yayaState, db);
    }

    public void pushInteger(int integer) {
        _pushInteger(this.yayaState, integer);
    }

    public void pushString(String str) {
        if (str == null) {
            _pushNil(this.yayaState);
        } else {
            _pushString(this.yayaState, str);
        }
    }

    public void pushString(byte[] bytes) {
        if (bytes == null) {
            _pushNil(this.yayaState);
        } else {
            _pushString(this.yayaState, bytes, bytes.length);
        }
    }

    public void pushBoolean(boolean bool) {
        _pushBoolean(this.yayaState, bool ? 1 : 0);
    }

    public void getTable(int idx) {
        _getTable(this.yayaState, idx);
    }

    public void getField(int idx, String k) {
        _getField(this.yayaState, idx, k);
    }

    public void rawGet(int idx) {
        _rawGet(this.yayaState, idx);
    }

    public void rawGetI(int idx, int n) {
        _rawGetI(this.yayaState, idx, n);
    }

    public void createTable(int narr, int nrec) {
        _createTable(this.yayaState, narr, nrec);
    }

    public void newTable() {
        _newTable(this.yayaState);
    }

    public int getMetaTable(int idx) {
        return _getMetaTable(this.yayaState, idx);
    }

    public void getFEnv(int idx) {
        _getFEnv(this.yayaState, idx);
    }

    public void setTable(int idx) {
        _setTable(this.yayaState, idx);
    }

    public void setField(int idx, String k) {
        _setField(this.yayaState, idx, k);
    }

    public void rawSet(int idx) {
        _rawSet(this.yayaState, idx);
    }

    public void rawSetI(int idx, int n) {
        _rawSetI(this.yayaState, idx, n);
    }

    public int setMetaTable(int idx) {
        return _setMetaTable(this.yayaState, idx);
    }

    public int setFEnv(int idx) {
        return _setFEnv(this.yayaState, idx);
    }

    public void call(int nArgs, int nResults) {
        _call(this.yayaState, nArgs, nResults);
    }

    public int pcall(int nArgs, int nResults, int errFunc) {
        return _pcall(this.yayaState, nArgs, nResults, errFunc);
    }

    public int yield(int nResults) {
        return _yield(this.yayaState, nResults);
    }

    public int resume(int nArgs) {
        return _resume(this.yayaState, nArgs);
    }

    public int status() {
        return _status(this.yayaState);
    }

    public int gc(int what, int data) {
        return _gc(this.yayaState, what, data);
    }

    public int getGcCount() {
        return _getGcCount(this.yayaState);
    }

    public int next(int idx) {
        return _next(this.yayaState, idx);
    }

    public int error() {
        return _error(this.yayaState);
    }

    public void concat(int n) {
        _concat(this.yayaState, n);
    }

    public int LdoFile(String fileName) {
        return _LdoFile(this.yayaState, fileName);
    }

    public int LdoString(String str) {
        return _LdoString(this.yayaState, str);
    }

    public int LgetMetaField(int obj, String e) {
        return _LgetMetaField(this.yayaState, obj, e);
    }

    public int LcallMeta(int obj, String e) {
        return _LcallMeta(this.yayaState, obj, e);
    }

    public int Ltyperror(int nArg, String tName) {
        return _Ltyperror(this.yayaState, nArg, tName);
    }

    public int LargError(int numArg, String extraMsg) {
        return _LargError(this.yayaState, numArg, extraMsg);
    }

    public String LcheckString(int numArg) {
        return _LcheckString(this.yayaState, numArg);
    }

    public String LoptString(int numArg, String def) {
        return _LoptString(this.yayaState, numArg, def);
    }

    public double LcheckNumber(int numArg) {
        return _LcheckNumber(this.yayaState, numArg);
    }

    public double LoptNumber(int numArg, double def) {
        return _LoptNumber(this.yayaState, numArg, def);
    }

    public int LcheckInteger(int numArg) {
        return _LcheckInteger(this.yayaState, numArg);
    }

    public int LoptInteger(int numArg, int def) {
        return _LoptInteger(this.yayaState, numArg, def);
    }

    public void LcheckStack(int sz, String msg) {
        _LcheckStack(this.yayaState, sz, msg);
    }

    public void LcheckType(int nArg, int t) {
        _LcheckType(this.yayaState, nArg, t);
    }

    public void LcheckAny(int nArg) {
        _LcheckAny(this.yayaState, nArg);
    }

    public int LnewMetatable(String tName) {
        return _LnewMetatable(this.yayaState, tName);
    }

    public void LgetMetatable(String tName) {
        _LgetMetatable(this.yayaState, tName);
    }

    public void Lwhere(int lvl) {
        _Lwhere(this.yayaState, lvl);
    }

    public int Lref(int t) {
        return _Lref(this.yayaState, t);
    }

    public void LunRef(int t, int ref) {
        _LunRef(this.yayaState, t, ref);
    }

    public int LgetN(int t) {
        return _LgetN(this.yayaState, t);
    }

    public void LsetN(int t, int n) {
        _LsetN(this.yayaState, t, n);
    }

    public int LloadFile(String fileName) {
        return _LloadFile(this.yayaState, fileName);
    }

    public int LloadString(String s) {
        return _LloadString(this.yayaState, s);
    }

    public int LloadBuffer(byte[] buff, String name) {
        return _LloadBuffer(this.yayaState, buff, (long) buff.length, name);
    }

    public String Lgsub(String s, String p, String r) {
        return _Lgsub(this.yayaState, s, p, r);
    }

    public String LfindTable(int idx, String fname, int szhint) {
        return _LfindTable(this.yayaState, idx, fname, szhint);
    }

    public void pop(int n) {
        _pop(this.yayaState, n);
    }

    public synchronized void getGlobal(String global) {
        _getGlobal(this.yayaState, global);
    }

    public synchronized void setGlobal(String name) {
        _setGlobal(this.yayaState, name);
    }

    public void openBase() {
        _openBase(this.yayaState);
    }

    public void openTable() {
        _openTable(this.yayaState);
    }

    public void openIo() {
        _openIo(this.yayaState);
    }

    public void openOs() {
        _openOs(this.yayaState);
    }

    public void openString() {
        _openString(this.yayaState);
    }

    public void openMath() {
        _openMath(this.yayaState);
    }

    public void openDebug() {
        _openDebug(this.yayaState);
    }

    public void openPackage() {
        _openPackage(this.yayaState);
    }

    public void openLibs() {
        _openLibs(this.yayaState);
    }

    public Object getObjectFromUserdata(int idx) throws YayaException {
        return _getObjectFromUserdata(this.yayaState, idx);
    }

    public boolean isObject(int idx) {
        return _isObject(this.yayaState, idx);
    }

    public void pushJavaObject(Object obj) {
        _pushJavaObject(this.yayaState, obj);
    }

    public void pushJavaFunction(JavaFunction func) throws YayaException {
        _pushJavaFunction(this.yayaState, func);
    }

    public boolean isJavaFunction(int idx) {
        return _isJavaFunction(this.yayaState, idx);
    }

    public void pushObjectValue(Object obj) throws YayaException {
        if (obj == null) {
            pushNil();
        } else if (obj instanceof Boolean) {
            pushBoolean(((Boolean) obj).booleanValue());
        } else if (obj instanceof Number) {
            pushNumber(((Number) obj).doubleValue());
        } else if (obj instanceof String) {
            pushString((String) obj);
        } else if (obj instanceof JavaFunction) {
            pushJavaFunction((JavaFunction) obj);
        } else if (obj instanceof YayaObject) {
            ((YayaObject) obj).push();
        } else if (obj instanceof byte[]) {
            pushString((byte[]) obj);
        } else {
            pushJavaObject(obj);
        }
    }

    public synchronized Object toJavaObject(int idx) throws YayaException {
        Object obj;
        obj = null;
        if (isBoolean(idx)) {
            obj = new Boolean(toBoolean(idx));
        } else if (type(idx) == 4) {
            obj = toString(idx);
        } else if (isFunction(idx)) {
            obj = getYayaObject(idx);
        } else if (isTable(idx)) {
            obj = getYayaObject(idx);
        } else if (type(idx) == 3) {
            obj = new Double(toNumber(idx));
        } else if (isUserdata(idx)) {
            if (isObject(idx)) {
                obj = getObjectFromUserdata(idx);
            } else {
                obj = getYayaObject(idx);
            }
        } else if (isNil(idx)) {
            obj = null;
        }
        return obj;
    }

    public YayaObject getYayaObject(String globalName) {
        return new YayaObject(this, globalName);
    }

    public YayaObject getYayaObject(YayaObject parent, String name) throws YayaException {
        if (parent.L.getCPtrPeer() == this.yayaState.getPeer()) {
            return new YayaObject(parent, name);
        }
        throw new YayaException("Object must have the same YayaState as the parent!");
    }

    public YayaObject getYayaObject(YayaObject parent, Number name) throws YayaException {
        if (parent.L.getCPtrPeer() == this.yayaState.getPeer()) {
            return new YayaObject(parent, name);
        }
        throw new YayaException("Object must have the same YayaState as the parent!");
    }

    public YayaObject getYayaObject(YayaObject parent, YayaObject name) throws YayaException {
        if (parent.getYayaState().getCPtrPeer() == this.yayaState.getPeer() && parent.getYayaState().getCPtrPeer() == name.getYayaState().getCPtrPeer()) {
            return new YayaObject(parent, name);
        }
        throw new YayaException("Object must have the same YayaState as the parent!");
    }

    public YayaObject getYayaObject(int index) {
        return new YayaObject(this, index);
    }

    public static Number convertYayaNumber(Double db, Class retType) {
        if (retType.isPrimitive()) {
            if (retType == Integer.TYPE) {
                return new Integer(db.intValue());
            }
            if (retType == Long.TYPE) {
                return new Long(db.longValue());
            }
            if (retType == Float.TYPE) {
                return new Float(db.floatValue());
            }
            if (retType == Double.TYPE) {
                return db;
            }
            if (retType == Byte.TYPE) {
                return new Byte(db.byteValue());
            }
            if (retType == Short.TYPE) {
                return new Short(db.shortValue());
            }
        } else if (retType.isAssignableFrom(Number.class)) {
            if (retType.isAssignableFrom(Integer.class)) {
                return new Integer(db.intValue());
            }
            if (retType.isAssignableFrom(Long.class)) {
                return new Long(db.longValue());
            }
            if (retType.isAssignableFrom(Float.class)) {
                return new Float(db.floatValue());
            }
            if (retType.isAssignableFrom(Double.class)) {
                return db;
            }
            if (retType.isAssignableFrom(Byte.class)) {
                return new Byte(db.byteValue());
            }
            if (retType.isAssignableFrom(Short.class)) {
                return new Short(db.shortValue());
            }
        }
        return null;
    }

    public String dumpStack() {
        int n = getTop();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            int t = type(i);
            sb.append(i).append(": ").append(typeName(t));
            if (t == 3) {
                sb.append(" = ").append(toNumber(i));
            } else if (t == 4) {
                sb.append(" = '").append(toString(i)).append("'");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
