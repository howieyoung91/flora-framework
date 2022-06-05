/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.transaction.support;

import xyz.yanghaoyu.flora.exception.TransactionException;
import xyz.yanghaoyu.flora.transaction.TransactionDefinition;

import javax.sql.DataSource;

@Deprecated
public class DefaultPlatformTransactionManager extends AbstractPlatformTransactionManager {
    private DataSource dataSource;

    @Override
    protected Object doGetTransaction() throws TransactionException {
        return null;
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {

    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {

    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {

    }
}
