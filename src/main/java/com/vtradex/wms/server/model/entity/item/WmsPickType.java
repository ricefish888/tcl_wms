package com.vtradex.wms.server.model.entity.item;

public interface WmsPickType {

    /**
     * 分配
     */
    public static String ALLOCATED = "ALLOCATED";
    /**
     * 拣货作业
     */
    public static String WORKING = "WORKING";
    /**
     * 发运完成
     */
    public static String FINISHED = "FINISHED";
}
