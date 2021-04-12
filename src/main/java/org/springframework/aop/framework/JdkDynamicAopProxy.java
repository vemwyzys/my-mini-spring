package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    /**
     * 返回代理对象
     *
     * @return
     */
    @Override
    public Object getProxy() {
        //Proxy.newProxyInstance()参数
        //loader:获取类加载器
        //interfaces:动态代理类需要实现的接口
        //h:动态代理方法在执行时,会去调用h里面的invoke方法区执行
        return Proxy.newProxyInstance(getClass().getClassLoader(), advised.getTargetSource().getTargetClass(), this);
    }

    /**
     * @param proxy  代理对象,newProxyInstance方法的返回对象
     * @param method 调用的方法
     * @param args 方法中的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //这里会把被调用的方法名传入, 在内部进行判断matches是不是需要被代理的
        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getTarget().getClass())) {
            //代理方法
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(advised.getTargetSource().getTarget(), method, args));
        }
        return method.invoke(advised.getTargetSource().getTarget(), args);
    }
}
