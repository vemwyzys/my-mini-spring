package org.springframework.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.service.HelloService;

import static org.assertj.core.api.Assertions.assertThat;

public class AwareInterfaceTest {

    @Test
    public void test(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        HelloService helloService = ((HelloService) applicationContext.getBean("helloService"));

        assertThat(helloService.applicationContext).isNotNull();
    }
}
