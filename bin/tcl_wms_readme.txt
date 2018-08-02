

WmsItemKey 的 LotInfo 属性说明
supplierCode  供应商编码
storageDate   存储日期
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


物料WmsItem属性说明：
userFieldV1  交接属性  {@link WmsItemHandOverAtt}
userFieldV2  JIT属性   {@link WmsItemJITAtt}
userFieldV3  拆包属性  {@link WmsItemUnPackingAtt}
userFieldV4  采购组
userFieldV5  单位描述


拣货单WmsPickticket属性说明
userField1   工厂编码
userField2   工厂名称


ASN属性说明：
userField5   项目类别 0为标准2为寄售 


WmsWorkDoc 作业单属性说明：
userField1 指定的库位(当单据类型为快捷出库时)



================================


1000	冰箱生产工厂
1100	冰箱生产工厂(外销)
2000	洗衣机生产工厂
2100	洗衣机生产工厂(外销)



RF的登录账号只能属于1个组织（仓库）。