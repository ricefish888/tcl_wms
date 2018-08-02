package com.vtradex.wms.server.service.order;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrder;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.production.DailyDeliverOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;

/**
 * 
 * 配货单业务处理接口
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月6日 上午11:00:30
 */
public interface WmsTransportOrderManager extends BaseManager {
    
    /**
     * 
     * 配货单生效
     * 
     * @author Yogurt_lei
     *
     * @date 2017年7月6日 上午11:03:23
     */
    @Transactional
    void activeOrder(WmsTransportOrder order);
    
    /**
     * 
     * 配货单失效
     * 
     * @author Yogurt_lei
     *
     * @date 2017年7月6日 上午11:03:32
     */
    @Transactional
    void unActiveOrder(WmsTransportOrder order);
    
    
    /** 
     * 配货单保存
     * xiang.li
     * 2017-7-6 15:31:55
     **/
	@Transactional
	void save(WmsTransportOrder wmsTransportOrder);
	
	
	
	/** 
     * 新建明细
     * xiang.li
     * 2017-7-7 15:01:49
     **/
//	@Transactional
//	void addDetail(Long id,Long dailyDeliverOrderDetailId,double quantity,WmsTransportOrderDetail wmsTransportOrderDetail);
	
	
	
	/** 
     * 删除明细
     * xiang.li
     * 2017-7-7 15:01:49
     **/
	@Transactional
	void deleteDetail(WmsTransportOrderDetail wmsTransportOrderDetail);
	
	
	/**
	 * 刪除配貨單
	 * xiang.li
	 * 2017-7-11 15:12:28
	 * @param wmsTransportOrder
	 */
	@Transactional
	void deleteTransportOrder(WmsTransportOrder wmsTransportOrder);
	
	
	
	
	/**
	 * 批量添加明细
	 *xiang.li
	 *2017-8-4 10:10:05
	 */
//	@Transactional
//	void addDeliveryOrderDetail(Long id,DailyDeliverOrderDetail dailyDeliverOrderDetail,List values);
	
	
	
	/**
	 * 添加交货单每日明细
	 */
//	@Transactional
//	void storeDetail(WmsDeliveryOrderDetail detail,double qty);
	
	
	

	/**
	 * 删除交货单每日明细
	 */
//	@Transactional
//	void deleteDeliveryOrderDetail(WmsDeliveryOrderDetail detail,double qty);
	
	
	
	/**
	 * 批量添加明细
	 *xiang.li
	 *2017-9-7 19:22:37
	 */
	@Transactional
	void addOrderDetail(Long id,WmsDeliveryOrderDetail detail,List values);
	
	
	
	/** 
     * 新建明细
     * xiang.li
     * 2017-9-7 19:36:49
     **/
	@Transactional
	void addNewDetail(Long id,Long deliverOrderDetailId,double quantity,WmsTransportOrderDetail wmsTransportOrderDetail);
	
	
	
	
	
	

}
