create or replace procedure rerun_fail_tasks

 /******************************************************************
*存储过程用途: 重新跑部分执行失败的task
******************************************************************/
 IS

BEGIN


--重新执行邮件发送失败的任务
UPDATE thorn_tasks A SET A.STATUS='READY' where a.type='SEND_EMAIL' and a.status='FAIL' and a.repeat_count<30;

--重新执行台账发送SAP 
UPDATE thorn_tasks A SET A.STATUS='READY' where a.subscriber='interfaceLogManager.sendWms2SapInterfaceLog' and a.status='FAIL' and a.repeat_count<30;

    COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
                rollback;
END ;
