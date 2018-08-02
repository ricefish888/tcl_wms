package com.vtradex.wms.server.model.entity.inventory;

/**
 * 移动类型
 * @author Administrator
 *
 */
public interface TclWmsMoveType {
	/**
	 * 采购收货移动类型
	 */
	String ASNMOVETYPE = "101";
	
	/**
	 * 取消收货移动类型
	 */
	String CANCELRECEIVETYPE = "122";
	
	/**
	 * 采购退货移动类型
	 */
	String PICKMOVETYPE = "161";
	
	/**
	 * 质检转合格移动类型
	 */
	String QCMOVETYPE = "321";
	
	/**
	 * 质检转不良品移动类型
	 */
	String QCBADMOVETYPE = "323";
	
	/**
	 * 生产订单发料移动类型
	 */
	String PRODUCTIONMOVETYPE="261";
	
	/**
	 * 生产订单退料移动类型
	 */
	String PRDRETURNMOVETYPR="262";
	
	/**
	 * 其它出库单移动类型
	 */
	String OTHEROUTMOVETYPE="Z42";
	
	/**
	 * 其它入库单移动类型
	 */
	String OTHERINMOVETYPE="Z41";
	
	/**
	 * 报废出库
	 */
	String BFOUTMOVETYPE="Z17";
	/**
	 * 报废入库
	 */
	String BFINMOVETYPR="Z18";
	
	/**
	 * 库存调拨--一个仓库两个工厂之间
	 */
	String KNDBMOVETYPE="DB311";
	
	/**
	 * 库存调拨--两个仓库之间
	 */
	String DBCKMOVETYPE="301";
	
	/**
	 * 销售交货单
	 */
	String OUTDELIVERYMOVETYPE="687";
	
	/**
	 * VMI库存转自管库存
	 */
	String VMIMOVETYPE = "411";
	
	/**
	 * 自管库存转VMI库存
	 */
	String ZGMOVETYPE = "412";
	/**
	 * 盘盈入库
	 */
	String PYRMOVETYPE = "Z37";
	/**
	 * 盘亏出库
	 */
	String PKCMOVETYPE = "Z38";
}
