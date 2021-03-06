package com.whz.beanLifecycle;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class BeanLifeCycle {
    @Test
    public void LifeCycleInBeanFactory(){
        Resource res = new ClassPathResource("com/whz/beanLifecycle/bean.xml");
        BeanFactory bf = new XmlBeanFactory(res);

        // 注册 BeanDefinition 之后，在实例化 BeanDefinition之前调用
        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory((XmlBeanFactory) bf);

        MyInstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = new MyInstantiationAwareBeanPostProcessor();
        ((ConfigurableBeanFactory)bf).addBeanPostProcessor(instantiationAwareBeanPostProcessor);

        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        ((ConfigurableBeanFactory)bf).addBeanPostProcessor(beanPostProcessor);

        Car car1 = (Car)bf.getBean("car");
        car1.introduce();


//        Car car2 = bf.getBean("car", Car.class);
//        car2.introduce();
//        car2.setColor("红色");
        ((XmlBeanFactory)bf).destroySingletons();
    }

//    public static void main(String[] args) {
//        LifeCycleInBeanFactory();
//    }
}



// 配置：<bean id="car" class="com.whz.beanLifecycle.Car" init-method="myInit" destroy-method="myDestory" p:brand="红旗CA72" p:maxSpeed="200"/>


// 1、BeanFactoryPostProcessor.postProcessBeanFactory():该接口是在spring容器解析完配置文件（注册了BeanDefinition）之后，在bean实例化之前被调用的

// 2、InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation：在调用bean构造函数实例化前被调用
//    调用Car()构造函数
// 2、InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation：在调用bean构造函数实例后前被调用

// 3、InstantiationAwareBeanPostProcessor.postProcessPropertyValues：给bean的属性赋值之前调用
//    调用setBrand()设置属性。
//    BeanNameAware.setBeanName()
//    BeanFactoryAware.setBeanFactory()
//
// 4、BeanPostProcessor.postProcessBeforeInitialization：Bean执行初始化方法前被调用
//    5、InitializingBean.afterPropertiesSet：在设置了所有的bean属性之后，由BeanFactory调用
//    6、调用<bean>配置中init-method
// 4、BeanPostProcessor.postProcessAfterInitialization：Bean执行初始化方法后被调用
//
// 7、DisposableBean.destroy：在bean被销毁的时候调用
//
