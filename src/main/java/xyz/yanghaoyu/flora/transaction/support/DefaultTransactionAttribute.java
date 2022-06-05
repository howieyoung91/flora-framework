/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.support;

import xyz.yanghaoyu.flora.transaction.TransactionAttribute;

public class DefaultTransactionAttribute
        extends DefaultTransactionDefinition implements TransactionAttribute {
    private String transactionManager;

    @Override
    public boolean rollbackOn(Throwable ex) {
        return ex instanceof RuntimeException || ex instanceof Error;
    }

    @Override
    public String getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(String transactionManager) {
        this.transactionManager = transactionManager;
    }
}
