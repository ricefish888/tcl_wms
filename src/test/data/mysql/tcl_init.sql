-- 库区关联sap仓库
alter table WMS_ZONE add SAPWAREHOUSE_ID NUMBER(19);
alter table WMS_ZONE add constraint WMS_ZONE_FK_SAP_WAREHOUSE foreign key (SAPWAREHOUSE_ID) references WMS_SAP_WAREHOUSE (ID);

ALTER TABLE WMS_DELIVERY_ORDER DROP COLUMN USER_ID;
ALTER TABLE PURCHASE_ORDER_DETAIL DROP CONSTRAINT UK_ASN_DETAIL;
ALTER TABLE PURCHASE_ORDER_DETAIL ADD CONSTRAINT UK_PURCHASE_ORDER_DETAIL UNIQUE(PURCHASE_ORDER_ID, LINE_NO);


insert into enumerate (id,enum_type,enum_value) values(seq_enumerate.nextval,'WmsWarningType','GEN_DELIVERY');
insert into enumerate (id,enum_type,enum_value) values(seq_enumerate.nextval,'WmsWarningType','QTXYZ');

insert into enumerate (id,enum_type,enum_value) values(seq_enumerate.nextval,'WmsWorkDocType','OUT_DIREC');
insert into enumerate (id,enum_type,enum_value) values(seq_enumerate.nextval,'WmsWorkDocType','XBJJ');
insert into global_params (ID, DISCRIMINATOR, PARAM_ID, TYPE, 
GROUP_NAME, PARAM_VALUE, PARAM_BEAN, DESCRIPTION, MODULE, CREATOR_ID,
CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME) values
 (seq_globalparam.nextval, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'checkOrderDir', 'P_STRING', '对账单附件路径配置', '/home/vtradex/checkOrderFile/', null, '对账单附件生成的路径', 'wms5', null, null, null, null, null, null);

 
 insert into global_params (ID, DISCRIMINATOR, PARAM_ID, TYPE, 
GROUP_NAME, PARAM_VALUE, PARAM_BEAN, DESCRIPTION, MODULE, CREATOR_ID,
CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME) values
 (seq_globalparam.nextval, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'downloadFileDir', 'P_STRING', '导入模板下载路径配置', '/home/vtradex/wmsDownloadFile/', null, '导入模板下载路径配置', 'wms5', null, null, null, null, null, null);

