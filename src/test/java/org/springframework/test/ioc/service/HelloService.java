package org.springframework.test.ioc.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class HelloService implements ApplicationContextAware, BeanFactoryAware {

    public ApplicationContext applicationContext;

    public BeanFactory beanFactory;

    private String foo;

    private String bar;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public void sayHello() {
        System.out.println(foo + " " + bar);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContextAware(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
