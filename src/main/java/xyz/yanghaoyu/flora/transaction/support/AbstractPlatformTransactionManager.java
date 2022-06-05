/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.support;

import xyz.yanghaoyu.flora.exception.TransactionException;
import xyz.yanghaoyu.flora.transaction.PlatformTransactionManager;
import xyz.yanghaoyu.flora.transaction.TransactionDefinition;
import xyz.yanghaoyu.flora.transaction.TransactionStatus;

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
        return startTransaction(definition, transaction, true);
    }

    protected DefaultTransactionStatus startTransaction(TransactionDefinition definition, Object transaction, boolean newTransaction) {
        DefaultTransactionStatus status = new DefaultTransactionStatus(transaction, newTransaction);
        doBegin(transaction, definition);
        return status;
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalArgumentException("Transaction is already completed - do not call or rollback more than once per transaction");
        }
        doCommit((DefaultTransactionStatus) status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        if (status.isCompleted()) {
            throw new IllegalArgumentException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }

        doRollback((DefaultTransactionStatus) status);
    }

    protected abstract Object doGetTransaction() throws TransactionException;

    protected abstract void doCommit(DefaultTransactionStatus status) throws TransactionException;

    protected abstract void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException;

    protected abstract void doRollback(DefaultTransactionStatus status) throws TransactionException;
}
