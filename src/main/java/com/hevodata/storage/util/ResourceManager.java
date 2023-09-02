package com.hevodata.storage.util;

public class ResourceManager {

    private static ResourceManager instance;

    public static ResourceManager getInstance(){
        if(instance == null) instance = new ResourceManager();
        return instance;
    }

    public ResourceManager() {
    }
}
