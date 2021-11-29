package xyz.yanghaoyu.flora.test.test5;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;

import java.util.StringJoiner;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/29 12:10<i/>
 * @version 1.0
 */

@Component
public class Bean {
    @Inject.ByType(value = MyFactoryBean.class)
    String c;

    @Override
    public String toString() {
        return new StringJoiner(", ", Bean.class.getSimpleName() + "[", "]")
                .add("c='" + c + "'")
                .toString();
    }
}
