package xyz.yanghaoyu.flora.core.aop.aspectj;

import xyz.yanghaoyu.flora.core.aop.interceptor.AdvicePoint;
import xyz.yanghaoyu.flora.core.aop.interceptor.MethodEnhanceAdviceInterceptor;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactory;
import xyz.yanghaoyu.flora.core.beans.factory.BeanFactoryAware;
import xyz.yanghaoyu.flora.exception.BeansException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 19:50<i/>
 * @version 1.0
 */
public class AnnotationAspectJExpressionPointcutAdvisorManager implements BeanFactoryAware {
    private final Map<String, AnnotationAspectJExpressionPointcutAdvisor> map
            = new ConcurrentHashMap<>(8);
    private       Collection<AnnotationAspectJExpressionPointcutAdvisor>  advisorsCache
            = map.values();
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
        if (advisorsCache == null) {
            advisorsCache = map.values();
        }
        return advisorsCache;
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
