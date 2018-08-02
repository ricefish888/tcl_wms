package com.vtradex.wms.server.model.entity.warehouse;

import com.vtradex.thorn.server.annotation.UniqueKey;
import com.vtradex.thorn.server.model.Entity;
import com.vtradex.wms.server.model.enums.BaseStatus;
import com.vtradex.wms.server.model.entity.base.WmsSapWarehouse;

public class WmsZone extends Entity {
    private static final long serialVersionUID = 1L;
    
    /** SAP仓库 */
    private WmsSapWarehouse wmsSapWarehouse;
    /** 所属仓库*/
    @UniqueKey
    private WmsWarehouse warehouse;
    /** 库区代码*/
    @UniqueKey
    private String code;
    /** 库区名称*/
    private String name;
    /** 额定最低温度*/
    private Double lowTemperature = 0D;
    /** 额定最高温度*/
    private Double highTemperature = 0D;
    /** 库区说明*/
    private String description;
    /** x坐标*/
    private Integer x_Pos = 0;
    /** y坐标*/
    private Integer y_Pos = 0;  
    /** 
     * 状态
     * 
     * {@link BaseStatus}
     */
    private String status = BaseStatus.ENABLED;
    
    /**
     * ERP财务系统逻辑仓代码
     */
    private String erpCode;
    
    public WmsWarehouse getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(WmsWarehouse warehouse) {
        this.warehouse = warehouse;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getLowTemperature() {
        return lowTemperature;
    }
    public void setLowTemperature(Double lowTemperature) {
        this.lowTemperature = lowTemperature;
    }
    public Double getHighTemperature() {
        return highTemperature;
    }
    public void setHighTemperature(Double highTemperature) {
        this.highTemperature = highTemperature;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getX_Pos() {
        return x_Pos;
    }
    public void setX_Pos(Integer x_Pos) {
        this.x_Pos = x_Pos;
    }
    public Integer getY_Pos() {
        return y_Pos;
    }
    public void setY_Pos(Integer y_Pos) {
        this.y_Pos = y_Pos;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getErpCode() {
        return erpCode;
    }
    public void setErpCode(String erpCode) {
        this.erpCode = erpCode;
    }
    public WmsSapWarehouse getWmsSapWarehouse() {
        return wmsSapWarehouse;
    }
    public void setWmsSapWarehouse(WmsSapWarehouse wmsSapWarehouse) {
        this.wmsSapWarehouse = wmsSapWarehouse;
    }
}
