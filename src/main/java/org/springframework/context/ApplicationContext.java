package org.springframework.context;

import org.springframework.beans.core.io.ResourceLoader;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * 应用上下文
 */
public interface ApplicationContext
        extends ListableBeanFactory,
        HierarchicalBeanFactory,
        ResourceLoader,
        ApplicationEventPublisher {
}
