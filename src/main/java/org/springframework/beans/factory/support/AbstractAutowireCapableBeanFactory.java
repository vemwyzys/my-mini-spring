package org.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Method;


/**
 * 实现可执行自动装配 的bean工厂
 */
public abstract class AbstractAutowireCapableBeanFactory
        extends AbstractBeanFactory
        implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    /**
     * 构建bean
     *
     * @param beanName
     * @param beanDefinition
     * @return
     * @throws BeansException
     */
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(beanName, beanDefinition);
    }

    /**
     * 实际构建bean的方法
     * 使用bean名和bean定义创建bean对象
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) {
        Object bean = null;
        try {
            bean = createBeanInstance(beanDefinition);
            //为bean填充属性
            applyPropertyValues(beanName, bean, beanDefinition);
            //执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed", e);
        }

        //注册有销毁方法的bean
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

        addSingleton(beanName, bean);
        return bean;
    }

    /**
     * 实例化bean
     *
     * @param beanDefinition
     * @return
     */
    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        return instantiationStrategy.instantiate(beanDefinition);
    }

    /**
     * 为bean充填属性
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        try {
            Class<?> beanClass = beanDefinition.getBeanClass();
            for (PropertyValue pv : beanDefinition.getPropertyValues().getPropertyValues()) {
                String name = pv.getName();
                Object value = pv.getValue();
                if (value instanceof BeanReference) {
                    //beanA依赖beanB，先实例化beanB
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                }

                //通过属性的set方法设置属性
//                Class<?> type = beanClass.getDeclaredField(name).getType();
//                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
//                Method method = beanClass.getDeclaredMethod(methodName, type);
//                method.invoke(bean, value);

                //通过反射设置属性  by hutool工具类
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values for bean: " + beanName, e);
        }
    }

    /**
     * 初始化bean
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @return
     */
    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {

        //工厂初始化bean之前,检测是否需要感知
        if (bean instanceof BeanFactoryAware) {
            //需要感知则让其持有工厂
            ((BeanFactoryAware) bean).setBeanFactoryAware(this);
        }

        //执行BeanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        //执行bean的初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Throwable ex) {
            throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", ex);
        }

        //执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        return wrappedBean;
    }


    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        //遍历所有的bean前置处理器
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            //找到beanName对应则处理
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 执行bean配置(beanDefinition持有初始化方法名)的初始化方法
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @throws Throwable
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Throwable {
        System.out.println("执行bean[" + beanName + "]的初始化方法");
        //如果bean本身实现了初始化bean接口,则直接调用方法
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        //如果发现有配置出来的初始化方法,则调用此方法
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getBeanClass(), initMethodName);
            if (initMethod == null) {
                throw new BeansException("Could not find an init method named '" + initMethodName
                        + "' on bean with name '" + beanName + "'");
            }
            initMethod.invoke(bean);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    /**
     * 注册有销毁方法的bean
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        //bean继承自DisposableBean 或 有自定义的销毁方法
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
        }
    }
}
