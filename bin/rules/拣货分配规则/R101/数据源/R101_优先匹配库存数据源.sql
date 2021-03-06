SELECT
 INV.ID AS "库存.库存ID",
 LOC.ID AS "库存.库位ID",
 ITEM.ID AS "库存.货品ID",
 UNIT.UNIT AS "库存.货品包装",
 INV.STATUS AS "库存.库存状态",
 INV.QTY AS "库存.库存数量",
 ITEM_KEY.STORAGE_DATE AS "库存.存储日期",
 ITEM_KEY.EXTEND_PROPC12 AS "库存.最小包装量"
 FROM WMS_INVENTORY INV
 LEFT JOIN WMS_ITEM_KEY ITEM_KEY ON ITEM_KEY.ID = INV.ITEM_KEY_ID
 LEFT JOIN WMS_ITEM ITEM ON ITEM.ID = ITEM_KEY.ITEM_ID
 LEFT JOIN WMS_LOCATION LOC ON LOC.ID = INV.LOCATION_ID
 LEFT JOIN WMS_ZONE AREA ON AREA.ID = LOC.ZONE_ID
 LEFT JOIN WMS_PACKAGE_UNIT UNIT ON UNIT.ID = INV.PACKAGE_UNIT_ID
WHERE 1=1
 AND LOC.INOUT_LOCK in ('INLOCKED','UNLOCKED')
 AND LOC.STATUS = 'ENABLED'
 AND INV.OPERATION_STATUS = 'NORMAL'
 AND LOC.TYPE = 'STORAGE'
 AND LOC.EXCEPTION_FLAG = 'N'
 AND LOC.COUNT_LOCK = 'N'
 AND INV.LOCK_STATUS = 'N'
 AND INV.QTY > 0
 /~仓库ID: AND LOC.WAREHOUSE_ID = {仓库ID}~/
 /~拣货库位定位分类: AND LOC.ALLOCATION_CATEGORY = {拣货库位定位分类}~/
 /~货品ID: AND ITEM.ID = {货品ID}~/
 /~货主ID: AND INV.COMPANY_ID = {货主ID}~/
 /~库存状态: AND INV.STATUS = {库存状态}~/
 /~优先匹配拼接条件: AND #{优先匹配拼接条件}~/
 AND  INV.ID NOT IN (
 SELECT
 INV.ID
 FROM WMS_INVENTORY INV
 LEFT JOIN WMS_ITEM_KEY ITEM_KEY ON ITEM_KEY.ID = INV.ITEM_KEY_ID
 LEFT JOIN WMS_ITEM ITEM ON ITEM.ID = ITEM_KEY.ITEM_ID
 LEFT JOIN WMS_LOCATION LOC ON LOC.ID = INV.LOCATION_ID
 LEFT JOIN WMS_ZONE AREA ON AREA.ID = LOC.ZONE_ID
 LEFT JOIN WMS_PACKAGE_UNIT UNIT ON UNIT.ID = INV.PACKAGE_UNIT_ID
WHERE 1=1
 AND LOC.INOUT_LOCK in ('INLOCKED','UNLOCKED')
 AND LOC.STATUS = 'ENABLED'
 AND INV.OPERATION_STATUS = 'NORMAL'
 AND LOC.TYPE = 'STORAGE'
 AND LOC.EXCEPTION_FLAG = 'N'
 AND LOC.COUNT_LOCK = 'N'
 AND INV.LOCK_STATUS = 'N'
 AND INV.QTY > 0
 /~仓库ID: AND LOC.WAREHOUSE_ID = {仓库ID}~/
 /~拣货库位定位分类: AND LOC.ALLOCATION_CATEGORY = {拣货库位定位分类}~/
 /~货品ID: AND ITEM.ID = {货品ID}~/
 /~货主ID: AND INV.COMPANY_ID = {货主ID}~/
 /~库存状态: AND INV.STATUS = {库存状态}~/
 /~强制匹配拼接条件: AND #{强制匹配拼接条件}~/)
