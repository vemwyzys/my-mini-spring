## 一、实现简单的bean容器，能够实例化bean

## 二、bean定义（BeanDefinition） & bean注册表（BeanDefinitionRegistry）
|类名|说明|
|:---|:---|
|BeanDefinition|该类保存Bean定义。包括bean的名字String beanClassName、类型Class beanClass|
|BeanDefinitionRegistry|接口，定义可以注册BeanDefinition。管理所有的beanDefinition|
|BeanFactory|接口，定义可以获取Bean|
|AbstractAutowireCapableBeanFactory|抽象类，实现BeanFactory接口。具体实现了指定String beanName查找BeanDefinition去实例化出Bean的方法|

## 三、bean实例化策略
|类名|说明|
|:---|:---|
|InstantiationStrategy|策略接口，定义通过BeanDefinition实例化出bean|
|SimpleInstantiationStrategy|策略实现类，简单方式实现实例化出bean|
|CglibInstantiationStrategy|策略实现类，继承上一策略，cglib生成子类代理的方式|
### 1.工厂持有InstantiationStrategy的策略类接口

## 四、为bean填充属性
|类名|说明|
|:---|:---|
|PropertyValue|类，保存Bean的属性名String和属性值Object|
|PropertyValues|类，Property集合类|
|BeanDefinition|更新：该类保存Bean定义。持有bean的名字String beanClassName、类型Class beanClass、属性列表PropertyValues propertyValues|
### 1.工厂创建bean时：在实例化成功后，根据bean定义持有的属性集合填充属性值


## 五、为bean注入bean
|类名|说明|
|:---|:---|
|BeanReference|类，定义Bean依赖另一个Bean时的属性，持有被依赖bean的名称|
### 1.当bean创建时后填充属性时发现属性是另外一个bean时，先实例化另外的这个bean（暂不考虑循环依赖）

## 六、资源、资源加载器、xml文件定义bean
|类名|说明|
|:---|:---|
|Resource|接口，标识一个外部资源。通过getInputStream()方法获取资源的输入流 （三个实现类：url、classpath下文件、文件系统下文件）|
|ResourceLoader|接口，资源加载器。通过getResource(String location)方法获取一个Resource对象|
|BeanDefinitionReader|接口，解析BeanDefinition的接口。通过loadBeanDefinitions(String)来从一个地址加载bean定义|
|AbstractBeanDefinitionReader|实现上面接口的抽象类。并为实现如何读取bean定义，但规范了此接口的基本结构：持有beanRegistry(用于将解析到的属性放到登记处)；内置一个resourceLoader，用于将获取到的资源地址适配到合适的获取inputStream的实现|
|XmlBeanDefinitionReader|具体实现了loadBeanDefinitions()方法，从xml文件中读取类定义|