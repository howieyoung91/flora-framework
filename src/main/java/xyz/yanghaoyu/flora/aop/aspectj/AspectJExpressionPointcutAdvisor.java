package xyz.yanghaoyu.flora.aop.aspectj;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import xyz.yanghaoyu.flora.aop.Pointcut;
import xyz.yanghaoyu.flora.aop.PointcutAdvisor;

import java.util.List;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    // 切入点
    private AspectJExpressionPointcut pointcut;
    // private Advice advice;
    // 拦截器
    private MethodInterceptor methodInterceptor;
    // // 拦截器链
    // private List<MethodInterceptor> methodInterceptors;
    // 表达式
    private String expression;

    @Override
    public Pointcut getPointcut() {
        if (pointcut == null) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return methodInterceptor;
    }

    // @Override
    // public List<MethodInterceptor> getAdvices() {
    //     return methodInterceptors;
    // }

    public AspectJExpressionPointcutAdvisor setAdvice(MethodInterceptor advice) {
        this.methodInterceptor = advice;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public AspectJExpressionPointcutAdvisor setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
