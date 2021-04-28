package org.springframework.context;

import java.util.EventListener;

/**
 * @param <E> 指定泛型
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    void onApplicationEvent(E event);
}
