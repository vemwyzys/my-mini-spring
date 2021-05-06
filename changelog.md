## 一、实现简单的bean容器，能够实例化bean

## 二、bean定义（BeanDefinition） & bean注册表（BeanDefinitionRegistry）

|动作|类名|说明|
|:---|:---|:---|
|+|BeanDefinition|该类保存Bean定义。包括bean的名字String beanClassName、类型Class beanClass|
|+|BeanDefinitionRegistry|接口，定义可以注册BeanDefinition。管理所有的beanDefinition|
|+|BeanFactory|接口，定义可以获取Bean|
|+|AbstractAutowireCapableBeanFactory|抽象类，实现BeanFactory接口。具体实现了指定String beanName查找BeanDefinition去实例化出Bean的方法|

## 三、bean实例化策略

|动作|类名|说明|
|:---|:---|:---|
|+|InstantiationStrategy|策略接口，定义通过BeanDefinition实例化出bean|
|+|SimpleInstantiationStrategy|策略实现类，简单方式实现实例化出bean|
|+|CglibInstantiationStrategy|策略实现类，继承上一策略，cglib生成子类代理的方式|

### 1.工厂持有InstantiationStrategy的策略类接口

## 四、为bean填充属性

|动作|类名|说明|
|:---|:---|:---|
|+|PropertyValue|类，保存Bean的属性名String和属性值Object|
|+|PropertyValues|类，Property集合类|
|*|BeanDefinition|更新类：该类保存Bean定义。持有bean的名字String beanClassName、类型Class beanClass、属性列表PropertyValues propertyValues|

### 1.工厂创建bean时：在实例化成功后，根据bean定义持有的属性集合填充属性值

## 五、为bean注入bean

|动作|类名|说明|
|:---|:---|:---|
|+|BeanReference|类，定义Bean依赖另一个Bean时的属性，持有被依赖bean的名称|

### 1.当bean创建时后填充属性时发现属性是另外一个bean时，先实例化另外的这个bean（暂不考虑循环依赖）

## 六、资源、资源加载器、xml文件定义bean

|动作|类名|说明|
|:---|:---|:---|
|+|Resource|接口，标识一个外部资源。通过getInputStream()方法获取资源的输入流 （三个实现类：url、classpath下文件、文件系统下文件）|
|+|ResourceLoader|接口，资源加载器。通过getResource(String location)方法获取一个Resource对象|
|+|BeanDefinitionReader|接口，解析BeanDefinition的接口。通过loadBeanDefinitions(String)来从一个地址加载bean定义|
|+|AbstractBeanDefinitionReader|实现上面接口的抽象类。并为实现如何读取bean定义，但规范了此接口的基本结构：持有beanRegistry(用于将解析到的属性放到登记处)；内置一个resourceLoader，用于将获取到的资源地址适配到合适的获取inputStream的实现|
|+|XmlBeanDefinitionReader|具体实现了loadBeanDefinitions()方法，从xml文件中读取类定义|

## 七、调整bean工厂

|动作|类名|说明|
|:---|:---|:---|
|+|ListableBeanFactory|接口，定义返回所有指定class类型的实例|
|+|ConfigurableListableBeanFactory|接口，继承上面一个接口，增加定义根据名称查找beanDefinition&提前实例化所有单例实例|

### 具体工厂实现类 实现上面接口

## 八、开始增加bean post处理

|动作|类名|说明|
|:---|:---|:---|
|+|BeanFactoryPostProcessor|接口，持有工厂，在bean实例化之前就修改beanDefinition(工厂内部持有)|
|+|BeanPostProcessor|接口，被工厂持有此类的集合，用于修改实例化后的bean|
|+|AutowireCapableBeanFactory|接口，执行BeanPostProcessors的postProcessInitialization 的before、after方法|

####1.BeanFactoryPostProcess和BeanPostProcessor是spring框架中具有重量级地位的两个接口，理解了这两个接口的作用，基本就理解spring的核心原理了。为了降低理解难度分两个小节实现。

####2.BeanFactoryPostProcessor是spring提供的容器扩展机制，允许我们在bean实例化之前修改bean的定义信息即BeanDefinition的信息。其重要的实现类有PropertyPlaceholderConfigurer和CustomEditorConfigurer，PropertyPlaceholderConfigurer的作用是用properties文件的配置值替换xml文件中的占位符，CustomEditorConfigurer的作用是实现类型转换。BeanFactoryPostProcessor的实现比较简单，看单元测试BeanFactoryPostProcessorAndBeanPostProcessorTest#testBeanFactoryPostProcessor追下代码。

####3.BeanPostProcessor也是spring提供的容器扩展机制，不同于BeanFactoryPostProcessor的是，BeanPostProcessor在bean实例化后修改bean或替换bean。BeanPostProcessor是后面实现AOP的关键。BeanPostProcessor的两个方法分别在bean执行初始化方法（后面实现）之前和之后执行，理解其实现重点看单元测试BeanFactoryPostProcessorAndBeanPostProcessorTest#testBeanPostProcessor和AbstractAutowireCapableBeanFactory#initializeBean方法，有些地方做了微调，可不必关注。

## 九.应用上下文ApplicationContext
####1.BeanFactory是spring的基础设施，面向spring本身；而ApplicationContext面向spring的使用者，应用场合使用ApplicationContext。
####2.应用上下文ApplicationContext是spring中较之于BeanFactory更为先进的IOC容器，ApplicationContext除了拥有BeanFactory的所有功能外，还支持特殊类型bean如上一节中的BeanFactoryPostProcessor和BeanPostProcessor的自动识别、资源加载、容器事件和监听器、国际化支持、单例bean自动初始化等。
####3.具体实现查看AbstractApplicationContext#refresh方法即可。注意BeanFactoryPostProcessor和BeanPostProcessor的自定识别，这样就可以在xml文件中配置二者而不需要像上一节一样手动添加到容器中了。

## 十.bean初始化和销毁方法
在spring中，定义bean的初始化和销毁方法有三种方法：
- 在xml文件中制定init-method和destroy-method
- 继承自InitializingBean和DisposableBean
- 在方法上加注解PostConstruct和PreDestroy

第三种通过BeanPostProcessor实现，在扩展篇中实现，本节只实现前两种。

针对第一种在xml文件中指定初始化和销毁方法的方式，在BeanDefinition中增加属性initMethodName和destroyMethodName。

初始化方法在AbstractAutowireCapableBeanFactory#invokeInitMethods执行。DefaultSingletonBeanRegistry中增加属性disposableBeans保存拥有销毁方法的bean，拥有销毁方法的bean在AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary中注册到disposableBeans中。为了确保销毁方法在虚拟机关闭之前执行，向虚拟机中注册一个钩子方法，查看AbstractApplicationContext#registerShutdownHook（非web应用需要手动调用该方法）。当然也可以手动调用ApplicationContext#close方法关闭容器。

AbstractAutowireCapableBeanFactory#doCreateBean在实际构建bean时调用#registerDisposableBeanIfNecessary->注册需要被destroy的bean

## 十一.Aware接口
Aware是感知、意识的意思，Aware接口是标记性接口，其实现子类能感知容器相关的对象。常用的Aware接口有BeanFactoryAware和ApplicationContextAware，分别能让其实现者感知所属的BeanFactory和ApplicationContext。

让实现BeanFactoryAware接口的类能感知所属的BeanFactory，实现比较简单，查看AbstractAutowireCapableBeanFactory#initializeBean前三行。

实现ApplicationContextAware的接口感知ApplicationContext，是通过BeanPostProcessor。由bean的生命周期可知，bean实例化后会经过BeanPostProcessor的前置处理和后置处理。定义一个BeanPostProcessor的实现类ApplicationContextAwareProcessor，在AbstractApplicationContext#refresh方法中加入到BeanFactory中，在前置处理中为bean设置所属的ApplicationContext。

## 十二.bean作用域,增加prototype的支持
每次向容器获取prototype作用域bean时，容器都会创建一个新的实例。在BeanDefinition中增加描述bean的作用域的字段scope/singleton/prototype，创建prototype作用域bean时（AbstractAutowireCapableBeanFactory#doCreateBean），不往singletonObjects中增加该bean。prototype作用域bean不执行销毁方法，查看AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary方法。

## 十三.FactoryBean
FactoryBean是一种特殊的bean，当向容器获取该bean时，容器不是返回其本身，而是返回其FactoryBean#getObject方法的返回值，可通过编码方式定义复杂的bean。

实现逻辑比较简单，当容器发现bean为FactoryBean类型时，调用其getObject方法返回最终bean。当FactoryBean#isSingleton==true，将最终bean放进缓存中，下次从缓存中获取。改动点见AbstractBeanFactory#getBean。

## 十四.容器事件和事件监听器
ApplicationContext容器提供了完善的事件发布和事件监听功能。

|动作|类名|说明|
|:---|:---|:---|
|+|ApplicationEvent|应用事件类|
|+|自定义各个继承ApplicationEvent的事件类||
|+|ApplicationListener<E extends ApplicationEvent> extends EventListener|接口,订阅ApplicationEvent类的事件|
|+|自定义各个接收上面各个事件类的订阅类(实现ApplicationListener)||

ApplicationEventMulticaster接口是注册监听器和发布事件的抽象接口，AbstractApplicationContext包含其实现类实例作为其属性，使得ApplicationContext容器具有注册监听器和发布事件的能力(委托其完成发布事件)。
在AbstractApplicationContext#refresh方法中，会实例化ApplicationEventMulticaster、注册监听器并发布容器刷新事件ContextRefreshedEvent；
在AbstractApplicationContext#doClose方法中，发布容器关闭事件ContextClosedEvent。

## 十五.切点表达式
JoinPoint，织入点，指需要执行代理操作的某个类的某个方法(仅支持方法级别的JoinPoint)；
Pointcut是JoinPoint的表述方式，能捕获JoinPoint。

最常用的切点表达式是AspectJ的切点表达式。
需要匹配类，定义ClassFilter接口；
匹配方法，定义MethodMatcher接口。
PointCut需要同时匹配类和方法，包含ClassFilter和MethodMatcher,
AspectJExpressionPointcut是支持AspectJ切点表达式的PointCut实现，简单实现,仅支持execution函数。

## 十六.基于JDK的动态代理
AopProxy是获取代理对象的抽象接口，JdkDynamicAopProxy的基于JDK动态代理的具体实现。
TargetSource，被代理对象的封装。MethodInterceptor，方法拦截器，是AOP Alliance的"公民"，顾名思义，可以拦截方法，可在被代理执行的方法前后增加代理行为。

## 十七.基于CGLIB的动态代理
基于CGLIB的动态代理实现逻辑也比较简单，查看CglibAopProxy。
与基于JDK的动态代理在运行期间为接口生成对象的代理对象不同，
基于CGLIB的动态代理能在运行期间动态构建字节码的class文件，为类生成子类，因此被代理类不需要继承自任何接口。

## 十八. AOP代理工厂
增加AOP代理工厂ProxyFactory，由AdvisedSupport#proxyTargetClass属性决定使用JDK动态代理还是CGLIB动态代理。

## 十九. 几种常用的Advice：BeforeAdvice/AfterAdvice/AfterReturningAdvice/ThrowsAdvice...
Spring将AOP联盟中的Advice细化出各种类型的Advice，
常用的有BeforeAdvice/AfterAdvice/AfterReturningAdvice/ThrowsAdvice，
我们可以通过扩展MethodInterceptor来实现。

只简单实现BeforeAdvice，有兴趣的同学可以帮忙实现另外几种Advice。
定义MethodBeforeAdviceInterceptor拦截器，在执行被代理方法之前，先执行BeforeAdvice的方法。

## 二十. PointcutAdvisor：Pointcut和Advice的组合
Advisor是包含一个Pointcut和一个Advice的组合，Pointcut用于捕获JoinPoint，Advice决定在JoinPoint执行某种操作。
实现了一个支持aspectj表达式的AspectJExpressionPointcutAdvisor。

## 二十一. 动态代理融入bean生命周期
结合前面讲解的bean的生命周期，BeanPostProcessor处理阶段可以修改和替换bean，正好可以在此阶段返回代理对象替换原对象。
不过我们引入一种特殊的BeanPostProcessor——InstantiationAwareBeanPostProcessor，
如果InstantiationAwareBeanPostProcessor处理阶段返回代理对象，会导致短路，不会继续走原来的创建bean的流程，
具体实现查看AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation。

DefaultAdvisorAutoProxyCreator是处理横切逻辑的织入返回代理对象的InstantiationAwareBeanPostProcessor实现类，当对象实例化时，生成代理对象并返回。

## 二十二. 属性占位符 PropertyPlaceholderConfigurer
经常需要将配置信息配置在properties文件中，然后在XML文件中以占位符的方式引用。

实现思路很简单，在bean实例化之前，编辑BeanDefinition，解析XML文件中的占位符，然后用properties文件中的配置值替换占位符。
而BeanFactoryPostProcessor具有编辑BeanDefinition的能力，因此PropertyPlaceholderConfigurer继承自BeanFactoryPostProcessor。

## 二十三. 包扫描
结合bean的生命周期，包扫描只不过是扫描特定注解的类，提取类的相关信息组装成BeanDefinition注册到容器中。

在XmlBeanDefinitionReader中发现并解析**```<context:component-scan />```**标签，
扫描类组装BeanDefinition然后注册到容器中的操作在ClassPathBeanDefinitionScanner#doScan中实现。


