package com.vtradex.wms.server.service.item.pojo;


import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

import com.vtradex.engine.utils.CollectionUtils;
import com.vtradex.sequence.service.sequence.SequenceGenerater;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.message.Task;
import com.vtradex.thorn.server.model.security.ThornBaseOrganization;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.BussinessModelHolder;
import com.vtradex.wms.server.enumeration.WarehouseEnumeration;
import com.vtradex.wms.server.model.component.LotInfo;
import com.vtradex.wms.server.model.entity.base.Wms2SapInterfaceLogType;
import com.vtradex.wms.server.model.entity.base.WmsAccountCloseDay;
import com.vtradex.wms.server.model.entity.base.WmsFactoryXmlb;
import com.vtradex.wms.server.model.entity.base.WmsSapFactory;
import com.vtradex.wms.server.model.entity.base.WmsSapWarehouseType;
import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.inventory.InventoryLedgerCodeType;
import com.vtradex.wms.server.model.entity.inventory.TclWmsInventoryLedger;
import com.vtradex.wms.server.model.entity.inventory.TclWmsMoveType;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.inventory.WmsInventoryLog;
import com.vtradex.wms.server.model.entity.inventory.WmsStorageDaily;
import com.vtradex.wms.server.model.entity.item.UnitLevel;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemHandOverAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsPackageUnit;
import com.vtradex.wms.server.model.entity.order.PurchaseOrderDetail;
import com.vtradex.wms.server.model.entity.order.WmsTransportOrderDetail;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.ProductionOrder;
import com.vtradex.wms.server.model.entity.production.ProductionOrderDetail;
import com.vtradex.wms.server.model.entity.production.ProductionWarehouseCode;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrder;
import com.vtradex.wms.server.model.entity.production.WmsDeliveryOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrder;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderDetail;
import com.vtradex.wms.server.model.entity.production.WmsReservedOrderType;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.enums.WmsAsnGenType;
import com.vtradex.wms.server.model.enums.WmsInventoryLogType;
import com.vtradex.wms.server.model.enums.WmsPickTicketStatus;
import com.vtradex.wms.server.model.enums.WmsPickticketBillTypeCode;
import com.vtradex.wms.server.model.enums.WmsPickticketGenType;
import com.vtradex.wms.server.service.interf.InterfaceLogManager;
import com.vtradex.wms.server.service.item.TclMessageManager;
import com.vtradex.wms.server.service.message.pojo.DefaultWmsMessageManager;
import com.vtradex.wms.server.service.model.interfaceLog.InterfaceLogTaskType;
import com.vtradex.wms.server.service.pickticket.WmsPickticketManager;
import com.vtradex.wms.server.service.production.ProductionOrderManager;
import com.vtradex.wms.server.service.task.CallOracleProcManager;
import com.vtradex.wms.server.utils.DateUtil;
import com.vtradex.wms.server.utils.DoubleUtils;
import com.vtradex.wms.server.utils.StringHelper;
import com.vtradex.wms.webservice.model.TaskSubscriber;
import com.vtradex.wms.webservice.sap.model.Wms2SapEInventory;
import com.vtradex.wms.webservice.sap.model.Wms2SapEInventoryArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapInventoryLedger;
import com.vtradex.wms.webservice.sap.model.Wms2SapInventoryLedgerArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapItemAttr;
import com.vtradex.wms.webservice.utils.CommonHelper;
import com.vtradex.wms.webservice.utils.XmlObjectConver;

public class DefaultTclMessageManager extends DefaultWmsMessageManager implements TclMessageManager{
	
	private InterfaceLogManager interfaceLogManager;
	private SequenceGenerater sequenceGenerater;

	public DefaultTclMessageManager(InterfaceLogManager interfaceLogManager, SequenceGenerater sequenceGenerater) {
		this.interfaceLogManager = interfaceLogManager;
		this.sequenceGenerater = sequenceGenerater;
	}
	public void subscriberCreatePackageUnit(Object object) {
		WmsItem item = (WmsItem) object;
		//判断商品是否已有最小包装，如没有则自动创建，如有则修改最小包装名称为当前包装名称
		WmsPackageUnit packageUnit = EntityFactory.getEntity(WmsPackageUnit.class);
		//设置数字1为默认的拆箱级别
		packageUnit.setConvertFigure(1D);
		packageUnit.setUnit(item.getBaseUnit());
		packageUnit.setUnitLevel(UnitLevel.A);
		packageUnit.setWeight(item.getWeight());
		packageUnit.setVolume(item.getVolume());
		packageUnit.setDescription(item.getUserFieldV5());
		item.addPackageUnit(packageUnit);
		
		commonDao.store(item);
	}
	@Override
	public void createItemLog(WmsItem item) {
		Wms2SapItemAttr wia = new Wms2SapItemAttr();
		wia.setItemCode(item.getCode());
		// JITATT属性
		if(WmsItemJITAtt.NO_JIT.equals(item.getUserFieldV2())){
			wia.setJitAttr("03");
		}else if(WmsItemJITAtt.JIT_UPLINE_SETTLE.equals(item.getUserFieldV2())){
			wia.setJitAttr("02");
		}else if(WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(item.getUserFieldV2())){
			wia.setJitAttr("01");
		}else{
			throw new BusinessException("物料的JITATT属性值为空");
		}
		//交接属性
		if(WmsItemHandOverAtt.LINE_EDGE.equals(item.getUserFieldV1())){
			wia.setHandoverAttr("01");
		}else if(WmsItemHandOverAtt.T_1_AREA.equals(item.getUserFieldV1())){
			wia.setHandoverAttr("02");
		}else{
			throw new BusinessException("物料的交接属性值为空");
		}
		String xml = XmlObjectConver.toXML(wia);
		interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_ITEMPROPERTY, Wms2SapInterfaceLogType.ITEMPROPERTY, xml, item.getId(),item.getCode());
	}
	
	 /** 系统上线后  根据某天的库存重新初始化日期
	 * 由于之前的库存日志不准 ， 系统上线后在某天重新初始化日结 则将当前的库存作为今天的期末库存。 以后算日结不能比这一天早。 
	 * 执行时需要确保当天不会再产生库存日志。
	 * */
	public void initDailyInventory() {
		Date currentDate = new Date();
		CallOracleProcManager callOracleProcManager = (CallOracleProcManager)applicationContext.getBean("callOracleProcManager");
		callOracleProcManager.INIT_STORAGE_DAILY();
		
		//1、重算hashcode
		String queryPreStorageDaily = "from WmsStorageDaily ";
		List<WmsStorageDaily> sds = commonDao.findByQuery(queryPreStorageDaily);
		
		for(WmsStorageDaily w : sds) {
			LotInfo l = w.getLotInfo();
			w.setLotInfo(l);  //重新计算hashcode
			commonDao.store(w);
		}
		//2.传接口
		sendStorageDailyToSap(currentDate);
		
	}
	@Override
	public void createDailyInventory() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1); //昨天  因为这个定时任务是每天的零点以后执行
		
		compute(calendar.getTime());
	}
	
	/**库存日结 选择日期然后从选择的那天一直算到当前日期*/
	public void createDailyInventory2(Date date) {
		Date today = DateUtil.getTodayBegin(); //今天
		if(!date.before(today)) {
			throw new BusinessException("日期必须在当前日期之前");
		}
		int days = DateUtil.getMargin(DateUtil.formatDateYMDToStr(today),DateUtil.formatDateYMDToStr(date));
//		String hql = "FROM WmsWarehouse warehouse WHERE warehouse.baseOrganization.id = :baseOrganizationId";
//		WmsWarehouse wh = (WmsWarehouse)commonDao.findByQueryUniqueResult(hql,"baseOrganizationId",BaseOrganizationHolder.getThornBaseOrganization().getId());
		Calendar calendar = Calendar.getInstance();
		for(int i = 0;i <= days; i++){
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, i);
			compute(calendar.getTime());
		}
	}
	/**
	 *  date  需要日结的日期
	 * */
//	private void compute(Date date, Long wmsWarehouseId) {
	@SuppressWarnings({ "unchecked", "unused" })
	private void compute(Date date) {
		
		
		
		//如果WmsStorageDaily里面有数据  则传入的date不能小于 WmsStorageDaily表里面的最小的compute_date-1; 因为要取前一天的期初。
		String hql2="select min(trunc(computeDate)) from WmsStorageDaily";
		Object minComputeDateObj = (Object)commonDao.findByQueryUniqueResult(hql2, new String[]{}, new Object[]{});
		if(minComputeDateObj!=null) {
			Date minComputeDate = (Date)minComputeDateObj;
			Date minDate = DateUtil.addDayToDate(minComputeDate, 1);//
			Date date2 = DateUtil.getFirstHourOFDay(date);
			if(date2.before(minDate) ) {
				throw new BusinessException("最小日结日期不能小于"+DateUtil.format(minDate, "yyyyMMdd"));
			}
			
		}
		
		
		
		CommonHelper.log("开始计算结转日期为"+DateUtil.formatDateYMDToStr(date)+"的日结");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar = DateUtils.truncate(calendar, Calendar.DATE);
		Date currentDate = calendar.getTime(); //需要日期的日期  今天
		
		calendar.add(Calendar.DATE, -1);
		Date beforeOneDate = calendar.getTime();//前一天的日期  昨天
		
		calendar.add(Calendar.DATE, 2);
		Date afterOneDate = calendar.getTime(); //后一天的日期 明天
		
		CommonHelper.log("前一天日期="+DateUtil.formatDateYMDToStr(beforeOneDate)+"--------后一天日期="+DateUtil.formatDateYMDToStr(afterOneDate));
		CommonHelper.log("开始删除结转日期为"+DateUtil.formatDateYMDToStr(currentDate)+"的日结");
		// 删除当天该库区的StorageDaily数据
		String hql = "from WmsStorageDaily where computeDate=:curDate";
		List<WmsStorageDaily> storageDailys = commonDao.findByQuery(hql,new String[] {  "curDate" }, new Object[] { currentDate });
		if (storageDailys != null && storageDailys.size() > 0) {
			commonDao.deleteAll(storageDailys);
		} 
		CommonHelper.log("删除完毕，开始重新计算结转日期为" + DateUtil.formatDateYMDToStr(currentDate)+"的日结");
		
		// 拷贝前一天的数据作为当天的初始数据
		long startTimeMillis = System.currentTimeMillis();
		CommonHelper.log("复制"+DateUtil.formatDateYMDToStr(beforeOneDate)+"的期末大于0的库存为"+DateUtil.formatDateYMDToStr(currentDate)+"的期初");
		String queryPreStorageDaily = "from WmsStorageDaily where endCount>0  and computeDate=:computeDate ";
		List<WmsStorageDaily> preStorageDailys = commonDao.findByQuery(
				queryPreStorageDaily, new String[] { "computeDate" }, new Object[] { beforeOneDate });
		
		List<WmsStorageDaily> curStorageDailys = new ArrayList<WmsStorageDaily>();
		for (WmsStorageDaily preStorageDaily : preStorageDailys) {
			WmsStorageDaily curStorageDaily = EntityFactory.getEntity(WmsStorageDaily.class);
//			curStorageDaily.setWarehouse(preStorageDaily.getWarehouse());
			curStorageDaily.setComputeDate(currentDate);
			curStorageDaily.setItem(preStorageDaily.getItem());
			curStorageDaily.setLotInfo(preStorageDaily.getLotInfo());
			curStorageDaily.setInventoryStatus(preStorageDaily.getInventoryStatus());
			
			curStorageDaily.setStartCount(preStorageDaily.getEndCount());//前一天的期末数量作为当天的期初数量
			curStorageDaily.setInCount(0D);
			curStorageDaily.setOutCount(0D);
			curStorageDaily.setEndCount(preStorageDaily.getEndCount());
			
			commonDao.store(curStorageDaily);
			curStorageDailys.add(curStorageDaily);
		}
		preStorageDailys = null;
		CommonHelper.log("期初复制完毕，开始计算库存日志");
		
		String queryInventoryLogHql = "from WmsInventoryLog log "
				+ " where log.updateInfo.createdTime <:endTime "
				+ " and log.updateInfo.createdTime>=:startTime "
//				+ " and log.warehouse.id=:warehouseId "
//				+ " and log.type NOT IN ('MOVE','LOCK','UNLOCK','VMI_OUT','VMI_IN') "
				+ " and log.type in (:logtypes) "
				+ "order by log.updateInfo.createdTime ";
		
		List<String> logtypes = new ArrayList<String>();
		logtypes.add(WmsInventoryLogType.RECEIVING);//收货
		logtypes.add(WmsInventoryLogType.CANCEL_RECEIVING);//取消收货
		logtypes.add(WmsInventoryLogType.SHIPPING); //发货
		logtypes.add(WmsInventoryLogType.QTY_CHANGE); //数量调整
		logtypes.add(WmsInventoryLogType.ZCP_CHANGE); //正残品调拨
		logtypes.add(WmsInventoryLogType.NWX_CHANGE); //内外销调整
		logtypes.add(WmsInventoryLogType.STATUS_CHANGE); //状态调整
		logtypes.add(WmsInventoryLogType.GYS_CHANGE);//修改供应商
		logtypes.add(WmsInventoryLogType.ITEM_KEY_CHANGE);//属性调整
		
		
		List<WmsInventoryLog> inventoryLogs = commonDao.findByQuery(
				queryInventoryLogHql, new String[] { "endTime", "startTime","logtypes" }, 
				new Object[] { afterOneDate,currentDate,logtypes });
		for (WmsInventoryLog inventoryLog : inventoryLogs) {
			WmsStorageDaily tempStorageDaily = EntityFactory.getEntity(WmsStorageDaily.class);
			
//			tempStorageDaily.setWarehouse(inventoryLog.getWarehouse());
			tempStorageDaily.setComputeDate(currentDate);
			tempStorageDaily.setItem(inventoryLog.getItem());
			
			LotInfo ol =  inventoryLog.getLotInfo();
			LotInfo lotInfo = new LotInfo();
			//lotInfo需要处理  不需要记录的批次字段不能写
//			lotInfo.setLot(ol.getLot());
//			lotInfo.setStorageDate(ol.getStorageDate());
			lotInfo.setSupplierCode(ol.getSupplierCode()); //供应商编码

			lotInfo.setExtendPropC8(ol.getExtendPropC8());//货主   0标准 2寄售
			lotInfo.setExtendPropC9(ol.getExtendPropC9());//供应商名称
			lotInfo.setExtendPropC10(ol.getExtendPropC10());//工厂编码
			lotInfo.setExtendPropC11(ol.getExtendPropC11());//工厂名称
			
			String kcdd = "";
			if(!StringHelper.isNullOrEmpty(ol.getExtendPropC19()) && ol.getExtendPropC19().startsWith("W")){
				kcdd = ol.getExtendPropC19();
			}else{
				kcdd = this.getSapWarehouCodeByXmlb(inventoryLog.getInventoryStatus(), lotInfo.getExtendPropC8(), kcdd);
//				kcdd = this.getSapWarehouseCode(inventoryLog.getInventoryStatus(), inventoryLog.getWarehouse().getCode(), kcdd);
			}
			lotInfo.setExtendPropC19(kcdd);//库存地点
			
			
			tempStorageDaily.setLotInfo(lotInfo);
			tempStorageDaily.setInventoryStatus(inventoryLog.getInventoryStatus());
			
			
			
			if(inventoryLog.getChangeQty()>=0){ //变化数量大于0 库存增加写入库数量
				tempStorageDaily.setInCount(inventoryLog.getChangeQty());//WMS入库数量
			}
			else{
				tempStorageDaily.setOutCount(inventoryLog.getChangeQty());//WMS出库数量
			}
			WmsStorageDaily daily = null;
			if (curStorageDailys.contains(tempStorageDaily)) { //如果包含
				daily = curStorageDailys.get(curStorageDailys.indexOf(tempStorageDaily)); //从内存取值
				daily.setStartCount(DoubleUtils.add(daily.getStartCount(), tempStorageDaily.getStartCount()));
				daily.setOutCount(DoubleUtils.add(daily.getOutCount(), tempStorageDaily.getOutCount()));
				daily.setInCount(DoubleUtils.add(daily.getInCount(), tempStorageDaily.getInCount()));
				daily.setEndCount(DoubleUtils.sub(DoubleUtils.add(daily.getStartCount(),daily.getInCount()), Math.abs(daily.getOutCount())));
				
			}
			else{
				daily = EntityFactory.getEntity(WmsStorageDaily.class);
				
//				daily.setWarehouse(tempStorageDaily.getWarehouse());
				daily.setComputeDate(tempStorageDaily.getComputeDate());
				daily.setItem(tempStorageDaily.getItem());
				daily.setLotInfo(tempStorageDaily.getLotInfo());
				daily.setInventoryStatus(tempStorageDaily.getInventoryStatus());
				daily.setStartCount(DoubleUtils.add(daily.getStartCount(), tempStorageDaily.getStartCount()));
				daily.setInCount(DoubleUtils.add(daily.getInCount(), tempStorageDaily.getInCount()));
				daily.setOutCount(DoubleUtils.add(daily.getOutCount(), tempStorageDaily.getOutCount()));
				daily.setEndCount(DoubleUtils.sub(DoubleUtils.add(daily.getStartCount(),daily.getInCount()), Math.abs(daily.getOutCount())));
				
				curStorageDailys.add(daily);
			}
			
			commonDao.store(daily);
		}
		CommonHelper.log("日结计算完毕，开始写入SAP接口......");
		
		sendStorageDailyToSap(currentDate);
		
		inventoryLogs = null;
		
	}
	
	private void sendStorageDailyToSap(Date currentDate) {
		String hql1 = "SELECT sd.lotInfo.extendPropC10,"//工厂
				+ "sd.lotInfo.extendPropC19,"//库存地点
				+ "sd.item.code,"//物料号
				+ "sd.item.name," //物料描述
				+ "to_char(sd.computeDate,'yyyyMMdd'),"//库存日期
				+ "MAX(sd.lotInfo.supplierCode),"//供应商编码
				+ "sd.lotInfo.extendPropC8," //库存类型
				+ "nvl(SUM(sd.unlimCount),0) ,"//非限制数量
				+ "nvl(SUM(sd.checkInventory),0) ,"//质检库存
				+ "nvl(SUM(sd.startCount),0) ,";//WMS期初数量
		
		String jshql2 = "(select nvl(sum(d.inCount+d.outCount),0) from WmsStorageDaily d where d.inventoryStatus='待检' " +
					"and d.item.code=sd.item.code and d.item.name=sd.item.name " +
					"and d.lotInfo.extendPropC10=sd.lotInfo.extendPropC10 and d.lotInfo.extendPropC19=sd.lotInfo.extendPropC19 " +
					"and d.lotInfo.supplierCode=sd.lotInfo.supplierCode and d.lotInfo.extendPropC8=sd.lotInfo.extendPropC8 " +
					"and to_char(d.computeDate,'yyyyMMdd')=to_char(sd.computeDate,'yyyyMMdd')),";//待检状态差异数量，大于0，传SAP时写入库数量，小于0写出库数量
		String bzhql2 = "(select nvl(sum(d.inCount+d.outCount),0) from WmsStorageDaily d where d.inventoryStatus='待检' " +
					"and d.item.code=sd.item.code and d.item.name=sd.item.name " +
					"and d.lotInfo.extendPropC10=sd.lotInfo.extendPropC10 and d.lotInfo.extendPropC19=sd.lotInfo.extendPropC19 " +
					"and d.lotInfo.extendPropC8=sd.lotInfo.extendPropC8 " +
					"and to_char(d.computeDate,'yyyyMMdd')=to_char(sd.computeDate,'yyyyMMdd')),";//待检状态差异数量，大于0，传SAP时写入库数量，小于0写出库数量
		String hql3 = "nvl(SUM(sd.inCount),0) ,"//WMS入库数量
				+ "nvl(SUM(sd.outCount),0) ,"//WMS出库数量
				+ "nvl(SUM(sd.endCount),0) "//WMS期末数量
				+ "FROM WmsStorageDaily sd "
				+ "WHERE 1=1 "
				+ " AND sd.lotInfo.extendPropC8 =:extendPropC8 "
				+ "AND to_char(sd.updateInfo.createdTime,'yyyyMMdd') =:currentDate " +
				" AND sd.computeDate =:computeDate " ;
		String bzgroupbyhql = " GROUP BY sd.lotInfo.extendPropC10,"
				+ "sd.lotInfo.extendPropC19,"
				+ "sd.item.code,"
				+ "sd.item.name,"
				+ "to_char(sd.computeDate,'yyyyMMdd'),"
				+ "sd.lotInfo.extendPropC8 ";
		
		String jsgroupbyHql =" GROUP BY sd.lotInfo.extendPropC10,"
			+ "sd.lotInfo.extendPropC19,"
			+ "sd.item.code,"
			+ "sd.item.name,"
			+ "to_char(sd.computeDate,'yyyyMMdd'),"
			+ "sd.lotInfo.supplierCode, "
			+ "sd.lotInfo.extendPropC8 ";
		List<Object[]> infos = new ArrayList<Object[]>();
		//寄售日结数据
		List<Object[]> jsInfos = commonDao.findByQuery(hql1+jshql2+hql3+jsgroupbyHql, new String[]{"extendPropC8","currentDate","computeDate"}, 
				new Object[]{WmsFactoryXmlb.JS,DateUtil.format(new Date(), "yyyyMMdd"),currentDate});
		//标准日结数据
		List<Object[]> bzInfos = commonDao.findByQuery(hql1+bzhql2+hql3+bzgroupbyhql, new String[]{"extendPropC8","currentDate","computeDate"}, 
				new Object[]{WmsFactoryXmlb.BZ,DateUtil.format(new Date(), "yyyyMMdd"),currentDate});
		infos.addAll(bzInfos);
		infos.addAll(jsInfos);
		if(!infos.isEmpty()){
			createWms2SapInterfaceLog(infos);
		}
		infos = null;
		CommonHelper.log("SAP接口写入完成，日结完成...");
	}
	private void createWms2SapInterfaceLog(List<Object[]> infos) {
		Wms2SapEInventoryArray arrays = new Wms2SapEInventoryArray();
		Wms2SapEInventory[] wsei = new Wms2SapEInventory[infos.size()];
		int i =0;
		for(Object[] o :infos){
			Wms2SapEInventory wse = new Wms2SapEInventory();
			wse.setWERKS(o[0]==null ? "" :o[0].toString());//工厂
			wse.setLGORT(o[1]==null ? "" :o[1].toString());//库存地点
//			wse.setLGORT("B001");
			wse.setMATNR(o[2].toString());//物料号
			wse.setMAKTX(o[3]==null ? "" :o[3].toString());//物料描述
			wse.setERDAT(o[4].toString());//库存日期
			//只有寄售类型的库存才传供应商，标准类型的不用传供应商
			if(WmsFactoryXmlb.JS.equals(o[6].toString())){
				wse.setLIFNR(o[5].toString());//供应商编码
				wse.setSOBKZ("K");//库存类型
			}else{
				wse.setLIFNR(null);
				wse.setSOBKZ(null);
			}
			wse.setMENGE1(o[7].toString());//非限制数量
			wse.setMENGE2(o[8].toString());//质检库存
			wse.setBMENG(o[9].toString());//WMS期初数量
			//待检状态差异数量，大于0，传SAP时写入库数量，小于0写出库数量
			Double dealQty = Double.valueOf(o[10].toString());
			Double inQty = Double.valueOf(o[11].toString());
			Double outQty = Double.valueOf(o[12].toString());
			if(dealQty>0){
				wse.setIMENG((inQty+dealQty)+"");//WMS入库数量
				wse.setOMENG(Math.abs(outQty)+"");//WMS出库数量
			}else{
				wse.setIMENG(inQty.toString());//WMS入库数量
				wse.setOMENG(Math.abs(dealQty+outQty)+"");//WMS出库数量
			}
			wse.setMENGE(o[13].toString());//WMS期末数量
			wsei[i]=wse;
			arrays.setWms2SapEInventorys(wsei);
			i++;
		}
		
		String xml = XmlObjectConver.toXML(arrays);
//		System.out.println(xml);
		interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_DAYLYINVENTORY, Wms2SapInterfaceLogType.DAYLYINVENTORY, xml, null,infos.get(0)[4]+"");
	}
	
	
	private String genLedgerLineNo(String wmsCode,String moveType) {
		//ledger.getMoveType()+"###"+ledger.getWmsCode();
		String hql = "from TclWmsInventoryLedger l where l.moveType=:moveType and l.wmsCode=:wmsCode";
		List<TclWmsInventoryLedger> ledgers =commonDao.findByQuery(hql,new String[]{"moveType","wmsCode"},new Object[]{moveType,wmsCode});
		int length = 0;
		if(!ledgers.isEmpty()) {
			length = ledgers.size();
		}
		length=length+1;
		
		return CommonHelper.addCharBeforeStr(length+"", 4, "0");
		
		
	}
	
	public void sendReceiveInfo2SAP(WmsASNDetail detail,Double receiveQty){
		detail = commonDao.load(WmsASNDetail.class, detail.getId());
		if(receiveQty<=0){
			return;
		}
		WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
		if(WmsAsnGenType.BHRKD.equals(asn.getBillType().getCode())
				|| WmsAsnGenType.DBRKD.equals(asn.getBillType().getCode())
				|| WmsAsnGenType.THRKD.equals(asn.getBillType().getCode())){//调拨入库的数据在调拨出库的时候就已经传了
			return;
		}
//		if(WmsAsnGenType.ZCRKD.equals(asn.getBillType().getCode()) && WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(detail.getItem().getUserFieldV2())){
//			return;
//		}
		if(WmsPickticketBillTypeCode.SCTLD.equals(asn.getBillType().getCode()) || WmsAsnGenType.JITRKD.equals(asn.getBillType().getCode())){//JIT下线入库走生产退料接口传SAP，同时传采购退货数据给SAP
			ProductionOrder pdo = (ProductionOrder) commonDao.findByQueryUniqueResult("FROM ProductionOrder o where o.code=:code", "code", asn.getCustomerBill());
			ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.PRDRETURNMOVETYPR, 
					asn.getBillType().getCode(), detail.getItem(), pdo.getFactory(), asn.getSupplier().getCode(),
					detail.getInventoryStatus(),detail.getLotInfo().getExtendPropC19(),asn.getUserField5());
    		ledger.setProductionCode(asn.getCustomerBill());
    		
    		ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, Long.valueOf(detail.getUserField2()));
    		ledger.setReservedCode(pod.getReservedOrder());//预留号
    		ledger.setResProject(pod.getReservedProject());//预留行项目号
    		//已改 不传采购退货给SAP了 --2017-08-14
//    		if(WmsAsnGenType.JITRKD.equals(asn.getBillType().getCode())){ //JIT下线入库走生产退料接口传SAP，同时传采购退货数据给SAP
//    			TclWmsInventoryLedger outLedger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
//    			outLedger = this.genTclWmsInventoryLedger(outLedger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.PICKMOVETYPE, asn.getBillType().getCode(), detail.getItem(), pdo.getFactory(), asn.getSupplier().getCode(),detail.getInventoryStatus(),detail.getLotInfo().getExtendPropC19());
//    			outLedger.setQuantity(receiveQty);
//    			if(WmsFactoryXmlb.BZ.equals(asn.getUserField5())){
//    				outLedger.setInvType(null);
//            	}else{
//            		outLedger.setInvType("K");
//            	}
//    	    	commonDao.store(outLedger);
//    		}
    	}else if(WmsAsnGenType.ZCRKD.equals(asn.getBillType().getCode())){
    		WmsTransportOrderDetail tod = detail.getTransportOrderDetail();
        	if(tod == null){
        		return;
        	}
        	WmsDeliveryOrderDetail dod = tod.getDeliveryOrderDetail();
        	PurchaseOrderDetail pod = dod.getPurchaseOrderDetail();
        	ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.ASNMOVETYPE, 
        			asn.getBillType().getCode(), detail.getItem(), pod.getFactory(), asn.getSupplier().getCode(),
        			detail.getInventoryStatus(),dod.getKcdd(),pod.getPstyp());
        	ledger.setPoCode(pod.getPurchaseOrder().getCode());
        	ledger.setPoLineNo(pod.getEbelp());
        	if(!StringHelper.isNullOrEmpty(dod.getKcdd()) && dod.getKcdd().startsWith("W")){
        		ledger.setLocationCode(dod.getKcdd());
        	}
        	ledger.setDoCode(dod.getDeliveryOrder().getCode());
        	ledger.setDoLineNo(dod.getDeliveryOrder().getProject());
        	//交货单的创建方式：判断依据--如果交货单的SAP交货单号SapCode不为空且和交货单编码CODE一致 则接口传输过来  SAP创建，
        	//否则交货单的SAP交货单号SapCode为空或者SAP交货单号与交货单编码不一致，则WMS创建
        	if(!StringHelper.isNullOrEmpty(dod.getDeliveryOrder().getSapCode()) && dod.getDeliveryOrder().getSapCode().equals(dod.getDeliveryOrder().getCode())){
        		ledger.setCreateType("S");
        	}
        	if(StringHelper.isNullOrEmpty(dod.getDeliveryOrder().getSapCode()) || !dod.getDeliveryOrder().getSapCode().equals(dod.getDeliveryOrder().getCode())){
        		ledger.setCreateType("W");
        	}
    	}else if (WmsAsnGenType.QTRKD.equals(asn.getBillType().getCode()) || WmsAsnGenType.PYRKD.equals(asn.getBillType().getCode())){
    		WmsSapFactory factory = getSapFactory(asn.getUserField7());
    		ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.OTHERINMOVETYPE, 
    				asn.getBillType().getCode(), detail.getItem(), factory, asn.getSupplier().getCode(),
    				detail.getInventoryStatus(),detail.getLotInfo().getExtendPropC19(),asn.getUserField5());
    		if(WmsAsnGenType.PYRKD.equals(asn.getBillType().getCode())){
    			ledger.setMoveType(TclWmsMoveType.PYRMOVETYPE);
    			ledger.setCostCenter(asn.getUserField6());
    		}
    	}else if(WmsAsnGenType.BFRKD.equals(asn.getBillType().getCode())){
    		ledger.setCostCenter(asn.getUserField6());
    		WmsSapFactory factory = getSapFactory(asn.getUserField7());
    		ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.BFINMOVETYPR,
    				asn.getBillType().getCode(), detail.getItem(), factory, asn.getSupplier().getCode(),
    				detail.getInventoryStatus(),detail.getLotInfo().getExtendPropC19(),asn.getUserField5());
    	}else if(WmsAsnGenType.YLRKD.equals(asn.getBillType().getCode()) || WmsAsnGenType.YLTLD.equals(asn.getBillType().getCode())){
    		String hql = "FROM WmsReservedOrder order WHERE order.code =:code ";
    		WmsReservedOrder reOrder = (WmsReservedOrder) commonDao.findByQueryUniqueResult(hql, "code",asn.getCustomerBill());
    		ledger.setReservedCode(reOrder.getCode());
    		WmsReservedOrderDetail wod = commonDao.load(WmsReservedOrderDetail.class, Long.valueOf(detail.getUserField1()));
    		ledger.setResProject(wod.getProject());
    		String moveType = "";
    		if(WmsAsnGenType.YLRKD.equals(asn.getBillType().getCode())){
    			moveType = reOrder.getYdlx();
    		}else{
    			if(WmsReservedOrderType.Z01.equals(reOrder.getYdlx())){
    				moveType = WmsReservedOrderType.Z02;
    			}
    			if(WmsReservedOrderType.Z03.equals(reOrder.getYdlx())){
    				moveType = WmsReservedOrderType.Z04;
    			}
    			if(WmsReservedOrderType.Z311.equals(reOrder.getYdlx())){
    				moveType = WmsReservedOrderType.Z312;
    			}
    		}
    		ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), moveType, 
    				asn.getBillType().getCode(), detail.getItem(), reOrder.getFactory(), asn.getSupplier().getCode(),
    				detail.getInventoryStatus(),detail.getLotInfo().getExtendPropC19(),asn.getUserField5());
    	}
    	ledger.setQuantity(receiveQty);
    	ledger.setBaseUnit(detail.getPackageUnit().getUnit());
    	commonDao.store(ledger);
    }
	/**生成发送给sap的单号以及报文*/
	public void genSendToSapLog() {
//		throw new BusinessException("接口暂时关闭台账信息先不回传");
		//暂时只放开收货的回传SAP
//		//收货类型先处理  由于取消需要用收货类型的凭证号，凭证号在传接口的时候才生成.
//		genSendToSapLog(TclWmsMoveType.ASNMOVETYPE);//收货的先处理
//		genSendToSapLog(TclWmsMoveType.CANCELRECEIVETYPE);
//		genSendToSapLog(TclWmsMoveType.QCMOVETYPE);
//		genSendToSapLog(TclWmsMoveType.QCBADMOVETYPE);
//		
////		genSendToSapLog(null);
		
		List<String> types = new ArrayList<String>(); //根据id顺序处理，因为有一张单子先收货，在取消，然后在收货，如果先全部传收货过去，数量会大于计划数量，sap处理不了
		
		types.add(TclWmsMoveType.ASNMOVETYPE);
		types.add(TclWmsMoveType.CANCELRECEIVETYPE);
		types.add(TclWmsMoveType.QCMOVETYPE);
		types.add(TclWmsMoveType.QCBADMOVETYPE);
		//同一个销售交货单要合并成一个传给SAP
		types.add(TclWmsMoveType.OUTDELIVERYMOVETYPE);
		
		genSendToSapLog(types);
		//20171026--接口放开了,传空值就查出所有没传给SAP的报文来处理
		genSendToSapLog(null);
	}
	
	private void genSendToSapLog(List<String> moveTypes) {
		
		String hql = "";
		List<TclWmsInventoryLedger> ledgers = null;
		if(moveTypes== null || moveTypes.isEmpty()) {
			
			 hql = "from TclWmsInventoryLedger ledger "
				  		+ " where ledger.genXmlFlag is false AND ledger.minTransDate <= sysdate "
				  		+ "order by ledger.id asc ";
			 ledgers = commonDao.findByQuery(hql);
		}
		else {
			  hql = "from TclWmsInventoryLedger ledger "
			  		+ " where ledger.genXmlFlag is false AND ledger.minTransDate <= sysdate and ledger.moveType in (:moveTypes) "
			  		+ "order by ledger.id asc ";
			  ledgers = commonDao.findByQuery(hql,new String[]{"moveTypes"},new Object[]{moveTypes});
		}
//		Map<String,List<TclWmsInventoryLedger>> map = new HashMap<String,List<TclWmsInventoryLedger>>();
//		for(TclWmsInventoryLedger ledger : ledgers) {
//			String key = ledger.getMoveType()+"###"+ledger.getWmsCode() + "###"+ledger.getId(); //加上ID，一行一个报文，防止出问题
//			if(!map.containsKey(key)) {
//				map.put(key, new ArrayList<TclWmsInventoryLedger>());
//			}
//			map.get(key).add(ledger);
//			
//		}
//		Set<String> keys = map.keySet();
//		for(String key : keys) { //一个key生成一个报文
//			createInventoryLedgerArrayLog(map.get(key));
//		}
		Map<String,List<TclWmsInventoryLedger>> map = new HashMap<String,List<TclWmsInventoryLedger>>();
		for(TclWmsInventoryLedger l : ledgers) {
			String key = "";
			if(TclWmsMoveType.OUTDELIVERYMOVETYPE.equals(l.getMoveType())){
				key = l.getMoveType()+"###"+l.getWmsCode() ;
				if(!map.containsKey(key)) {
					map.put(key, new ArrayList<TclWmsInventoryLedger>());
				}
				map.get(key).add(l);
			}else{
				List<TclWmsInventoryLedger> list = new ArrayList<TclWmsInventoryLedger>();
				list.add(l);
				createInventoryLedgerArrayLog(list);
			}
		}
		//同一个销售交货单要合并成一个传给SAP
		if(!map.isEmpty()){
			Set<String> keys = map.keySet();
			for(String key : keys) { //一个key生成一个报文
				createInventoryLedgerArrayLog(map.get(key));
			}
		}
	}
	/**创建报文传sap*/
	private void createInventoryLedgerArrayLog(List<TclWmsInventoryLedger> ledgers) {
		if(ledgers == null || ledgers.isEmpty()) {
			return;
		}
		TclWmsInventoryLedger l = ledgers.get(0);
		
		Wms2SapInventoryLedger[] array = new Wms2SapInventoryLedger[ledgers.size()];
		int i=0;
		String code = "";
		String taskType = "";
		String interfaceLogType="";
		DecimalFormat df = new DecimalFormat("0.#########");   
		/*收货移动类型*/
		if(TclWmsMoveType.ASNMOVETYPE.equals(l.getMoveType())){ //入库
			i=0;
		    code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.RK, 8);
			for(TclWmsInventoryLedger ledger : ledgers) {
				
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo().toString());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setLIFNR(ledger.getSupplierCode());
				wil.setSOBKZ(ledger.getInvType());
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setEBELN(ledger.getPoCode());
				wil.setEBELP(ledger.getPoLineNo().toString());
				wil.setINSMK(ledger.getInvStatus());
				wil.setVBELN_IM(ledger.getDoCode());
				wil.setVBELP_IM(ledger.getDoLineNo() == null ? "":ledger.getDoLineNo().toString());
				wil.setMEINS(ledger.getBaseUnit());
				wil.setTYPE_IM(ledger.getCreateType());
				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				
				
				
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_RECEIVEINFO;
			interfaceLogType= Wms2SapInterfaceLogType.RECEIVEINFO;
		}
		/* 取消收货移动类型*/
		else if(TclWmsMoveType.CANCELRECEIVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.RK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
//				String lineNo = CommonHelper.addCharBeforeStr((i+1)+"", 4, "0");
//				ledger.setLineNo(lineNo); //重新生成行号  0001 0002
//				dd
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setBWART(ledger.getMoveType());
				wil.setXBLNR(ledger.getPoCode());
				wil.setZEILE(ledger.getLineNo());
				wil.setMENGE(df.format(ledger.getQuantity()));
				
				
				TclWmsInventoryLedger oldLedger = commonDao.load(TclWmsInventoryLedger.class, ledger.getOldLedgerId()); //务必确保取消收货在收货报文生成之后执行。
				if(oldLedger==null) {
					throw new BusinessException("根据OLDID="+ledger.getOldLedgerId()+"未找到库存台账记录");
				}
				ledger.setWmsInCode(oldLedger.getCode());
				
				wil.setLFBNR(ledger.getWmsInCode()); //被取消的入库单的WMS凭证号码；
				wil.setSGTXT("取消收货");
				
				wil.setLFPOS(ledger.getWmsInCodeLineNo()); ////被取消的入库单的WMS凭证行号；

				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			
			taskType = InterfaceLogTaskType.SEND_CANCELRECEIVEINFO;
			interfaceLogType= Wms2SapInterfaceLogType.CANCELRECEIVEINFO;
		}
		//采购退货移动类型
		else if(TclWmsMoveType.PICKMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.CK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setEKGRP(ledger.getItem().getUserFieldV4());
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				if(ledger.getSapFactory() == null){
					wil.setWERKS(null);
				}else{
					wil.setWERKS(ledger.getSapFactory().getCode());
				}
				wil.setLGORT(ledger.getLocationCode());
				wil.setLIFNR(ledger.getSupplierCode());
				wil.setSOBKZ(ledger.getInvType());
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setRETPO("X");
				wil.setINSMK(ledger.getInvStatus());
				
				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_PICKCONFIRMINFO;
			interfaceLogType= Wms2SapInterfaceLogType.PICKCONFIRMINFO;
		}
		//质检转合格移动类型
		else if(TclWmsMoveType.QCMOVETYPE.equals(l.getMoveType()) || TclWmsMoveType.QCBADMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.ZJ, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
//				
//				String lineNo = CommonHelper.addCharBeforeStr((i+1)+"", 4, "0");
//				ledger.setLineNo(lineNo); //重新生成行号  0001 0002
				
				
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				if(ledger.getSapFactory() == null){
					wil.setWERKS(null);
				}else{
					wil.setWERKS(ledger.getSapFactory().getCode());
				}
				wil.setLGORT(ledger.getLocationCode());
				wil.setLIFNR(ledger.getSupplierCode());
				wil.setSOBKZ(ledger.getInvType());
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setUMLGO(ledger.getQcLocCode());
				wil.setMEINS(ledger.getBaseUnit());
				
				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_QCRECORDINFO;
			interfaceLogType= Wms2SapInterfaceLogType.QCRECORDINFO;
		}
		//生产订单发料移动类型
		else if(TclWmsMoveType.PRODUCTIONMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.CK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				
				wil.setSOBKZ(ledger.getInvType());
				if(ledger.getInvType() == null){
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setAUFNR(ledger.getProductionCode());
				wil.setRSNUM(ledger.getReservedCode());
				wil.setRSPOS(ledger.getResProject());
				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_PRODUCTIONINFO;
			interfaceLogType= Wms2SapInterfaceLogType.PRODUCTIONINFO;
		}
		//生产订单退料移动类型
		else if(TclWmsMoveType.PRDRETURNMOVETYPR.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.RK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo().toString());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setSOBKZ(ledger.getInvType());
				if(StringHelper.isNullOrEmpty(ledger.getInvType())){
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setAUFNR(ledger.getProductionCode());
				wil.setRSNUM(ledger.getReservedCode());
				wil.setRSPOS(ledger.getResProject());
				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_PRDRETURNINFO;
			interfaceLogType= Wms2SapInterfaceLogType.PRDRETURNINFO;
		}
		/**预留出库单 */
		else if(StringHelper.in(l.getMoveType(), new String[]{WmsReservedOrderType.Z01,WmsReservedOrderType.Z03,WmsReservedOrderType.Z311,WmsReservedOrderType.Z02,WmsReservedOrderType.Z04,WmsReservedOrderType.Z312})) {
		 
			i=0;
			if(StringHelper.in(l.getMoveType(), new String[]{WmsReservedOrderType.Z02,WmsReservedOrderType.Z04,WmsReservedOrderType.Z312})){
				code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.RK, 8);
			}else{
				code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.CK, 8);
			}
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setLIFNR(ledger.getSupplierCode());
				wil.setSOBKZ(ledger.getInvType());
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setRSNUM(ledger.getReservedCode());
				wil.setRSPOS(ledger.getResProject());
				if(ledger.getInvType() == null){//项目类别==标准  则不传供应商编码
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				array[i]=wil;
				i++;

				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_SHIPRESINFO;
			interfaceLogType= Wms2SapInterfaceLogType.RESINFO;
			
		}
		//其它出库单移动类型
		else if(TclWmsMoveType.OTHEROUTMOVETYPE.equals(l.getMoveType())
				|| TclWmsMoveType.PKCMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.CK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setSOBKZ(ledger.getInvType());
				if(ledger.getInvType() == null){
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				wil.setMENGE(df.format(ledger.getQuantity()));
				if(TclWmsMoveType.PKCMOVETYPE.equals(l.getMoveType())){
					wil.setKOSTL(ledger.getCostCenter());
					
				}
				if(StringHelper.in(ledger.getSapFactory().getCode(), new String[]{"1000","1100"})){
					wil.setPRCTR("0530M11000");
				}
				if(StringHelper.in(ledger.getSapFactory().getCode(), new String[]{"2000","2100"})){
					wil.setPRCTR("0530M22000");
				}
				
				array[i]=wil;
				i++;
	
				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_OTHEROUTINFO;
			interfaceLogType= Wms2SapInterfaceLogType.OTHEROUTINFO;
		}
		//其它入库单移动类型
		else if(TclWmsMoveType.OTHERINMOVETYPE.equals(l.getMoveType()) 
				|| TclWmsMoveType.PYRMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.RK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setSOBKZ(ledger.getInvType());
				if(ledger.getInvType() == null){
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				wil.setMENGE(df.format(ledger.getQuantity()));
				if(TclWmsMoveType.PYRMOVETYPE.equals(l.getMoveType())){
					wil.setKOSTL(ledger.getCostCenter());
					
				}
				//利润中心不要了，SAP自己根据工厂来写
//				if(StringHelper.in(ledger.getSapFactory().getCode(), new String[]{"1000","1100"})){
//					wil.setPRCTR("0530M11000");
//				}
//				if(StringHelper.in(ledger.getSapFactory().getCode(), new String[]{"2000","2100"})){
//					wil.setPRCTR("0530M22000");
//				}
				array[i]=wil;
				i++;
	
				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_OTHERININFO;
			interfaceLogType= Wms2SapInterfaceLogType.OTHERININFO;
		}
		//报废入库  报废出库
		else if(TclWmsMoveType.BFINMOVETYPR.equals(l.getMoveType()) || TclWmsMoveType.BFOUTMOVETYPE.equals(l.getMoveType())){
			
			i=0;
			code ="";
			if(TclWmsMoveType.BFINMOVETYPR.equals(l.getMoveType())) {
				code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.RK, 8);
			}
			else if(TclWmsMoveType.BFOUTMOVETYPE.equals(l.getMoveType())){
				code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.CK, 8);
			}
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				
			
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setSOBKZ(ledger.getInvType());
				if(ledger.getInvType() == null){
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setKOSTL(ledger.getCostCenter());
				
				array[i]=wil;
				i++;
	
				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			
			}
			taskType = InterfaceLogTaskType.SEND_BFOUTORININFO;
			interfaceLogType= Wms2SapInterfaceLogType.BFOUTORININFO;
		}
		//库存调拨 内外销   库存调拨两个仓库
		else if(TclWmsMoveType.KNDBMOVETYPE.equals(l.getMoveType()) || TclWmsMoveType.DBCKMOVETYPE.equals(l.getMoveType()) || TclWmsMoveType.VMIMOVETYPE.equals(l.getMoveType()) || TclWmsMoveType.ZGMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.DB, 8);
			 
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				if(TclWmsMoveType.KNDBMOVETYPE.equals(l.getMoveType())){
					wil.setBWART("311");
				}else{
					wil.setBWART(ledger.getMoveType());
				}
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				if(ledger.getInvType() == null){
					wil.setLIFNR(null);
				}else{
					wil.setLIFNR(ledger.getSupplierCode());
				}
				wil.setSOBKZ(ledger.getInvType());
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setUMWRK(ledger.getRecFatoryCode());
				wil.setUMLGO(ledger.getRecWarehouse());
				
				array[i]=wil;
				i++;
	
				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
				
			}
			if(TclWmsMoveType.KNDBMOVETYPE.equals(l.getMoveType())){
				taskType = InterfaceLogTaskType.SEND_KNDBINFO;
				interfaceLogType= Wms2SapInterfaceLogType.KNDBINFO;
			}else{
				taskType = InterfaceLogTaskType.SEND_DBCKINFO;
				interfaceLogType= Wms2SapInterfaceLogType.DBCKINFO;
			}
		}
		//销售交货单
		else if(TclWmsMoveType.OUTDELIVERYMOVETYPE.equals(l.getMoveType())){
			i=0;
			code = sequenceGenerater.generateSequence(InventoryLedgerCodeType.CK, 8);
			
			for(TclWmsInventoryLedger ledger : ledgers) {
				Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
				wil.setType(ledger.getBillType());
				wil.setFRBNR(code);
				wil.setBLDAT(sdf.format(ledger.getCurDate()));
				wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
				wil.setZEILE(ledger.getLineNo());
				wil.setBWART(ledger.getMoveType());
				wil.setMATNR(ledger.getItem().getCode());
				wil.setWERKS(ledger.getSapFactory().getCode());
				wil.setLGORT(ledger.getLocationCode());
				wil.setLIFNR(ledger.getSupplierCode());
				wil.setSOBKZ(ledger.getInvType());
				wil.setMENGE(df.format(ledger.getQuantity()));
				wil.setINSMK(ledger.getInvStatus());
				wil.setVBELN_IM(ledger.getDoCode());
				wil.setVBELP_IM(ledger.getDoLineNo());
				
				array[i]=wil;
				i++;
	
				ledger.setCode(code);
				ledger.setGenXmlFlag(Boolean.TRUE);
				commonDao.store(ledger);
			}
			taskType = InterfaceLogTaskType.SEND_XSJHDINFO;
			interfaceLogType= Wms2SapInterfaceLogType.XSJHDINFO;
		}
		
		
		Wms2SapInventoryLedgerArray la = new Wms2SapInventoryLedgerArray();
		la.setWms2SapInventoryLedgers(array);
		String xml = XmlObjectConver.toXML(la);
		interfaceLogManager.createWmsToSapInterfaceLog(taskType,interfaceLogType, xml,null, code);
		
		
		
	}
	
	
	
	
	
	private void createInventoryLedgerLog(TclWmsInventoryLedger ledger){
		if(TclWmsMoveType.ASNMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo().toString());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setEBELN(ledger.getPoCode());
			wil.setEBELP(ledger.getPoLineNo().toString());
			wil.setINSMK(ledger.getInvStatus());
			wil.setVBELN_IM(ledger.getDoCode());
			wil.setVBELP_IM(ledger.getDoLineNo() == null ? "":ledger.getDoLineNo().toString());
			wil.setMEINS(ledger.getBaseUnit());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_RECEIVEINFO, Wms2SapInterfaceLogType.RECEIVEINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.CANCELRECEIVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setBWART(ledger.getMoveType());
			wil.setXBLNR(ledger.getPoCode());
			wil.setZEILE(ledger.getLineNo());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setLFBNR(ledger.getSapCode());
			wil.setSGTXT("取消收货");
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_CANCELRECEIVEINFO, Wms2SapInterfaceLogType.CANCELRECEIVEINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.PICKMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setEKGRP(ledger.getItem().getUserFieldV4());
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			if(ledger.getSapFactory() == null){
				wil.setWERKS(null);
			}else{
				wil.setWERKS(ledger.getSapFactory().getCode());
			}
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setRETPO("X");
			wil.setINSMK(ledger.getInvStatus());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_PICKCONFIRMINFO, Wms2SapInterfaceLogType.PICKCONFIRMINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.QCMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			if(ledger.getSapFactory() == null){
				wil.setWERKS(null);
			}else{
				wil.setWERKS(ledger.getSapFactory().getCode());
			}
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setUMLGO(ledger.getQcLocCode());
			wil.setMEINS(ledger.getBaseUnit());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_QCRECORDINFO, Wms2SapInterfaceLogType.QCRECORDINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.PRODUCTIONMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setAUFNR(ledger.getProductionCode());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_PRODUCTIONINFO, Wms2SapInterfaceLogType.PRODUCTIONINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.PRDRETURNMOVETYPR.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo().toString());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setAUFNR(ledger.getProductionCode());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_PRDRETURNINFO, Wms2SapInterfaceLogType.PRDRETURNINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(WmsPickticketBillTypeCode.YLCKD.equals(ledger.getBillType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setRSNUM(ledger.getReservedCode());
			wil.setRSPOS(ledger.getResProject());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_SHIPRESINFO, Wms2SapInterfaceLogType.RESINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.OTHEROUTMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setKOSTL(ledger.getSapFactory().getCode());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_OTHEROUTINFO, Wms2SapInterfaceLogType.OTHEROUTINFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.OTHERINMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setKOSTL(ledger.getSapFactory().getCode());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_OTHERININFO, Wms2SapInterfaceLogType.OTHERININFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.BFINMOVETYPR.equals(ledger.getMoveType()) || TclWmsMoveType.BFOUTMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setKOSTL(ledger.getCostCenter());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_BFOUTORININFO, Wms2SapInterfaceLogType.BFOUTORININFO, xml, ledger.getId(),ledger.getCode());
		}
		else if(TclWmsMoveType.KNDBMOVETYPE.equals(ledger.getMoveType()) || TclWmsMoveType.DBCKMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setUMWRK(ledger.getRecFatoryCode());
			wil.setUMLGO(ledger.getRecWarehouse());
			String xml = XmlObjectConver.toXML(wil);
			if(TclWmsMoveType.KNDBMOVETYPE.equals(ledger.getMoveType())){
				interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_KNDBINFO, Wms2SapInterfaceLogType.KNDBINFO, xml, ledger.getId(),ledger.getCode());
			}else{
				interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_DBCKINFO, Wms2SapInterfaceLogType.DBCKINFO, xml, ledger.getId(),ledger.getCode());
			}
		}
		else if(TclWmsMoveType.OUTDELIVERYMOVETYPE.equals(ledger.getMoveType())){
			Wms2SapInventoryLedger wil = new Wms2SapInventoryLedger();
			wil.setType(ledger.getBillType());
			wil.setFRBNR(ledger.getCode());
			wil.setBLDAT(sdf.format(ledger.getCurDate()));
			wil.setBUDAT(sdf.format(ledger.getReceiveDate()));
			wil.setZEILE(ledger.getLineNo());
			wil.setBWART(ledger.getMoveType());
			wil.setMATNR(ledger.getItem().getCode());
			wil.setWERKS(ledger.getSapFactory().getCode());
			wil.setLGORT(ledger.getLocationCode());
			wil.setLIFNR(ledger.getSupplierCode());
			wil.setSOBKZ(ledger.getInvType());
			wil.setMENGE(ledger.getQuantity().toString());
			wil.setINSMK(ledger.getInvStatus());
			wil.setVBELN_IM(ledger.getDoCode());
			wil.setVBELP_IM(ledger.getDoLineNo());
			String xml = XmlObjectConver.toXML(wil);
			interfaceLogManager.createWmsToSapInterfaceLog(InterfaceLogTaskType.SEND_XSJHDINFO, Wms2SapInterfaceLogType.XSJHDINFO, xml, ledger.getId(),ledger.getCode());
		}
	}
	
	public TclWmsInventoryLedger getReceiveDate(TclWmsInventoryLedger ledger){
//		String hql = " SELECT MAX(TO_CHAR(d.accountCloseTime,'YYYY-MM-DD HH24:mm')),MIN(TO_CHAR(d.accountCloseTime,'YYYY-MM-DD HH24:mm')) FROM WmsAccountCloseDay d";
		String hql = " FROM WmsAccountCloseDay d ";
		List<WmsAccountCloseDay> closeDays = commonDao.findByQuery(hql);
		//账单关闭日 改成时间段 同时在时间段之内的当时不传接口，结束时间之后再传
    	if(closeDays.isEmpty()){
    		ledger.setCurDate(new Date());
        	ledger.setReceiveDate(new Date());
        	ledger.setMinTransDate(new Date());
    	}else{
    		for(WmsAccountCloseDay closeDay : closeDays){
        		if(closeDay.getAccountCloseTime().before(new Date()) && closeDay.getMaxAccountCloseTime().after(new Date())){
        			ledger.setCurDate(new Date());
                	ledger.setReceiveDate(closeDay.getMaxAccountCloseTime());
                	ledger.setMinTransDate(closeDay.getMaxAccountCloseTime());
        		}else{
        			ledger.setCurDate(new Date());
                	ledger.setReceiveDate(new Date());
                	ledger.setMinTransDate(new Date());
        		}
        	}
    	}
		return ledger;
	}
	/**
	 * 获取下一个月的第一天
	 * @return
	 */
	public static Date nextMonthFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }
	
	public void sendCancelReceiveInfo2SAP(WmsASNDetail detail,Double cancleQty){
//		asn = commonDao.load(WmsASN.class, asn.getId());
//        for (WmsASNDetail detail : asn.getDetails()) {
			if(WmsAsnGenType.JITRKD.equals(detail.getAsn().getBillType().getCode()) 
					|| WmsAsnGenType.BHRKD.equals(detail.getAsn().getBillType().getCode())
					|| WmsAsnGenType.DBRKD.equals(detail.getAsn().getBillType().getCode())
					|| WmsAsnGenType.THRKD.equals(detail.getAsn().getBillType().getCode())){//调拨入库的数据在调拨出库的时候就已经传了
				return;
			}
//			if(WmsAsnGenType.ZCRKD.equals(detail.getAsn().getBillType().getCode()) && WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(detail.getItem().getUserFieldV2())){
//				return;
//			}
        	WmsTransportOrderDetail tod = detail.getTransportOrderDetail();
        	if(tod == null){
        		return;
        	}
        	WmsDeliveryOrderDetail dod = tod.getDeliveryOrderDetail();
        	PurchaseOrderDetail pod = dod.getPurchaseOrderDetail();
        	//取消收货要给SAP传收货凭证
        	String hql = "FROM TclWmsInventoryLedger l WHERE l.wmsCode =:wmsCode AND l.moveType =:moveType AND l.item.code =:itemCode " +
        			" AND l.doCode=:doCode order by l.lineNo desc ";
        	List<TclWmsInventoryLedger> recLedgers = commonDao.findByQuery(hql, new String[]{"wmsCode","itemCode","moveType","doCode"}, 
        			new Object[]{detail.getAsn().getCode(),detail.getItem().getCode(),TclWmsMoveType.ASNMOVETYPE,dod.getDeliveryOrder().getCode()}); 
        	
        	Double mustCancelqty = cancleQty; //需要取消的数量
        	for(TclWmsInventoryLedger l : recLedgers) { //循环取消
        		if(mustCancelqty<=0d) {
        			break;
        		}
        		Double oldLedgerCanCancelQty = l.getQuantity() - l.getCancelQuantity(); //老ledger可以取消的数量
        		if(oldLedgerCanCancelQty<=0d) {
        			continue; //全部被取消的明细不处理
        		}
        		if(oldLedgerCanCancelQty>=mustCancelqty) {//1张就够了
        			TclWmsInventoryLedger t_ledger = this.getLedger(detail);
        			t_ledger.setPoCode(pod.getPurchaseOrder().getCode());
        			t_ledger.setSapFactory(pod.getPurchaseOrder().getSapFactory());
        			t_ledger.setWmsInCode(l.getCode()); // 先记录
        			t_ledger.setOldLedgerId(l.getId());
        			t_ledger.setWmsInCodeLineNo(l.getLineNo());
        			t_ledger.setInvType(l.getInvType());
        			t_ledger.setQuantity(mustCancelqty);
        			l.setCancelQuantity(l.getCancelQuantity()+mustCancelqty);
        			
        			commonDao.store(l);
        			commonDao.store(t_ledger);
        			
        			mustCancelqty = 0d;
        		}
        		else {
        			TclWmsInventoryLedger t_ledger = this.getLedger(detail);
        			t_ledger.setPoCode(pod.getPurchaseOrder().getCode());
        			t_ledger.setSapFactory(pod.getPurchaseOrder().getSapFactory());
        			t_ledger.setWmsInCode(l.getCode());
        			t_ledger.setOldLedgerId(l.getId());
        			t_ledger.setWmsInCodeLineNo(l.getLineNo());
        			t_ledger.setInvType(l.getInvType());
        			t_ledger.setQuantity(oldLedgerCanCancelQty);
        			l.setCancelQuantity(l.getCancelQuantity()+oldLedgerCanCancelQty);
        			commonDao.store(l);
        			commonDao.store(t_ledger);
        			
        			mustCancelqty = mustCancelqty - oldLedgerCanCancelQty;
        		}
        	}
        	if(mustCancelqty>0d) {
        		throw new BusinessException("接口数量取消失败，剩余"+mustCancelqty+"无法取消");
        	}
//        }
	}
	
	private TclWmsInventoryLedger getLedger(WmsASNDetail asnDetail) {
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
    	ledger.setWarehouse(asnDetail.getAsn().getWarehouse());
    	ledger.setWmsCode(asnDetail.getAsn().getCode());
    	ledger.setBillType(asnDetail.getAsn().getBillType().getCode());
    	ledger.setItem(asnDetail.getItem());
    	
    	 
    	
    	ledger = getReceiveDate(ledger);
//    	ledger.setPoCode(pod.getPurchaseOrder().getCode());
    	ledger.setMoveType(TclWmsMoveType.CANCELRECEIVETYPE);
//    	ledger.setQuantity(cancleQty);
    	
    	ledger.setLineNo(this.genLedgerLineNo(ledger.getWmsCode(), ledger.getMoveType()));//生成报文时会重新生成
    	
    	return ledger;
	}
	
	public WmsSapFactory getSapFactory(String code){
		String hql = "FROM WmsSapFactory f WHERE f.code =:code ";
		return (WmsSapFactory) commonDao.findByQueryUniqueResult(hql, "code", code);
	}
	
	public void sendQcRecordInfo2SAP(WmsASNDetail detail,String qcStatus,Double QcQty){
		WmsASN asn = commonDao.load(WmsASN.class, detail.getAsn().getId());
		if(WmsAsnGenType.JITRKD.equals(asn.getBillType().getCode()) 
				|| WmsAsnGenType.BHRKD.equals(asn.getBillType().getCode())
				|| WmsAsnGenType.DBRKD.equals(asn.getBillType().getCode())
				|| WmsAsnGenType.THRKD.equals(asn.getBillType().getCode())){
			return;
		}
//		if(WmsAsnGenType.ZCRKD.equals(asn.getBillType().getCode()) && WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(detail.getItem().getUserFieldV2())){
//			return;
//		}
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
		WmsSapFactory factory = getSapFactory(detail.getLotInfo().getExtendPropC10());
		if("不良品".equals(qcStatus)){
			ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.QCBADMOVETYPE,
					asn.getBillType().getCode(), detail.getItem(), factory, detail.getLotInfo().getSupplierCode(),
					qcStatus,detail.getLotInfo().getExtendPropC19(),detail.getLotInfo().getExtendPropC8());
			//质检成不良品需要传对应的坏料库存地点,是W开头的也是传对应的坏料库存地点
			ledger.setQcLocCode(this.getSapWarehouseCode(qcStatus, detail.getLotInfo().getExtendPropC8(), null));
		}else{
			ledger = this.genTclWmsInventoryLedger(ledger, asn.getWarehouse(), asn.getCode(), TclWmsMoveType.QCMOVETYPE, 
					asn.getBillType().getCode(), detail.getItem(), factory, detail.getLotInfo().getSupplierCode(),
					qcStatus,detail.getLotInfo().getExtendPropC19(),detail.getLotInfo().getExtendPropC8());
		}
    	
    	ledger.setQuantity(QcQty);
    	ledger.setBaseUnit(detail.getPackageUnit().getUnit());
    	commonDao.store(ledger);
    	if("不良品".equals(qcStatus)){//如果质检成不良品，传给SAP两条台账记录，一个正常的移动类型323的合格库存地点到不良品库存地点，一个移动类型321，走不良品库存地点到不良品库存地点
    		TclWmsInventoryLedger newLedger = new TclWmsInventoryLedger();
    		BeanUtils.copyEntity(newLedger, ledger);
    		newLedger.setId(null);
    		newLedger.setMoveType(TclWmsMoveType.QCMOVETYPE);
    		newLedger.setLocationCode(ledger.getQcLocCode());
    		commonDao.store(newLedger);
    	}
	}
	
	public void sendProductionShipInfo(Long detailId,WmsInventory inventory,Double pickQty,Map<Long,Double> map){
		WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, detailId);
		WmsPickTicket pick = commonDao.load(WmsPickTicket.class, detail.getPickTicket().getId());
		
		//库存地点
    	String kcdd = inventory.getItemKey().getLotInfo().getExtendPropC19();
		
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
		ledger.setWarehouse(pick.getWarehouse());
		ledger.setBillType(pick.getBillType().getCode());
		ledger.setWmsCode(pick.getCode());
		ledger = getReceiveDate(ledger);
		
		//供应商原则上用库存里面的比较好，但是传给SAP时由于 前期库存都是按虚拟供应商导入的，传虚拟供应商SAP处理不了，所以库存上供应商如果是虚拟 就取拣货单上的
		String supplierCode = "";
    	if("XN".equals(inventory.getItemKey().getLotInfo().getSupplierCode())){
    		if(pick.getSupplier() != null){
    			supplierCode = pick.getSupplier().getCode();
    		}else{
    			supplierCode = inventory.getItemKey().getLotInfo().getSupplierCode();
    		}
    	}else{
    		supplierCode = inventory.getItemKey().getLotInfo().getSupplierCode();
    	}
    	//特殊库存
    	if(WmsFactoryXmlb.BZ.equals(inventory.getItemKey().getLotInfo().getExtendPropC8())){
    		ledger.setInvType(null);
    	}else{
    		ledger.setInvType("K");
    	}
    	WmsSapFactory factory = getSapFactory(pick.getUserField1());
    	ledger.setSapFactory(factory);
		//生产订单和预留单发料时，与拣货单明细属于多对一的情况，事先把要回传的工单/预留单的明细ID放到map中，此处遍历map传给SAP各工单/预留单的明细发的数量
    	if(WmsPickticketGenType.SCLLD.equals(pick.getBillType().getCode())){
    		Set<Long> keys = map.keySet();
    		for(Long key : keys) {//一个key生成一个报文
    			TclWmsInventoryLedger poLedger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
    			ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, key);//工单明细
    			poLedger = this.genTclWmsInventoryLedger(poLedger, pick.getWarehouse(), pick.getCode(), TclWmsMoveType.PRODUCTIONMOVETYPE, 
    					pick.getBillType().getCode(), detail.getItem(), pod.getProductionOrder().getFactory(),supplierCode,inventory.getStatus(), 
    					kcdd,inventory.getItemKey().getLotInfo().getExtendPropC8());
    			poLedger.setProductionCode(pick.getRelatedBill1());
    			poLedger.setReservedCode(pod.getReservedOrder());
    			poLedger.setResProject(pod.getReservedProject());
    			if(pickQty<=0){
    				break;
    			}
    			if(pickQty>=map.get(key)){
    				poLedger.setQuantity(map.get(key));
    				pickQty -= map.get(key);
    			}else{
    				poLedger.setQuantity(pickQty);
    				pickQty =0D;
    			}
    			commonDao.store(poLedger);
    		} 
    		return;
    	}
    	if(WmsPickticketBillTypeCode.YLCKD.equals(pick.getBillType().getCode())){
    		Set<Long> keys = map.keySet();
    		for(Long key : keys) {//一个key生成一个报文
    			TclWmsInventoryLedger reLedger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
    			WmsReservedOrderDetail reservedOrderDetail = commonDao.load(WmsReservedOrderDetail.class, key);
    			reLedger = this.genTclWmsInventoryLedger(reLedger, pick.getWarehouse(), pick.getCode(), reservedOrderDetail.getReservedOrder().getYdlx(), 
    					pick.getBillType().getCode(), detail.getItem(), factory,supplierCode,inventory.getStatus(),
    					kcdd,inventory.getItemKey().getLotInfo().getExtendPropC8());
    			reLedger.setResProject(reservedOrderDetail.getProject());
    			reLedger.setReservedCode(reservedOrderDetail.getReservedOrder().getCode());
    			if(pickQty<=0){
    				break;
    			}
    			if(pickQty>=map.get(key)){
    				reLedger.setQuantity(map.get(key));
    				pickQty -= map.get(key);
    			}else{
    				reLedger.setQuantity(pickQty);
    				pickQty =0D;
    			}
    			commonDao.store(reLedger);
    		}
    		return;
    	}
    	if(WmsPickticketBillTypeCode.QTCKD.equals(pick.getBillType().getCode())){
    		ledger.setMoveType(TclWmsMoveType.OTHEROUTMOVETYPE);
    	}
    	if( WmsPickticketBillTypeCode.PKCKD.equals(pick.getBillType().getCode())){
    		ledger.setMoveType(TclWmsMoveType.PKCMOVETYPE);
    		ledger.setCostCenter(pick.getUserField4());
    	}
    	if(WmsPickticketBillTypeCode.BFCKD.equals(pick.getBillType().getCode())){
    		ledger.setMoveType(TclWmsMoveType.BFOUTMOVETYPE);
    		ledger.setCostCenter(pick.getUserField4());
    	}
    	if(WmsPickticketBillTypeCode.DBCKD.equals(pick.getBillType().getCode())){
    		if(WarehouseEnumeration.VMI.equals(pick.getCustomer().getWarehouse().getCode())){//自管仓调拨出库到VMI
    			ledger.setMoveType(TclWmsMoveType.ZGMOVETYPE);
        		ledger.setRecFatoryCode(pick.getUserField2());
        		ledger.setRecWarehouse(this.getSapWarehouseCode(inventory.getStatus(), WmsFactoryXmlb.JS,null));
        		ledger.setInvType("K");//调拨到VMI仓 特殊库存直接传"K"寄售
    		}else{//VMI调拨出库到自管仓或自管仓之间调拨
    			if(WarehouseEnumeration.VMI.equals(pick.getWarehouse().getCode())){
    				ledger.setMoveType(TclWmsMoveType.VMIMOVETYPE);
        			ledger.setInvType("K");//涉及到411/412的调拨特殊库存都传K
            		ledger.setRecFatoryCode(pick.getUserField2());
            		ledger.setRecWarehouse(this.getSapWarehouseCode(inventory.getStatus(), WmsFactoryXmlb.BZ,null));
    			}else{
    				ledger.setMoveType(TclWmsMoveType.DBCKMOVETYPE);
    				ledger.setRecFatoryCode(pick.getUserField2());
            		ledger.setRecWarehouse(this.getSapWarehouseCode(inventory.getStatus(), inventory.getItemKey().getLotInfo().getExtendPropC8(),null));
    			}
    		}
    	}
    	if(WmsPickticketBillTypeCode.TGYSCK.equals(pick.getBillType().getCode())){
    		ledger.setMoveType(TclWmsMoveType.PICKMOVETYPE);
    	}
    	ledger.setLineNo(this.genLedgerLineNo(ledger.getWmsCode(), ledger.getMoveType()));
    	ledger.setItem(detail.getItem());
    	
    	if(!StringHelper.isNullOrEmpty(kcdd) && kcdd.startsWith("W")){
    		ledger.setLocationCode(kcdd);
    	}else{
    		ledger.setLocationCode(this.getSapWarehouseCode(inventory.getStatus(), inventory.getItemKey().getLotInfo().getExtendPropC8(),kcdd));
    	}
    	if("待检".equals(inventory.getStatus())){
    		ledger.setInvStatus("X");
    	}else{
    		ledger.setInvStatus(null);
    	}
    	
    	ledger.setSupplierCode(supplierCode);
    	
    	ledger.setQuantity(pickQty);
    	commonDao.store(ledger);
	}
	/**
	 * 库存调拨--一个仓库两个工厂之间的调拨
	 */
	public void sendChangeTypeInfo(WmsInventory inventory,Double newFactoryQty,String newFactoryCode,String wareHouseCode){
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
		//库存地点
		String kcdd = inventory.getItemKey().getLotInfo().getExtendPropC19();
		
		String wmsCode = sequenceGenerater.generateSequence(InventoryLedgerCodeType.KJDB, 8);//内外销调拨没有WMS对应的业务单据号，生成一个流水号
		WmsSapFactory factory = getSapFactory(inventory.getItemKey().getLotInfo().getExtendPropC10());
		
		ledger = this.genTclWmsInventoryLedger(ledger, inventory.getWarehouse(), wmsCode, TclWmsMoveType.DBCKMOVETYPE, 
				WmsPickticketBillTypeCode.KNDBD, inventory.getItem(), factory, inventory.getItemKey().getLotInfo().getSupplierCode(),
				inventory.getStatus(),kcdd,inventory.getItemKey().getLotInfo().getExtendPropC8());
		//库存地点按标准寄售来判断了，所以VMI到自管仓，收货地点就是标准的，自管仓到VMI收货库存地点就是寄售
		String recInvType = "";
		
		//如果库存上的仓库和传过来的仓库不同,表明做了库间调拨
		if(inventory.getWarehouse().getCode().equals(WarehouseEnumeration.VMI) && !inventory.getWarehouse().getCode().equals(wareHouseCode)){//如果移出仓库为VMI，VMI库存转自管库存
			ledger.setMoveType(TclWmsMoveType.VMIMOVETYPE);
			ledger.setInvType("K");
			recInvType=WmsFactoryXmlb.BZ;
		}
		if(StringHelper.in(inventory.getWarehouse().getCode(), new String[]{WarehouseEnumeration.BX,WarehouseEnumeration.XYJ}) 
				&&WarehouseEnumeration.VMI.equals(wareHouseCode)){//如果移出仓库为洗衣机或者冰箱,自管库存转VMI库存
			ledger.setMoveType(TclWmsMoveType.ZGMOVETYPE);
			ledger.setInvType("K");
			recInvType=WmsFactoryXmlb.JS;
		}
    	ledger.setQuantity(newFactoryQty);
    	ledger.setRecFatoryCode(newFactoryCode);
    	
    	if(!StringHelper.isNullOrEmpty(kcdd) && kcdd.startsWith("W")){
    		ledger.setRecWarehouse(kcdd);
    	}else{
    		//内外销调拨、VMI和自管仓之间的调拨都是走这一个方法
    		//如果是内外销调拨收货库存地点按照库存的来
    		//仓库间的调拨收货库存地点看调拨入的仓库
    		if(StringHelper.isNullOrEmpty(recInvType)){
    			ledger.setRecWarehouse(this.getSapWarehouseCode(inventory.getStatus(), inventory.getItemKey().getLotInfo().getExtendPropC8(),kcdd));
    		}else{
    			ledger.setRecWarehouse(this.getSapWarehouseCode(inventory.getStatus(), recInvType,kcdd));
    		}
    	}
    	commonDao.store(ledger);
	}
	/**生产订单入库JIT下线出库 需要同时传入库信息给SAP*/
	public void shipJITDownLine(WmsInventory inventory,WmsTask task,Double qty){
		ProductionOrderDetail pod = commonDao.load(ProductionOrderDetail.class, task.getProductionDetailId());
		if(pod==null){
			throw new BusinessException("未找到生产订单明细");
		}
		
		//库存地点
		String kcdd = inventory.getItemKey().getLotInfo().getExtendPropC19();
		
		//出库的信息
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
		
		ledger = this.genTclWmsInventoryLedger(ledger, inventory.getWarehouse(), task.getWorkDoc().getCode(), TclWmsMoveType.PRODUCTIONMOVETYPE, 
				WmsPickticketBillTypeCode.SCLLD, pod.getItem(), pod.getProductionOrder().getFactory(),
				inventory.getItemKey().getLotInfo().getSupplierCode(),inventory.getStatus(),kcdd,inventory.getItemKey().getLotInfo().getExtendPropC8());
    	
		ledger.setProductionCode(pod.getProductionOrder().getCode());
		ledger.setReservedCode(pod.getReservedOrder());
		ledger.setResProject(pod.getReservedProject());
    	
    	ledger.setQuantity(qty);
    	commonDao.store(ledger);
//    	WmsWorkDoc doc = task.getWorkDoc();
    	//JIT下线料 改成入库的时候回传给SAP了，此处注掉
//    	if(WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(doc.getUserField3())){//JIT下线结算需要给sap传一入一出信息,JIT上线结算不需要传入库信息
//    		//入库信息
//        	TclWmsInventoryLedger inLedger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
//        	String hql = "FROM WmsJITDownLineRecord r WHERE r.detail.item.id =:itemId AND r.detail.receivedQty-r.backQuantity > 0 AND r.asnCode =:asnCode ";
//        	List<WmsJITDownLineRecord> records = commonDao.findByQuery(hql, new String[]{"itemId","asnCode"}, new Object[]{task.getItem().getId(),inventory.getItemKey().getLotInfo().getExtendPropC7()});
//        	Double backQuantity = qty; //回传的数量
//        	for(WmsJITDownLineRecord record : records){
//        		if(backQuantity<=0d) {
//        			break;
//        		}
//        		Double adjustQty = Arith.sub(record.getDetail().getReceivedQty(),record.getBackQuantity());
//        		//如果剩余没回传的数量大于等于此次回传的数量一次就行
//        		if(Arith.sub(adjustQty,backQuantity) >=0 ){
//        			WmsASNDetail detail = record.getDetail();
//        			WmsTransportOrderDetail tod = detail.getTransportOrderDetail();
//                	
//                	WmsDeliveryOrderDetail dod = tod.getDeliveryOrderDetail();
//                	PurchaseOrderDetail orderDetail = dod.getPurchaseOrderDetail();
//                	inLedger = this.genTclWmsInventoryLedger(inLedger, detail.getAsn().getWarehouse(), detail.getAsn().getCode(), TclWmsMoveType.ASNMOVETYPE, detail.getAsn().getBillType().getCode(), detail.getItem(), orderDetail.getFactory(), detail.getAsn().getSupplier().getCode(),inventory.getStatus(),kcdd);
//                	inLedger.setPoCode(orderDetail.getPurchaseOrder().getCode());
//                	inLedger.setPoLineNo(orderDetail.getEbelp());
//                	if(WmsFactoryXmlb.BZ.equals(orderDetail.getPstyp())){
//                		inLedger.setInvType(null);
//                	}else{
//                		inLedger.setInvType("K");
//                	}
//                	//交货单的创建方式：判断依据--如果交货单的SAP交货单号SapCode不为空且和交货单编码CODE一致 则接口传输过来  SAP创建，
//                	//否则交货单的SAP交货单号SapCode为空或者SAP交货单号与交货单编码不一致，则WMS创建
//                	if(!StringHelper.isNullOrEmpty(dod.getDeliveryOrder().getSapCode()) && dod.getDeliveryOrder().getSapCode().equals(dod.getDeliveryOrder().getCode())){
//                		inLedger.setCreateType("S");
//                	}
//                	if(StringHelper.isNullOrEmpty(dod.getDeliveryOrder().getSapCode()) || !dod.getDeliveryOrder().getSapCode().equals(dod.getDeliveryOrder().getCode())){
//                		inLedger.setCreateType("W");
//                	}
//                	inLedger.setDoCode(dod.getDeliveryOrder().getCode());
//                	inLedger.setDoLineNo(dod.getDeliveryOrder().getProject());
//                	inLedger.setQuantity(backQuantity);
//                	inLedger.setBaseUnit(detail.getPackageUnit().getUnit());
//                	commonDao.store(inLedger);
//        			record.setBackQuantity(record.getBackQuantity()+backQuantity);
//        			commonDao.store(record);
//        			backQuantity = 0D;
//        		}else{
//        			WmsASNDetail detail = record.getDetail();
//        			WmsTransportOrderDetail tod = detail.getTransportOrderDetail();
//                	
//                	WmsDeliveryOrderDetail dod = tod.getDeliveryOrderDetail();
//                	PurchaseOrderDetail orderDetail = dod.getPurchaseOrderDetail();
//                	inLedger = this.genTclWmsInventoryLedger(inLedger, detail.getAsn().getWarehouse(), detail.getAsn().getCode(), TclWmsMoveType.ASNMOVETYPE, detail.getAsn().getBillType().getCode(), detail.getItem(), orderDetail.getFactory(), detail.getAsn().getSupplier().getCode(),inventory.getStatus(),kcdd);
//                	inLedger.setPoCode(orderDetail.getPurchaseOrder().getCode());
//                	inLedger.setPoLineNo(orderDetail.getEbelp());
//                	if(WmsFactoryXmlb.BZ.equals(orderDetail.getPstyp())){
//                		inLedger.setInvType(null);
//                	}else{
//                		inLedger.setInvType("K");
//                	}
//                	//交货单的创建方式：判断依据--如果交货单的SAP交货单号SapCode不为空且和交货单编码CODE一致 则接口传输过来  SAP创建，
//                	//否则交货单的SAP交货单号SapCode为空或者SAP交货单号与交货单编码不一致，则WMS创建
//                	if(!StringHelper.isNullOrEmpty(dod.getDeliveryOrder().getSapCode()) && dod.getDeliveryOrder().getSapCode().equals(dod.getDeliveryOrder().getCode())){
//                		inLedger.setCreateType("S");
//                	}
//                	if(StringHelper.isNullOrEmpty(dod.getDeliveryOrder().getSapCode()) || !dod.getDeliveryOrder().getSapCode().equals(dod.getDeliveryOrder().getCode())){
//                		inLedger.setCreateType("W");
//                	}
//                	inLedger.setDoCode(dod.getDeliveryOrder().getCode());
//                	inLedger.setDoLineNo(dod.getDeliveryOrder().getProject());
//                	inLedger.setQuantity(adjustQty);
//                	inLedger.setBaseUnit(detail.getPackageUnit().getUnit());
//                	commonDao.store(inLedger);
//                	record.setBackQuantity(record.getBackQuantity()+adjustQty);
//                	commonDao.store(record);
//        			backQuantity = backQuantity - adjustQty;
//        			
//        		}
//        	}
//    	}
    	
	}
	/***库存日结用*/
	private String getSapWarehouCodeByXmlb(String inventoryStatus,String xmlb,String kcdd) {
		String sapWarehouseCode="";
		if(!StringHelper.isNullOrEmpty(kcdd) && kcdd.startsWith("W")){
			return kcdd;
		}
		if("2".equals(xmlb)) { //VMI仓  寄售
			if("不良品".equals(inventoryStatus)) {//VMI坏仓
				sapWarehouseCode=WmsSapWarehouseType.V101;
			}
			else {//好仓
				sapWarehouseCode=WmsSapWarehouseType.V001;
			}
		}
		else {//自管仓  标准
			if("不良品".equals(inventoryStatus)) {//自管坏仓
				sapWarehouseCode=WmsSapWarehouseType.H001;
			}
			else {//自管好仓
				sapWarehouseCode=WmsSapWarehouseType.Y003;
			}
		}
		
		return sapWarehouseCode;
		
	}
	/**获取sap仓库*/
	private String getSapWarehouseCode(String inventoryStatus,String invType,String kcdd) {
		String sapWarehouseCode="";
		if(!StringHelper.isNullOrEmpty(kcdd) && kcdd.startsWith("W")){
			return kcdd;
		}
		if(WmsFactoryXmlb.JS.equals(invType)) { //VMI仓
			if("不良品".equals(inventoryStatus)) {//VMI坏仓
				sapWarehouseCode=WmsSapWarehouseType.V101;
			}
			else {//好仓
				sapWarehouseCode=WmsSapWarehouseType.V001;
			}
		}
		else {//自管仓
			if("不良品".equals(inventoryStatus)) {//自管坏仓
				sapWarehouseCode=WmsSapWarehouseType.H001;
			}
			else {//自管好仓
				sapWarehouseCode=WmsSapWarehouseType.Y003;
			}
		}
		
		return sapWarehouseCode;
		
	}
	/**销售交货单传SAP*/
	public void sendOutDeliveryShipInfo(WmsBol bol){
		String hql = "select bd.pickTicketDetail.id from WmsBolDetail bd where bd.bol.id =:bolId ";
		List<Long> pickDetailIds = commonDao.findByQuery(hql, "bolId", bol.getId());
		
		hql = " SELECT distinct d.deliveryOrderDetail.deliveryOrder.id FROM DeliveryOrderDetailPtDetail d WHERE d.pickticketDetail.id in(:pickticketDetailId)  ";
		List<Long> doIds = commonDao.findByQuery(hql, new String[]{"pickticketDetailId"}, new Object[]{pickDetailIds});
		if(doIds.size()>1){
			throw new BusinessException("该BOL:"+bol.getCode()+"加入了"+doIds.size()+"个销售拣货单");
		}
		WmsDeliveryOrder order = commonDao.load(WmsDeliveryOrder.class, doIds.get(0));
		
		WmsPickTicketDetail pickDetail = commonDao.load(WmsPickTicketDetail.class, pickDetailIds.get(0));
		
		for(WmsDeliveryOrderDetail dod : order.getDetails()){
			TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
			ledger = this.genTclWmsInventoryLedger(ledger, pickDetail.getPickTicket().getWarehouse(), pickDetail.getPickTicket().getCode(), TclWmsMoveType.OUTDELIVERYMOVETYPE, pickDetail.getPickTicket().getBillType().getCode(), dod.getItem(), dod.getFactory(),null,"合格",dod.getKcdd(),WmsFactoryXmlb.BZ);
			ledger.setDoCode(dod.getDeliveryOrder().getCode());
			ledger.setDoLineNo(dod.getPosnr());
//			ledger.setInvType(null);
			ledger.setQuantity(dod.getDelivedQuantityBu());
			commonDao.store(ledger);
		}
	}
	/**创建台账*/
	private TclWmsInventoryLedger genTclWmsInventoryLedger(TclWmsInventoryLedger ledger,WmsWarehouse warehouse,
			String wmsCode,String moveType,String billTypeCode,WmsItem item,WmsSapFactory factory,
			String supplierCode,String invStatus,String kcdd,String invType){
		ledger.setWarehouse(warehouse);
    	ledger.setWmsCode(wmsCode);
    	String lineNo = genLedgerLineNo(wmsCode,moveType);
    	ledger.setLineNo(lineNo);
    	ledger.setBillType(billTypeCode);
    	ledger = getReceiveDate(ledger);
    	ledger.setSupplierCode(supplierCode);
    	ledger.setSapFactory(factory);
    	ledger.setItem(item);
    	if("合格".equals(invStatus)){
    		ledger.setInvStatus(null);
    	}else{
    		ledger.setInvStatus("X");
    	}
    	if(WmsFactoryXmlb.BZ.equals(invType)){
    		ledger.setInvType(null);
    	}else{
    		ledger.setInvType("K");
    	}
    	ledger.setMoveType(moveType);
    	if(TclWmsMoveType.QCMOVETYPE.equals(moveType) || TclWmsMoveType.QCBADMOVETYPE.equals(moveType)){
    		if("不良品".equals(invStatus)){
    			ledger.setLocationCode(this.getSapWarehouseCode("合格", invType,kcdd));
        		ledger.setQcLocCode(this.getSapWarehouseCode(invStatus, invType,kcdd));
        	}else{
        		ledger.setLocationCode(this.getSapWarehouseCode(invStatus, invType,kcdd));
        		ledger.setQcLocCode(this.getSapWarehouseCode(invStatus, invType,kcdd));
        	}
    	}else{
    		ledger.setLocationCode(this.getSapWarehouseCode(invStatus, invType,kcdd));
    	}
    	
    	commonDao.store(ledger);
		
		return ledger;
	}
	/**
	 * 保存账单关闭日
	 */
	public void storeWmsAccountCloseDay(WmsAccountCloseDay closeDay){
		if(closeDay.getMaxAccountCloseTime().before(closeDay.getAccountCloseTime())){
			throw new BusinessException("账单关闭结束日期不能早于账单关闭开始日期");
		}
		commonDao.store(closeDay);
	}
	
	/**生产订单导入拣配*/
	public void importProductionFile(File file) {
		CommonHelper.log("拣配工单导入开始");
		ProductionOrderManager pom = (ProductionOrderManager)applicationContext.getBean("productionOrderManager");
//		List<WmsPickTicket> pts = pom.importProductionFile(file);
		pom.genProdOrderImportTask(file);
//		this.importProductionOrder(pts);
		
	}
	private void importProductionOrder(List<WmsPickTicket> pts){
		CommonHelper.log("拣货单生成结束，开始自动分配，总共需要分配"+pts.size()+"张拣货单");
		
		List<Map<String,Object>> isNotPro=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> isPro=new ArrayList<Map<String,Object>>();
		List<WmsPickTicket> result=new ArrayList<WmsPickTicket>();
		String hql = "";
		for(WmsPickTicket pt : pts) {
			hql = "from ProductionOrder where code = :code";
			ProductionOrder po = (ProductionOrder) commonDao.findByQueryUniqueResult(hql, "code", pt.getRelatedBill1());
			//查工单
			if(po != null) { //查到了工单
				Map<String,Object> isProMap=new HashMap<String,Object>();
				isProMap.put("BT", po.getBeginTime());
				isProMap.put("GC", po.getCode());
				isProMap.put("PT", pt);
				isPro.add(isProMap);
			}
			else {//没有工单 是线体
				String hql2 = "select min(o.beginTime) from ProductionOrder o where o.code in (select p.userField1 from WmsPickTicketDetail p where p.pickTicket.id=:pickTicketId)";
				Object o = commonDao.findByQueryUniqueResult(hql2, "pickTicketId", pt.getId());
				if(o!=null){
					Map<String,Object> isNotProMap=new HashMap<String,Object>();
					isNotProMap.put("BT", o);
					isNotProMap.put("CX", pt.getRelatedBill1());
					isNotProMap.put("PT", pt);
					isNotPro.add(isNotProMap);
				}else{
					result.add(pt);
				}
			}
		}
		if(!isPro.isEmpty()){
			CollectionUtils.sort((List)isPro,"BT","升序","GC","升序");
			for(Map<String,Object> map : isPro) {
				result.add((WmsPickTicket)map.get("PT"));
			}
		}
		if(!isNotPro.isEmpty()){
			CollectionUtils.sort((List)isNotPro,"BT","升序","CX","升序");
			for(Map<String,Object> map : isNotPro) {
				result.add((WmsPickTicket)map.get("PT"));
			}
		}

		WmsPickticketManager wmsPickticketManager = (WmsPickticketManager) applicationContext.getBean("wmsPickticketManager");
		int i=0;
		
		List<WmsPickTicket> mustCancelPt = new ArrayList<WmsPickTicket>(); //需要取消拣配的拣货单  T-2的分到自管仓的库存的 等全部分配完成后全部取消分配
    	for(WmsPickTicket p : result){
    		i++;
    		CommonHelper.log("开始分配第"+i+"/"+pts.size()+"张拣货单"+p.getCode());
    		try {
				//自动分配
				wmsPickticketManager.autoAllocate(p);
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    		//如果工单上记录了是T-2，且生成的拣货单是自管仓，则把自管仓的已分配状态的拣货单取消分配。   2017-10-16 19:57:04   为了控制洗衣机的VMI仓T-2拣货问题。
    		
			String hql2 = "FROM ProductionOrder po WHERE po.locCode=:locCode AND po.code=:code ";
			List<ProductionOrder> pos = commonDao.findByQuery(hql2,
					new String[] { "locCode", "code" },
					new Object[] { ProductionWarehouseCode.T2, p.getRelatedBill1() });
			if (!pos.isEmpty()) {
				hql2 = "from WmsPickTicket pick where pick.relatedBill1 =:relatedBill1 "
						+ "and pick.warehouse.code in('"
						+ WarehouseEnumeration.BX
						+ "','"
						+ WarehouseEnumeration.XYJ
						+ "')"
						+ " and pick.status=:status ";
				List<WmsPickTicket> picks = commonDao.findByQuery(hql2,
						new String[] { "relatedBill1", "status" },
						new Object[] { p.getRelatedBill1(),
								WmsPickTicketStatus.ALLOCATED });
				mustCancelPt.addAll(picks);
//				for (WmsPickTicket pick : picks) {
//					try {
//						wmsPickticketManager.cancelAllocate(pick);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
    	//全部取消分配
    	for (WmsPickTicket pick : mustCancelPt) {
			try {
				wmsPickticketManager.cancelAllocate(pick);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 生产订单导入--定时任务调用
	 * 此方法只能由EDI调用
	 */
	public void importProductionOrderByTask(Long warhouseId){
		CommonHelper.log("拣配工单导入开始");
		ProductionOrderManager pom = (ProductionOrderManager)applicationContext.getBean("productionOrderManager");
		List<WmsPickTicket> pts = pom.importProductionOrder();
		Map<WmsWarehouse,List<WmsPickTicket>> warehouseMap = new HashMap<WmsWarehouse, List<WmsPickTicket>>();
		//根据仓库把拣货单分开，下面设置session用
		for(WmsPickTicket pick :pts){
			WmsWarehouse warehouse = commonDao.load(WmsWarehouse.class, pick.getWarehouse().getId());
			if(!warehouseMap.containsKey(warehouse)){
				List<WmsPickTicket> picks = new ArrayList<WmsPickTicket>();
				picks.add(pick);
				warehouseMap.put(warehouse, picks);
			}else{
				warehouseMap.get(warehouse).add(pick);
			}
		}
		Set<WmsWarehouse> keys = warehouseMap.keySet();
		for(WmsWarehouse key :keys){
			//此方法由edi来跑 edi没有session调用规则会报错，此处设置session
			ThornBaseOrganization baseOrganization = commonDao.load(ThornBaseOrganization.class, key.getBaseOrganization().getId());
			BussinessModelHolder.setThornBusinessModel(baseOrganization.getBusinessModel());
			BaseOrganizationHolder.setThornBaseOrganization(baseOrganization);
			
			this.importProductionOrder(warehouseMap.get(key));
			//分配完成后清空刚才设置的session
			BussinessModelHolder.setThornBusinessModel(null);
			BaseOrganizationHolder.setThornBaseOrganization(null);
		}
	}
	/**
	 * 库存调拨--好料仓坏料仓间的调拨
	 */
	public void sendChangeStatusInfo(WmsInventory inventory,Double changQty,String newInvStatus){
		TclWmsInventoryLedger ledger = EntityFactory.getEntity(TclWmsInventoryLedger.class);
		//库存地点
		String kcdd = inventory.getItemKey().getLotInfo().getExtendPropC19();
		
		String wmsCode = sequenceGenerater.generateSequence(InventoryLedgerCodeType.KJDB, 8);//内外销调拨没有WMS对应的业务单据号，生成一个流水号
		WmsSapFactory factory = getSapFactory(inventory.getItemKey().getLotInfo().getExtendPropC10());
		
		ledger = this.genTclWmsInventoryLedger(ledger, inventory.getWarehouse(), wmsCode, TclWmsMoveType.KNDBMOVETYPE, 
				WmsPickticketBillTypeCode.KNDBD, inventory.getItem(), factory, inventory.getItemKey().getLotInfo().getSupplierCode(),
				inventory.getStatus(),kcdd,inventory.getItemKey().getLotInfo().getExtendPropC8());
		//好料转坏料需要传对应的坏料库存地点,是W开头的也是传对应的好料库存地点
		ledger.setLocationCode(this.getSapWarehouseCode(inventory.getStatus(), inventory.getItemKey().getLotInfo().getExtendPropC8(), null));
		
    	ledger.setQuantity(changQty);
    	ledger.setRecFatoryCode(factory.getCode());
    	//好料转坏料需要传对应的坏料库存地点,是W开头的也是传对应的坏料库存地点
    	ledger.setRecWarehouse(this.getSapWarehouseCode(newInvStatus, inventory.getItemKey().getLotInfo().getExtendPropC8(),null));
    	commonDao.store(ledger);
	}
	public void insertWarehouseAutoImportPro(Long warehouseId) { 
		CommonHelper.log("插入自动导入拣配的任务，仓库ID："+warehouseId);
		Task xyjTask = new Task(InterfaceLogTaskType.IMPORTPRODUCTIONORDER, TaskSubscriber.IMPORTPRODUCTIONORDER, warehouseId); 
    	commonDao.store(xyjTask);
	}
	public void insertAllWarehouseAutoImportPro() { 
		insertWarehouseAutoImportPro(2L);//洗衣机
		insertWarehouseAutoImportPro(3L);//冰箱
    	
    }
}