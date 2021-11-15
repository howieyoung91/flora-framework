package xyz.yanghaoyu.flora.test.test4.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 21:27<i/>
 * @version 1.0
 */

@Component
public class UserServiceImpl implements UserService {
    @Inject.ByType
    private UserDao userDao;

    @Override
    public void list() {
        userDao.list();
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }
}
