package org.springframework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;

/**
 * 实现此接口,感知ApplicationContext
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContextAware(ApplicationContext applicationContext) throws BeansException;
}
