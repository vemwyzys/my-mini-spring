package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

/**
 * 实现该接口,能够感知BeanFactory
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactoryAware(BeanFactory beanFactory) throws BeansException;

}
