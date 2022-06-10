package xyz.yanghaoyu.flora.core.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.core.OrderComparator;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.*;
import xyz.yanghaoyu.flora.core.beans.factory.support.ApplicationContextAwareProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.support.BeanDefinitionRegistry;
import xyz.yanghaoyu.flora.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.core.context.ConfigurableApplicationContext;
import xyz.yanghaoyu.flora.core.context.event.*;
import xyz.yanghaoyu.flora.core.convert.support.DefaultConversionService;
import xyz.yanghaoyu.flora.core.io.loader.DefaultResourceLoader;
import xyz.yanghaoyu.flora.exception.BeansException;
import xyz.yanghaoyu.flora.util.BeanUtil;

import java.util.Collection;
import java.util.Map;

import static xyz.yanghaoyu.flora.constant.BuiltInBean.CONVERTER_FACTORY_BEAN;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/9 11:38<i/>
 * @version 1.0
 */

public abstract class AbstractApplicationContext
        extends DefaultResourceLoader implements ConfigurableApplicationContext {
    public static final  String APPLICATION_EVENT_MULTICASTER_BEAN_NAME
                                       = BeanUtil.builtInBeanName(ApplicationEventMulticaster.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationContext.class);
    private static final String BANNER =
            "\n" +
            " _______  __        ______   .______          ___      \n" +
            "|   ____||  |      /  __  \\  |   _  \\        /   \\     \n" +
            "|  |__   |  |     |  |  |  | |  |_)  |      /  ^  \\    \n" +
            "|   __|  |  |     |  |  |  | |      /      /  /_\\  \\   \n" +
            "|  |     |  `----.|  `--'  | |  |\\  \\----./  _____  \\  \n" +
            "|__|     |_______| \\______/  | _| `._____/__/     \\__\\ \n";

    private ApplicationEventMulticaster applicationEventMulticaster;

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

        // 2. 处理 @Configuration
        additionallyLoadBeanDefinition();
    }

    protected final void initBeanFactory() {
        // 3. 初始化 ApplicationContextAwareProcessor 为框架提供对象感知功能
        initApplicationContextAwareProcessor();

        // 4. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors();

        // 5. 生成并注册 BeanPostProcessor  要在 Bean 实例化之前执行注册操作,
        registerBeanPostProcessors();

        // 6. 初始化与事件相关的组件
        initApplicationEvent();

        // 7. 提前实例化单例 Bean 对象
        finishBeanFactoryInitialization();

        // 8. 发布容器刷新完成事件
        finishRefresh();
    }


    protected final void printBanner() {
        System.out.println(AbstractApplicationContext.BANNER);
        LOGGER.trace("start ApplicationContext#refresh");
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    // 设置类型转换器、提前实例化单例Bean对象
    protected void finishBeanFactoryInitialization() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        //  默认开启类型转换器
        String conversionServiceFactoryBeanName = BeanUtil.builtInBeanName(CONVERTER_FACTORY_BEAN);
        if (beanFactory.containsBean(conversionServiceFactoryBeanName)) {
            LOGGER.trace("start init [ConvertService]...");
            DefaultConversionService conversionService =
                    beanFactory.getBean(conversionServiceFactoryBeanName, DefaultConversionService.class);

            if (conversionService != null) {
                beanFactory.setConversionService(conversionService);
            }
            LOGGER.trace("finish init [ConvertService]");
        }

        LOGGER.trace("start preInstantiate [Singleton Bean]...");
        // 提前实例化单例 Bean 对象
        beanFactory.preInstantiateSingletons();
        LOGGER.trace("finish preInstantiate [Singleton Bean]...");
    }

    private void initApplicationContextAwareProcessor() {
        // 由于 BeanFactory 无法感知 ApplicationContext,
        // 因此使用 ApplicationContextAwareProcessor 对 bean 进行增强
        // 在 执行 BeanPostProcessorBeforeInit 时 将会判断 是否实现 ApplicationContextAware
        // 如果实现,将会把 Context 注入
        // LOGGER.trace("init [BeanAware] ...");
        ApplicationContextAwareProcessor acp = new ApplicationContextAwareProcessor(this);
        getBeanFactory().addBeanPostProcessor(acp);
    }

    /**
     * 初始化与事件相关的组件
     */
    private void initApplicationEvent() {
        LOGGER.trace("start init [ApplicationEvent] ...");
        // 初始化事件广播器 把广播器放到容器中
        initApplicationEventMulticaster();

        //  注册事件监听器 把监听器加入广播器中
        registerListeners();
        LOGGER.trace("finish init [ApplicationEvent] ...");
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
        LOGGER.trace("register [ApplicationListener] ...");
        getBeansOfType(ApplicationListener.class).values()
                .forEach(listener -> applicationEventMulticaster.addApplicationListener(listener));
    }

    private void additionallyLoadBeanDefinition() {
        LOGGER.trace("start resolve [Configuration] ...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        beanFactory.getBeansOfType(BeanDefinitionRegistryPostProcessor.class).values()
                .stream().sorted(OrderComparator.INSTANCE)
                .forEach(processor -> processor.postProcessBeanDefinitionRegistry((BeanDefinitionRegistry) beanFactory));
        LOGGER.trace("finish resolve [Configuration]");
    }

    /**
     * 触发 BeanFactoryPostProcessors
     */
    private void invokeBeanFactoryPostProcessors() {
        LOGGER.trace("start register [BeanFactoryPostProcessor] ...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // 调用处理器
        Collection<BeanFactoryPostProcessor> processors = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values();
        // 跳过 @Configuration 的处理器 已经调用过了
        processors.stream()
                .sorted(OrderComparator.INSTANCE)  // sort
                .filter(processor -> !(processor instanceof ConfigurationClassPostProcessor))
                .forEach(processor -> processor.postProcessBeanFactory(beanFactory));
        LOGGER.trace("finish register [BeanFactoryPostProcessor] ...");
    }


    /**
     * 把 BeanPostProcessors 添加到 BeanFactory 中
     */
    private void registerBeanPostProcessors() {
        LOGGER.trace("start register [BeanPostProcessor] ...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        beanFactory.getBeansOfType(BeanPostProcessor.class).values()
                .forEach(beanFactory::addBeanPostProcessor);
        LOGGER.trace("finish register [BeanPostProcessor]");
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));
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
        LOGGER.trace("finish ApplicationContext#refresh");
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
    public <T> T getBean(Class<T> type) {
        return getBeanFactory().getBean(type);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public boolean containsSingletonBean(String name) {
        return getBeanFactory().containsSingletonBean(name);
    }

    @Override
    public <T> String[] getBeanNamesForType(Class<T> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return getBeanFactory();
    }
}
