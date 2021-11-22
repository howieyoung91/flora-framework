package xyz.yanghaoyu.flora.test.test4.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Value;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/14 21:26<i/>
 * @version 1.0
 */

@Component
public class UserDaoImpl implements UserDao {
    @Value("${username}")
    public Double temp;

    @Override
    public void list() {
        System.out.println(temp);
    }
}