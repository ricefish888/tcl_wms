-- 货主主数据
update wms_company set type='N' where type is null;
--拣货单是否越库
update wms_pick_ticket set allow_cross='N' where allow_cross is null;
--批次规则字段更新
update wms_lot_rule set TRACK_QA_DATE='N' where TRACK_QA_DATE is null;
update wms_lot_rule set TRACK_ERP_CODE='N' where TRACK_ERP_CODE is null;
ALTER TABLE `wms_bol_detail`
MODIFY COLUMN `BOL_ID`  bigint(20) NULL AFTER `ID`;

--库存锁定字段更新
update wms_inventory set lock_status = 'N' where lock_status is null;

-- 更新SO版本号
update wms_so set version =0  where version is null; 
-- 更新PT版本号
update wms_pick_ticket set version =0  where version is null; 
-- 更新wms_work_doc版本号
update wms_work_doc set version =0  where version is null; 
-- 更新wms_wave_doc版本号
update wms_wave_doc set version =0  where version is null; 
-- 更新wms_task版本号
update wms_task set version =0  where version is null; 
-- 更新wms_asn版本号
update wms_asn set version =0  where version is null; 
-- 更新wms_asn_detail版本号
update wms_asn_detail set version =0  where version is null; 
--新建版本字段
alter table wms_asn add version NUMBER(19);
alter table wms_work_doc add version NUMBER(19);

--表结构文档v1.8.9
-- 更新WMS_COUNT_RECORD是否已登记
update WMS_COUNT_RECORD set IS_COUNTED ='N'  where IS_COUNTED is null; 
-- 更新WMS_PT_DETAIL_REQUIRE是否允许修改
update WMS_PT_DETAIL_REQUIRE set ALLOW_MODIFIED ='Y'  where ALLOW_MODIFIED is null; 
-- 更新WMS_PT_DETAIL_REQUIRE是否允许修改
update WMS_LOCATION set CHECKING_SEQUENCE =0  where CHECKING_SEQUENCE is null; 

--表结构文档v1.9.0
--新建route线路字段
alter table WMS_PICK_TICKET add ROUTE varchar(100);
alter table WMS_CUSTOMER add ROUTE varchar(100);

--更新WMS_PO字段
alter table WMS_PO add EXPIRED_DATE DATE;
alter table WMS_PO add USER_FIELD_1 varchar(50);
alter table WMS_PO add USER_FIELD_2 varchar(50);
alter table WMS_PO add USER_FIELD_3 varchar(50);
alter table WMS_PO add USER_FIELD_4 varchar(50);
alter table WMS_PO add USER_FIELD_5 varchar(50);
alter table WMS_PO add USER_FIELD_6 varchar(50);
alter table WMS_PO add USER_FIELD_7 varchar(50);
--更新WMS_PO_DETAIL字段
alter table WMS_PO_DETAIL add USER_FIELD_1 varchar(50);
alter table WMS_PO_DETAIL add USER_FIELD_2 varchar(50);

--更新WMS_INVENTORY_LOG字段
alter table WMS_INVENTORY_LOG add PRODUCT_DATE DATE;
alter table WMS_INVENTORY_LOG add EXPIRE_DATE DATE;
alter table WMS_INVENTORY_LOG add LOT varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC1 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC2 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC3 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC4 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC5 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC6 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC7 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC8 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC9 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC10 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC11 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC12 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC13 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC14 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC15 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC16 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC17 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC18 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC19 varchar(50);
alter table WMS_INVENTORY_LOG add EXTEND_PROPC20 varchar(50);


--更新WMS_ITEM
ALTER TABLE WMS_ITEM ADD COUNT_LOT_RULE_ID NUMBER(19);
alter table WMS_ITEM add constraint fk_COUNT_LOT_RULE_ID foreign key(COUNT_LOT_RULE_ID) references WMS_LOT_RULE (ID);
--盘点登记表
ALTER TABLE WMS_COUNT_RECORD ADD INVENTORY_STATUS varchar(50);
ALTER TABLE WMS_COUNT_RECORD ADD PRODUCT_DATE date DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD EXPIRE_DATE date DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD QA_DATE date DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD SOI varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  ASN_CUSTOMER_BILL  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  SUPPLIER_CODE  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  SERIAL_NO  varchar(100) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  ERP_CODE  varchar(100) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC1  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC2  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC3  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC4  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC5  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC6  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC7  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC8  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC9  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC10  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC11  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC12  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC13  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC14  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC15  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC16  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC17  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC18  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC19  varchar(50) DEFAULT NULL;
ALTER TABLE WMS_COUNT_RECORD ADD  EXTEND_PROPC20  varchar(50) DEFAULT NULL;

--盘点方案
ALTER TABLE WMS_COUNT_PLAN ADD  COUNT_STATUS  char(1) ;

--任务组名称
ALTER TABLE WMS_TASK_GROUP ADD  NAME  char(200) ;

--PODetail增加字段
ALTER TABLE WMS_PO_DETAIL ADD  INVENTORY_STATUS  char(100) ;

--wms_packing表
alter table WMS_PACKING add USER_FIELD_1 varchar(100);
alter table WMS_PACKING add USER_FIELD_2 varchar(100);
ALTER TABLE WMS_PACKING ADD WAREHOUSE_ID NUMBER(19);
alter table WMS_PACKING add constraint fk_PACKING_WAREHOUSE_ID foreign key(WAREHOUSE_ID) references WMS_WAREHOUSE (ID);

--波次单新增字段
alter table WMS_WAVE_DOC add WEIGHT FLOAT;
alter table WMS_WAVE_DOC add VOLUME FLOAT;
update WMS_WAVE_DOC set WEIGHT=0  where WEIGHT is null;
update WMS_WAVE_DOC set VOLUME=0  where VOLUME is null;