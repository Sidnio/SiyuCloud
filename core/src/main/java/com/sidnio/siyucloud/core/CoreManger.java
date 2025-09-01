package com.sidnio.siyucloud.core;

import com.sidnio.siyucloud.core.network.NetWorkManger;

public class CoreManger {
    public   NetWorkManger network = null;


    public CoreManger() {
        network = new NetWorkManger();

    }
}
