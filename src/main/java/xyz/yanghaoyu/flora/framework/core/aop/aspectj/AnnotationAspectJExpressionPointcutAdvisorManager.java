/*
 * Copyright ©2022-2022 Howie Young, All rights reserved.
 * Copyright ©2022-2022 杨浩宇，保留所有权利。
 */

package xyz.yanghaoyu.flora.framework.core.aop.aspectj;

import xyz.yanghaoyu.flora.framework.core.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.framework.core.aop.interceptor.MethodEnhanceAdviceInterceptor;
import xyz.yanghaoyu.flora.framework.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.framework.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.framework.exception.BeansException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 19:50<i/>
 * @version 1.0
 */
public class AnnotationAspectJExpressionPointcutAdvisorManager implements BeanFactoryAware {
    private final Map<String, AnnotationAspectJExpressionPointcutAdvisor> map = new ConcurrentHashMap<>(8);
    private       BeanFactory                                             beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void registerMethodEnhanceAdvice(String pointCutExpr, AdvicePoint point) {
        AnnotationAspectJExpressionPointcutAdvisor advisor = map.get(pointCutExpr);
        if (advisor == null) {
            advisor = new AnnotationAspectJExpressionPointcutAdvisor(pointCutExpr, new MethodEnhanceAdviceInterceptor());
            map.put(pointCutExpr, advisor);
        }
        advisor.addAdvicePoint(point);
    }

    private Collection<AnnotationAspectJExpressionPointcutAdvisor> getAdvisorCandidates() {
        return map.values();
    }

    public Collection<AnnotationAspectJExpressionPointcutAdvisor> getAdvisorCandidates(Class<?> clazz) {
        LinkedList<AnnotationAspectJExpressionPointcutAdvisor> candidate = new LinkedList<>();
        Collection<AnnotationAspectJExpressionPointcutAdvisor> advisors  = getAdvisorCandidates();
        for (AnnotationAspectJExpressionPointcutAdvisor advisor : advisors) {
            if (!advisor.getPointcut().getClassFilter().matches(clazz)) {
                continue;
            }
            candidate.add(advisor);
        }
        return candidate;
    }
}
