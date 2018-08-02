# /Users/administrator/software/maven-1.0.2/repository/cxf/jars/apache-cxf-2.6.2.tar/bin/wsdl2java -frontend jaxws21 -p com.vtradex.edi.server.webservice.client.sap -d $(pwd)/src/main/java -client http://pidev01.tcl.com:50000/dir/wsdl?p=sa/759e3fa20e4c359290d5f07170734a57

#sap公共的处理接口回传服务
CXF_HOME=/Users/administrator/software/maven-1.0.2/repository/cxf/jars/apache-cxf-2.6.2.tar

#sap_commcallback.wsdl  wms调用此接口返回wms的数据处理结果
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.commcallback -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/sap_commcallback.wsdl


#sap_wms2sapdelivery.wsdl  wms调用此接口把生成的交货单回传sap。
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.delivery -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/sap_wms2sapdelivery.wsdl


#wmsInv2sap.wsdl WMS日结库存传输SAP
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.inv -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/wmsInv2sap.wsdl


#wmsItem2sap.wsdl WMS物料属性回传SAP
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.item -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/wmsItem2sap.wsdl

#wmsSupplierConfirm.wsdl 供应商确认
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.supplier -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/wmsSupplierConfirm.wsdl

#cancelReceive.wsdl 取消收货
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.cancelreceive -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/cancelReceive.wsdl

#returnBack.wsdl 采购退货
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.returnback -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/returnBack.wsdl

#wmsquality2sap.wsdl 质检转合格 
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.quality -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/wmsquality2sap.wsdl

#receiveInfo.wsdl  采购收货入库
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.receiveinfo -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/receiveInfo.wsdl 

#resData.wsdl 预留出库回传
$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.resdata -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/resData.wsdl


#other.wsdl 其他收发料：
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.othership -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/other.wsdl

#报废出库：bfck.wsdl
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.bfck -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/bfck.wsdl

#外向交货单过账：saleOutDelivery.wsdl
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.saleoutdelivery -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/saleOutDelivery.wsdl

#生产工单发料/退料 prdMove.wsdl
#$CXF_HOME/bin/wsdl2java -frontend jaxws21 -p com.vtradex.wms.webservice.client.sap.prdmove -d $(pwd)/src/main/java -client $(pwd)/src/main/resources/wsdl/prdMove.wsdl

