package com.example.plugin;

import com.example.plugin.mixin.Bridge;

public class MyBridge implements Bridge {
    @Override
    public void interact(String msg) {
        System.err.println("This is a template mixin bridge. msg: " + msg);
        System.err.println("PLEASE REMOVE THIS MIXIN BRIDGE AND REPLACE IT WITH YOUR OWN!");
    }
}
