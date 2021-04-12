package org.springframework.test.aop;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.test.service.HelloService;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class PointcutExpressionTest {

    @Test
    public void testPointcutExpression() throws NoSuchMethodException {
        //创建切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* org.springframework.test.service.HelloService.*(..))");

        //指定类
        Class<HelloService> clazz = HelloService.class;
        //指定类的方法
        Method method = clazz.getDeclaredMethod("sayHello");

        //判断切点是否匹配此类
        assertThat(pointcut.matches(clazz)).isTrue();

        //判断切点是否匹配此方法
        assertThat(pointcut.matches(method, clazz)).isTrue();

    }
}
