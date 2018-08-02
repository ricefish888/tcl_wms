create or replace procedure update_all_user_exp_per (error_message OUT VARCHAR2)
--错误信息 如果没有报错 则必须为空或者null
 /******************************************************************
*存储过程用途: 更新所有非供应商用户的导出权限
******************************************************************/
 IS
     CURSOR c_supuserdata IS

select u.id as userid from thorn_users u where u.login_name not in (
select s.code from wms_supplier s -- where s.code<>'XN'
);
      errorinfo varchar2(1000); --内容
      uinfo c_supuserdata%ROWTYPE;
BEGIN
    OPEN c_supuserdata;
    LOOP
          FETCH c_supuserdata INTO uinfo;
          EXIT WHEN c_supuserdata%NOTFOUND;

          INSERT_USER_EXP_PER(uinfo.userid,errorinfo);
    END LOOP;
    CLOSE c_supuserdata;
    COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
          dbms_output.put_line(sqlerrm);
        error_message:=substr(sqlerrm,1,1000);
                rollback;
END ;
