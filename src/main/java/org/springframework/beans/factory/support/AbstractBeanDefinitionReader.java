package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.core.io.DefaultResourceLoader;
import org.springframework.beans.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private ResourceLoader resourceLoader;

    /**
     * 构造时就指定bean定义登记处
     * 使用默认的资源加载器
     *
     * @param registry
     */
    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(registry, new DefaultResourceLoader());
    }

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public void loadBeanDefinitions(String[] locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }
}
