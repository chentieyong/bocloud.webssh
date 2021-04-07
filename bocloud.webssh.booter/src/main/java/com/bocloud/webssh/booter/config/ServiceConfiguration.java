package com.bocloud.webssh.booter.config;

import com.bocloud.common.enums.BoCloudService;
import com.bocloud.service.core.CurrentService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.reflect.ConstructorAccessor;
import sun.reflect.ReflectionFactory;

/**
 * 服务启动配置
 *
 * @author wangyu
 * @version 1.0
 * @since 2018年3月30日
 */
@Component
public class ServiceConfiguration implements InitializingBean {

    @Autowired
    private CurrentService service;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Class[] classes = new Class[3];
        classes[0] = String.class;
        classes[1] = int.class;
        classes[2] = String.class;
        ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();
        ConstructorAccessor accessor = reflectionFactory.newConstructorAccessor(BoCloudService.class.getDeclaredConstructor(classes));

        Object[] params = new Object[3];
        params[0] = "WEBSSH";
        params[1] = 1;
        params[2] = "webssh";
        BoCloudService service = (BoCloudService) accessor.newInstance(params);
        this.service.setService(service);
    }
}
