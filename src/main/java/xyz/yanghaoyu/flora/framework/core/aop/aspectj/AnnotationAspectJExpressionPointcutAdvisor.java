/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.aspectj;

import org.aopalliance.aop.Advice;
import xyz.yanghaoyu.flora.framework.core.aop.Pointcut;
import xyz.yanghaoyu.flora.framework.core.aop.PointcutAdvisor;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.MultiMethodInterceptor;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/13 13:47<i/>
 * @version 1.0
 */

public class AnnotationAspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    private AspectJExpressionPointcut pointcut;          // 切入点
    private MultiMethodInterceptor    methodInterceptor; // 拦截器
    private String                    expression;        // 表达式

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

    public void addAdvicePoint(AdvicePoint point) {
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
