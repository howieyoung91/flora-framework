/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.support;

import xyz.yanghaoyu.flora.transaction.TransactionAttribute;

public abstract class DelegatingTransactionAttribute
        extends DelegatingTransactionDefinition implements TransactionAttribute {
    private final TransactionAttribute targetAttribute;

    public DelegatingTransactionAttribute(TransactionAttribute targetAttribute) {
        super(targetAttribute);
        this.targetAttribute = targetAttribute;
    }

    @Override
    public boolean rollbackOn(Throwable ex) {
        return targetAttribute.rollbackOn(ex);
    }

    @Override
    public String getTransactionManager() {
        return targetAttribute.getTransactionManager();
    }
}
