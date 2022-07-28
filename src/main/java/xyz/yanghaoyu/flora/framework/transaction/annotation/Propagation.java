/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.annotation;

import xyz.yanghaoyu.flora.framework.transaction.TransactionDefinition;

public enum Propagation {
    REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED);
    // todo support
    // SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),
    // MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),
    // REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),
    // NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),
    // NEVER(TransactionDefinition.PROPAGATION_NEVER),
    // NESTED(TransactionDefinition.PROPAGATION_NESTED);

    private final int value;

    Propagation(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
