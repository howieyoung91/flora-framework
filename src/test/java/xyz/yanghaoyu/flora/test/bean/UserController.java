package xyz.yanghaoyu.flora.test.bean;

import xyz.yanghaoyu.flora.annotation.Component;
import xyz.yanghaoyu.flora.annotation.Inject;
import xyz.yanghaoyu.flora.annotation.Scope;

@Component
@Scope.Singleton
public class UserController {
    @Inject.ByType
    UserService userService;

    public void show() {
        System.out.println(userService);
    }
}
