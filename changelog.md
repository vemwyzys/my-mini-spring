## 一、简单的bean容器，能够实例化bean

## 二、bean定义（BeanDefinition） & bean注册表（BeanDefinitionRegistry）
### 1.bean定义持有目标类文件
### 2.bean注册表 管理 所有bean定义
### 3.bean工厂通过「实现bean注册表，继承bean工厂的实例化实现」达成注册bean和创建bean

## 三、bean实例化策略
### 1.简单通过构造器创建
### 2.通过cglib方式（继承1）
### 3.工厂持有实例化bean的策略类接口

## 四、为bean填充属性
### 1.增加PropertyValue类，域为bean的属性类（属性名，属性值）
### 2.bean定义持有目标类文件和目标类文件的属性列表类
### 3.工厂创建bean时根据bean定义持有的属性map填充属性值

## 五、为bean注入bean
### 1.新增bean引用类(BeanReference),bean属性可以是bean引用类的对象，
### 2.当bean创建时后填充属性时发现属性是另外一个bean时，先实例化另外的这个bean（暂不考虑循环依赖）
