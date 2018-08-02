create or replace procedure send_email (
in_emailtype in varchar2, --邮件类型 SYSTEM
in_receiver in varchar2, --收件人名称
in_toemail in varchar2, --收件人email地址
in_subject in varchar2,--邮件主题
in_content in varchar2 --邮件内容
)
/***
发送邮件专用的存储过程
**/
is
  new_emailrecord_id number(19); --email_record 表的序列
begin

  --查询email_record 的下一个序列
  select oseq_email_record.nextval into new_emailrecord_id from dual;


  insert into email_record
  (ID, RECEIVER, THEME, CONTENT,
   ATTACH_PATH, BEGIN_TIME, LAST_SEND_TIME, SEND_COUNT, STATUS,
   CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME,
   EMAIL_ADDRESS, EMAIL_TO,
   ATTACH_NAME, EMAIL_TYPE, EMAIL_CC, ERROR_INFO, REALATE_CODE)
  values (
  new_emailrecord_id,
  in_receiver,
  in_subject,
  in_content,
  null, sysdate, null, 0, 'READY',
    1,'admin', SYSDATE, 1, 'admin', SYSDATE,
   null, in_toemail,
   null, in_emailtype, null, null, null);

  ---------
  insert into thorn_tasks
  (ID, TYPE, SUBSCRIBER,
  MESSAGE_ID, CREATE_TIME, START_TIME, NEXT_PROCESS_TIME, END_TIME, STATUS, REPEAT_COUNT)
  values (seq_thorn_task.nextval, 'SEND_EMAIL', 'interfaceLogManager.sendEmail',
   new_emailrecord_id, sysdate, null, sysdate, null, 'READY', 0);

COMMIT;

end;
