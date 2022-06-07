/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction;

import java.sql.Connection;

public interface TransactionDefinition {
    // ===================================== 七种传播行为 =====================================
    int PROPAGATION_REQUIRED      = 0; // 如果正处于一个事务中，则加入，否则创建一个新的事务, default
    int PROPAGATION_SUPPORTS      = 1; // 如果正处于一个事务中，则加入，否则不使用事务
    int PROPAGATION_MANDATORY     = 2; // 如果正处于一个事务中，则加入，否则抛出异常
    int PROPAGATION_REQUIRES_NEW  = 3; // 如果正处于一个事务中，则先挂起当前事务，然后创建
    int PROPAGATION_NOT_SUPPORTED = 4; // 如果正处于一个事务中，则挂起当前事务，不使用
    int PROPAGATION_NEVER         = 5; // 如果正处于一个事务中，则抛出异常
    int PROPAGATION_NESTED        = 6; // 如果正处于一个事务中，则创建一个事务嵌套其中，否则创建一个新事务
    // ===================================== 七种传播行为 =====================================

    // ===================================== 四种隔离级别 =====================================
    int DEFAULT_ISOLATION          = -1; // 使用默认的隔离级别
    int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED; // 读未提交
    int ISOLATION_READ_COMMITTED   = Connection.TRANSACTION_READ_COMMITTED;   // 读已提交
    int ISOLATION_REPEATABLE_READ  = Connection.TRANSACTION_REPEATABLE_READ;  // 可重复读
    int ISOLATION_SERIALIZABLE     = Connection.TRANSACTION_SERIALIZABLE;     // 串行化
    // ===================================== 四种隔离级别 =====================================

    int DEFAULT_TIMEOUT = -1; // 默认超时时间

    int getPropagationBehavior();

    // int getIsolationLevel();

    int getTimeout();

    // boolean isReadOnly();

    String getName();
}
 