/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.io.reader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xyz.yanghaoyu.flora.framework.constant.XmlTag;
import xyz.yanghaoyu.flora.framework.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.framework.core.beans.factory.config.BeanReference;
import xyz.yanghaoyu.flora.framework.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.framework.core.context.annotation.ClassPathBeanDefinitionScanner;
import xyz.yanghaoyu.flora.framework.core.io.Resource;
import xyz.yanghaoyu.flora.framework.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.util.ComponentUtil;
import xyz.yanghaoyu.flora.framework.util.IocUtil;
import xyz.yanghaoyu.flora.framework.util.StringUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:12<i/>
 * @version 1.0
 */

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionFileReader {
    private Function<String, Function<BeanDefinition, Function<BeanDefinitionRegistry, Boolean>>> shouldRegister =
            (beanName -> beanDefinition -> beanDefinitionRegistry -> {
                if (getRegistry().containsBeanDefinition(beanName)) {
                    throw new BeansException("Duplicate beanName [" + beanName + "] is not allowed");
                }
                return true;
            });

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    public void setShouldRegister(Function<String, Function<BeanDefinition, Function<BeanDefinitionRegistry, Boolean>>> shouldRegister) {
        this.shouldRegister = shouldRegister;
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException | ClassNotFoundException | ParserConfigurationException | SAXException e) {
            throw new BeansException("IOException parsing XML document from " + resource, e);
        }
    }

    @Override
    public void loadBeanDefinitions(Resource... resources) throws BeansException {
        for (Resource resource : resources) {
            loadBeanDefinitions(resource);
        }
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        Resource resource = this.getResourceLoader().getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(String... locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        Element root = doc.getDocumentElement();

        if (!Objects.equals(root.getNodeName(), XmlTag.BEANS)) {
            return;
        }
        parseComponentScan(root);
        parseEnableAop(root);
        parsePropertyPlaceholderConfigurer(root);
        parseBeans(root);
        IocUtil.enableTypeConvert(getRegistry());
    }


    private void parsePropertyPlaceholderConfigurer(Element root) {
        NodeList enablePropertyPlaceholderConfigurerList = root.getElementsByTagName(XmlTag.ENABLE_PROPERTY_SOURCE);
        if (enablePropertyPlaceholderConfigurerList.getLength() == 1) {
            Node tag = enablePropertyPlaceholderConfigurerList.item(0);
            String location = ((Element) tag).getAttribute("location");
            // 注册进容器
            IocUtil.enablePropertyPlaceholderConfigurer(getRegistry(), location);
        } else if (enablePropertyPlaceholderConfigurerList.getLength() > 1) {
            // 一个xml中只能出现一个 <enable-property-placeholder-configurer/>
            throw new BeansException("duplicate declaration <" + XmlTag.ENABLE_PROPERTY_SOURCE + "/> !");
        }
    }

    private void parseEnableAop(Element root) {
        NodeList aopNodeList = root.getElementsByTagName(XmlTag.ENABLE_AOP);
        if (aopNodeList.getLength() == 1) {
            IocUtil.enableAop(getRegistry());
        } else if (aopNodeList.getLength() > 1) {
            // 一个xml中只能出现一个 <enable-aop/>
            throw new BeansException("duplicate declaration <" + XmlTag.ENABLE_AOP + "/> !");
        }
    }

    private void parseComponentScan(Element root) {
        NodeList componentScanNodeList = root.getElementsByTagName(XmlTag.COMPONENT_SCAN);
        if (componentScanNodeList.getLength() == 1) {
            // do scanPackage
            Node componentScan = componentScanNodeList.item(0);
            if (componentScan instanceof Element) {
                String basePackage = ((Element) componentScan).getAttribute(XmlTag.BASE_PACKAGE);
                if (StringUtil.isEmpty(basePackage)) {
                    throw new BeansException("The value of " + XmlTag.BASE_PACKAGE + " attribute can not be empty or null");
                }
                String[] basePaths = basePackage.split(",");
                // 注入 AutowiredAnnotationProcessor 这样才能在 BeanProcessor 中注入属性
                IocUtil.enableAutowiredAnnotations(getRegistry());
                // 扫包
                Set<BeanDefinition> beanDefinitions = new ClassPathBeanDefinitionScanner().scan(basePaths);

                for (BeanDefinition beanDefinition : beanDefinitions) {
                    String beanName = ComponentUtil.determineBeanName(beanDefinition);
                    register(beanName, beanDefinition);
                }

            }
        } else if (componentScanNodeList.getLength() > 1) {
            // 一个xml中只能出现一个 <component-scan/>
            throw new BeansException("duplicate declaration <component-scan /> !");
        }
    }

    private void parseBeans(Element root) throws ClassNotFoundException {
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (!(item instanceof Element) || !Objects.equals(XmlTag.BEAN, childNodes.item(i).getNodeName())) {
                continue;
            }
            // 解析 bean 标签
            Element bean = (Element) childNodes.item(i);
            String clazzName = bean.getAttribute(XmlTag.CLASS);
            // String name = bean.getAttribute("name");
            String beanName = bean.getAttribute(XmlTag.ID);
            String initMethodName = bean.getAttribute(XmlTag.INIT_METHOD);
            String destroyMethodName = bean.getAttribute(XmlTag.DESTROY_METHOD);
            String beanScope = bean.getAttribute(XmlTag.SCOPE);
            // 解析 类
            Class<?> clazz = null;
            clazz = Class.forName(clazzName);
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // 解析 id
            // 优先级 id > name
            if (StringUtil.isEmpty(beanName)) {
                beanName = StringUtil.lowerFirstChar(clazz.getSimpleName());
            }
            // 解析初始化方法
            beanDefinition.setInitMethodName(initMethodName);
            // 解析销毁方法
            beanDefinition.setDestroyMethodName(destroyMethodName);
            // 解析 scope
            if (StringUtil.isNotEmpty(beanScope)) {
                beanDefinition.setScope(beanScope);
            }

            // 读取属性并填充
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                if (!(bean.getChildNodes().item(j) instanceof Element)) {
                    continue;
                }
                if (!XmlTag.PROPERTY.equals(bean.getChildNodes().item(j).getNodeName())) {
                    continue;
                }
                // 解析标签：property
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute(XmlTag.NAME);
                String attrValue = property.getAttribute(XmlTag.VALUE);
                String attrRef = property.getAttribute(XmlTag.REF);
                // 获取属性值：引入对象、值对象
                Object value = StringUtil.isNotEmpty(attrRef)
                        ? new BeanReference(attrRef)
                        : attrValue;
                // 创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            // 注册 BeanDefinition
            register(beanName, beanDefinition);
        }
    }

    private void register(String beanName, BeanDefinition beanDefinition) {
        if (shouldRegister(beanName, beanDefinition)) {
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }

    private boolean shouldRegister(String beanName, BeanDefinition beanDefinition) {
        return shouldRegister.apply(beanName).apply(beanDefinition).apply(getRegistry());
    }
}
