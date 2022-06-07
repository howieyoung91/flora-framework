/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.support;

import xyz.yanghaoyu.flora.exception.TransactionException;
import xyz.yanghaoyu.flora.transaction.PlatformTransactionManager;
import xyz.yanghaoyu.flora.transaction.TransactionDefinition;
import xyz.yanghaoyu.flora.transaction.TransactionStatus;

import java.util.List;

public abstract class AbstractPlatformTransactionManager implements PlatformTransactionManager {
    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        if (definition == null) {
            definition = new DefaultTransactionDefinition();
        }

        // check timeout
        if (definition.getTimeout() < TransactionDefinition.DEFAULT_TIMEOUT) {
            throw new TransactionException("Invalid transaction timeout " + definition.getTimeout() + "transaction name: " + definition.getName());
        }

        Object transaction = doGetTransaction();

        if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED) {
            // 不存在事务
            if (!TransactionSynchronizationManager.isSynchronizationActive()) {
                return startTransaction(definition, transaction, null);
            }

            // 存在事务, 不用开启事务
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                return new DefaultTransactionStatus(transaction, false, false);
            }
        }
        else {
            // NESTED
            if (TransactionSynchronizationManager.isSynchronizationActive()) {
                DefaultTransactionStatus status = new DefaultTransactionStatus(transaction, false, false);
                status.createAndHoldSavepoint();
                return status;
            }
        }
        return startTransaction(definition, transaction, null);
    }

    private DefaultTransactionStatus startTransaction(
            TransactionDefinition definition, Object transaction, Object suspendedResources
    ) {
        DefaultTransactionStatus status = new DefaultTransactionStatus(transaction, true, true);
        status.setSuspendedResources(suspendedResources);
        doBegin(transaction, definition); // 开启事务
        prepareSynchronization(status, definition); // 交给事务同步管理器管理
        return status;
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalArgumentException("Transaction is already completed - do not call or rollback more than once per transaction");
        }
        DefaultTransactionStatus defaultTransactionStatus = (DefaultTransactionStatus) status;
        try {
            if (status.hasSavepoint()) {
                (defaultTransactionStatus).releaseHeldSavepoint();
            }
            else if (status.isNewTransaction()) {
                doCommit(defaultTransactionStatus);
            }
        }
        finally {
            cleanupAfterCompletion(defaultTransactionStatus);
        }
    }

    private void cleanupAfterCompletion(DefaultTransactionStatus status) {
        status.setCompleted();
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.clear();
        }
        if (status.isNewTransaction()) {
            doCleanupAfterCompletion(status.getTransaction());
        }
        if (status.getSuspendedResources() != null) {
            Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
            resume(transaction, (SuspendedResourcesHolder) status.getSuspendedResources());
        }
    }

    /**
     * 恢复上下文
     */
    protected final void resume(Object transaction, SuspendedResourcesHolder resourcesHolder) {
        if (resourcesHolder != null) {
            Object suspendedResources = resourcesHolder.suspendedResources;
            if (suspendedResources != null) {
                doResume(transaction, suspendedResources);
            }
            List<TransactionSynchronization> suspendedSynchronizations = resourcesHolder.suspendedSynchronizations;
            if (suspendedSynchronizations != null) {
                TransactionSynchronizationManager.setActualTransactionActive(resourcesHolder.wasActive);
                TransactionSynchronizationManager.setCurrentTransactionName(resourcesHolder.name);
                doResumeSynchronization(suspendedSynchronizations);
            }
        }
    }


    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalArgumentException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }


        DefaultTransactionStatus defaultTransactionStatus = (DefaultTransactionStatus) status;
        try {
            if (defaultTransactionStatus.hasSavepoint()) {
                defaultTransactionStatus.rollbackToHeldSavepoint();
            }
            else if (defaultTransactionStatus.isNewTransaction()) {
                doRollback(defaultTransactionStatus);
            }
        }
        finally {
            cleanupAfterCompletion(defaultTransactionStatus);
        }
    }

    /**
     * 挂起一个事务，把上下文保存到 SuspendedResourcesHolder
     */
    protected final SuspendedResourcesHolder suspend(Object transaction) throws TransactionException {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            // 移除 TransactionSynchronization
            List<TransactionSynchronization> syncs = doSuspendSynchronization();
            try {
                Object suspendedResources = null;
                if (transaction != null) {
                    suspendedResources = doSuspend(transaction);
                }
                String name = TransactionSynchronizationManager.getCurrentTransactionName();
                TransactionSynchronizationManager.setCurrentTransactionName(null);
                boolean wasActive = TransactionSynchronizationManager.isActualTransactionActive();
                TransactionSynchronizationManager.setActualTransactionActive(false);
                // 保存上下文到 SuspendedResourcesHolder
                return new SuspendedResourcesHolder(suspendedResources, syncs, name, wasActive);
            }
            catch (RuntimeException | Error exception) {
                doResumeSynchronization(syncs);
                throw exception;
            }
        }
        else if (transaction != null) {
            // Transaction active but no synchronization active.
            Object suspendedResources = doSuspend(transaction);
            // 不用处理 TransactionSynchronizationManager 了
            // 直接返回 SuspendedResourcesHolder
            return new SuspendedResourcesHolder(suspendedResources);
        }
        return null;
    }


    /**
     * 触发所有 synchronization#suspend 并移出
     */
    private List<TransactionSynchronization> doSuspendSynchronization() {
        List<TransactionSynchronization> syncs = TransactionSynchronizationManager.getSynchronizations();
        for (TransactionSynchronization sync : syncs) {
            sync.suspend();
        }
        TransactionSynchronizationManager.clearSynchronization();
        return syncs;
    }

    /**
     * 触发所有 synchronization#resume 并重新注册到 TransactionSynchronizationManager
     */
    private void doResumeSynchronization(List<TransactionSynchronization> syncs) {
        TransactionSynchronizationManager.initSynchronization();
        for (TransactionSynchronization sync : syncs) {
            sync.resume();
            TransactionSynchronizationManager.registerSynchronization(sync);
        }
    }

    protected void prepareSynchronization(DefaultTransactionStatus status, TransactionDefinition definition) {
        if (status.isNewSynchronization()) {
            TransactionSynchronizationManager.setActualTransactionActive(status.hasTransaction());
            // TransactionSynchronizationManager.setCurrentTransactionIsolationLevel(
            //         definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT ?
            //                 definition.getIsolationLevel() : null);
            // TransactionSynchronizationManager.setCurrentTransactionReadOnly(definition.isReadOnly());
            TransactionSynchronizationManager.setCurrentTransactionName(definition.getName());
            TransactionSynchronizationManager.initSynchronization();
        }
    }

    protected abstract Object doGetTransaction() throws TransactionException;

    protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;

    protected void doCleanupAfterCompletion(Object transaction) {}

    protected abstract void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException;

    protected abstract void doRollback(DefaultTransactionStatus status) throws TransactionException;

    protected Object doSuspend(Object transaction) throws TransactionException {
        return null;
    }

    protected void doResume(Object transaction, Object suspendedResources) {}

    protected static final class SuspendedResourcesHolder {
        private final Object                           suspendedResources;
        private       List<TransactionSynchronization> suspendedSynchronizations;
        private       String                           name;
        // private       boolean                          readOnly;
        // private       Integer                          isolationLevel;
        private       boolean                          wasActive;

        private SuspendedResourcesHolder(Object suspendedResources) {
            this.suspendedResources = suspendedResources;
        }

        public SuspendedResourcesHolder(
                Object suspendedResources, List<TransactionSynchronization> suspendedSynchronizations,
                String name, boolean wasActive) {
            this.suspendedResources = suspendedResources;
            this.suspendedSynchronizations = suspendedSynchronizations;
            this.name = name;
            this.wasActive = wasActive;
        }
    }
}

