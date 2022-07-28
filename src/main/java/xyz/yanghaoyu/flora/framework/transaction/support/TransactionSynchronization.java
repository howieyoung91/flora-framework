/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.support;

import java.io.Flushable;

public interface TransactionSynchronization extends Flushable {
    // int STATUS_COMMITTED   = 0;
    // int STATUS_ROLLED_BACK = 1;
    // int STATUS_UNKNOWN     = 2;

    default void suspend() {
    }

    default void resume() {
    }

    @Override
    default void flush() {
    }

    // default void beforeCommit(boolean readOnly) {
    // }
    //
    // default void beforeCompletion() {
    // }
    //
    // default void afterCommit() {
    // }
    //
    // default void afterCompletion(int status) {
    // }

}