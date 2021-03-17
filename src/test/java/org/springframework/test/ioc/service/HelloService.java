package org.springframework.test.ioc.service;

public class HelloService {

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
}