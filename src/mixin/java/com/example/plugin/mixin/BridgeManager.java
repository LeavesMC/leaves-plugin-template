package com.example.plugin.mixin;

public class BridgeManager {
    public static BridgeManager INSTANCE = new BridgeManager();
    private Bridge bridge;

    private BridgeManager() {}

    public void setBridge(Bridge bridge) {
        this.bridge = bridge;
    }

    public Bridge getBridge() {
        return bridge;
    }
}
