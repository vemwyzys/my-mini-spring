package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.core.io.DefaultResourceLoader;
import org.springframework.beans.factory.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextCloseEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象应用上下文
 */
public abstract class AbstractApplicationContext
        extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    /**
     * 事件发布者
     */
    private ApplicationEventMulticaster applicationEventMulticaster;

    /**
     * 上下文刷新:
     * 创建工厂,加载bean定义列表
     * 加载bean前置处理器列表
     * 执行BeanFactoryPostProcessor,以修改beanDefinition
     * 加载所有bean的后置处理器
     *
     * @throws BeansException
     */
    @Override
    public void refresh() throws BeansException {
        //创建BeanFactory,并加载BeanDefinition
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        //感知bean
        ///这里手动添加一个BeanPostProcessor(ApplicationContextAwareProcessor)->让继承ApplicationContextAware的bean能感知bean
        beanFactory.addBeanPostProcessor(new ApplicationContextProcessor(this));
        //在bean实例化之前,执行BeanFactoryPostProcessor,以修改beanDefinition
        invokeBeanFactoryPostProcessors(beanFactory);

        //BeanPostProcessor需要在其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        //初始化事件发布者
        initApplicationEventMulticaster();
        //注册事件监听者
        registerListeners();

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();

        //发布容器刷新完成事件
        finishRefresh();
    }

    /**
     * 创建BeanFactory,并加载BeanDefinition
     *
     * @throws BeansException
     */
    protected abstract void refreshBeanFactory() throws BeansException;

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    /**
     * 执行BeanFactoryPostProcessor
     *
     * @param beanFactory
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        //获取工厂里已经注册了的所有beanFactoryPostProcessor
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            //执行所有beanFactoryPostProcessor,以替换beanDefinition里面的属性
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 注册BeanPostProcessor
     *
     * @param beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 初始化事件发布者
     */
    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
    }

    /**
     * 注册事件监听器
     */
    protected void registerListeners() {
        //将所有ApplicationListener的实现类注册到事件发布者(applicationEventMulticaster)
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }
    }

    protected void finishRefresh() {
        publishEvent(new ContextCloseEvent(this));
    }


    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public void close() {
        doClose();
    }

    protected void doClose() {
        //发布容器关闭事件
        publishEvent(new ContextCloseEvent(this));

        //执行单例bean的销毁方法
        destroyBeans();
    }

    protected void destroyBeans() {
        getBeanFactory().destroySingletons();
    }

    @Override
    public void registerShutdownHook() {
        Thread shutdownHook = new Thread(this::doClose);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
