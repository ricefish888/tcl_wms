package com.vtradex.wms.server.service.receiving;


import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.client.ui.table.RowData;
import com.vtradex.wms.server.model.entity.base.WmsBarCodeRepPrintRecord;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;

/**
 * 
 * Tcl定制化发货单业务
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月21日 下午2:32:39
 */
public interface WmsTclASNManager {	
	
	/**
	 * 刷新标签数量
	 */
	@Transactional
	void refreshDetail(WmsASN asn);

	/**
	 * 
	 * 收货回写采购单和交货单
	 * 
	 * @author Yogurt_lei
	 *
	 * @date 2017年7月17日 下午1:37:57
	 */
	@Transactional
	Long receivedWriteBackPoAndDo2(WmsASNDetail detail,Double qty);
	
	/**
     * 
     * 收货回写采购单和交货单
     * 
     * @author Yogurt_lei
     *
     * @date 2017年7月17日 下午1:37:57
     */
	@Transactional
	void receivedWriteBackPoAndDo(WmsASN asn);
	
	
	
	/** 整单收货
	 * @param wmsASN
	 */
	@Transactional
	void receiveAll(Long asnId,Long workerId);
	
	/**
     * RF收货
     * @param asnId
     * @param itemCode
     * @param receiveQty
     * @param itemStateId
     * @param workerId
     */
	@Transactional
    public void rfReceiving(Long asnId,String itemCode,double receiveQty,Long workerId,Long detailId);
	
	/**
	 * 整单质检确认
	 */
	@Transactional
	void allQcRecord(Long asnId, Long qcStatusId,Long workerId);
	
	/**
	 * 单一质检确认
	 */
	@Transactional
	void singleQcRecord(Long recordId, Long qcStatusId, Long workerId,double qcNumber);
	
	
	
	/**
	 * 打印标签
	 */
	Map printLabel(List<WmsASN> wmsAsns);
	
	/**
	 * 条码补打
	 */
	boolean print(Map map);
	
	
	/**
	 * 预览打印，判断asn明细上标签数量
	 */
	boolean printSupplierLabel(Map map);
	
	/**
	 *  单一明细收货
	 * @param detail
	 * @param packageUnitId
	 * @param quantity
	 * @param itemStateId
	 * @param workerId
	 */
	@Transactional
	void detailReceive(WmsASNDetail detail, Long packageUnitId,double quantity,String itemStateId,Long workerId);
	
	/** 
	 * 保存asn时,编码由规则产生
	 * */
	@Transactional
	void storeASN(WmsASN asn);
	
	
	/**
	 * 移除ASN明细
	 * @param details
	 */
	@Transactional
	void removeDetails(WmsASNDetail detail);
	
    /**
     *打印asn明细 
     */
	@Transactional
	Map printWmsASNDetail(WmsASNDetail detail,Integer quantity);
	
	
	
    /**
     *打印asn明细,判断标签数量
     */
	@Transactional
	boolean printWmsSupplierASNDetail(Map map);
	
	
	/**
	 * 打印asn明细张数，初始化方法
	 */
	public RowData getQuantity(Map map);
	
	
	

	/**
	 * 打印供应商收货明细标签
	 */
	Map printSupplierWmsASN(List<WmsASNDetail> wmsAsns);
	
	/**
	 *  部分收货创建上架单
	 * @param wmsAsn
	 */
	@Transactional
	WmsWorkDoc manualCreateWorkDoc(WmsASN wmsAsn);
	
	
	/**
	 * 修改保管员
	 * @param wmsAsn
	 */
	@Transactional
	void modifyKeeper(WmsASN wmsAsn);
	
	/**
	 * 交货关闭,把未收货的数量退回给交货单
	 */
	@Transactional
	void closeDeliverOrder(WmsASN asn);
	
	/**
	 * 根据保管员创建上架单
	 * @param wmsAsn
	 * @return
	 */
	@Transactional
	public List<WmsWorkDoc> manualCreateWorkDocs(WmsASN wmsAsn) ;
	
	
	/**统计打印次数*/
	@Transactional
	public void countPrintTime(String parentIds,String ip,String raq);
	
	
	/**
	 * 是否打印
	 */
	@Transactional
	public void isPrint(String parentIds,String raq);
	
	
	
	
	/**
	 * 拣货作业单打印次数
	 */
	@Transactional
	public void printTimes(String parentIds,String raq);
}
