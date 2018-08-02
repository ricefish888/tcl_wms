package com.vtradex.wms.server.service.workdoc;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;

/**
 * 
 * Tcl定制化作业单业务
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月19日 下午4:28:04
 */
public interface WmsTclWorkDocManager extends WmsWorkDocManager{
    
    /**
     * 
     * 保存快捷出库作业单
     *
     * @author Yogurt_lei
     *
     * @date 2017年7月19日 下午4:28:49
     */
    @Transactional
    void saveQuickShippingWorkDoc(Long locationId, WmsWorkDoc workDoc);
    
    /**
     * 公用的创建workdoc方法 --fs
     * @param locationId
     * @param workDoc
     * @param jitType 上线结算/下线结算
     * @return
     */
    @Transactional
    public WmsWorkDoc newWorkDoc(Long locationId, WmsWorkDoc workDoc,String jitType,WmsWarehouse warehouse);

    /**
     * 
     * 生效配送单
     *
     * @author Yogurt_lei
     *
     * @date 2017年7月20日 上午10:46:52
     */
    @Transactional
    void activeQuickShippingWorkDoc(WmsWorkDoc workDoc);
    
    /**
     * 失效配送单
     * @author fs
     * @date 2017-9-22 09:04:17
     */
    @Transactional
    void unActiveQuickShippingWorkDoc(WmsWorkDoc workDoc);
    
    /**
     * 删除配送单
     * @author fs
     * @date 2017-9-22 09:04:17
     */
    @Transactional
    void deleteWorkDoc(WmsWorkDoc workDoc);

    /**
     * 
     * 发运快捷出库作业单
     *
     * @author Yogurt_lei
     *
     * @date 2017年7月20日 上午10:47:48
     */
    @Transactional
    void shipQuickShippingWorkDoc(WmsWorkDoc workDoc);
    
    /**
     * 
     * 明细发运快捷出库作业确认
     *
     * @param task
     *
     * @author Yogurt_lei
     *
     * @date 2017年7月21日 上午9:58:15
     */
    @Transactional
    void detailShipQuickShippingTask(WmsTask task);
    
    /**
     * JIT出库添加明细
     * @param workDoc
     */
    @Transactional
    void addDetail(ProductionOrderDetail orderDetail,List tableValues,Long workDocId);
    
    /**
     * 添加JIT明细公用方法  fs
     * @param orderDetail
     * @param quantity
     * @param workDocId
     */
    void dealProductOrderDetail(ProductionOrderDetail orderDetail,Double quantity,Long workDocId);
    
    /**
     * JIT workdoc发运
     * @param workDoc
     */
    @Transactional
    void shipJitOrder(WmsWorkDoc workDoc);
    
    /**配送单添加明细方法重写*/
    public void addToMoveDocDetail(Long workDocId,WmsInventory inventory, List tableValues);

//    /**根据task id找库存,按存储日期排序*/
//    public List<WmsTask> sortTaskByInv(List<Long> ids);
    
    /**修改保管员*/
    @Transactional
    void modifyKeeper(WmsWorkDoc workDoc);
	/**
	 * 回写数量
	 */
	@Transactional
	Map<Long,Double> updatePickTicket(WmsTask task,Double unPickQty,int x);
	
	
	/**
	 * 删除JIT出库单
	 * @param task
	 */
	@Transactional
    void deleteJIT(WmsWorkDoc workDoc);
}
