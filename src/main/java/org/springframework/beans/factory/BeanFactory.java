package org.springframework.beans.factory;


import java.util.HashMap;
import java.util.Map;

public interface BeanFactory {

    /**
     * 获取bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName);

}
