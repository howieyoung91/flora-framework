package xyz.yanghaoyu.flora.core.beans.factory.xml;

import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xyz.yanghaoyu.flora.core.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanReference;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.constant.XmlTagConst;
import xyz.yanghaoyu.flora.core.context.annotation.ClassPathBeanDefinitionScanner;
import xyz.yanghaoyu.flora.core.io.ResourceLoader;
import xyz.yanghaoyu.flora.core.io.reader.AbstractBeanDefinitionFileReader;
import xyz.yanghaoyu.flora.core.io.Resource;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.IocUtil;
import xyz.yanghaoyu.flora.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:12<i/>
 * @version 1.0
 */

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionFileReader {
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (IOException | ClassNotFoundException e) {
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

    protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        if (!Objects.equals(root.getNodeName(), XmlTagConst.BEANS)) {
            return;
        }
        parseComponentScan(root);
        parseEnableAop(root);
        parsePropertyPlaceholderConfigurer(root);
        parseBeans(root);
    }

    private void parsePropertyPlaceholderConfigurer(Element root) {
        NodeList enablePropertyPlaceholderConfigurerList = root.getElementsByTagName(XmlTagConst.ENABLE_PROPERTY_PLACEHOLDER_CONFIGURER);
        if (enablePropertyPlaceholderConfigurerList.getLength() == 1) {
            Node tag = enablePropertyPlaceholderConfigurerList.item(0);
            String location = ((Element) tag).getAttribute("location");
            // 注册进容器
            IocUtil.enablePropertyPlaceholderConfigurer(getRegistry(), location);
        } else if (enablePropertyPlaceholderConfigurerList.getLength() > 1) {
            // 一个xml中只能出现一个 <enable-property-placeholder-configurer/>
            throw new BeansException("duplicate declaration <" + XmlTagConst.ENABLE_PROPERTY_PLACEHOLDER_CONFIGURER + "/> !");
        }
    }

    private void parseEnableAop(Element root) {
        NodeList aopNodeList = root.getElementsByTagName(XmlTagConst.ENABLE_AOP);
        if (aopNodeList.getLength() == 1) {
            IocUtil.enableAop(getRegistry());
        } else if (aopNodeList.getLength() > 1) {
            // 一个xml中只能出现一个 <enable-aop/>
            throw new BeansException("duplicate declaration <" + XmlTagConst.ENABLE_AOP + "/> !");
        }
    }

    private void parseComponentScan(Element root) {
        NodeList componentScanNodeList = root.getElementsByTagName(XmlTagConst.COMPONENT_SCAN);
        if (componentScanNodeList.getLength() == 1) {
            // do scanPackage
            Node componentScan = componentScanNodeList.item(0);
            if (componentScan instanceof Element) {
                String basePackage = ((Element) componentScan).getAttribute(XmlTagConst.BASE_PACKAGE);
                if (StringUtil.isEmpty(basePackage)) {
                    throw new BeansException("The value of " + XmlTagConst.BASE_PACKAGE + " attribute can not be empty or null");
                }
                String[] basePaths = basePackage.split(",");
                // 扫包
                new ClassPathBeanDefinitionScanner(getRegistry()).doScan(basePaths);
                // 注入 AutowiredAnnotationProcessor 这样才能在 BeanProcessor 中注入属性
                IocUtil.enableComponentScan(getRegistry());
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
            if (!(item instanceof Element) || !Objects.equals(XmlTagConst.BEAN, childNodes.item(i).getNodeName())) {
                continue;
            }
            // 解析bean标签
            Element bean = (Element) childNodes.item(i);
            String clazzName = bean.getAttribute(XmlTagConst.CLASS);
            // String name = bean.getAttribute("name");
            String beanId = bean.getAttribute(XmlTagConst.ID);
            String initMethodName = bean.getAttribute(XmlTagConst.INIT_METHOD);
            String destroyMethodName = bean.getAttribute(XmlTagConst.DESTROY_METHOD);
            String beanScope = bean.getAttribute(XmlTagConst.SCOPE);
            // 解析 类
            Class<?> clazz = null;
            clazz = Class.forName(clazzName);
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // 解析 id
            // 优先级 id > name
            if (StringUtil.isEmpty(beanId)) {
                beanId = StringUtil.lowerFirstChar(clazz.getSimpleName());
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
                if (!XmlTagConst.PROPERTY.equals(bean.getChildNodes().item(j).getNodeName())) {
                    continue;
                }
                // 解析标签：property
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute(XmlTagConst.NAME);
                String attrValue = property.getAttribute(XmlTagConst.VALUE);
                String attrRef = property.getAttribute(XmlTagConst.REF);
                // 获取属性值：引入对象、值对象
                Object value = StringUtil.isNotEmpty(attrRef)
                        ? new BeanReference(attrRef)
                        : attrValue;
                // 创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            // 如果已经存在
            if (getRegistry().containsBeanDefinition(beanId)) {
                throw new BeansException("Duplicate beanId[" + beanId + "] is not allowed");
            }
            // 注册 BeanDefinition
            getRegistry().registerBeanDefinition(beanId, beanDefinition);
        }
    }
}
