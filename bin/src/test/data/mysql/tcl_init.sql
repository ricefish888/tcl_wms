-- 库区关联sap仓库
alter table WMS_ZONE add SAPWAREHOUSE_ID NUMBER(19);
alter table WMS_ZONE add constraint WMS_ZONE_FK_SAP_WAREHOUSE foreign key (SAPWAREHOUSE_ID) references WMS_SAP_WAREHOUSE (ID);

ALTER TABLE WMS_DELIVERY_ORDER DROP COLUMN USER_ID;
ALTER TABLE PURCHASE_ORDER_DETAIL DROP CONSTRAINT UK_ASN_DETAIL;
ALTER TABLE PURCHASE_ORDER_DETAIL ADD CONSTRAINT UK_PURCHASE_ORDER_DETAIL UNIQUE(PURCHASE_ORDER_ID, LINE_NO);


insert into enumerate (id,enum_type,enum_value) values(seq_enumerate.nextval,'WmsWarningType','GEN_DELIVERY');
insert into enumerate (id,enum_type,enum_value) values(seq_enumerate.nextval,'WmsWarningType','QTXYZ');