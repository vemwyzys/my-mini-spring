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