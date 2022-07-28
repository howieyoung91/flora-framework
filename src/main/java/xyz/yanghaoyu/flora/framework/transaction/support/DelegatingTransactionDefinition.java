/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.support;

import xyz.yanghaoyu.flora.framework.transaction.TransactionDefinition;

public abstract class DelegatingTransactionDefinition implements TransactionDefinition {
    private final TransactionDefinition targetDefinition;

    public DelegatingTransactionDefinition(TransactionDefinition targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    @Override
    public int getPropagationBehavior() {return targetDefinition.getPropagationBehavior();}

    // @Override
    // public int getIsolationLevel() {return targetDefinition.getIsolationLevel();}

    @Override
    public int getTimeout() {return targetDefinition.getTimeout();}

    // @Override
    // public boolean isReadOnly() {return targetDefinition.isReadOnly();}

    @Override
    public String getName() {return targetDefinition.getName();}

    @Override
    public int hashCode() {return targetDefinition.hashCode();}

    @Override
    public boolean equals(Object obj) {return targetDefinition.equals(obj);}

    @Override
    public String toString() {return targetDefinition.toString();}
}
