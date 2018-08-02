SELECT 
pickTicket.ID as "拣货单.ID"
FROM WMS_PICK_TICKET pickTicket
LEFT JOIN WMS_COMPANY company on company.ID=pickTicket.COMPANY_ID
LEFT JOIN WMS_WAREHOUSE warehouse on warehouse.id=pickTicket.WAREHOUSE_ID
LEFT JOIN WMS_LOCATION location on location.id=pickTicket.SHIP_LOCATION_ID
WHERE 1=1
and pickTicket .IS_EXECUTABLE='Y'
and pickTicket.STATUS='OPEN'
and pickTicket.WAVE_DOC_ID is null
and location.TYPE='SHIP'
/~货主编码: and company.code = {货主编码}~/
/~仓库ID: and warehouse.id={仓库ID}~/
/~拣货单起始时间: and pickTicket.ORDER_DATE >={拣货单起始时间}~/
/~拣货单截止时间: and pickTicket.ORDER_DATE <={拣货单截止时间}~/
/~承运商ID:and pickTicket.CARRIER_ID={承运商ID}~/