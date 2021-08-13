package xyz.yanghaoyu.flora.test;

import xyz.yanghaoyu.flora.beans.factory.FactoryBean;


public class IUserDaoFactory implements FactoryBean<IUserDao> {
    @Override
    public IUserDao getObject() throws Exception {
        return new IUserDao() {
            @Override
            public String queryUserName(String uId) {
                return uId;
            }
        };
    }

    @Override
    public Class<?> getObjectType() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
