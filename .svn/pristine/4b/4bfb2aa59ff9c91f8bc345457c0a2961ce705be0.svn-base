package com.vtradex.wms.server.helper;

import java.util.HashMap;
import java.util.Map;

import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.wms.server.utils.StringHelper;

/**
 * tcl 条码解析类
 */
public class WmsBarCodeParse {

	/** 物料编码*/
	public static final String KEY_ITEMCODE = "KEY_ITEMCODE";
	/** 批次*/
	public static final String KEY_LOTNO = "KEY_LOTNO";
	/**ASN_ID  2017年10月19日09:08:02 修改成_id 本来是asn_detail_id*/
	public static final String KEY_ASN_ID = "KEY_ASN_ID";
	
	
	public static final String KEY_SPLIT="_";
	 
	/**
	 * @Description:  编码规则  ：   物料编码_批号_asnID_占位符1_占位符2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map parse(String barcode) {
		
		String[] infos = barcode.split(KEY_SPLIT);
		
		Map result = new HashMap(); 
		result.put(WmsBarCodeParse.KEY_ITEMCODE, infos[0]); 
		result.put(WmsBarCodeParse.KEY_LOTNO, infos[1]);
		result.put(WmsBarCodeParse.KEY_ASN_ID, infos[2]);
		return result;
	}
	/**
	 * 判断是不是物料条码,是返回true,不是返回false
	 * @param barCode
	 * @return
	 */
	public static Boolean isBarCode(String barCode){
		if(StringHelper.isNullOrEmpty(barCode)){
			throw new BusinessException("条码不能为空");
		}
		if(barCode.indexOf(KEY_SPLIT)>0){
			return true;
		}
		return false;
		
	}

	 

}
