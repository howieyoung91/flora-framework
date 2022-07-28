/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.transaction.support;

public interface ResourceHolder {

	/**
	 * Reset the transactional state of this holder.
	 */
	void reset();

	/**
	 * Notify this holder that it has been unbound from transaction synchronization.
	 */
	void unbound();

	/**
	 * Determine whether this holder is considered as 'void',
	 * i.e. as a leftover from a previous thread.
	 */
	boolean isVoid();

}
