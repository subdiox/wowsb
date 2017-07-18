package com.yaya.sdk.connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class YayaInvocationHandler implements InvocationHandler {
    private YayaObject obj;

    public YayaInvocationHandler(YayaObject obj) {
        this.obj = obj;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws YayaException {
        Object obj;
        synchronized (this.obj.L) {
            YayaObject func = this.obj.getField(method.getName());
            if (func.isNil()) {
                obj = null;
            } else {
                Class retType = method.getReturnType();
                if (retType.equals(Void.class) || retType.equals(Void.TYPE)) {
                    func.call(args, 0);
                    obj = null;
                } else {
                    obj = func.call(args, 1)[0];
                    if (obj != null && (obj instanceof Double)) {
                        obj = YayaLib.convertYayaNumber((Double) obj, retType);
                    }
                }
            }
        }
        return obj;
    }
}
