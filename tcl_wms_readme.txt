edi需要在tomcat的catalina.sh中增加 -Dvtradex_edi=1   来判断是edi的机器   然后执行规则的时候需要设置session
比如：JAVA_OPTS='-server  -Xms1024m -Xmx4096m -XX:PermSize=1024M -XX:MaxPermSize=1024M -Dvtradex_edi=1'

前台页面hql中取用户loginName的session方式为 #{SESSION_LOGIN_NAME}

WmsItemKey 的 LotInfo 属性说明
supplierCode  供应商编码
storageDate   存储日期
extendPropC6  工单线体
extendPropc7  asn单号
extendPropc8  货主   0标准 2寄售
extendPropc9  供应商名称
extendPropc10 工厂编码
extendPropc11 工厂名称
extendPropc12 最小包装量

extendPropc13 工单号/线体   VMI交接单入库生成的asndetail需要
extendPropc14 线体生产日期  VMI交接单入库生成的asndetail需要
extendPropc15 pickticketdetail_id    VMI交接单入库生成的asndetail需要

extendPropc16  asndetail_id
extendPropc17  条码
extendPropc18  出库优先级。默认是0 如果是退料等需要优先出库的  则为1.
extendPropc19  库存地点  里面可能是W开头的SAP库位，这种是双经销的，货直接送到对应的客户那边。
extendPropC20  作业单号

物料WmsItem属性说明：
userFieldV1  交接属性  {@link WmsItemHandOverAtt}
userFieldV2  JIT属性   {@link WmsItemJITAtt}
userFieldV3  拆包属性  {@link WmsItemUnPackingAtt}
userFieldV4  采购组
userFieldV5  单位描述
userFieldV6  物料类型
userFieldV7  是否跨工厂物料
userFieldV8  物料组
userFieldV9  采购组描述
userFieldV10 是否录入编码{@link WmsItemScanCode}
userFieldD1  最小包装量

拣货单WmsPickticket属性说明
userField1   出库工厂编码
userField2   入库工厂编码  调拨需要用到
userField3  记录拣货单生成来源  
userField4   成本中心
userField5	备注
userField6	产线描述
userField7  产线

拣货单明细WmsPickTicketDetail属性说明
userField1   工单号   冰箱的拣配单作业确认时用来把工单号记录到库存上

ASN属性说明：
userField5   项目类别 0为标准2为寄售 
userField6   成本中心
userField7   工厂编码
userField8   JIT属性


WmsWorkDoc 作业单属性说明：
userField1 指定的库位  配送单用
userField2 配送类型   直接出库/线边交接    配送单用
userField3 JIT属性   上线结算/下线结算   JIT出库单用
userField4 备注
userField5 产线描述


WmsASNDetail属性说明:
userField1 预留单明细ID
userField2 工单明细ID
================================


1000	冰箱生产工厂
1100	冰箱生产工厂(外销)
2000	洗衣机生产工厂
2100	洗衣机生产工厂(外销)



RF的登录账号只能属于1个组织（仓库）。