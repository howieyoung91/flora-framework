package xyz.yanghaoyu.flora.aop.aspectj;

import org.aopalliance.aop.Advice;
import xyz.yanghaoyu.flora.aop.Pointcut;
import xyz.yanghaoyu.flora.aop.PointcutAdvisor;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    // 切面
    private AspectJExpressionPointcut pointcut;
    // 具体的拦截方法 传入的是拦截器
    private Advice advice;
    // 表达式
    private String expression;

    @Override
    public Pointcut getPointcut() {
        if (null == pointcut) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }


    public AspectJExpressionPointcutAdvisor setAdvice(Advice advice) {
        this.advice = advice;
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
