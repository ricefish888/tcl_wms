package com.vtradex.wms.server.service.spring.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 
 * Tcl 客户化 bean定义处理
 * 
 * @author <a href="zhen.lei@vtradex.com">Yogurt_lei</a>
 * 
 * @date 2017年7月18日 下午9:59:38
 */
public class TclCustomPostProcessor implements BeanFactoryPostProcessor, BeanPostProcessor {
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 重新注册接口对应的实现
        DefaultListableBeanFactory register = (DefaultListableBeanFactory) beanFactory;
        register.registerBeanDefinition("wmsPickticketClientManager", beanFactory.getBeanDefinition("wmsTclPickticketClientManager"));
        register.registerBeanDefinition("wmsPickticketManager", beanFactory.getBeanDefinition("wmsTclPickticketManager"));
        register.registerBeanDefinition("wmsBolManager", beanFactory.getBeanDefinition("wmsTclBolManager"));
        register.registerBeanDefinition("wmsASNManager", beanFactory.getBeanDefinition("wmsTclASNManager"));
        register.registerBeanDefinition("wmsWorkDocManager", beanFactory.getBeanDefinition("wmsTclWorkDocManager"));
        register.registerBeanDefinition("wmsMessageManager", beanFactory.getBeanDefinition("tclMessageManager"));
        register.registerBeanDefinition("wmsInventoryManageManager", beanFactory.getBeanDefinition("wmsTclInventoryManageManager"));
        register.registerBeanDefinition("wmsInventoryManager", beanFactory.getBeanDefinition("wmsTclInventoryManager"));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
