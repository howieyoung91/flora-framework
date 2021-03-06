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
        // 0. ?????? Banner
        printBanner();

        createBeanFactoryAndLoadBeanDefinition();

        initBeanFactory();
    }

    protected final void createBeanFactoryAndLoadBeanDefinition() {
        // 1. ?????? BeanFactory??? ????????? BeanDefinition
        refreshBeanFactory();

        // 2. ?????? @Configuration
        additionallyLoadBeanDefinition();
    }

    protected final void initBeanFactory() {
        // 3. ????????? ApplicationContextAwareProcessor ?????????????????????????????????
        initApplicationContextAwareProcessor();

        // 4. ??? Bean ???????????????????????? BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors();

        // 5. ??????????????? BeanPostProcessor  ?????? Bean ?????????????????????????????????,
        registerBeanPostProcessors();

        // 6. ?????????????????????????????????
        initApplicationEvent();

        // 7. ????????????????????? Bean ??????
        finishBeanFactoryInitialization();

        // 8. ??????????????????????????????
        finishRefresh();
    }


    protected final void printBanner() {
        System.out.println(AbstractApplicationContext.BANNER);
        LOGGER.trace("start ApplicationContext#refresh");
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    public abstract ConfigurableListableBeanFactory getBeanFactory();

    // ?????????????????????????????????????????????Bean??????
    protected void finishBeanFactoryInitialization() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        //  ???????????????????????????
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
        // ????????????????????? Bean ??????
        beanFactory.preInstantiateSingletons();
        LOGGER.trace("finish preInstantiate [Singleton Bean]...");
    }

    private void initApplicationContextAwareProcessor() {
        // ?????? BeanFactory ???????????? ApplicationContext,
        // ???????????? ApplicationContextAwareProcessor ??? bean ????????????
        // ??? ?????? BeanPostProcessorBeforeInit ??? ???????????? ???????????? ApplicationContextAware ????????????,????????? Context ??????
        ApplicationContextAwareProcessor acp = new ApplicationContextAwareProcessor(this);
        getBeanFactory().addBeanPostProcessor(acp);
    }

    /**
     * ?????????????????????????????????
     */
    private void initApplicationEvent() {
        LOGGER.trace("start init [ApplicationEvent] ...");
        // ???????????????????????? ???????????????????????????
        initApplicationEventMulticaster();

        //  ????????????????????? ??????????????????????????????
        registerListeners();
        LOGGER.trace("finish init [ApplicationEvent] ...");
    }

    /**
     * ????????????????????????
     * ??? ??????????????? ????????? bean ?????????
     */
    private void initApplicationEventMulticaster() {
        LOGGER.trace("register [ApplicationEventMulticaster]");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * ??? ????????? ????????? ??????????????? ???
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
     * ?????? BeanFactoryPostProcessors
     */
    private void invokeBeanFactoryPostProcessors() {
        LOGGER.trace("start register [BeanFactoryPostProcessor] ...");
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        // ???????????????
        Collection<BeanFactoryPostProcessor> processors = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values();
        // ?????? @Configuration ???????????? ??????????????????
        processors.stream()
                .sorted(OrderComparator.INSTANCE)  // sort
                .filter(processor -> !(processor instanceof ConfigurationClassPostProcessor))
                .forEach(processor -> processor.postProcessBeanFactory(beanFactory));
        LOGGER.trace("finish register [BeanFactoryPostProcessor] ...");
    }


    /**
     * ??? BeanPostProcessors ????????? BeanFactory ???
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
     * ??????????????????
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
