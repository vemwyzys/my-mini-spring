package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * 执行BeanPostProcessors的postProcessBeforeInitialization方法
     * 初始化bean的前置处理
     *
     * @param existingBEan
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBEan, String beanName)
            throws BeansException;

    /**
     * 执行BeanPostProcessors的postProcessAfterInitialization方法
     * 初始化bean的后置处理
     *
     * @param existingBEan
     * @param beanName
     * @return
     * @throws BeansException
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBEan, String beanName)
            throws BeansException;
}