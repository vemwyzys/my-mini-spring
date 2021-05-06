package org.springframework.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.bean.Car;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PackageScanTest {

    @Test
    public void testScanPackage() throws Exception {

        //classpath:后字符串不能以package开头
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:test-package-scan.xml");

        Car car = applicationContext.getBean("car", Car.class);
        assertThat(car).isNotNull();
    }
}
