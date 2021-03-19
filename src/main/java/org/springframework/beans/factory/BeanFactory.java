package org.springframework.beans.factory;


public interface BeanFactory {

    /**
     * 获取bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

}
