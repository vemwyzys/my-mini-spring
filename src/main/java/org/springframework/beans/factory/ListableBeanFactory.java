package org.springframework.beans.factory;

import org.springframework.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory {

    /**
     * 返回指定类型的所有实例
     *
     * @param type
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回定义的所有bean的名称
     *
     * @return
     */
    String[] getBeanDefinitionNames();

}
