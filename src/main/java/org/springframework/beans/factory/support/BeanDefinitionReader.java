package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.core.io.Resource;
import org.springframework.beans.core.io.ResourceLoader;

/**
 * 读取bean定义信息
 * BeanDefinition的接口
 */
public interface BeanDefinitionReader {

    /**
     * 获取bean定义注册处
     *
     * @return
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 获取资源加载器
     *
     * @return
     */
    ResourceLoader getResourceLoader();

    /**
     * 通过 资源 加载bean定义
     *
     * @param resource
     * @throws BeansException
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 通过location字符串加载bean定义
     *
     * @param location
     * @throws BeansException
     */
    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String[] locations) throws BeansException;
}
