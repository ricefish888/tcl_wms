-- 清除业务数据
delete from wms_received_record;
delete from wms_asn_detail;
delete from wms_asn;
delete from wms_inventory;
delete from wms_packing_detail;
delete from wms_packing;
delete from wms_bol_detail;
delete from wms_task;
delete from wms_item_key;
delete from wms_work_doc;
delete from wms_bol;
delete from wms_pt_detail_require;
delete from wms_pick_ticket_detail;
delete from wms_pick_ticket;
delete from wms_so_detail_require;
delete from wms_so_detail;
delete from wms_so;
delete from wms_inventory_log;
delete from wms_count_detail;
delete from Wms_Count_Record;
delete from wms_count_plan;
delete from wms_wave_doc;
delete from wms_inventory_log;
delete from wms_count_record;
delete from wms_count_detail;
delete from wms_count_plan;
delete from wms_po_detail;
delete from wms_po;
delete from thorn_interface_log;
update wms_dock_clustering set status='AVAILABLE';
update Wms_Location set count_Lock='N';
update Wms_Location set exception_Flag='N';

--清除基础数据
delete from wms_company;
delete from wms_master_group;
delete from wms_package_unit;
delete from wms_item;
delete from wms_bill_type;
delete from wms_lot_rule;
delete from wms_inventory_state;