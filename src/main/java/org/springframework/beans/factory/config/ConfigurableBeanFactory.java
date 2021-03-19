package org.springframework.beans.factory.config;

import org.springframework.beans.factory.BeanFactory;

/**
 * 定义可以管理 bean后置处理器集合
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
