SELECT 
safetyInventory.ITEM_ID AS "安全库存集.货品ID",
item.CODE AS "安全库存集.货品代码",
item.ID AS "安全库存集.货品ID",
company.CODE AS "安全库存集.货主代码",
company.NAME AS "安全库存集.货主名称",
safetyInventory.TRIGGER_QTY AS "安全库存集.最小数量",
safetyInventory.MAX_QTY AS "安全库存集.最大数量"
from wms_safety_inventory safetyInventory 
LEFT JOIN wms_item item ON safetyInventory.ITEM_ID = item.ID 
LEFT JOIN wms_company company ON item.MASTER_GROUP_ID = company.MASTER_GROUP_ID 
LEFT JOIN wms_warehouse_company warehouseCompany on warehouseCompany.COMPANY_ID = company.ID
 WHERE 1=1 
 /~仓库ID: AND warehouseCompany.WAREHOUSE_ID = {仓库ID}~/
 /~补货区: AND safetyInventory.ALLOCATION_CATEGORY = {补货区}~/