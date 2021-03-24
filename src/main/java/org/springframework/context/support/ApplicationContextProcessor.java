package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 在bean实例化之前检测是否实现了 感知接口
     * 是则使其持有应用上下文
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContextAware(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
