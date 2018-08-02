package com.vtradex.wms.webservice.sap.model;

import com.vtradex.wms.server.service.annotation.XmlDataNote;

@XmlDataNote("Wms2SapInventoryLedgerArray")
public class Wms2SapInventoryLedgerArray {
	 
	private Wms2SapInventoryLedger[] wms2SapInventoryLedgers;

	public Wms2SapInventoryLedger[] getWms2SapInventoryLedgers() {
		return wms2SapInventoryLedgers;
	}

	public void setWms2SapInventoryLedgers(Wms2SapInventoryLedger[] wms2SapInventoryLedgers) {
		this.wms2SapInventoryLedgers = wms2SapInventoryLedgers;
	}
 
	
	
}
