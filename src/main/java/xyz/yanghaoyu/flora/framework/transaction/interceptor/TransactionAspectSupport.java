/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.interceptor;

import xyz.yanghaoyu.flora.framework.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.framework.exception.BeansException;
import xyz.yanghaoyu.flora.framework.structure.ConcurrentReferenceHashMap;
import xyz.yanghaoyu.flora.framework.transaction.PlatformTransactionManager;
import xyz.yanghaoyu.flora.framework.transaction.TransactionAttribute;
import xyz.yanghaoyu.flora.framework.transaction.TransactionAttributeSource;
import xyz.yanghaoyu.flora.framework.transaction.TransactionStatus;
import xyz.yanghaoyu.flora.framework.transaction.support.DelegatingTransactionAttribute;
import xyz.yanghaoyu.flora.framework.util.NamedThreadLocal;
import xyz.yanghaoyu.flora.framework.util.ReflectUtil;
import xyz.yanghaoyu.flora.framework.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

public abstract class TransactionAspectSupport implements BeanFactoryAware {
    private static final ThreadLocal<TransactionInfo> TRANSACTION_INFO_HOLDER
            = new NamedThreadLocal<>("current aspect-driven transaction");

    private BeanFactory                                       beanFactory;
    private ConcurrentMap<Object, PlatformTransactionManager> transactionManagers = new ConcurrentReferenceHashMap(4);
    private TransactionAttributeSource                        source;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setTransactionAttributeSource(TransactionAttributeSource source) {
        this.source = source;
    }


    // -----------------------------------------------------------------------------------------------------
    // ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇    private methods    ⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇⬇
    // -----------------------------------------------------------------------------------------------------

    /**
     * 在事务内执行方法，交给子类调用
     */
    protected Object invokeWithinTransaction(Method method, Class<?> targetClass,
                                             InvocationCallback invocation) throws Throwable {
        TransactionAttribute       attribute          = source.getTransactionAttribute(method, targetClass);
        PlatformTransactionManager transactionManager = determineTransactionManager(attribute);

        TransactionInfo info = createTransactionIfNecessary(
                transactionManager, attribute, ReflectUtil.getQualifiedMethodName(method, targetClass));

        Object result = null;
        try {
            result = invocation.proceedWithInvocation();
        }
        catch (Throwable ex) {
            completeTransactionAfterThrowing(info, ex);
            throw ex;
        }
        finally {
            cleanupTransactionInfo(info);
        }
        commitTransactionAfterReturning(info);
        return result;
    }

    private PlatformTransactionManager determineTransactionManager(TransactionAttribute attribute) {
        if (attribute == null) {
            return null;
        }
        String transactionManagerName = attribute.getTransactionManager();
        if (StringUtil.hasText(transactionManagerName)) {
            return getTransactionManager(transactionManagerName);
        }
        return null;
    }


    private PlatformTransactionManager getTransactionManager(String transactionManagerName) {
        PlatformTransactionManager transactionManager = transactionManagers.get(transactionManagerName);
        if (transactionManager == null) {
            transactionManager = beanFactory.getBean(transactionManagerName, PlatformTransactionManager.class);
            transactionManagers.putIfAbsent(transactionManagerName, transactionManager);
        }
        return transactionManager;
    }

    private TransactionInfo createTransactionIfNecessary(PlatformTransactionManager manager,
                                                         TransactionAttribute attribute, String id) {
        TransactionStatus status = null;
        if (attribute != null && attribute.getName() == null) {
            attribute = wrapTransactionAttribute(attribute, id);
        }
        if (attribute != null && manager != null) {
            status = manager.getTransaction(attribute);
        }
        return prepareTransactionInfo(manager, attribute, id, status);
    }

    private TransactionAttribute wrapTransactionAttribute(TransactionAttribute attribute, String id) {
        return new DelegatingTransactionAttribute(attribute) {
            @Override
            public String getName() {
                return id;
            }
        };
    }

    protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager manager,
                                                     TransactionAttribute attribute,
                                                     String id, TransactionStatus status) {
        TransactionInfo info = new TransactionInfo(manager, attribute, id);
        if (attribute != null) {
            info.newTransactionStatus(status);
        }
        info.bindToThread();
        return info;
    }

    /**
     * 在抛出异常之后完成事务
     */
    protected void completeTransactionAfterThrowing(TransactionInfo info, Throwable ex) {
        TransactionStatus status = info.getTransactionStatus();
        if (status == null) {
            return;
        }
        TransactionAttribute attribute = info.getTransactionAttribute();
        if (attribute == null) {
            return;
        }

        PlatformTransactionManager manager = info.getTransactionManager();

        Throwable throwable = getActualException(ex);
        if (attribute.rollbackOn(throwable)) {
            manager.rollback(status);
        }
        else {
            manager.commit(status);
        }
    }

    private Throwable getActualException(Throwable ex) {
        Throwable throwable = ex;
        while (throwable.getClass() == InvocationTargetException.class) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

    protected void cleanupTransactionInfo(TransactionInfo info) {
        info.restoreThreadLocalStatus();
    }

    /**
     * 事务正常执行 提交事务
     */
    protected void commitTransactionAfterReturning(TransactionInfo info) {
        TransactionStatus status = info.getTransactionStatus();
        if (status == null) {
            return;

        }
        info.getTransactionManager().commit(status);
    }

    // -----------------------------------------------------------------------------------------------------
    // ⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆    private methods    ⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆⬆
    // -----------------------------------------------------------------------------------------------------


    protected interface InvocationCallback {
        Object proceedWithInvocation() throws Throwable;
    }

    protected static final class TransactionInfo {
        private final String                     joinPointIdentification;
        private final PlatformTransactionManager transactionManager;
        private final TransactionAttribute       transactionAttribute;
        private       TransactionStatus          transactionStatus;
        private       TransactionInfo            oldTransactionInfo;

        public TransactionInfo(PlatformTransactionManager transactionManager,
                               TransactionAttribute transactionAttribute,
                               String joinPointIdentification) {
            this.transactionManager = transactionManager;
            this.transactionAttribute = transactionAttribute;
            this.joinPointIdentification = joinPointIdentification;
        }

        public PlatformTransactionManager getTransactionManager() {
            if (transactionManager == null) {
                throw new IllegalStateException("No PlatformTransactionManager set");
            }
            return transactionManager;
        }

        public String getJoinPointIdentification() {
            return joinPointIdentification;
        }

        public TransactionAttribute getTransactionAttribute() {
            return transactionAttribute;
        }

        public void newTransactionStatus(TransactionStatus status) {
            this.transactionStatus = status;
        }

        public TransactionStatus getTransactionStatus() {
            return transactionStatus;
        }

        public boolean hasTransaction() {
            return transactionStatus != null;
        }

        private void bindToThread() {
            oldTransactionInfo = TRANSACTION_INFO_HOLDER.get();
            TRANSACTION_INFO_HOLDER.set(this);
        }

        private void restoreThreadLocalStatus() {
            TRANSACTION_INFO_HOLDER.set(oldTransactionInfo);
        }
    }
}
