package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.ioc.bean.Car;

import static org.assertj.core.api.Assertions.assertThat;

public class FactoryBeanTest {

    @Test
    public void testFactoryBean(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:factory-bean.xml");
        Object car = applicationContext.getBean("car");
//        assertThat(car.getBrand()).isEqualTo("porsche");
    }
}
