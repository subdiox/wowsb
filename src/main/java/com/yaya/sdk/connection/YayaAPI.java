package com.yaya.sdk.connection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class YayaAPI {
    private YayaAPI() {
    }

    public static int objectIndex(int codeState, Object obj, String methodName) throws YayaException {
        int i;
        YayaLib L = YayaStateFactory.getExistingState(codeState);
        synchronized (L) {
            Class clazz;
            int top = L.getTop();
            Object[] objs = new Object[(top - 1)];
            if (obj instanceof Class) {
                clazz = (Class) obj;
            } else {
                clazz = obj.getClass();
            }
            Method[] methods = clazz.getMethods();
            Method method = null;
            for (int i2 = 0; i2 < methods.length; i2++) {
                if (methods[i2].getName().equals(methodName)) {
                    Class[] parameters = methods[i2].getParameterTypes();
                    if (parameters.length == top - 1) {
                        boolean okMethod = true;
                        int j = 0;
                        while (j < parameters.length) {
                            try {
                                objs[j] = compareTypes(L, parameters[j], j + 2);
                                j++;
                            } catch (Exception e) {
                                okMethod = false;
                            }
                        }
                        if (okMethod) {
                            method = methods[i2];
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (method == null) {
                throw new YayaException("Invalid method call. No such method.");
            }
            try {
                Object ret;
                if (Modifier.isPublic(method.getModifiers())) {
                    method.setAccessible(true);
                }
                if (obj instanceof Class) {
                    ret = method.invoke(null, objs);
                } else {
                    ret = method.invoke(obj, objs);
                }
                if (ret == null) {
                    i = 0;
                } else {
                    L.pushObjectValue(ret);
                    i = 1;
                }
            } catch (Exception e2) {
                throw new YayaException(e2);
            }
        }
        return i;
    }

    public static int classIndex(int codeState, Class clazz, String searchName) throws YayaException {
        int i;
        synchronized (YayaStateFactory.getExistingState(codeState)) {
            if (checkField(codeState, clazz, searchName) != 0) {
                i = 1;
            } else if (checkMethod(codeState, clazz, searchName) != 0) {
                i = 2;
            } else {
                i = 0;
            }
        }
        return i;
    }

    public static int javaNewInstance(int codeState, String className) throws YayaException {
        YayaLib L = YayaStateFactory.getExistingState(codeState);
        synchronized (L) {
            try {
                L.pushJavaObject(getObjInstance(L, Class.forName(className)));
            } catch (Exception e) {
                throw new YayaException(e);
            }
        }
        return 1;
    }

    public static int javaNew(int codeState, Class clazz) throws YayaException {
        YayaLib L = YayaStateFactory.getExistingState(codeState);
        synchronized (L) {
            L.pushJavaObject(getObjInstance(L, clazz));
        }
        return 1;
    }

    public static int javaLoadLib(int codeState, String className, String methodName) throws YayaException {
        int i = 0;
        synchronized (YayaStateFactory.getExistingState(codeState)) {
            try {
                Object obj = Class.forName(className).getMethod(methodName, new Class[]{YayaLib.class}).invoke(null, new Object[]{L});
                if (obj == null || !(obj instanceof Integer)) {
                } else {
                    i = ((Integer) obj).intValue();
                }
            } catch (Exception e) {
                throw new YayaException("Error on calling method. Library could not be loaded. " + e.getMessage());
            } catch (Exception e2) {
                throw new YayaException(e2);
            }
        }
        return i;
    }

    private static Object getObjInstance(YayaLib L, Class clazz) throws YayaException {
        Object ret;
        synchronized (L) {
            int top = L.getTop();
            Object[] objs = new Object[(top - 1)];
            Constructor[] constructors = clazz.getConstructors();
            Constructor constructor = null;
            for (int i = 0; i < constructors.length; i++) {
                Class[] parameters = constructors[i].getParameterTypes();
                if (parameters.length == top - 1) {
                    boolean okConstruc = true;
                    int j = 0;
                    while (j < parameters.length) {
                        try {
                            objs[j] = compareTypes(L, parameters[j], j + 2);
                            j++;
                        } catch (Exception e) {
                            okConstruc = false;
                        }
                    }
                    if (okConstruc) {
                        constructor = constructors[i];
                        break;
                    }
                }
            }
            if (constructor == null) {
                throw new YayaException("Invalid method call. No such method.");
            }
            try {
                ret = constructor.newInstance(objs);
                if (ret == null) {
                    throw new YayaException("Couldn't instantiate java Object");
                }
            } catch (Exception e2) {
                throw new YayaException(e2);
            }
        }
        return ret;
    }

    public static int checkField(int codeState, Object obj, String fieldName) throws YayaException {
        int i = 0;
        YayaLib L = YayaStateFactory.getExistingState(codeState);
        synchronized (L) {
            Class objClass;
            if (obj instanceof Class) {
                objClass = (Class) obj;
            } else {
                objClass = obj.getClass();
            }
            try {
                Field field = objClass.getField(fieldName);
                if (field == null) {
                } else {
                    try {
                        Object ret = field.get(obj);
                        if (obj == null) {
                        } else {
                            L.pushObjectValue(ret);
                            i = 1;
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e2) {
            }
        }
        return i;
    }

    public static int checkMethod(int codeState, Object obj, String methodName) {
        int i;
        synchronized (YayaStateFactory.getExistingState(codeState)) {
            Class clazz;
            if (obj instanceof Class) {
                clazz = (Class) obj;
            } else {
                clazz = obj.getClass();
            }
            Method[] methods = clazz.getMethods();
            for (Method name : methods) {
                if (name.getName().equals(methodName)) {
                    i = 1;
                    break;
                }
            }
            i = 0;
        }
        return i;
    }

    public static int createProxyObject(int codeState, String implem) throws YayaException {
        YayaLib L = YayaStateFactory.getExistingState(codeState);
        synchronized (L) {
            try {
                if (L.isTable(2)) {
                    L.pushJavaObject(L.getYayaObject(2).createProxy(implem));
                } else {
                    throw new YayaException("Parameter is not a table. Can't create proxy.");
                }
            } catch (Exception e) {
                throw new YayaException(e);
            }
        }
        return 1;
    }

    private static Object compareTypes(YayaLib L, Class parameter, int idx) throws YayaException {
        boolean okType = true;
        Object obj = null;
        if (L.isBoolean(idx)) {
            if (parameter.isPrimitive()) {
                if (parameter != Boolean.TYPE) {
                    okType = false;
                }
            } else if (!parameter.isAssignableFrom(Boolean.class)) {
                okType = false;
            }
            obj = new Boolean(L.toBoolean(idx));
        } else if (L.type(idx) == 4) {
            if (parameter.isAssignableFrom(String.class)) {
                obj = L.toString(idx);
            } else {
                okType = false;
            }
        } else if (L.isFunction(idx)) {
            if (parameter.isAssignableFrom(YayaObject.class)) {
                obj = L.getYayaObject(idx);
            } else {
                okType = false;
            }
        } else if (L.isTable(idx)) {
            if (parameter.isAssignableFrom(YayaObject.class)) {
                obj = L.getYayaObject(idx);
            } else {
                okType = false;
            }
        } else if (L.type(idx) == 3) {
            obj = YayaLib.convertYayaNumber(new Double(L.toNumber(idx)), parameter);
            if (obj == null) {
                okType = false;
            }
        } else if (L.isUserdata(idx)) {
            if (L.isObject(idx)) {
                Object userObj = L.getObjectFromUserdata(idx);
                if (parameter.isAssignableFrom(userObj.getClass())) {
                    obj = userObj;
                } else {
                    okType = false;
                }
            } else if (parameter.isAssignableFrom(YayaObject.class)) {
                obj = L.getYayaObject(idx);
            } else {
                okType = false;
            }
        } else if (L.isNil(idx)) {
            obj = null;
        } else {
            throw new YayaException("Invalid Parameters.");
        }
        if (okType) {
            return obj;
        }
        throw new YayaException("Invalid Parameter.");
    }
}
