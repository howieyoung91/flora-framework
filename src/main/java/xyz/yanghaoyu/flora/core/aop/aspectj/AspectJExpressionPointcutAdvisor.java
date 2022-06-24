package xyz.yanghaoyu.flora.core.aop.aspectj;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import xyz.yanghaoyu.flora.core.aop.Pointcut;
import xyz.yanghaoyu.flora.core.aop.PointcutAdvisor;

@Deprecated
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    private AspectJExpressionPointcut pointcut;          // 切入点
    private MethodInterceptor         methodInterceptor; // 拦截器
    private String                    expression;        // 表达式


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
