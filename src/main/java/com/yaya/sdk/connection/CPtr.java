package com.yaya.sdk.connection;

public class CPtr {
    private long peer;

    public boolean equals(Object other) {
        boolean z = true;
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (CPtr.class != other.getClass()) {
            return false;
        }
        if (this.peer != ((CPtr) other).peer) {
            z = false;
        }
        return z;
    }

    protected long getPeer() {
        return this.peer;
    }

    CPtr() {
    }
}
