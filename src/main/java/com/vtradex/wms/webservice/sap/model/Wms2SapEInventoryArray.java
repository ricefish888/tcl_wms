package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

@XmlDataNote("Wms2SapEInventoryArray")
public class Wms2SapEInventoryArray {

	private Wms2SapEInventory[] wms2SapEInventorys;

	public Wms2SapEInventory[] getWms2SapEInventorys() {
		return wms2SapEInventorys;
	}

	public void setWms2SapEInventorys(Wms2SapEInventory[] wms2SapEInventorys) {
		this.wms2SapEInventorys = wms2SapEInventorys;
	}
	
}
