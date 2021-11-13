package xyz.yanghaoyu.flora.aop.aspectj;

import org.aopalliance.aop.Advice;
import xyz.yanghaoyu.flora.aop.Pointcut;
import xyz.yanghaoyu.flora.aop.interceptor.MethodEnhanceAdviceInterceptor;
import xyz.yanghaoyu.flora.aop.interceptor.MethodPoint;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/11/12 19:50<i/>
 * @version 1.0
 */
public class AnnotationAspectJExpressionPointcutAdvisorManager {
    private Map<String, AnnotationAspectJExpressionPointcutAdvisor> map = new HashMap<>();

    public void addMethodEnhanceAdvice(String pointCutExpr, MethodPoint point) {
        AnnotationAspectJExpressionPointcutAdvisor advisor = map.get(pointCutExpr);
        if (advisor == null) {
            advisor = new AnnotationAspectJExpressionPointcutAdvisor(pointCutExpr, new MethodEnhanceAdviceInterceptor());
            map.put(pointCutExpr, advisor);
        }
        advisor.addMethodPoint(point);
    }

    public Map<String, AnnotationAspectJExpressionPointcutAdvisor> getMap() {
        return map;
    }
}
