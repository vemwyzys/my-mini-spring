package org.springframework.test.ioc;

import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.test.ioc.bean.Car;
import org.springframework.test.ioc.bean.Person;

import static org.assertj.core.api.Assertions.assertThat;

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void TestPopulateBeanWithPropertyValues() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "nick"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));

        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        beanFactory.registerBeanDefinition("person", beanDefinition);
        Person person = (Person) beanFactory.getBean("person");

        assertThat(person).isNotNull();
        assertThat(person.getAge()).isEqualTo(18);
        assertThat(person.getName()).isEqualTo("nick");
        System.out.println(person);
    }

    @Test
    public void testPopulateBeanWithBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //实例化Car实例
        PropertyValues propertyValues4Car = new PropertyValues();
        propertyValues4Car.addPropertyValue(new PropertyValue("brand", "porsche"));
        BeanDefinition beanDefinition4Car = new BeanDefinition(Car.class, propertyValues4Car);
        beanFactory.registerBeanDefinition("car", beanDefinition4Car);

        //注册Person实例
        PropertyValues propertyValues4Person = new PropertyValues();
        propertyValues4Person.addPropertyValue(new PropertyValue("name", "nick"));
        propertyValues4Person.addPropertyValue(new PropertyValue("age", 18));
        //Person实例依赖Car实例
        propertyValues4Person.addPropertyValue(new PropertyValue("car", new BeanReference("car")));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues4Person);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        assertThat(person).isNotNull();
        assertThat(person.getAge()).isEqualTo(18);
        assertThat(person.getName()).isEqualTo("nick");
        Car car = person.getCar();
        assertThat(car).isNotNull();
        assertThat(car.getBrand()).isEqualTo("porsche");
        System.out.println(person);

    }
}