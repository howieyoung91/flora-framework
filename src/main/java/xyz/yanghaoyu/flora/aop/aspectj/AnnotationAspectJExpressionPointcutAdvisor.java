package xyz.yanghaoyu.flora.aop.aspectj;

import org.aopalliance.aop.Advice;
import xyz.yanghaoyu.flora.aop.Pointcut;
import xyz.yanghaoyu.flora.aop.PointcutAdvisor;
import xyz.yanghaoyu.flora.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.aop.interceptor.MultiMethodInterceptor;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 13:47<i/>
 * @version 1.0
 */

public class AnnotationAspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    // 切入点
    private AspectJExpressionPointcut pointcut;
    // 拦截器
    private MultiMethodInterceptor methodInterceptor;
    // 表达式
    private String expression;


    public AnnotationAspectJExpressionPointcutAdvisor(String expression, MultiMethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
        this.expression = expression;
    }

    @Override
    public Pointcut getPointcut() {
        if (pointcut == null) {
            pointcut = new AspectJExpressionPointcut(expression);
        }
        return pointcut;
    }

    public void addMethodPoint(AdvicePoint point) {
        this.methodInterceptor.addAdvice(point);
    }

    @Override
    public Advice getAdvice() {
        return methodInterceptor;
    }


    public AnnotationAspectJExpressionPointcutAdvisor setAdvice(MultiMethodInterceptor advice) {
        this.methodInterceptor = advice;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public AnnotationAspectJExpressionPointcutAdvisor setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
