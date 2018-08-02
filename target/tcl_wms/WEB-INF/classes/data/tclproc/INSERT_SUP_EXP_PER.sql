create or replace procedure INSERT_SUP_EXP_PER(in_userid in number,error_message OUT VARCHAR2)
--错误信息 如果没有报错 则必须为空或者null
 /******************************************************************
*存储过程用途: 插入导出权限
*存储过程名称: 供应商导出maintain界面的权限
******************************************************************/

 IS
    tmp_exp_count number :=10000;

BEGIN 
  
delete from EXPORT_PERMISSIONS p where p.user_id=in_userid;


insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID,
CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
'maintainWmsDeliveryOrderDetailPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
'maintainWmsDeliveryOrderPage', tmp_exp_count, in_userid, 3, 1, 'admin' , SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
 'maintainPurchaseOrderPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
 'maintainWmsItemMinPackageQtyPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
'maintainWmsSupplierASNPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
 'maintainCheckOrderPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
'maintainPurchaseOrderDetailPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
 'maintainWmsInventorySupplerPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
 'maintainDownloadFilePage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
 'maintainWmsSupplierASNDetailPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
'maintainwmsTransportOrderPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);


insert into EXPORT_PERMISSIONS (ID, DISCRIMINATOR, ELEMENT_ID, LIMIT_NUM, USER_ID, BUSINESS_MODEL_ID, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME, BE_DELETE)
values (SEQ_EXPORTPERMISSIONS.NEXTVAL, 'com.vtradex.thorn.server.model.security.ExportPermission',
'maintainwmsTransportOrderDetailPage', tmp_exp_count, in_userid, 3, 1, 'admin', SYSDATE, 1, 'admin',SYSDATE, 0);

commit;
    EXCEPTION
        WHEN OTHERS THEN
        error_message:=substr(sqlerrm,1,1000);
                rollback;
END ;
