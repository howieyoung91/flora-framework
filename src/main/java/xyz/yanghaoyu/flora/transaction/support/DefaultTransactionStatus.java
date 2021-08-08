/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.support;

public class DefaultTransactionStatus extends AbstractTransactionStatus {
    private final Object  transaction;
    private final boolean newTransaction;
    private final boolean newSynchronization;
    private       Object  suspendedResources;

    public DefaultTransactionStatus(Object transaction, boolean newTransaction, boolean newSynchronization) {
        this.transaction = transaction;
        this.newTransaction = newTransaction;
        this.newSynchronization = newSynchronization;
    }

    public Object getTransaction() {
        return transaction;
    }

    public boolean hasTransaction() {
        return this.transaction != null;
    }

    @Override
    public boolean isNewTransaction() {
        return hasTransaction() && this.newTransaction;
    }

    public boolean isNewSynchronization() {
        return newSynchronization;
    }

    public Object getSuspendedResources() {
        return suspendedResources;
    }

    public void setSuspendedResources(Object suspendedResources) {
        this.suspendedResources = suspendedResources;
    }
}