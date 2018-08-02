create or replace procedure INSERT_USER_EXP_PER(in_userid in number,error_message OUT VARCHAR2)
--错误信息 如果没有报错 则必须为空或者null
 /******************************************************************
*存储过程用途: 插入导出权限
*存储过程名称: 用户所有权限页面的导出权限
******************************************************************/

 IS
    tmp_exp_count number :=50000;

BEGIN

delete from EXPORT_PERMISSIONS p where p.user_id=in_userid;


insert into EXPORT_PERMISSIONS

select SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
tmp.element_id, tmp_exp_count, in_userid, tmp.business_model_id, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0 

from (
select distinct p.element_id,r.id as role_id,r.business_model_id from permissions p 
inner join thorn_roles r on r.id = p.role_id
where 1=1 
and p.discriminator='page'
and instr(p.element_id,'.')  <= 0 --elementid不包含.
and p.role_id in (
  --查询所有角色
  select gr.role_id
  from thorn_group_user gu 
  inner join thorn_group_role gr on gr.group_id=gu.group_id
  where gu.user_id =in_userid
  union all
  select ru.role_id from thorn_role_user ru
  where ru.user_id=in_userid
  union all
  select 0 from dual
)
order by p.element_id
) tmp;


commit;
    EXCEPTION
        WHEN OTHERS THEN
        error_message:=substr(sqlerrm,1,1000);
                rollback;
END ;
