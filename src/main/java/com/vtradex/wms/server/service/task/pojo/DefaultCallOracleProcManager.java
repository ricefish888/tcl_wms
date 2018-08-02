package com.vtradex.wms.server.service.task.pojo;

import java.sql.CallableStatement;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.vtradex.thorn.server.dao.hibernate.HibernateCommonDao;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.service.task.CallOracleProcManager;
import com.vtradex.wms.server.utils.StringHelper;

/**
 * @Description:   调用oracle 存储过程专用 主要用于defaultcrontabmanager来调用oracle的过程做定时任务
 * @Author:        <a href="xuyan.xia@vtradex.net">夏绪焰</a>
 * @CreateDate:    Nov 12, 2012 3:36:34 PM
 * @version:       v1.0
 */
public class DefaultCallOracleProcManager extends DefaultBaseManager implements CallOracleProcManager {
    /***调用没有入参只有一个出参的存储过程 且出参是错误信息*/
    private String callNullInParamProc(String procName) {
        final String proc = procName;
        String error = (String) ((HibernateCommonDao) commonDao).getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                // 需要执行的存储过程
                String procedure = "{call "+proc+"(?)}";
                CallableStatement callableStatement = session.connection().prepareCall(procedure);
                // 注册输出参数
                callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
                callableStatement.execute();
                // 取回输出参数
                String result = callableStatement.getString(1);
                callableStatement.close();
                return result;
            }
        });
        if(StringHelper.isNullOrEmpty(error)) { 
            return "";
        }
        else {//如果错误信息不为空 表示出错  直接抛出异常 上层会接住
            throw new BusinessException(error);
        }
    }
    
    
    public String INSERT_SUP_EXP_PER(final Long supUserId) {
        String error = (String) ((HibernateCommonDao) commonDao).getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                // 需要执行的存储过程
                String procedure = "{call INSERT_SUP_EXP_PER(?,?)}";
                CallableStatement callableStatement = session.connection().prepareCall(procedure);
                callableStatement.setLong(1, supUserId); //输入参数
                // 注册输出参数
                callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
                callableStatement.execute();
                // 取回输出参数
                String result = callableStatement.getString(2);
                callableStatement.close();
                return result;
            }
        });
        if(StringHelper.isNullOrEmpty(error)) { 
            return "";
        }
        else {//如果错误信息不为空 表示出错  直接抛出异常 上层会接住
            throw new BusinessException(error);
        }
    }
    
    
    public String MOVE_THORN_TASK() {
        return callNullInParamProc("MOVE_THORN_TASK");
    }
    
    public String INIT_STORAGE_DAILY() {
        return callNullInParamProc("INIT_STORAGE_DAILY");
    }
    
//    /**
//     * 库存预警给采购员 并发短信
//     * */
//    public String ITEM_SAFEINVENTORY_PRO() {
//        return callNullInParamProc("ITEM_SAFEINVENTORY_PRO");
//    }
    

}
