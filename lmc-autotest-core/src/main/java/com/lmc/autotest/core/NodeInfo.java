package com.lmc.autotest.core;

import lombok.Data;

@Data
public class NodeInfo {
    public String node;
    public int cpu;
    public int memory;
    public int threads;
}
