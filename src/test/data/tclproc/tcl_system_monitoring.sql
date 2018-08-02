create or replace procedure tcl_system_monitoring

is
to_emailaddress varchar2(100) :='jianxiang.hang@vtradex.com'; --错误信息发送的邮箱

error_jhd_gd number(19) :=0;--拣货单明细没有工单明细对应关系
error_jhd_yl number(19) :=0;--拣货单明细没有预留明细对应关系'
error_jhd_pc number(19) :=0;--拣货单明细没有对应批次属性要求
error_vmijj number(19) :=0;--VMI交接出入库差异数据
error_pczj_fh number(19) :=0;--查询批次追踪的发货数量和工单上的发货数量不一致


begin

  --查询拣货单明细没有工单明细对应关系的数据
  --select distinct W.CODE,pick.code,pick.update_time,pick.created_time,pick.creator,oldpick.code
  select count(1) into error_jhd_gd
  from wms_pick_ticket_detail p
  left join wms_pick_ticket pick on p.pick_ticket_id=pick.id
  LEFT JOIN WMS_WAREHOUSE W ON PICK.WAREHOUSE_ID=W.ID
  left join wms_pick_ticket oldpick on pick.original_id=oldpick.id
  where p.id not in(
  select ptpd.pickticket_detail_id from production_d_ptd ptpd
  )
  and pick.status!='CLOSED'
  and pick.status!='FINISHED'
  AND PICK.USER_FIELD3='SCLLD'
  and p.expected_qty>0
  ;

  if error_jhd_gd >0 then
    send_email('SYSTEM','VTRADEX',to_emailaddress,
    '系统监控异常','查询拣货单明细没有工单明细对应关系的数据'||error_jhd_gd||'条异常，请核实！');
  end if;

  commit;

  --查询拣货单明细没有预留明细对应关系的数据及处理
 -- select distinct W.CODE,pick.code,pick.update_time,pick.created_time,pick.creator,oldpick.code
 select count(1) into error_jhd_yl  from wms_pick_ticket_detail p 
  left join wms_pick_ticket pick on p.pick_ticket_id=pick.id
  LEFT JOIN WMS_WAREHOUSE W ON PICK.WAREHOUSE_ID=W.ID
  left join wms_pick_ticket oldpick on pick.original_id=oldpick.id
  where p.id not in(
  select ptpd.pickticket_detail_id from reserved_d_ptd ptpd 
  )
  and pick.status!='CLOSED'
  and pick.status!='FINISHED'
  AND PICK.USER_FIELD3='YLCKD'
  and p.expected_qty>0
  ;
  
  if error_jhd_yl >0 then
    send_email('SYSTEM','VTRADEX',to_emailaddress,
    '系统监控异常','查询拣货单明细没有预留明细对应关系的数据'||error_jhd_yl||'条异常，请核实！');
  end if;

  commit;
  
  
  --拣货单明细没有对应批次属性要求
  select count(1) into error_jhd_pc 
   from wms_pick_ticket_detail p where 1=1 --and  p.expected_qty<=0.0000001
  and p.id not in (450487,655202)
  and not exists (select 1 from wms_pt_detail_require m where m.pick_ticket_detail_id=p.id);

  if error_jhd_pc >0 then
    send_email('SYSTEM','VTRADEX',to_emailaddress,
    '系统监控异常','拣货单明细没有对应批次属性要求的数据'||error_jhd_pc||'条异常，请核实！');
  end if;

  commit;



  --VMI交接出入库差异数据
  select count(1) into error_vmijj from (
  select I.CODE,sum(
  CASE WHEN a.type='VMI_OUT' and ( BL.CODE not in('SCLLD','DBCKD') ) then 0 else a.change_qty end) as qty from wms_inventory_log a 
  inner join wms_item i on i.id = a.item_id
  left join wms_pick_ticket pt on pt.code= a.related_bill_code 
  LEFT JOIN WMS_BILL_TYPE BL ON BL.ID  = PT.BILL_TYPE_ID
  where    a.type in ('VMI_IN','VMI_OUT') --and i.code='2107100005'
  GROUP BY I.CODE
   ) tmp where tmp.qty<>0;
   
  if error_vmijj >0 then
    send_email('SYSTEM','VTRADEX',to_emailaddress,
    '系统监控异常','VMI交接出入库差异数据'||error_vmijj||'条异常，请核实！');
  end if;

  commit;
  
  
  
  
 --查询批次追踪的发货数量和工单上的发货数量不一致
 
   --select t1.*,t2.*,t1.shiplotqty-t2.proqty 
   select count(1) into error_pczj_fh from (
  select i.code,NVL(sum(g.qty-g.return_qty),0) as shiplotqty
  from wms_shippinglot_truck g 
  inner join wms_item i on i.id = g.item_id
  where 1=1 and g.bill_type='SCLLD'
  and i.user_field_v2<>'JIT_DOWNLINE_SETTLE'
  group by i.code
  order by i.code
  ) t1 full join 
  (

  select i.code,NVL(sum(d.shipped_quantity_bu-d.old_shipped_quantitybu),0) as proqty
  from production_order_detail d inner join production_order o on o.id = d.production_order_id
  inner join wms_item i on i.id = d.item_id
  where 1=1 and (d.shipped_quantity_bu-d.old_shipped_quantitybu)>0
  and i.user_field_v2<>'JIT_DOWNLINE_SETTLE'
  group by i.code
  order by i.code
  ) t2 on t1.code = t2.code
  where   round(NVL(t1.shiplotqty,0),3)<>round(NVL(t2.proqty,0),3)

  ;
   if error_pczj_fh >0 then
    send_email('SYSTEM','VTRADEX',to_emailaddress,
    '系统监控异常','查询批次追踪的发货数量和工单上的发货数量不一致数据'||error_pczj_fh||'条异常，请核实！');
  end if;

  commit;
  
  
   
  
end;

