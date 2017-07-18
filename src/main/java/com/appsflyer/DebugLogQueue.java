package com.appsflyer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DebugLogQueue {
    private static DebugLogQueue ourInstance = new DebugLogQueue();
    List<Item> queue = new ArrayList();

    public static class Item {
        private String msg;
        private long timestamp = new Date().getTime();

        public Item(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return this.msg;
        }

        public long getTimestamp() {
            return this.timestamp;
        }
    }

    public static DebugLogQueue getInstance() {
        return ourInstance;
    }

    private DebugLogQueue() {
    }

    public void push(String msg) {
        this.queue.add(new Item(msg));
    }

    public Item pop() {
        if (this.queue.size() == 0) {
            return null;
        }
        Item item = (Item) this.queue.get(0);
        this.queue.remove(0);
        return item;
    }
}
