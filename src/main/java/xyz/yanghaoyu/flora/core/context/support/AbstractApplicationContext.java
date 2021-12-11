package xyz.yanghaoyu.flora.core.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.constant.BuiltInBean;
import xyz.yanghaoyu.flora.core.aop.autoproxy.annotation.AnnotationAwareAspectJAutoProxyCreator;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.ConfigurationBeanBeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.support.ApplicationContextAwareProcessor;
import xyz.yanghaoyu.flora.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.core.context.ConfigurableApplicationContext;
import xyz.yanghaoyu.flora.core.context.event.ApplicationEvent;
import xyz.yanghaoyu.flora.core.context.event.ApplicationEventMulticaster;
import xyz.yanghaoyu.flora.core.context.event.ContextRefreshedEvent;
import xyz.yanghaoyu.flora.core.context.event.SimpleApplicationEventMulticaster;
import xyz.yanghaoyu.flora.core.convert.converter.Converter;
import xyz.yanghaoyu.flora.core.convert.converter.ConverterFactory;
import xyz.yanghaoyu.flora.core.convert.support.DefaultConversionService;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:38<i/>
 * @version 1.0
 */

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationContext.class);
    private ApplicationEventMulticaster applicationEventMulticaster;
    private static final String BANNER =
            "\n" +
            " _______  __        ______   .______          ___      \n" +
            "|   ____||  |      /  __  \\  |   _  \\        /   \\     \n" +
            "|  |__   |  |     |  |  |  | |  |_)  |      /  ^  \\    \n" +
            "|   __|  |  |     |  |  |  | |      /      /  /_\\  \\   \n" +
            "|  |     |  `----.|  `--'  | |  |\\  \\----./  _____  \\  \n" +
            "|__|     |_______| \\______/  | _| `._____/__/     \\__\\ \n";

    @Override
    public void refresh() throws BeansException {
        // 0. 打印 Banner
        printBanner();

        createBeanFactoryAndLoadBeanDefinition();

        initBeanFactory();
    }

    protected final void createBeanFactoryAndLoadBeanDefinition() {
        // 1. 创建 BeanFactory， 并加载 BeanDefinition
        refreshBeanFactory();

        // 处理 @Configuration
        additionallyLoadBeanDefinition();
    }

    protected final void initBeanFactory() {
        // 3. 初始化 ApplicationContextAwareProcessor 为框架提供对象感知功能
        initApplicationContextAwareProcessor();

        // 5. 生成并注册 BeanPostProcessor  要在 Bean 实例化之前执行注册操作,
        registerBeanPostProcessors();

        // 4. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors();

        // 6. 初始化与事件相关的组件
        initApplicationEvent();

        // 7. 提前实例化单例Bean对象
        // beanFactory.preInstantiateSingletons();
        finishBeanFactoryInitialization();

        // 8. 发布容器刷新完成事件
        finishRefresh();
    }


    protected final void printBanner() {
        System.out.println(AbstractApplicationContext.BANNER);
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    // 设置类型转换器、提前实例化单例Bean对象
    protected void finishBeanFactoryInitialization() {
        LOGGER.trace("init [ConvertService]...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        //  默认开启类型转换器
        if (beanFactory.containsBean(BuiltInBean.CONVERTER_FACTORY_BEAN.getName())) {

            DefaultConversionService conversionService = beanFactory.getBean(
                    BuiltInBean.CONVERTER_FACTORY_BEAN.getName(),
                    DefaultConversionService.class
            );

            if (conversionService != null) {
                beanFactory.setConversionService(conversionService);
            }

            // HashSet<Object> set = new HashSet<>();
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                Class beanClass = beanFactory.getBeanDefinition(beanDefinitionName).getBeanClass();
                Class<?>[] interfaces = beanClass.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    if (anInterface.equals(Converter.class)) {
                        Converter bean = (Converter) beanFactory.getBean(beanDefinitionName);
                        conversionService.addConverter(bean);
                    } else if (anInterface.equals(ConverterFactory.class)) {
                        Object bean = beanFactory.getBean(beanDefinitionName);
                        conversionService.addConverterFactory((ConverterFactory) bean);
                    }
                }
            }
        }
        LOGGER.trace("preInstantiate [Singleton Bean]...");

        // 提前实例化单例 Bean 对象
        beanFactory.preInstantiateSingletons();


    }

    private void initApplicationContextAwareProcessor() {
        // 由于 BeanFactory 无法感知 ApplicationContext,
        // 因此使用 ApplicationContextAwareProcessor 对 bean 进行增强
        // 在 执行 BeanPostProcessorBeforeInit 时 将会判断 是否实现 ApplicationContextAware
        // 如果实现,将会把 Context 注入
        LOGGER.trace("init [BeanAware] ...");
        ApplicationContextAwareProcessor acp = new ApplicationContextAwareProcessor(this);
        getBeanFactory().addBeanPostProcessor(acp);
    }

    /**
     * 初始化与事件相关的组件
     */
    private void initApplicationEvent() {
        LOGGER.trace("init [ApplicationEvent] ...");
        // 初始化事件广播器 把广播器放到容器中
        initApplicationEventMulticaster();

        //  注册事件监听器 把监听器加入广播器中
        registerListeners();
    }

    /**
     * 初始化事件广播器
     * 把 事件广播器 注册到 bean 容器中
     */
    private void initApplicationEventMulticaster() {
        LOGGER.trace("register [ApplicationEventMulticaster]");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 把 监听器 注册到 事件广播器 中
     */
    private void registerListeners() {
        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : applicationListeners) {
            LOGGER.trace("register [ApplicationListener] ...");
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    private void additionallyLoadBeanDefinition() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        Collection<ConfigurationBeanBeanFactoryPostProcessor> configBeanFactoryPostProcesses = beanFactory.getBeansOfType(ConfigurationBeanBeanFactoryPostProcessor.class).values();
        for (ConfigurationBeanBeanFactoryPostProcessor configBeanFactoryPostProcess : configBeanFactoryPostProcesses) {
            configBeanFactoryPostProcess.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 触发 BeanFactoryPostProcessors
     */
    private void invokeBeanFactoryPostProcessors() {
        LOGGER.trace("register [BeanFactoryPostProcessor] ...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 调用处理器
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        BeanFactoryPostProcessor proxyBeanFactoryPostProcessor = null;
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            // 支持 aop 的 处理 最后再调用 防止 aop 丢失
            if (beanFactoryPostProcessor instanceof AnnotationAwareAspectJAutoProxySupportBeanFactoryPostProcessor) {
                proxyBeanFactoryPostProcessor = beanFactoryPostProcessor;
                continue;
            }
            // 跳过 @Configuration 的处理器 已经调用过了
            if (beanFactoryPostProcessor instanceof ConfigurationBeanBeanFactoryPostProcessor) {
                continue;
            }
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }

        if (proxyBeanFactoryPostProcessor != null) {
            // 先 BeanFactoryPostProcessor 获取到 enhance 信息
            proxyBeanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
            // 添加 对 aop 支持的 bean post processor
            Collection<AnnotationAwareAspectJAutoProxyCreator> values = beanFactory.getBeansOfType(AnnotationAwareAspectJAutoProxyCreator.class).values();
            for (BeanPostProcessor value : values) {
                beanFactory.addBeanPostProcessor(value);
            }
        }
    }


    /**
     * 把 BeanPostProcessors 添加到 BeanFactory 中
     */
    private void registerBeanPostProcessors() {
        LOGGER.trace("register [BeanPostProcessor] ...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        Map<String, BeanPostProcessor> beanPostProcessorMap = getBeanFactory().getBeansOfType(BeanPostProcessor.class);
        // 从 声明的 bean 中挑选出 BeanPostProcessor 进行注册
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            // 延迟加载
            if (beanPostProcessor instanceof AnnotationAwareAspectJAutoProxyCreator) {
                continue;
            }
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }

    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * 发布一个事件
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        this.applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public boolean containsSingletonBean(String name) {
        return getBeanFactory().containsSingletonBean(name);
    }
}
