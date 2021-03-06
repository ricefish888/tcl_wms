package com.vtradex.wms.rfserver.service.delivery.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import com.vtradex.rf.common.RfConstant;
import com.vtradex.rf.common.exception.RfBusinessException;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.model.EntityFactory;
import com.vtradex.thorn.server.model.security.ThornRole;
import com.vtradex.thorn.server.model.security.ThornUser;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.service.security.UserManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.web.security.BaseOrganizationHolder;
import com.vtradex.thorn.server.web.security.UserHolder;
import com.vtradex.wms.rfserver.common.RfMessageCode;
import com.vtradex.wms.rfserver.service.delivery.TclRfDeliveryManager;
import com.vtradex.wms.rfserver.service.receiving.TclRfAsnManager;
import com.vtradex.wms.server.helper.WmsBarCodeParse;
import com.vtradex.wms.server.model.entity.base.WmsBarCodeRepPrintRecord;
import com.vtradex.wms.server.model.entity.base.WmsCustomer;
import com.vtradex.wms.server.model.entity.bol.WmsBol;
import com.vtradex.wms.server.model.entity.bol.WmsBolDetail;
import com.vtradex.wms.server.model.entity.bol.WmsBolStatus;
import com.vtradex.wms.server.model.entity.bol.WmsBolType;
import com.vtradex.wms.server.model.entity.inventory.WmsInventory;
import com.vtradex.wms.server.model.entity.item.WmsItem;
import com.vtradex.wms.server.model.entity.item.WmsItemHandOverAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemJITAtt;
import com.vtradex.wms.server.model.entity.item.WmsItemScanCode;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicket;
import com.vtradex.wms.server.model.entity.pickticket.WmsPickTicketDetail;
import com.vtradex.wms.server.model.entity.production.WmsWorkDocType;
import com.vtradex.wms.server.model.entity.receiving.WmsASN;
import com.vtradex.wms.server.model.entity.receiving.WmsASNDetail;
import com.vtradex.wms.server.model.entity.warehouse.WmsWarehouse;
import com.vtradex.wms.server.model.entity.workdoc.WmsTask;
import com.vtradex.wms.server.model.entity.workdoc.WmsWorkDoc;
import com.vtradex.wms.server.model.enums.WmsSupplierCode;
import com.vtradex.wms.server.model.enums.WmsTaskStatus;
import com.vtradex.wms.server.model.enums.WmsWorkDocStatus;
import com.vtradex.wms.server.service.bol.WmsBolManager;
import com.vtradex.wms.server.service.bol.WmsTclBolManager;
import com.vtradex.wms.server.service.workdoc.WmsTclWorkDocManager;
import com.vtradex.wms.server.utils.StringHelper;
@SuppressWarnings("rawtypes")
public class DefaultTclRfDeliveryManager extends DefaultBaseManager implements TclRfDeliveryManager{
	
	protected WmsBolManager bolManager;
	
	protected WmsTclBolManager tclbolManager;
	
	protected WmsTclWorkDocManager tclworkDocManager;
	
	protected UserManager userManager;
	
	public DefaultTclRfDeliveryManager(WmsBolManager bolManager,WmsTclBolManager tclbolManager,WmsTclWorkDocManager tclworkDocManager,UserManager userManager) {
		this.bolManager = bolManager;
		this.tclbolManager = tclbolManager;
		this.tclworkDocManager = tclworkDocManager;
		this.userManager = userManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map deliveryInfo(Map deliveryMap) throws RfBusinessException {
		Long cusId = new Long(deliveryMap.get("id").toString().trim());
		WmsCustomer cus = commonDao.load(WmsCustomer.class,cusId);
		if(null!=cus){
			deliveryMap.put("receiver", cus.getName());
			deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map createBol(Map deliveryMap) throws RfBusinessException {
		Long cusId = new Long(deliveryMap.get("id").toString().trim());
		
		String fi = deliveryMap.get("followInput").toString().trim();
		if(null!=cusId){
			WmsCustomer cus = commonDao.load(WmsCustomer.class,cusId);
			WmsBol bol = EntityFactory.getEntity(WmsBol.class);
			bol.setCustomer(cus);
			bol.setCustomerWarehouseId(cus.getWarehouse().getId());//收货人仓库
			bol.setType(WmsBolType.VMI);
			bol.setExpectedDeliveryTime(new Date());
			if(!StringHelper.isNullOrEmpty(fi)){
				bol.setContainerCode(fi);//跟踪号
			}
			bolManager.storeBOL(bol);
			deliveryMap.put(RfConstant.ENTITY_ID, bol.getId());
			deliveryMap.put("bolCode", bol.getCode());
			deliveryMap.put("receiver", cus.getName());
			deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map showDetail(Map deliveryMap) throws RfBusinessException {
		Long bolId = new Long(deliveryMap.get("id").toString().trim());
		WmsBol bol = commonDao.load(WmsBol.class, bolId);
		deliveryMap.put("bolCode", bol.getCode());
		WmsCustomer cus = commonDao.load(WmsCustomer.class, bol.getCustomer().getId());
		deliveryMap.put("receiver", cus.getName());
		WmsWarehouse house = commonDao.load(WmsWarehouse.class, cus.getWarehouse().getId());
		deliveryMap.put("warehouseId", house.getId()+"");
		deliveryMap.put(RfConstant.ENTITY_ID, bol.getId());
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map bolInfo(Map deliveryMap) throws RfBusinessException {
		Long bolId = new Long(deliveryMap.get("id").toString().trim());
		WmsBol bol = commonDao.load(WmsBol.class, bolId);
		String hql ="select detail.pickTicketDetail.item.code, detail.pickTicketDetail.item.name ,sum(detail.planQty) From WmsBolDetail detail where detail.bol.id=:bolId group by detail.pickTicketDetail.item.code, detail.pickTicketDetail.item.name";
		List<Object[]> details = commonDao.findByQuery(hql, "bolId", bol.getId());
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <details.size();i++){
//			物料代码：
//			物料名称：
//			数量
			Object[] detail = details.get(i);
			String itemCode = detail[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = detail[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty =Double.valueOf(detail[2].toString());
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(details.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		deliveryMap.put("itemInfos", itemInfoStr);
		deliveryMap.put(RfConstant.ENTITY_ID, bol.getId());
		deliveryMap.put("bolCode", bol.getCode());
		WmsCustomer cus = commonDao.load(WmsCustomer.class, bol.getCustomer().getId());
		deliveryMap.put("receiver", cus.getName());
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map pickInfo(Map deliveryMap) throws RfBusinessException {
		Long bdId = new Long(deliveryMap.get("id").toString().trim());
		WmsBolDetail wbd = commonDao.load(WmsBolDetail.class, bdId);
		String flag = deliveryMap.get("full")==null?"":deliveryMap.get("full").toString().trim();
		if("Y".equals(flag)){
			deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
			return deliveryMap;
		}
		if(null!=wbd){
			WmsPickTicketDetail detail = commonDao.load(WmsPickTicketDetail.class, wbd.getPickTicketDetail().getId());
			WmsPickTicket wpt = commonDao.load(WmsPickTicket.class, detail.getPickTicket().getId());
			WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, wbd.getWorkDoc().getId());
			String pickCode = doc.getCode();
			WmsItem item = commonDao.load(WmsItem.class, detail.getItem().getId());
			String itemCode = item.getCode();
			String itemName = item.getName();
			String qty = wbd.getPickedQty()+"";
			deliveryMap.put(RfConstant.ENTITY_ID, wbd.getId());
			deliveryMap.put("pcode",	 pickCode);
			deliveryMap.put("itemCode",itemCode);
			deliveryMap.put("itemName",itemName);
			deliveryMap.put("qty",qty);
			deliveryMap.put("qtyInput", qty);
			deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}
		return deliveryMap;
	}

	public Map workDocInfo(Map deliveryMap) throws RfBusinessException {
//		String flag = deliveryMap.get("full")==null?"":deliveryMap.get("full").toString().trim();
//		if("Y".equals(flag)){
//			deliveryMap.put(RfConstant.FORWARD_VALUE, "all_success");
//			return deliveryMap;
//		}
		Long workDocId = new Long(deliveryMap.get("id").toString().trim());
		WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, workDocId);
		String hql = "from WmsBolDetail d where d.workDoc.id =:workDocId and d.bol.id is null";
		List<WmsBolDetail> bolDetails = commonDao.findByQuery(hql, "workDocId", workDocId);
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <bolDetails.size();i++){
//			物料代码：
//			物料名称：
//			数量:
			WmsBolDetail detail = bolDetails.get(i);
			String itemCode = detail.getItemKey().getItem().getCode().toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = detail.getItemKey().getItem().getName().toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty =detail.getPickedQty();
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(bolDetails.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		deliveryMap.put("pcode", doc.getCode());
		deliveryMap.put("itemInfos", itemInfoStr);
		return deliveryMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map addDetail(Map deliveryMap) throws RfBusinessException {
		Long bdId = new Long(deliveryMap.get("id").toString().trim());
		Double qty = 0d;
		try {
			qty = new Double(deliveryMap.get("qtyInput").toString().trim());//数量
		} catch (Exception e) {
			e.printStackTrace();
			deliveryMap.put("qtyInput", "");
			throw new RfBusinessException("数量格式输入有误!!",deliveryMap);
		}
		String bolCode = deliveryMap.get("bolCode").toString().trim();
		WmsBol bol = (WmsBol)commonDao.findByQueryUniqueResult("FROM WmsBol bol where bol.code=:bc", "bc",bolCode);
		bol = commonDao.load(WmsBol.class, bol.getId());
		WmsBolDetail wwd = commonDao.load(WmsBolDetail.class, bdId);
		bol.setPurchaseOrderCode(wwd.getPickTicketDetail().getPickTicket().getRelatedBill1());
		commonDao.store(bol);
//		String barCode =  deliveryMap.get("itemInput").toString().trim();//货品条码
//		if(StringUtils.isEmpty(barCode)){
//			throw new RfBusinessException("条码不能为空");
//		}
//		String itemCode = "";
//		if(WmsBarCodeParse.isBarCode(barCode)){
//			Map map = WmsBarCodeParse.parse(barCode);
//			itemCode = map.get(WmsBarCodeParse.KEY_ITEMCODE).toString().trim();
//		}else{
//			itemCode = barCode;
//		}
//		WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", itemCode);
//		if(null==item){
//			deliveryMap.put("itemInput", "");
//			throw new RfBusinessException("货品不存在!!",deliveryMap);
//		}
//		if(!item.getCode().equals(wwd.getItemKey().getItem().getCode())){
//			deliveryMap.put("itemInput", "");
//			throw new RfBusinessException("货品输入有误!请扫描"+wwd.getItemKey().getItem().getCode()+"物料的条码",deliveryMap);
//		}
		List tem = new ArrayList();
		tem.add(qty);
		Double quantity = wwd.getPickedQty();//BOL明细的已拣数量
		tclbolManager.addBOLDetail(bol.getId(), wwd, tem);
		if(quantity.equals(qty) || wwd.getPickedQty() == 0D){
			deliveryMap.put("full","Y");
		}
		deliveryMap.put(RfConstant.ENTITY_ID, wwd.getId());
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}

	public Map addBolDetails(Map deliveryMap) throws RfBusinessException {
		Long workDocId = new Long(deliveryMap.get("id").toString().trim());
		WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, workDocId);
		String code = deliveryMap.get("itemCode").toString().trim();
		String hql = "from WmsBolDetail d where d.workDoc.id =:workDocId and d.bol.id is null and d.itemKey.item.code=:code ";
		List<WmsBolDetail> bolDetails = commonDao.findByQuery(hql, new String[]{"workDocId","code"}, new Object[]{workDocId,code});
		
		Double qty = 0d;
		try {
			qty = new Double(deliveryMap.get("qtyInput").toString().trim());//数量
		} catch (Exception e) {
			e.printStackTrace();
			deliveryMap.put("qtyInput", "");
			throw new RfBusinessException("数量格式输入有误!!",deliveryMap);
		}
//		Long bolDetailId = new Long(deliveryMap.get("bolDetailId").toString());
//		WmsBolDetail bolDetail = commonDao.load(WmsBolDetail.class, bolDetailId);
		
		String bolCode = deliveryMap.get("bolCode").toString().trim();
		WmsBol bol = (WmsBol)commonDao.findByQueryUniqueResult("FROM WmsBol bol where bol.code=:bc", "bc",bolCode);
		bol = commonDao.load(WmsBol.class, bol.getId());
		for(WmsBolDetail bolDetail : bolDetails){
			if(qty ==0){
				break;
			}
			List tem = new ArrayList();
			if(qty>=bolDetail.getPickedQty()){
				tem.add(bolDetail.getPickedQty());
				qty-=bolDetail.getPickedQty();
			}else{
				tem.add(qty);
				qty =0D;
			}
			tclbolManager.addBOLDetail(bol.getId(), bolDetail, tem);
		}
		
		String hql2 = "from WmsBolDetail d where d.workDoc.id =:workDocId and d.bol.id is null";
		List<WmsBolDetail> wmsBolDetails = commonDao.findByQuery(hql2, "workDocId", workDocId);
		if(wmsBolDetails.isEmpty()){
			deliveryMap.put(RfConstant.ENTITY_ID, workDocId);
			deliveryMap.put(RfConstant.FORWARD_VALUE, "all_success");
			deliveryMap.put("itemInput", "");
			deliveryMap.put("qtyInput", "");
		}else{
			deliveryMap.put("itemInput", "");
			deliveryMap.put("qtyInput", "");
			deliveryMap.put(RfConstant.ENTITY_ID, workDocId);
			deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}
//		if(quantity.equals(qty) || bolDetail.getPickedQty() == 0D){
//			deliveryMap.put("full","Y");
//		}
		return deliveryMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map showDetails(Map deliveryMap) throws RfBusinessException {
		Map<String, Object> perResult = new HashMap<String, Object>();
		
		perResult.putAll(deliveryMap);
		perResult.put(RfConstant.ENTITY_ID, "");
		
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		//清除部分值
		deliveryMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		perResult.put("itemInput", null);
		perResult.put("qtyInput", null);
		perResult.put("full", null);
		deliveryMap.put(RfConstant.PERSISTENT_VALUE, perResult);//将持久map放入返回map中
		return deliveryMap;//返回原map
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map checkCode(Map deliveryMap) throws RfBusinessException {
		String barcode = null;
		if(null!=deliveryMap.get("barCodeInput")){
			barcode = deliveryMap.get("barCodeInput").toString().trim();//货品条码
			TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
			tclRfAsnManager.checkBarCode(barcode);
			if(!WmsBarCodeParse.isBarCode(barcode)){
				deliveryMap.put("barCodeInput", "");
				throw new RfBusinessException("条码格式不正确",deliveryMap);
			}
			Map map = WmsBarCodeParse.parse(barcode);
			String itemCode = (String)map.get(WmsBarCodeParse.KEY_ITEMCODE);
			WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", itemCode);
			if(null==item){
				deliveryMap.put("barCodeInput", "");
				throw new RfBusinessException("物料条码不存在!!",deliveryMap);
			}
			String info = map.get(WmsBarCodeParse.KEY_LOTNO).toString().trim();
			boolean flag = checkInfo(info);
			String asnId = map.get(WmsBarCodeParse.KEY_ASN_ID).toString().trim();
			if(!flag){
				deliveryMap.put("barCodeInput", "");
				throw new RfBusinessException("条码格式不正确!!",deliveryMap);
			}else{
				try{
					this.genBarCode(item, info, barcode, asnId);
				}catch(RfBusinessException e){
					deliveryMap.put("barCodeInput", "");
					throw new RfBusinessException(e.getCode(),deliveryMap);
				}
				deliveryMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
				deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
			}
		}
		return deliveryMap;
	}

	/**
	 * 生成条码补打记录
	 * @param item
	 * @param lotNo
	 * @param barCode
	 * @param asnId
	 */
	public void genBarCode(WmsItem item,String lotNo,String barCode,String asnId ){
		WmsBarCodeRepPrintRecord barRecord = EntityFactory.getEntity(WmsBarCodeRepPrintRecord.class);
		String wareHql = "FROM WmsWarehouse house where house.baseOrganization.id=:hbi and house.status='ENABLED'";
		WmsWarehouse house = (WmsWarehouse)commonDao.findByQueryUniqueResult(wareHql, "hbi",BaseOrganizationHolder.getThornBaseOrganization().getId());
		if(null==house){
			throw new RfBusinessException("没有找到对应仓库!!");
		}
		barRecord.setWarehouse(house);
		barRecord.setItem(item);
		barRecord.setLotkey(lotNo);
		barRecord.setBarcode(barCode);
		WmsASN asn=null;
		if(null!=asnId){
			asn  = commonDao.load(WmsASN.class, Long.valueOf(asnId));
			if(null==asn){//没有ASN  说明是初始化库存  或者前期asndetail的库存
//				deliveryMap.put("barCodeInput", "");
//				throw new RfBusinessException("Asn不存在!!",deliveryMap);
				String hql = "from WmsInventory inv where inv.itemKey.lotInfo.extendPropC17=:barcode order by inv.id asc";
				List<WmsInventory> invs = commonDao.findByQuery(hql,new String[]{"barcode"},new Object[]{barCode});
				if(invs.size()>0) { //库存中有
					WmsInventory inv = invs.get(0);
					barRecord.setAsnCode("");
					barRecord.setXmlb(inv.getItemKey().getLotInfo().getExtendPropC8());
					barRecord.setStoreaDate(inv.getItemKey().getLotInfo().getStorageDate());
					barRecord.setFactoryCode(inv.getItemKey().getLotInfo().getExtendPropC10());
					barRecord.setSupplierCode(inv.getItemKey().getLotInfo().getSupplierCode());
					barRecord.setSupplierName(inv.getItemKey().getLotInfo().getExtendPropC11());
					barRecord.setMinPackageQty(null);//最小包装量
				}
				else { //库存中也没有
					barRecord.setAsnCode("");
					barRecord.setXmlb("");
					barRecord.setStoreaDate(null);
					barRecord.setFactoryCode("");
					barRecord.setSupplierCode(WmsSupplierCode.XN);
					barRecord.setSupplierName(WmsSupplierCode.XN_NAME);
					barRecord.setMinPackageQty(null);//最小包装量
				}
			}
			else {//有detail
				barRecord.setAsnCode(asn.getCode());
				barRecord.setXmlb(asn.getUserField5());
				barRecord.setStoreaDate(asn.getOrderDate());
				barRecord.setFactoryCode(asn.getUserField7());
				if(asn.getSupplier()!=null) {
					barRecord.setSupplierCode(asn.getSupplier().getCode());
					barRecord.setSupplierName(asn.getSupplier().getName());
				}
				barRecord.setMinPackageQty(null);//最小包装量
			}
		}
//		barRecord.setWmsASNDetail(detail);
		barRecord.setInsertTime(new Date());
		barRecord.setRfUser(UserHolder.getUser().getName());
		commonDao.store(barRecord);
	}
	@SuppressWarnings("unused")
	private boolean checkInfo(String info) {
		String yy = info.substring(0,4);
		String mm = info.substring(4,6);
		String ls = info.substring(6,info.length());
		Integer yt =null;
		Integer mt =null;
		Integer dt =null;
		Integer ld =null;
		try {
			yt = Integer.valueOf(yy);
			mt = Integer.valueOf(mm);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RfBusinessException("输入不合法!!");
		}
		if(yt<1900 ||yt>2100){
			throw new RfBusinessException("年份维护异常!");
		}
		if(mt<0||mt>12){
			throw new RfBusinessException("月份维护异常!!");
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map deliveryCodeInputCommit(Map deliveryMap)throws RfBusinessException {
		if(null!=deliveryMap.get("deliveryCodeInput")){
			String qcode = deliveryMap.get("deliveryCodeInput").toString().trim();
			if(!StringHelper.isNullOrEmpty(qcode)){
				WmsWorkDoc wwd = (WmsWorkDoc)commonDao.findByQueryUniqueResult("FROM  WmsWorkDoc wwd where wwd.code=:wc", "wc", qcode);
				if(null==wwd){
					deliveryMap.put("deliveryCodeInput", "");
					throw new RfBusinessException("未找到对应的交货出库单!!",deliveryMap);
				}
				List<Object[]> details = checkShip(wwd,Boolean.TRUE);
				StringBuffer itemInfoStr = new StringBuffer();
				for(int i = 0;i <details.size();i++){
//					物料代码：
//					物料名称：
//					数量:
					Object[] detail = details.get(i);
					String itemCode = detail[0].toString();
					itemInfoStr.append("物料代码:").append(itemCode).append("\n");
					String itemName = detail[1].toString();
					itemInfoStr.append("物料名称:").append(itemName).append("\n");
					Double expectedQty =Double.valueOf(detail[2].toString());
					itemInfoStr.append("数量:").append(expectedQty).append("\n");
					String locCode = detail[3].toString();
					itemInfoStr.append("移出库位:").append(locCode).append("\n");
					if(i<(details.size()-1)){
						itemInfoStr.append("----------------------\n");
					}
				}
				deliveryMap.put("workDocId", wwd.getId());
				deliveryMap.put("itemInfos", itemInfoStr);
				deliveryMap.put(RfConstant.FORWARD_VALUE,RfConstant.FORWARD_SUCCESS);
			}
		}
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	private void checkPermission(String userName, String psw,long wid) {
		ThornUser user = userManager.retrieve(userName);
		if(null==user){
			throw new RfBusinessException("用户不存在!!!");
		}
		String encodePassword = DigestUtils.shaHex(psw);
		if (!(user.getPassword().equals(encodePassword))) {
			throw new RfBusinessException("密码错误!!");
		}
		if ((user.getExpiryDate() != null) && (user.getExpiryDate().before(new Date()))){
			throw new RfBusinessException("此用户已过期!");
		}
		if ((user.getPasswordExpiryDate() != null)&& (user.getPasswordExpiryDate().before(new Date()))){
			throw new RfBusinessException("此用户密码已过期!");
		}
		if (!(user.isEnabled())) {
			throw new RfBusinessException("此用户已禁用请联系相关人员!!");
		}
		//判断用户是否绑定相应角色
		String rcode = "SCXJJY";
		ThornRole role = (ThornRole)commonDao.findByQueryUniqueResult("FROM ThornRole role where role.code=:rc", "rc",rcode);
		if(null==role){
			throw new RfBusinessException("没有维护相关角色!!");
		}
		String whHql = "FROM WmsWarehouse wh WHERE wh.baseOrganization.id=:wb and wh.id=:wi";
//		List<WmsWarehouse> wws = commonDao.findByQuery(whHql, new String[]{"wb","wi"}, new Object[]{BaseOrganizationHolder.getThornBaseOrganization().getId(),wid});
//		if(wws.size()<=0){
//			throw new RfBusinessException("数据错误!!");
//		}
		String hql = "SELECT u.name FROM ThornUser u,ThornGroup g,ThornRole r WHERE u.id IN ELEMENTS(g.users) AND r.id IN ELEMENTS(g.roles) AND u.id=:ui AND r.code=:ri";
		List<Object[]> objs = commonDao.findByQuery(hql, new String[]{"ui","ri"}, new Object[]{user.getId(),rcode});
		if(objs.size()<=0 ){
			throw new RfBusinessException("该用户无此权限!!");
		}
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> checkShip(WmsWorkDoc wwd,boolean flag) {
		String checkHql = "SELECT task.item.code,task.item.name,sum(task.pickedQty),task.fromLocation.code,task.itemKey.lotInfo.extendPropC6 FROM WmsTask task where task.workDoc=:tw " +
				"AND task.status !='FINISH' group by task.item.code, task.item.name,task.fromLocation.code,task.itemKey.lotInfo.extendPropC6";
		List<Object[]> tasks = commonDao.findByQuery(checkHql,"tw", wwd);
		if(tasks.size()==0){
//			throw new RfBusinessException("出库单明细不能为空!!");
		}
//		//整单发运
//		if(flag){
//			//生效才能发运
//			if(!WmsWorkDocStatus.ENABLED.equals(wwd.getStatus())){
//				throw new RfBusinessException("出库单状态未生效!!");
//			}
//		}else{
//			//明细发运作业中
//			if(!WmsWorkDocStatus.ENABLED.equals(wwd.getStatus())){
//				throw new RfBusinessException("明细出库条件不符合!!");
//			}
//		}
		return tasks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map deliveryOutCommit(Map deliveryMap) throws RfBusinessException {
		if(null==deliveryMap.get("userNameInput")||null==deliveryMap.get("pswInput")){
			throw new RfBusinessException("请输入用户名密码!!");
		}
		String userName = deliveryMap.get("userNameInput").toString().trim();
		String psw = deliveryMap.get("pswInput").toString().trim();
		if(StringHelper.isNullOrEmpty(userName)||StringHelper.isNullOrEmpty(psw)){
			throw new RfBusinessException("用户名密码异常!!!");
		}
		Long workId = deliveryMap.get("workDocId") ==null ? null :Long.valueOf(deliveryMap.get("workDocId").toString());
		if(null == workId){
			workId = deliveryMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString());
		}
		WmsWorkDoc wwd = commonDao.load(WmsWorkDoc.class, workId);
		checkPermission(userName,psw,wwd.getWarehouse().getId());
		String hql = "FROM WmsTask task WHERE task.workDoc.id =:workDocId ";
		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", wwd.getId());
		for(WmsTask task : tasks){
			task.setJjUserLoginName(userName);
			task.setJjTime(new Date());
			commonDao.store(task);
		}
		//调用发运
		tclworkDocManager.shipQuickShippingWorkDoc(wwd);
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    Map<String, Object> result = new HashMap<String, Object>();
	    
		result.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		deliveryMap.put("customerCode", wwd.getWarehouse().getCode());//仓库编码
	    perResult.putAll(deliveryMap);
	    perResult.put("customerCode", wwd.getWarehouse().getCode());//仓库编码
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map cancelOut(Map deliveryMap) throws RfBusinessException {
//		deliveryMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}

	@Override
	public Map showdeliveryInfo(Map deliveryMap) throws RfBusinessException {
		Long workDocId = deliveryMap.get("workDocId") ==null ? null : Long.valueOf(deliveryMap.get("workDocId").toString());
		if(null == workDocId){
			workDocId = deliveryMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(workDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		
		WmsWorkDoc wwd = commonDao.load(WmsWorkDoc.class, workDocId);
		List<Object[]> details = checkShip(wwd,Boolean.TRUE);
		StringBuffer itemInfoStr = new StringBuffer();
		for(int i = 0;i <details.size();i++){
//			物料代码：
//			物料名称：
//			数量:
			Object[] detail = details.get(i);
			String lineNo = detail[4].toString();
			itemInfoStr.append("生产线:").append(lineNo).append("\n");
			String itemCode = detail[0].toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = detail[1].toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty =Double.valueOf(detail[2].toString());
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			String locCode = detail[3].toString();
			itemInfoStr.append("移出库位:").append(locCode).append("\n");
			if(i<(details.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
		}
		deliveryMap.put("workDocId", wwd.getId());
		deliveryMap.put("deliveryCodeInput", wwd.getCode());
		deliveryMap.put("remark", wwd.getUserField4());
		deliveryMap.put("type", wwd.getUserField2().equals(WmsWorkDocType.T_1_AREA) ? "直接出库" : "线边交接");
		deliveryMap.put("itemInfos", itemInfoStr);
		deliveryMap.put(RfConstant.FORWARD_VALUE,RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map detailCodeInputCommit(Map deliveryMap)throws RfBusinessException {
		if(null!=deliveryMap.get("detailCodeInput")){
			String qcode = deliveryMap.get("detailCodeInput").toString().trim();
			if(!StringHelper.isNullOrEmpty(qcode)){
				WmsWorkDoc wwd = (WmsWorkDoc)commonDao.findByQueryUniqueResult("FROM  WmsWorkDoc wwd where wwd.code=:wc", "wc", qcode);
				if(null==wwd){
					deliveryMap.put("detailCodeInput", "");
					throw new RfBusinessException("未找到对应的交货出库单!!",deliveryMap);
				}
				deliveryMap.put(RfConstant.ENTITY_ID, wwd.getId());
				deliveryMap.put("workCode", wwd.getCode());
				deliveryMap.put(RfConstant.FORWARD_VALUE,RfConstant.FORWARD_SUCCESS);
			}
		}
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map itemCodeInputCommit(Map deliveryMap) throws RfBusinessException {
		if(null!=deliveryMap.get("itemCodeInput")){
			String barcode = deliveryMap.get("itemCodeInput").toString().trim();
			String itemCode = "";
			TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
			tclRfAsnManager.checkBarCode(barcode);
			WmsItem item = null;
			if(WmsBarCodeParse.isBarCode(barcode)){
				Map map = WmsBarCodeParse.parse(barcode);
				itemCode = map.get(WmsBarCodeParse.KEY_ITEMCODE).toString().trim();
				item = this.getWmsItem(itemCode);
				if(null==item){
					deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_FAIL);
					return deliveryMap;
				}
			}else{
				itemCode =barcode;
				item = this.getWmsItem(itemCode);
				if(null==item){
					deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_FAIL);
					return deliveryMap;
				}
				if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
					deliveryMap.put("itemCodeInput", "");
					throw new RfBusinessException("请扫描条码",deliveryMap);
				}
			}
			WmsWorkDoc wwd = null;
			if(null!=deliveryMap.get("workCode")){
				String wd = deliveryMap.get("workCode").toString().trim();
				wwd = (WmsWorkDoc)commonDao.findByQueryUniqueResult("FROM WmsWorkDoc wwd where wwd.code=:wc", "wc", wd);
			}
			if(null!=deliveryMap.get("parentId")){
				String wId = deliveryMap.get("parentId").toString().trim();
				if(!StringHelper.isNullOrEmpty(wId)){
					wwd = commonDao.load(WmsWorkDoc.class, Long.valueOf(wId));
				}
			}
			double qty  = getItemQty(wwd,item);
			if(qty <= 0D){
				deliveryMap.put("itemCodeInput", "");
				throw new RfBusinessException("该物料"+barcode+"已交接完",deliveryMap);
			}
			deliveryMap.put(RfConstant.ENTITY_ID, wwd.getId());
			deliveryMap.put("deliveryCode", wwd.getCode());
			deliveryMap.put("itemCode", item.getCode());
			deliveryMap.put("itemName", item.getName());
			deliveryMap.put("qty", qty);
			deliveryMap.put("qtyInput", qty);
			deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		}
		return deliveryMap;
	}

	private double getItemQty(WmsWorkDoc wwd, WmsItem item) {
		Double qty = 0d;
		String qtyHql = "SELECT sum(task.pickedQty) FROM WmsTask task where task.workDoc=:tw and task.item=:ti AND task.status !='FINISH' group by task.item.code, task.item.name";
		@SuppressWarnings("unchecked")
		List<Object> qtys = commonDao.findByQuery(qtyHql, new String[]{"tw","ti"}, new Object[]{wwd,item});
		if(qtys.size()>0){
			qty = (Double)qtys.get(0);
		}
		return qty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map showdetailInfo(Map deliveryMap) throws RfBusinessException {
		WmsWorkDoc wwd = null;
		if(null!=deliveryMap.get("workCode")){
			String wd = deliveryMap.get("workCode").toString().trim();
			wwd = (WmsWorkDoc)commonDao.findByQueryUniqueResult("FROM WmsWorkDoc wwd where wwd.code=:wc", "wc", wd);
		}
		deliveryMap.put(RfConstant.ENTITY_ID, wwd.getId());
		deliveryMap.put("workCode", wwd.getCode());
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map detailOutCommit(Map deliveryMap) throws RfBusinessException {
		Map<String, Object> result = new HashMap<String, Object>();
		if(null==deliveryMap.get("userNameInput")||null==deliveryMap.get("pswInput")){
			throw new RfBusinessException("请输入用户名密码!!");
		}
		String userName = deliveryMap.get("userNameInput").toString().trim();
		String psw = deliveryMap.get("pswInput").toString().trim();
		if(StringHelper.isNullOrEmpty(userName)||StringHelper.isNullOrEmpty(psw)){
			throw new RfBusinessException("用户名密码异常!!!");
		}
		WmsItem item = null;
		if(null!=deliveryMap.get("itemCode")){
			String itemCode = deliveryMap.get("itemCode").toString().trim();
			if(!StringHelper.isNullOrEmpty(itemCode)){
				item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", itemCode);
			}
		}
		WmsWorkDoc wwd = null;
		if(null!=deliveryMap.get("workCode")){
			String wd = deliveryMap.get("workCode").toString().trim();
			if(!StringHelper.isNullOrEmpty(wd)){
				wwd = (WmsWorkDoc)commonDao.findByQueryUniqueResult("FROM WmsWorkDoc wwd where wwd.code=:wc", "wc", wd);
			}
		}
		checkPermission(userName,psw,wwd.getWarehouse().getId());
		checkShip(wwd, Boolean.FALSE);
		String taskHql = "FROM WmsTask task where task.workDoc=:tw and task.item=:ti AND task.status !=:status";
		List<WmsTask> tasks = commonDao.findByQuery(taskHql, new String[]{"tw","ti","status"}, new Object[]{wwd,item,WmsTaskStatus.FINISH});
		Double qty = Double.valueOf(deliveryMap.get("qty").toString());
		String shipQtyStr= deliveryMap.get("qtyInput")==null ? "" : deliveryMap.get("qtyInput").toString() ;//用户录入的交接数量 
		Double shipQty =0D;
		if(StringHelper.isNullOrEmpty(shipQtyStr)){
			throw new RfBusinessException("交接数量不能为空");
		}
		try{
			shipQty = Double.valueOf(shipQtyStr);
		}catch(Exception e){
			throw new RfBusinessException("交接数量输入的非数字");
		}
		if(shipQty>qty){
			throw new RfBusinessException("交接数量不能大于"+qty);
		}
		for(WmsTask task : tasks){
			if(shipQty<=0d) {
				break;
			}
			Double pqty = task.getPickedQty();
			if(pqty<=shipQty) {
				//调用明细发运
				task.setJjUserLoginName(userName);//交接人
				task.setJjTime(new Date());//交接时间
				tclworkDocManager.detailShipQuickShippingTask(task);
				commonDao.store(task);
				shipQty -=pqty;
			}
			else { //需要拆数量
				
				
				//拆数量
				WmsTask newTask = new WmsTask();
				BeanUtils.copyEntity(newTask, task);
				newTask.setId(null);
				newTask.setPickedQty(newTask.getPickedQty() - shipQty);
				newTask.setPlanQty(newTask.getPlanQty()-shipQty);
				newTask.setPlanPackQty(newTask.getPlanPackQty() - shipQty);
				commonDao.store(newTask);
				
				//拆库存；
				 List<WmsInventory> invs = commonDao.findByQuery("FROM WmsInventory inv WHERE inv.task.id=:taskId", 
			                "taskId", task.getId());
			     if(invs.isEmpty()) {
			        	throw new BusinessException("未找到作业任务对应库存!");
			     }
			     if(invs.size()>1) {
			    	 throw new BusinessException("找到了"+invs.size()+"条库存！");
			     }
			     for(WmsInventory inventory : invs) {
			    	 if(StringHelper.isNullOrEmpty(inventory.getItemKey().getLotInfo().getExtendPropC15())) {
					       	throw new BusinessException("库存的扩展字段15应为拣货单明细ID，不能为空");
					     }
			    	 WmsInventory newInv = new WmsInventory();
			    	 BeanUtils.copyEntity(newInv, inventory);
			    	 newInv.setId(null);
			    	 newInv.setQty(newInv.getQty()-shipQty);
			    	 newInv.setPackQty(newInv.getPackQty()-shipQty);
			    	 newInv.setTask(newTask);
			    	 commonDao.store(newInv);
			    	 inventory.setQty(shipQty);
			    	 inventory.setPackQty(shipQty);
			     }
			      
				
				
				task.setPickedQty(shipQty);
				task.setPlanQty(shipQty);
				task.setPlanPackQty(shipQty);
				
				task.setJjUserLoginName(userName);//交接人
				task.setJjTime(new Date());//交接时间
				tclworkDocManager.detailShipQuickShippingTask(task);
				commonDao.store(task);
				
				
				
				shipQty=0D;
			}
			
			
		}
		if(WmsWorkDocStatus.FINISH.equals(wwd.getStatus())){
			deliveryMap.put("customerCode", wwd.getWarehouse().getCode());//仓库编码
			deliveryMap.put(RfConstant.FORWARD_VALUE, "all_success");
			result.put(RfConstant.FORWARD_VALUE, "all_success");
			deliveryMap.put(RfConstant.CLEAR_VALUE, Boolean.TRUE);
		}else{
			deliveryMap.put(RfConstant.FORWARD_VALUE, "part_success");
			result.put(RfConstant.FORWARD_VALUE, "part_success");
		}
		//设置永久值
	    Map<String, Object> perResult = new HashMap<String, Object>();
	    
	    perResult.putAll(deliveryMap);
	    perResult.put("customerCode", wwd.getWarehouse().getCode());//仓库编码
	    result.put(RfConstant.PERSISTENT_VALUE, perResult);
		result.put(RfConstant.CLEAR_VALUE, "true");
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map delDetail(Map deliveryMap) throws RfBusinessException {
		Long bid = Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString().trim());
		WmsBol bol = commonDao.load(WmsBol.class, bid);
		deliveryMap.put("bolCode", bol.getCode());
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map delInfo(Map deliveryMap) throws RfBusinessException {
		long bid = Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString().trim());
		WmsBolDetail detail = commonDao.load(WmsBolDetail.class,bid);
		//查询剩余的明细信息
		String bolCode =deliveryMap.get("bolCode").toString().trim();
		String hqlInfo = "SELECT  " +
				" workDoc.code," +
				" item.code," +
				" bolDetail.pickedQty FROM WmsBolDetail bolDetail" +
				" LEFT JOIN bolDetail.pickTicketDetail.pickTicket pickTicket " +
				" LEFT JOIN bolDetail.pickTicketDetail.item item " +
				" LEFT JOIN bolDetail.workDoc workDoc" +
				"  WHERE bolDetail.id=:bbc";
		List<Object[]> infos = commonDao.findByQuery(hqlInfo, "bbc", detail.getId());
		StringBuffer detailInfoStr = new StringBuffer();
		for(int i = 0;i <infos.size();i++){
//			物料代码：
//			物料名称：
//			数量:
			Object[] info = infos.get(i);
			String pickCode = info[0].toString();
			detailInfoStr.append("拣货作业单号:").append(pickCode).append("\n");
			String itemCode = info[1].toString();
			detailInfoStr.append("物料编码:").append(itemCode).append("\n");
			Double qty =Double.valueOf(info[2].toString());
			detailInfoStr.append("数量:").append(qty).append("\n");
			if(i<(infos.size()-1)){
				detailInfoStr.append("----------------------\n");
			}
		}
		deliveryMap.put(RfConstant.ENTITY_ID, detail.getId());
		deliveryMap.put("bolCode", bolCode);
		deliveryMap.put("detailInfoStr", detailInfoStr);
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map delConfirm(Map deliveryMap) throws RfBusinessException {
		long did = Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString().trim());
		WmsBolDetail detail = commonDao.load(WmsBolDetail.class, did);
		WmsBol bol = detail.getBol();
		deliveryMap.put(RfConstant.ENTITY_ID, bol.getId());
		bolManager.deleteBOLDetail(detail);
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}
	
	public Map workDocCommitBt(Map deliveryMap) throws RfBusinessException {
		Long workDocId = deliveryMap.get("workDocId") ==null ? null : Long.valueOf(deliveryMap.get("workDocId").toString());
		if(null == workDocId){
			workDocId = deliveryMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(workDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc wwd = commonDao.load(WmsWorkDoc.class, workDocId);
		String hql = "from WmsTask task where task.workDoc.id =:workDocId ";
		List<WmsTask> tasks = commonDao.findByQuery(hql, "workDocId", wwd.getId());
		if(tasks.isEmpty()){
			throw new BusinessException("配送单没有明细不能出库");
		}
		if(WmsWorkDocStatus.READY_ALLOCATE.equals(wwd.getStatus())){
			wwd.setStatus(WmsWorkDocStatus.ENABLED);
			tclworkDocManager.activeQuickShippingWorkDoc(wwd);
		}
		if(WmsWorkDocType.LINE_EDGE.equals(wwd.getUserField2())){
			deliveryMap.put(RfConstant.FORWARD_VALUE, "success_line_edge");
		}else{
			deliveryMap.put(RfConstant.FORWARD_VALUE, "success_t_1_area");
		}
		deliveryMap.put(RfConstant.ENTITY_ID, wwd.getId());
		deliveryMap.put("workCode", wwd.getCode());
		return deliveryMap;
		
	}
	/**
	 * 扫描物料
	 */
	public Map confirmItemCode(Map deliveryMap) throws RfBusinessException{
		Long workDocId = new Long(deliveryMap.get("id").toString().trim());
		WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, workDocId);
		String barCode =  deliveryMap.get("itemInput").toString().trim();//货品条码
		if(StringUtils.isEmpty(barCode)){
			throw new RfBusinessException("条码不能为空");
		}
		String itemCode = "";
		TclRfAsnManager tclRfAsnManager = (TclRfAsnManager) applicationContext.getBean("tclRfAsnManager");
		tclRfAsnManager.checkBarCode(barCode);
		WmsItem item = null;
		if(WmsBarCodeParse.isBarCode(barCode)){
			Map map = WmsBarCodeParse.parse(barCode);
			itemCode = map.get(WmsBarCodeParse.KEY_ITEMCODE).toString().trim();
			item = this.getWmsItem(itemCode);
			if(null==item){
				deliveryMap.put("itemInput", "");
				throw new RfBusinessException("货品不存在!!",deliveryMap);
			}
		}else{
			itemCode = barCode;
			item = this.getWmsItem(itemCode);
			if(null==item){
				deliveryMap.put("itemInput", "");
				throw new RfBusinessException("货品不存在!!",deliveryMap);
			}
			if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
				deliveryMap.put("itemInput", "");
				throw new RfBusinessException("请扫描条码",deliveryMap);
			}
		}
		String hql = "from WmsBolDetail d where d.workDoc.id =:workDocId and d.itemKey.item.id =:itemId and d.bol.id is null ";
		List<WmsBolDetail> bolDetails = commonDao.findByQuery(hql, new String[]{"workDocId","itemId"}, new Object[]{doc.getId(),item.getId()});
		if(bolDetails.isEmpty()){
			deliveryMap.put("itemInput", "");
			throw new RfBusinessException("货品"+barCode+"不在该拣货作业单下",deliveryMap);
		}
		Double qty = 0D;
		for(WmsBolDetail bd : bolDetails){
			qty+=bd.getPickedQty();
		}
		deliveryMap.put("id", workDocId);
		deliveryMap.put("itemCode", itemCode);
		deliveryMap.put("qtyInput", qty);
		deliveryMap.put("bolDetailId", bolDetails.get(0).getId());
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}
	
	public Map showBolDetailInfo(Map deliveryMap) throws RfBusinessException{
		Long workDocId = new Long(deliveryMap.get("id").toString().trim());
		String code = deliveryMap.get("itemCode").toString().trim();
		WmsWorkDoc doc = commonDao.load(WmsWorkDoc.class, workDocId);
		String hql = "from WmsBolDetail d where d.workDoc.id =:workDocId and d.bol.id is null and d.itemKey.item.code=:code ";
		List<WmsBolDetail> bolDetails = commonDao.findByQuery(hql, new String[]{"workDocId","code"}, new Object[]{workDocId,code});
		StringBuffer itemInfoStr = new StringBuffer();
		Double qty = 0D;
		for(int i = 0;i <bolDetails.size();i++){
//			物料代码：
//			物料名称：
//			数量:
			WmsBolDetail detail = bolDetails.get(i);
			String itemCode = detail.getItemKey().getItem().getCode().toString();
			itemInfoStr.append("物料代码:").append(itemCode).append("\n");
			String itemName = detail.getItemKey().getItem().getName().toString();
			itemInfoStr.append("物料名称:").append(itemName).append("\n");
			Double expectedQty =detail.getPickedQty();
			itemInfoStr.append("数量:").append(expectedQty).append("\n");
			if(i<(bolDetails.size()-1)){
				itemInfoStr.append("----------------------\n");
			}
			qty +=expectedQty;
		}
		deliveryMap.put("pcode", doc.getCode());
		deliveryMap.put("itemInfos", itemInfoStr);
		
		Long bolDetailId = new Long(deliveryMap.get("bolDetailId").toString().trim());
		WmsBolDetail bolDetail = commonDao.load(WmsBolDetail.class, bolDetailId);
		deliveryMap.put("itemCode",bolDetail.getItemKey().getItem().getCode());
		deliveryMap.put("itemName",bolDetail.getItemKey().getItem().getName());
		deliveryMap.put("qty", qty);
		
		return deliveryMap;
		
	}
	/**
	 * 校验物料是否存在
	 * @param deliveryMap
	 * @return
	 * @throws RfBusinessException
	 */
	public Map queryItem(Map deliveryMap) throws RfBusinessException{
		String barCode = deliveryMap.get("itemCodeInput") ==null ? null : deliveryMap.get("itemCodeInput").toString();
		if(barCode == null){
			throw new RfBusinessException("物料条码/编码不能为空");
		}
		String itemCode = "";
		WmsItem item = null;
		if(WmsBarCodeParse.isBarCode(barCode)){
			Map map = WmsBarCodeParse.parse(barCode);
			itemCode = map.get(WmsBarCodeParse.KEY_ITEMCODE).toString().trim();
			item = this.getWmsItem(itemCode);
			if(null==item){
				deliveryMap.put("itemCodeInput", "");
				throw new RfBusinessException("货品不存在!!",deliveryMap);
			}
		}else{
			itemCode = barCode;
			item = this.getWmsItem(itemCode);
			if(null==item){
				deliveryMap.put("itemCodeInput", "");
				throw new RfBusinessException("货品不存在!!",deliveryMap);
			}
			if(WmsItemScanCode.SCANCODE_NO.equals(item.getUserFieldV10())){
				deliveryMap.put("itemCodeInput", "");
				throw new RfBusinessException("请扫描条码",deliveryMap);
			}
		}
		deliveryMap.put("itemId", item.getId());
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}
	/**
	 * 显示物料属性信息
	 */
	public Map showWmsItemInfo(Map deliveryMap) throws RfBusinessException{
		Long itemId = deliveryMap.get("itemId") == null ? null : Long.valueOf(deliveryMap.get("itemId").toString());
		WmsItem item = commonDao.load(WmsItem.class, itemId);
		deliveryMap.put("itemCode", item.getCode());
		deliveryMap.put("itemName", item.getName());
		if(!StringHelper.isNullOrEmpty(item.getUserFieldV1())){
			deliveryMap.put("WmsItemHandOverAtt", item.getUserFieldV1().equals(WmsItemHandOverAtt.LINE_EDGE) ? "线边交接":"T-1区交接");
		}else{
			deliveryMap.put("WmsItemHandOverAtt","");
		}
		if(!StringHelper.isNullOrEmpty(item.getUserFieldV2())){
			if(WmsItemJITAtt.NO_JIT.equals(item.getUserFieldV2())){
				deliveryMap.put("WmsItemJITAtt","非JIT");
			}
			if(WmsItemJITAtt.JIT_DOWNLINE_SETTLE.equals(item.getUserFieldV2())){
				deliveryMap.put("WmsItemJITAtt","JIT下线结算");
			}
			if(WmsItemJITAtt.JIT_UPLINE_SETTLE.equals(item.getUserFieldV2())){
				deliveryMap.put("WmsItemJITAtt","JIT上线结算");
			}
		}else{
			deliveryMap.put("WmsItemJITAtt","");
		}
		if(!StringHelper.isNullOrEmpty(item.getUserFieldV3())){
			deliveryMap.put("WmsItemUnPackingAtt","仓库不可拆包");
		}else{
			deliveryMap.put("WmsItemUnPackingAtt","");
		}
		return deliveryMap;
	}
	
	public Map deleteWorkDoc(Map deliveryMap) throws RfBusinessException{
		Long workDocId = deliveryMap.get("quickWorkDocId") ==null ? null : Long.valueOf(deliveryMap.get("quickWorkDocId").toString());
		if(null == workDocId){
			workDocId = deliveryMap.get(RfConstant.ENTITY_ID) == null ? null
					: Long.valueOf(deliveryMap.get(RfConstant.ENTITY_ID).toString());
		}
		if(workDocId == null){
			throw new RfBusinessException(RfMessageCode.RF_MESSAGE_PARAM_LOSE);
		}
		WmsWorkDoc wwd = commonDao.load(WmsWorkDoc.class, workDocId);
		tclworkDocManager.deleteWorkDoc(wwd);
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
		
	}
	
	public Map activeDetailBt(Map deliveryMap) throws RfBusinessException{
		Long bolId = new Long(deliveryMap.get("id").toString().trim());
		WmsBol bol = commonDao.load(WmsBol.class, bolId);
		if(bol.getDetails().isEmpty()){
			throw new RfBusinessException("交接单未添加明细,不能生效");
		}
		bol.setStatus(WmsBolStatus.ACTIVE);
		commonDao.store(bol);
		deliveryMap.put(RfConstant.FORWARD_VALUE, RfConstant.FORWARD_SUCCESS);
		return deliveryMap;
	}
	/**
	 *获取物料 
	 */
	private WmsItem getWmsItem(String code){
		WmsItem item = (WmsItem)commonDao.findByQueryUniqueResult("FROM WmsItem item where item.code=:ic", "ic", code);
		return item;
	}
}
