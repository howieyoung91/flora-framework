package xyz.yanghaoyu.flora.core.aop.aspectj;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AspectJ表达式切入点
 */

public class AspectJExpressionPointcut implements AbstractPointcut {
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();
    // 切点表达式
    private final PointcutExpression pointcutExpression;

    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
    }

    public AspectJExpressionPointcut(String pointcutExpression) {
        PointcutParser pointcutParser = PointcutParser.
                getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution
                        (SUPPORTED_PRIMITIVES);
        this.pointcutExpression = pointcutParser.parsePointcutExpression(pointcutExpression);
    }

    public String getPointcutExpr() {
        return pointcutExpression.getPointcutExpression();
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return pointcutExpression.couldMatchJoinPointsInType(clazz);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
    }
}
