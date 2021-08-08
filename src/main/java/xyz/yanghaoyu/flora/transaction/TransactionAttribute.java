package xyz.yanghaoyu.flora.transaction;

public interface TransactionAttribute extends TransactionDefinition {
    boolean rollbackOn(Throwable ex);

    String getTransactionManager();
}