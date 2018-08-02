create or replace procedure bak_inv_everyday

 /******************************************************************
*存储过程用途: 每天晚上定时提取一份当前库存 保留  方便以后核查。由oracle的job调用，建议每天晚上23:55调用
******************************************************************/
 IS

BEGIN

delete from wms_tcl_inv_bak_day a where trunc(sysdate) = trunc(a.bak_time);

insert into  wms_tcl_inv_bak_day
select
sysdate as bak_time,
ik.extend_propc8,
ik.extend_propc10,
i.code,
INV.STATUS,
ik.supplier_code,
SUM(inv.qty) as qty
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
ik.extend_propc8,
ik.extend_propc10,
i.code,
INV.STATUS,
ik.supplier_code

ORDER BY ik.extend_propc10,
i.code,
INV.STATUS,
ik.supplier_code
;

 /***
  查询语句如下

  select tmp.日期,tmp.项目类别,tmp.工厂,tmp.物料,tmp.供应商,
  sum(tmp.待检库存),sum(tmp.合格库存),sum(tmp.不良品库存) from (
  select to_char(m.bak_time,'yyyy-MM-dd') as 日期,m.extend_propc8 as 项目类别,m.extend_propc10 工厂,m.code 物料,
  m.supplier_code as 供应商,
case when m.status='待检' then m.qty else 0 end as 待检库存,
  case when m.status='合格' then m.qty else 0 end as 合格库存，
    case when m.status='不良品' then m.qty else 0 end as 不良品库存
 from wms_tcl_inv_bak_day m
 where to_char(m.bak_time,'yyyy-MM-dd')= '2017-11-30'
 ) tmp
 group by tmp.日期,tmp.项目类别,tmp.工厂,tmp.物料,tmp.供应商
 ;

 */




  COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
                rollback;
END ;
