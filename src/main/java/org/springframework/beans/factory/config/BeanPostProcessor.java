package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * 后置处理器
 * 作用：处理已实例化后的bean
 */
public interface BeanPostProcessor {

    /**
     * 在bean执行初始化之前执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException;

    /**
     * 在bean执行初始化之后执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException;
}
