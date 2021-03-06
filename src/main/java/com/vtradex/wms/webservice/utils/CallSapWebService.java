package com.vtradex.wms.webservice.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.vtradex.wms.webservice.client.sap.bfck.DTScrapOut;
import com.vtradex.wms.webservice.client.sap.bfck.SIWmsScrapOutOB;
import com.vtradex.wms.webservice.client.sap.cancelreceive.DTReceiptUndo;
import com.vtradex.wms.webservice.client.sap.cancelreceive.SIWmsReceiptUndoOB;
import com.vtradex.wms.webservice.client.sap.commcallback.ZRFCWMSMSGRETURN;
import com.vtradex.wms.webservice.client.sap.commcallback.ZRFCWMSMSGRETURNPortType;
import com.vtradex.wms.webservice.client.sap.commcallback.ZRFCWMSMSGRETURNResponse;
import com.vtradex.wms.webservice.client.sap.commcallback.ZWMSMSGRET;
import com.vtradex.wms.webservice.client.sap.delivery.DTINDLV;
import com.vtradex.wms.webservice.client.sap.delivery.DTINDLV.ITEMS;
import com.vtradex.wms.webservice.client.sap.delivery.DTRetMsg;
import com.vtradex.wms.webservice.client.sap.delivery.SIWMSINBOUNDDLVOB;

import com.vtradex.wms.webservice.client.sap.inv.DTSTOCK;
import com.vtradex.wms.webservice.client.sap.inv.SIWmsStockOB;
import com.vtradex.wms.webservice.client.sap.invmove.DTStockMove;
import com.vtradex.wms.webservice.client.sap.invmove.SIWmsStockMoveOB;
import com.vtradex.wms.webservice.client.sap.item.DTMatAttr;
import com.vtradex.wms.webservice.client.sap.item.SIWmsMatAttrOB;
import com.vtradex.wms.webservice.client.sap.othership.DTOtherMove;
import com.vtradex.wms.webservice.client.sap.othership.SIWmsOtherMoveOB;
import com.vtradex.wms.webservice.client.sap.prdmove.DTPrdMove;
import com.vtradex.wms.webservice.client.sap.prdmove.SIWmsPrdMoveOB;
import com.vtradex.wms.webservice.client.sap.quality.DTINDInspect;
import com.vtradex.wms.webservice.client.sap.quality.SIWmsInspectOB;
import com.vtradex.wms.webservice.client.sap.receiveinfo.DTINDPOST;
import com.vtradex.wms.webservice.client.sap.receiveinfo.SIWmsIndPostOB;
import com.vtradex.wms.webservice.client.sap.resdata.DTResIssue;
import com.vtradex.wms.webservice.client.sap.resdata.SIWmsResIssueOB;
import com.vtradex.wms.webservice.client.sap.returnback.DTReceiptReturn;
import com.vtradex.wms.webservice.client.sap.returnback.SIWmsReceiptReturnOB;
import com.vtradex.wms.webservice.client.sap.saleoutdelivery.DTODVPOST;
import com.vtradex.wms.webservice.client.sap.saleoutdelivery.SIOutdeliveryPostOB;
import com.vtradex.wms.webservice.client.sap.supplier.DTVENConfirm;
import com.vtradex.wms.webservice.client.sap.supplier.SIWMSConfirmOB;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.base.SapCommonCallback;
import com.vtradex.wms.webservice.sap.base.SapCommonCallbackDetail;
import com.vtradex.wms.webservice.sap.model.Wms2SapDeliveryOrder;
import com.vtradex.wms.webservice.sap.model.Wms2SapEInventory;
import com.vtradex.wms.webservice.sap.model.Wms2SapEInventoryArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapInventoryLedger;
import com.vtradex.wms.webservice.sap.model.Wms2SapInventoryLedgerArray;
import com.vtradex.wms.webservice.sap.model.Wms2SapItemAttr;
import com.vtradex.wms.webservice.sap.model.Wms2SapSupplierDocStatus;

public class CallSapWebService {

	/***
	 * 交货单回传 wms生成的交货单传送给sap
	 */
	public static DTRetMsg callSapDelivery(Wms2SapDeliveryOrder deliveryOrder) {

		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_DELIVERY); // 交货单即可
		factory.setServiceClass(SIWMSINBOUNDDLVOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);

		SIWMSINBOUNDDLVOB port = (SIWMSINBOUNDDLVOB) factory.create();
		DTINDLV d = new DTINDLV();
		d.setCOMMAND(""); // 为空即可
		d.setCOUNT("1");// 明细总条数  1个交货单1行明细
		d.setDLVNO(deliveryOrder.getDoNo());// WMS交货单号

		ITEMS i = new ITEMS();

		i.setLINENO(deliveryOrder.getLineNo()); // 行项目

		i.setEBELN(deliveryOrder.getPoNo()); // 采购订单
		i.setEBELP(deliveryOrder.getPoLineNo()); // 采购订单行
		i.setMATNR(deliveryOrder.getItemCode());// 物料编码
		i.setMENGE(deliveryOrder.getQuantity());// 数量
		i.setMEINS(deliveryOrder.getUnit());// 单位
		i.setWERKS(deliveryOrder.getFactoryCode());// 工厂编码
		i.setLFDAT(deliveryOrder.getLFDAT()); //yyyyMMdd格式
		i.setLGORT(""); // 库位 SAP仓库 可以为空
		
		d.getITEMS().add(i);

		DTRetMsg response = port.siWMSINBOUNDDLVOB(d);
		WebServiceHelper.println(response);
		return response;
	}

	/***
	 * 公共回传接口
	 */
	public static ZRFCWMSMSGRETURNResponse callCommCallBack(SapCommonCallback sapCommonCallback) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_COMMONCALLBACK);
		factory.setServiceClass(ZRFCWMSMSGRETURNPortType.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		ZRFCWMSMSGRETURNPortType port = (ZRFCWMSMSGRETURNPortType) factory.create();

		ZRFCWMSMSGRETURN pp = new ZRFCWMSMSGRETURN();

		// pp.setITYP(SapInterfaceType.SAP_COMMONCALLBACK_ITYPE_ITEM);
		pp.setITYP(sapCommonCallback.getItype()); // 接口类型
		pp.setILOGNO(sapCommonCallback.getMessageId());
		List<ZWMSMSGRET> sap_details = new ArrayList<ZWMSMSGRET>();

		for (SapCommonCallbackDetail d : sapCommonCallback.getSapCommonCallbackDetails()) {

			ZWMSMSGRET sap_detail = new ZWMSMSGRET();
			// sap_detail.setMSGID(d.getMessageId());
			if (d.getLineNo() != null && !d.getLineNo().equals("")) {
				BigInteger lineno = new BigInteger(d.getLineNo().trim());
				sap_detail.setLINENO(lineno);
			}
			sap_detail.setFLG(d.getFlag());
			sap_detail.setMSGTXT(d.getRemark());

			sap_details.add(sap_detail);
		}

		ZRFCWMSMSGRETURN.TABRET sap_details_Array = new ZRFCWMSMSGRETURN.TABRET();
		sap_details_Array.getItem().addAll(sap_details);
		pp.setTABRET(sap_details_Array);
		// 设置客户端的配置信息，超时等.
		Client proxy = ClientProxy.getClient(port);
		HTTPConduit http = (HTTPConduit) proxy.getConduit();
		http.getClient().setReceiveTimeout(0);
		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
		httpClientPolicy.setConnectionTimeout(6000000);// 连接超时(毫秒)
		httpClientPolicy.setAllowChunking(false);// 取消块编码
		httpClientPolicy.setReceiveTimeout(6000000);// 响应超时(毫秒)
		ZRFCWMSMSGRETURNResponse response = port.zrfcWMSMSGRETURN(pp);

		WebServiceHelper.println(response);

		return response;

	}
	
	/**
	 * 库存日结回传sap
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.inv.DTRetMsg callSapInventory(Wms2SapEInventoryArray wsias){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_INVENTORY);
		factory.setServiceClass(SIWmsStockOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		Wms2SapEInventory[] wsis = wsias.getWms2SapEInventorys();
		SIWmsStockOB port = (SIWmsStockOB) factory.create();
		DTSTOCK d = new DTSTOCK();
		d.setCOMMAND("");
		d.setCOUNT(wsis.length+"");
		
		
		for(Wms2SapEInventory wsi : wsis){
			com.vtradex.wms.webservice.client.sap.inv.DTSTOCK.ITEMS i = new com.vtradex.wms.webservice.client.sap.inv.DTSTOCK.ITEMS();
			i.setBUKRS(wsi.getBUKRS());
			i.setBMENG(wsi.getBMENG());
			i.setERDAT(wsi.getERDAT());
			i.setIMENG(wsi.getIMENG());
			i.setLGORT(wsi.getLGORT());
			i.setLIFNR(wsi.getLIFNR());
			i.setMAKTX(wsi.getMAKTX());
			i.setMATNR(wsi.getMATNR());
			i.setMENGE(wsi.getMENGE());
			i.setMENGE1(wsi.getMENGE1());
			i.setMENGE2(wsi.getMENGE2());
			i.setOMENG(wsi.getOMENG());
			i.setSOBKZ(wsi.getSOBKZ());
			i.setWERKS(wsi.getWERKS());
			d.getITEMS().add(i);
		}
		com.vtradex.wms.webservice.client.sap.inv.DTRetMsg response = port.siWmsStockOB(d);
		
		WebServiceHelper.println(response);
		return response;
	}
	/**
	 * 物料属性回传sap
	 * @param wia
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.item.DTRetMsg callSapItem(Wms2SapItemAttr wia){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_ITEM); //物料
		factory.setServiceClass(SIWmsMatAttrOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWmsMatAttrOB port = (SIWmsMatAttrOB) factory.create();
		DTMatAttr d = new DTMatAttr();
		d.setCOMMAND("");
		d.setCOUNT("1");
		
		DTMatAttr.ITEMS i = new DTMatAttr.ITEMS();
		i.setMATNR(wia.getItemCode());
		i.setZEIAR(wia.getJitAttr());
		i.setZEIVR(wia.getHandoverAttr());
		d.getITEMS().add(i);
		
		com.vtradex.wms.webservice.client.sap.item.DTRetMsg response = port.siWmsMatAttrOB(d);
		
		WebServiceHelper.println(response);
		return response;
	}
	/**
	 * 供应商确认回传sap
	 * @param wsds
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.supplier.DTRetMsg callSapSupplier(Wms2SapSupplierDocStatus wsds){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_SUPPLIER); //供应商
		factory.setServiceClass(SIWMSConfirmOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWMSConfirmOB port = (SIWMSConfirmOB) factory.create();
		DTVENConfirm d = new DTVENConfirm();
		d.setCOMMAND("");
		d.setCOUNT("1");
		d.setTYPE(wsds.getType());
		
		DTVENConfirm.ITEMS i = new DTVENConfirm.ITEMS();
		i.setORDER(wsds.getOrderNo());
		i.setSTATUS(wsds.getStatus());
		d.getITEMS().add(i);
		
		com.vtradex.wms.webservice.client.sap.supplier.DTRetMsg response = port.siWMSConfirmOB(d);
		
		WebServiceHelper.println(response);
		return response;
	}
	/**
	 * 取消收货回传SAP
	 * @param ledger
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.cancelreceive.DTRetMsg callSapCancelReceive(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_CANCELRECEIVE);
		factory.setServiceClass(SIWmsReceiptUndoOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWmsReceiptUndoOB port = (SIWmsReceiptUndoOB) factory.create();
		DTReceiptUndo d = new DTReceiptUndo();
		d.setCOMMAND("");
		
		
		
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setFRBNR(ledger.getFRBNR());
			d.setBUDAT(ledger.getBUDAT());
			d.setBLDAT(ledger.getBLDAT());
			
			DTReceiptUndo.ITEMS i = new DTReceiptUndo.ITEMS();
			i.setLINENO(ledger.getZEILE());
			i.setLFBNR(ledger.getLFBNR());//返回给SAP的单号凭证
			i.setLFPOS(ledger.getLFPOS());//返回给SAP的凭证行项目号
			i.setBWART(ledger.getBWART());
			i.setSGTXT(ledger.getSGTXT());
			i.setMENGE(ledger.getMENGE());
			d.getITEMS().add(i);
		 }
		
		com.vtradex.wms.webservice.client.sap.cancelreceive.DTRetMsg response = port.siWmsReceiptUndoOB(d);
		
		WebServiceHelper.println(response);
		return response;
		
	}
	
	/**
	 * 采购退货出库回传SAP
	 * 
	 */
	public static com.vtradex.wms.webservice.client.sap.returnback.DTRetMsg callSapReturn(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_RETURN);
		factory.setServiceClass(SIWmsReceiptReturnOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWmsReceiptReturnOB port = (SIWmsReceiptReturnOB) factory.create();
		DTReceiptReturn d = new DTReceiptReturn();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setFRBNR(ledger.getFRBNR());
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			d.setEKGRP(ledger.getEKGRP());
			d.setLIFNR(ledger.getLIFNR());
			
			DTReceiptReturn.ITEMS i = new DTReceiptReturn.ITEMS();
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setWERKS(ledger.getWERKS());
			i.setLGORT(ledger.getLGORT());
			i.setBWART(ledger.getBWART());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setMENGE(ledger.getMENGE());
			i.setSGTXT(ledger.getSGTXT());
			d.getITEMS().add(i);
		}
		com.vtradex.wms.webservice.client.sap.returnback.DTRetMsg response = port.siWmsReceiptReturnOB(d);
		
		WebServiceHelper.println(response);
		return response;
	}
	
	/**
	 * 质检转合格回传SAP
	 * @param ledger
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.quality.DTRetMsg callSapQuality(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_QUALITY);
		factory.setServiceClass(SIWmsInspectOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWmsInspectOB port = (SIWmsInspectOB) factory.create();
		DTINDInspect d = new DTINDInspect();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setFRBNR(ledger.getFRBNR());
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			
			DTINDInspect.ITEMS i = new DTINDInspect.ITEMS();
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setWERKS(ledger.getWERKS());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setMENGE(ledger.getMENGE());
			i.setMEINS(ledger.getMEINS());
			i.setSGTXT(ledger.getSGTXT());
			i.setBWART(ledger.getBWART());
			i.setUMLGO(ledger.getUMLGO());
			d.getITEMS().add(i);
		}
		com.vtradex.wms.webservice.client.sap.quality.DTRetMsg response = port.siWmsInspectOB(d);
		
		WebServiceHelper.println(response);
		return response;
		
	}
	/**
	 * 收货回传SAP
	 * @param ledger
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.receiveinfo.DTRetMsg callSapReceive(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_RECEIVE);
		factory.setServiceClass(SIWmsIndPostOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWmsIndPostOB port = (SIWmsIndPostOB) factory.create();
		DTINDPOST d = new DTINDPOST();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setFRBNR(ledger.getFRBNR());
			d.setBUDAT(ledger.getBUDAT());
			d.setBLDAT(ledger.getBLDAT());
			d.setXBLNR(ledger.getXBLNR());
			
			DTINDPOST.ITEMS i = new DTINDPOST.ITEMS();
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setWERKS(ledger.getWERKS());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setMENGE(ledger.getMENGE());
			i.setMEINS(ledger.getMEINS());
			i.setEBELN(ledger.getEBELN());
			i.setEBELP(ledger.getEBELP());
			i.setINSMK(ledger.getINSMK());
			i.setVBELNIM(ledger.getVBELN_IM());
			i.setVBELPIM(ledger.getVBELP_IM());
			i.setTYPEIM(ledger.getTYPE_IM());
			d.getITEMS().add(i);
		}
		
		com.vtradex.wms.webservice.client.sap.receiveinfo.DTRetMsg response = port.siWmsIndPostOB(d);
		
		WebServiceHelper.println(response);
		return response;
	}
	
	/**
	 * 预留发料
	 * @return
	 */
	public static com.vtradex.wms.webservice.client.sap.resdata.DTRetMsg callSapResData(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_RES);
		factory.setServiceClass(SIWmsResIssueOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		
		SIWmsResIssueOB port = (SIWmsResIssueOB) factory.create();
		
		DTResIssue d = new DTResIssue();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			d.setFRBNR(ledger.getFRBNR());
			
			DTResIssue.ITEMS i = new DTResIssue.ITEMS();
			i.setBWART(ledger.getBWART());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setMENGE(ledger.getMENGE());
			i.setRSNUM(ledger.getRSNUM());
			i.setRSPOS(ledger.getRSPOS());
			i.setSGTXT(ledger.getSGTXT());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setWERKS(ledger.getWERKS());
			d.getITEMS().add(i);
		}
		
		com.vtradex.wms.webservice.client.sap.resdata.DTRetMsg response = port.siWmsResIssueOB(d);
		
		WebServiceHelper.println(response);
		return response;
	}
	
	/**
	 * 生产订单发料、退料回传SAP
	 */
	public static com.vtradex.wms.webservice.client.sap.prdmove.DTRetMsg callSapPrdMove(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_PRD);
		factory.setServiceClass(SIWmsPrdMoveOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		SIWmsPrdMoveOB port = (SIWmsPrdMoveOB) factory.create();
		
		DTPrdMove d = new DTPrdMove();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			d.setFRBNR(ledger.getFRBNR());
			
			DTPrdMove.ITEMS i = new DTPrdMove.ITEMS();
			i.setBWART(ledger.getBWART());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setMENGE(ledger.getMENGE());
			i.setRSNUM(ledger.getRSNUM());
			i.setRSPOS(ledger.getRSPOS());
			i.setSGTXT(ledger.getSGTXT());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setWERKS(ledger.getWERKS());
			i.setAUFNR(ledger.getAUFNR());
			d.getITEMS().add(i);
		}
		
		com.vtradex.wms.webservice.client.sap.prdmove.DTRetMsg response = port.siWmsPrdMoveOB(d);
		WebServiceHelper.println(response);
		return response;
	} 
	
	/**
	 * 外向交货单回传SAP 
	 */
	public static com.vtradex.wms.webservice.client.sap.saleoutdelivery.DTRetMsg callSapOutDelivery(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_OUTDELIVERY);
		factory.setServiceClass(SIOutdeliveryPostOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		SIOutdeliveryPostOB port = (SIOutdeliveryPostOB) factory.create();
		
		DTODVPOST d = new DTODVPOST();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			d.setFRBNR(ledger.getFRBNR());
			d.setXBLNR(ledger.getXBLNR());
			
			DTODVPOST.ITEMS i = new DTODVPOST.ITEMS();
			i.setBWART(ledger.getBWART());
			i.setINSMK(ledger.getINSMK());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setMEINS(ledger.getMEINS());
			i.setMENGE(ledger.getMENGE());
			i.setSGTXT(ledger.getSGTXT());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setVBELNIM(ledger.getVBELN_IM());
			i.setVBELPIM(ledger.getVBELP_IM());
			i.setWERKS(ledger.getWERKS());
			d.getITEMS().add(i);
		}
		com.vtradex.wms.webservice.client.sap.saleoutdelivery.DTRetMsg response = port.siOutdeliveryPostOB(d);
		WebServiceHelper.println(response);
		return response;
	}
	
	/**
	 * 报废出库回传SAP
	 */
	public static com.vtradex.wms.webservice.client.sap.bfck.DTRetMsg callSapBFCK(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_BFCK);
		factory.setServiceClass(SIWmsScrapOutOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		SIWmsScrapOutOB port = (SIWmsScrapOutOB) factory.create();
		
		DTScrapOut d = new DTScrapOut();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			d.setFRBNR(ledger.getFRBNR());
			
			DTScrapOut.ITEMS i = new DTScrapOut.ITEMS();
			i.setBWART(ledger.getBWART());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setMENGE(ledger.getMENGE());
			i.setSGTXT(ledger.getSGTXT());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setWERKS(ledger.getWERKS());
			i.setKOSTL(ledger.getKOSTL());
			d.getITEMS().add(i);
		}
		
		com.vtradex.wms.webservice.client.sap.bfck.DTRetMsg response = port.siWmsScrapOutOB(d);
		WebServiceHelper.println(response);
		return response;
	}
	
	/**
	 * 其他发料回传SAP
	 */
	public static com.vtradex.wms.webservice.client.sap.othership.DTRetMsg callSapOtherMove(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_OTHER);
		factory.setServiceClass(SIWmsOtherMoveOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		SIWmsOtherMoveOB port = (SIWmsOtherMoveOB) factory.create();
		
		DTOtherMove d = new DTOtherMove();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			d.setFRBNR(ledger.getFRBNR());
			
			DTOtherMove.ITEMS i = new DTOtherMove.ITEMS();
			i.setBWART(ledger.getBWART());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setMENGE(ledger.getMENGE());
			i.setSGTXT(ledger.getSGTXT());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setWERKS(ledger.getWERKS());
			i.setKOSTL(ledger.getKOSTL());
			d.getITEMS().add(i);
		}
		
		com.vtradex.wms.webservice.client.sap.othership.DTRetMsg response = port.siWmsOtherMoveOB(d);
		WebServiceHelper.println(response);
		return response;
	}
	
	/**
	 * 调拨出库回传SAP
	 */
	public static com.vtradex.wms.webservice.client.sap.invmove.DTRetMsg callSapInvMove(Wms2SapInventoryLedgerArray ledgers){
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress(WsConstants.SAP_WS_ADDRESS_DBCK);
		factory.setServiceClass(SIWmsStockMoveOB.class);
		factory.setUsername(WsConstants.SAP_WS_USERNAME);
		factory.setPassword(WsConstants.SAP_WS_PASSWORD);
		SIWmsStockMoveOB port = (SIWmsStockMoveOB) factory.create();
		
		DTStockMove d = new DTStockMove();
		d.setCOMMAND("");
		Wms2SapInventoryLedger[] wms2SapInventoryLedgers = ledgers.getWms2SapInventoryLedgers();
		d.setCOUNT(wms2SapInventoryLedgers.length+"");
		for (Wms2SapInventoryLedger ledger: wms2SapInventoryLedgers) {
			d.setFRBNR(ledger.getFRBNR());
			d.setBLDAT(ledger.getBLDAT());
			d.setBUDAT(ledger.getBUDAT());
			
			DTStockMove.ITEMS i = new DTStockMove.ITEMS();
			i.setBWART(ledger.getBWART());
			i.setLGORT(ledger.getLGORT());
			i.setLIFNR(ledger.getLIFNR());
			i.setLINENO(ledger.getZEILE());
			i.setMATNR(ledger.getMATNR());
			i.setMENGE(ledger.getMENGE());
			i.setSGTXT(ledger.getSGTXT());
			i.setSOBKZ(ledger.getSOBKZ());
			i.setWERKS(ledger.getWERKS());
			i.setUMLGO(ledger.getUMLGO());
			i.setUMWRK(ledger.getUMWRK());
			d.getITEMS().add(i);
		}
		com.vtradex.wms.webservice.client.sap.invmove.DTRetMsg response = port.siWmsStockMoveOB(d);
		WebServiceHelper.println(response);
		return response;
		
	}
	

}
