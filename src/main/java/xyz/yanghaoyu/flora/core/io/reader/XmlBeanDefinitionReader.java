package xyz.yanghaoyu.flora.core.io.reader;

import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xyz.yanghaoyu.flora.BeansException;
import xyz.yanghaoyu.flora.beans.factory.PropertyValue;
import xyz.yanghaoyu.flora.beans.factory.config.BeanDefinition;
import xyz.yanghaoyu.flora.beans.factory.config.BeanReference;
import xyz.yanghaoyu.flora.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.io.loader.ResourceLoader;
import xyz.yanghaoyu.flora.core.io.resource.Resource;
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
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (!(item instanceof Element) || !Objects.equals("bean", childNodes.item(i).getNodeName())) {
                continue;
            }
            // 解析bean标签
            Element bean = (Element) childNodes.item(i);
            String clazzName = bean.getAttribute("class");
            // 解析 类
            Class<?> clazz = null;
            clazz = Class.forName(clazzName);
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // 解析id
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            // 优先级 id > name
            String beanName = StringUtil.isNotEmpty(id) ? id : name;
            if (StringUtil.isEmpty(beanName)) {
                beanName = StringUtil.firstLowerCase(clazz.getSimpleName());
            }
            // 解析初始化方法
            String initMethodName = bean.getAttribute("init-method");
            beanDefinition.setInitMethodName(initMethodName);
            // 解析销毁方法
            String destroyMethodName = bean.getAttribute("destroy-method");
            beanDefinition.setDestroyMethodName(destroyMethodName);

            // 读取属性并填充
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                if (!(bean.getChildNodes().item(j) instanceof Element)) {
                    continue;
                }
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) {
                    continue;
                }
                // 解析标签：property
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
                // 获取属性值：引入对象、值对象
                Object value = StringUtil.isNotEmpty(attrRef)
                        ? new BeanReference(attrRef)
                        : attrValue;
                // 创建属性信息
                PropertyValue propertyValue = new PropertyValue(attrName, value);
                beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
            }
            // 如果已经存在
            if (getRegistry().containsBeanDefinition(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }
            // 注册 BeanDefinition
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
