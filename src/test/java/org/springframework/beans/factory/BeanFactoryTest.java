package org.springframework.beans.factory;

import org.junit.Test;

public class BeanFactoryTest {

    @Test
    public void registerBean() {
        BeanFactory beanFactory = new BeanFactory();
        beanFactory.registerBean("helloService", new HelloService());
        HelloService helloService = (HelloService) beanFactory.getBean("helloService");
    }

    @Test
    public void getBean() {
    }

}