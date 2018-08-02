create or replace procedure init_storage_daily(error_message OUT VARCHAR2)

 /******************************************************************
*存储过程用途: 根据当前日期重新初始化库存日结  这个存储过程执行完成之后，java代码还需要继续执行
1.修改hashcode字段
2.传接口
******************************************************************/
 IS

BEGIN

delete from wms_storage_daily;

insert into wms_storage_daily
(
id,
compute_date,
item_id,
lotinfo_hashcode,
supplier_code,
extend_propc8,
extend_propc9,
extend_propc10,--工厂编码
extend_propc11,--工厂名称
extend_propc19,--库存地点
inventory_status,--库存状态,
unlim_count,--非限制库存
check_inventory,--待检库存
start_count,--期初库存
in_count,--入库数量
out_count,--出库数量
end_count,--期末数量
creator_id,
creator,
created_time,
last_operator_id,
last_operator,
update_time
)

select wseq_wms_storage_daily.nextval,
tmp.compute_date,
tmp.item_id,
tmp.lotinfo_hashcode,
tmp.supplier_code,
tmp.extend_propc8,
tmp.extend_propc9,
tmp.extend_propc10,
tmp.extend_propc11,
tmp.extend_propc19,
tmp.inventory_status,
tmp.unlim_count,
tmp.check_inventory,
tmp.start_count,
tmp.in_count,
tmp.out_count,
tmp.end_count,

1,'admin',sysdate,1,'admin',sysdate from (
select
--wseq_wms_storage_daily.nextval as id,
sysdate  as compute_date,
i.id as item_id,
''   as lotinfo_hashcode,
ik.supplier_code  as supplier_code,
ik.extend_propc8,
ik.extend_propc9,
ik.extend_propc10,
ik.extend_propc11,
case  when ik.extend_propc8='2' and inv.status='不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'V101'
   when ik.extend_propc8='2' and inv.status<>'不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'V001'
 when ik.extend_propc8='0' and inv.status='不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'H001'
   when ik.extend_propc8='0' and inv.status<>'不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'Y003'
     else ik.extend_propc19 end as extend_propc19,
INV.STATUS as inventory_status,

case when inv.status='待检' then 0 else SUM(inv.qty) end  as unlim_count,--非限制库存
case when inv.status<>'待检' then 0 else SUM(inv.qty) end  as check_inventory,--待检库存
0 as start_count,--期初库存
SUM(inv.qty)  as in_count,--入库数量
0   as out_count,--出库数量
SUM(inv.qty)   as end_count--期末数量



from wms_inventory inv inner join wms_location loc on inv.location_id = loc.id
inner join wms_item i on i.id=inv.item_id
inner join wms_item_key ik on ik.id = inv.item_key_id
where 1=1 and inv.qty>0  -- and i.code in('2104030032','2108020041','2101810047')
and (
    (loc.code = 'FHC' and  inv.operation_status='READY_OUT') -- 发货仓的待出
   or (inv.operation_status='NORMAL')
   or (loc.code = 'X111' and  inv.operation_status='READY_OUT') --X111 待出
   or (loc.code = 'T1' and  inv.operation_status='READY_OUT') --t1 待出
    or (loc.code = 'XBC' and  inv.operation_status='NORMAL') --线边仓 正常
    or (loc.code = 'XBFH' and  inv.operation_status='READY_OUT') --线边仓 正常
    or (loc.code = 'JIT' and  inv.operation_status='READY_OUT') --JIT 正常
    or(loc.code = 'X111' and  inv.operation_status='READY_IN') --上架没有确认 但是有上架计划 X111带入 SHC待出
    or(loc.code = 'TJC' and  inv.operation_status='READY_OUT') --退捡仓
  or(loc.code = 'HLC') --坏料仓库 但是有上架计划 X111带入 SHC待出


)
GROUP BY
i.id,
ik.supplier_code,
ik.extend_propc8,
ik.extend_propc9,
ik.extend_propc10,
ik.extend_propc11,
case  when ik.extend_propc8='2' and inv.status='不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'V101'
   when ik.extend_propc8='2' and inv.status<>'不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'V001'
 when ik.extend_propc8='0' and inv.status='不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'H001'
   when ik.extend_propc8='0' and inv.status<>'不良品' and nvl(ik.extend_propc19,'-') not like 'W%' then 'Y003'
     else ik.extend_propc19 end,
INV.STATUS 

) tmp
;


/**
--更新库存地点。

update wms_storage_daily a set a.extend_propc19='-' where a.extend_propc19 is null;
update wms_storage_daily a set a.extend_propc19 = 'V101' where a.extend_propc8='2' and a.inventory_status='不良品' and a.extend_propc19 not like 'W%';
update wms_storage_daily a set a.extend_propc19 = 'V001' where a.extend_propc8='2' and a.inventory_status<>'不良品'  and a.extend_propc19 not like 'W%';
update wms_storage_daily a set a.extend_propc19 = 'H001' where a.extend_propc8='0' and a.inventory_status='不良品'   and a.extend_propc19 not like 'W%';
update wms_storage_daily a set a.extend_propc19 = 'Y003' where a.extend_propc8='0' and a.inventory_status<>'不良品'  and a.extend_propc19 not like 'W%';
*/
  --  COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
          error_message:=substr(sqlerrm,1,1000);
                rollback;
END ;
