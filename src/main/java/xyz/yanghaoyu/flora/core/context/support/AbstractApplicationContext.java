package xyz.yanghaoyu.flora.core.context.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.yanghaoyu.flora.core.beans.factory.ConfigurableListableBeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanFactoryPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.config.BeanPostProcessor;
import xyz.yanghaoyu.flora.core.beans.factory.support.ApplicationContextAwareProcessor;
import xyz.yanghaoyu.flora.core.context.ApplicationListener;
import xyz.yanghaoyu.flora.core.context.ConfigurableApplicationContext;
import xyz.yanghaoyu.flora.core.context.event.ApplicationEvent;
import xyz.yanghaoyu.flora.core.context.event.ApplicationEventMulticaster;
import xyz.yanghaoyu.flora.core.context.event.ContextRefreshedEvent;
import xyz.yanghaoyu.flora.core.context.event.SimpleApplicationEventMulticaster;
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
    private static final String banner =
            "\n" +
            " _______  __        ______   .______          ___      \n" +
            "|   ____||  |      /  __  \\  |   _  \\        /   \\     \n" +
            "|  |__   |  |     |  |  |  | |  |_)  |      /  ^  \\    \n" +
            "|   __|  |  |     |  |  |  | |      /      /  /_\\  \\   \n" +
            "|  |     |  `----.|  `--'  | |  |\\  \\----./  _____  \\  \n" +
            "|__|     |_______| \\______/  | _| `._____/__/     \\__\\ \n";

    @Override
    public void refresh() throws BeansException {
        System.out.println(AbstractApplicationContext.banner);

        // new StringBuilder().append("          _____                    _____           _______                   _____                    _____          \n").append("         /\\    \\                  /\\    \\         /::\\    \\                 /\\    \\                  /\\    \\         \n").append("        /::\\    \\                /::\\____\\       /::::\\    \\               /::\\    \\                /::\\    \\        \n").append("       /::::\\    \\              /:::/    /      /::::::\\    \\             /::::\\    \\              /::::\\    \\       \n").append("      /::::::\\    \\            /:::/    /      /::::::::\\    \\           /::::::\\    \\            /::::::\\    \\      \n").append("     /:::/\\:::\\    \\          /:::/    /      /:::/~~\\:::\\    \\         /:::/\\:::\\    \\          /:::/\\:::\\    \\     \n").append("    /:::/__\\:::\\    \\        /:::/    /      /:::/    \\:::\\    \\       /:::/__\\:::\\    \\        /:::/__\\:::\\    \\    \n").append("   /::::\\   \\:::\\    \\      /:::/    /      /:::/    / \\:::\\    \\     /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\   \n").append("  /::::::\\   \\:::\\    \\    /:::/    /      /:::/____/   \\:::\\____\\   /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\  \n").append(" /:::/\\:::\\   \\:::\\    \\  /:::/    /      |:::|    |     |:::|    | /:::/\\:::\\   \\:::\\____\\  /:::/\\:::\\   \\:::\\    \\ \n").append("/:::/  \\:::\\   \\:::\\____\\/:::/____/       |:::|____|     |:::|    |/:::/  \\:::\\   \\:::|    |/:::/  \\:::\\   \\:::\\____\\\n").append("\\::/    \\:::\\   \\::/    /\\:::\\    \\        \\:::\\    \\   /:::/    / \\::/   |::::\\  /:::|____|\\::/    \\:::\\  /:::/    /\n").append(" \\/____/ \\:::\\   \\/____/  \\:::\\    \\        \\:::\\    \\ /:::/    /   \\/____|:::::\\/:::/    /  \\/____/ \\:::\\/:::/    / \n").append("          \\:::\\    \\       \\:::\\    \\        \\:::\\    /:::/    /          |:::::::::/    /            \\::::::/    /  \n").append("           \\:::\\____\\       \\:::\\    \\        \\:::\\__/:::/    /           |::|\\::::/    /              \\::::/    /   \n").append("            \\::/    /        \\:::\\    \\        \\::::::::/    /            |::| \\::/____/               /:::/    /    \n").append("             \\/____/          \\:::\\    \\        \\::::::/    /             |::|  ~|                    /:::/    /     \n").append("                               \\:::\\    \\        \\::::/    /              |::|   |                   /:::/    /      \n").append("                                \\:::\\____\\        \\::/____/               \\::|   |                  /:::/    /       \n").append("                                 \\::/    /         ~~                      \\:|   |                  \\::/    /        \n").append("                                  \\/____/                                   \\|___|                   \\/____/         ").toString()
        // 1. 创建 BeanFactory， 并加载 BeanDefinition
        refreshBeanFactory();

        // 2. 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // AnnotationAspectJExpressionPointcutAdvisorManager manager = new AnnotationAspectJExpressionPointcutAdvisorManager();
        // beanFactory.registerSingleton(AnnotationAspectJExpressionPointcutAdvisorManager.class.getName(), manager);

        // 3. 初始化 ApplicationContextAwareProcessor 为框架提供对象感知功能
        initApplicationContextAwareProcessor();

        // TODO Aspect Inject
        // 4. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5. 生成并注册 BeanPostProcessor  要在 Bean 实例化之前执行注册操作,
        registerBeanPostProcessors(beanFactory);

        // 6. 初始化与事件相关的组件
        initApplicationEvent();

        // 7. 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        // 8. 发布容器刷新完成事件
        finishRefresh();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void initApplicationContextAwareProcessor() {
        // 由于 BeanFactory 无法感知 ApplicationContext,
        // 因此使用 ApplicationContextAwareProcessor 对 bean 进行增强
        // 在 执行 BeanPostProcessorBeforeInit 时 将会判断 是否实现 ApplicationContextAware
        // 如果实现,将会把 Context 注入
        ApplicationContextAwareProcessor acp = new ApplicationContextAwareProcessor(this);
        // getBeanFactory().registerSingleton("applicationContextAwareProcessor", acp);
        getBeanFactory().addBeanPostProcessor(acp);
    }

    /**
     * 初始化与事件相关的组件
     */
    private void initApplicationEvent() {
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
            applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    /**
     * 触发 BeanFactoryPostProcessors
     */
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap =
                beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * 把 BeanPostProcessors 添加到 BeanFactory 中
     */
    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        // 从 声明的 bean 中挑选出 BeanPostProcessor 进行注册
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
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
}
