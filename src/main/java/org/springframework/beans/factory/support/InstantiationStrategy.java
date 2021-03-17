package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 实例化策略
 */
public interface InstantiationStrategy {

    /**
     * 实例化方法
     *
     * @param beanDefinition
     * @return
     * @throws BeansException
     */
    Object instantiate(BeanDefinition beanDefinition) throws BeansException;

}
