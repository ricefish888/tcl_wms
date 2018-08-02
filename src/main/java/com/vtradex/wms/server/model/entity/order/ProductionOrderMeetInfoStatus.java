package com.vtradex.wms.server.model.entity.order;

/**
 * 齐套状态
 * @author fs
 *
 */
public interface ProductionOrderMeetInfoStatus {
	/** 齐套 */
	public static final String COMPLETE = "COMPLETE";
	/** 部分满足 */
	public static final String PART_COMPLETE = "PART_COMPLETE";
	/** 缺料 */
	public static final String SHORT = "SHORT";
	
}
