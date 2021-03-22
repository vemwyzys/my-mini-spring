package org.springframework.beans.factory;


import org.springframework.beans.BeansException;

public interface BeanFactory {

    /**
     * 获取bean
     *
     * @param beanName
     * @return
     */
    Object getBean(String beanName);

    /**
     * 根据名称和类型查找bean
     *
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

}
