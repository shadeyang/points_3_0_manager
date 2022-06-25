package com.ruoyi.web.core.datasource;

import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.framework.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@ConditionalOnProperty(
        prefix = "spring.datasource.druid",
        name = "test",
        havingValue = "false",
        matchIfMissing = true
)
@Component
@Order(0)
@Lazy(false)
public class PointsDataSourceAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(PointsDataSourceAspect.class);

	@Pointcut("execution(* com.wt2024.points.repository..*RepositoryImpl.*(..))")
    public void checkArgs() {
    }

    // 这里切到你的方法目录
    @Before("checkArgs()")
    public void process(JoinPoint joinPoint) throws NoSuchMethodException, SecurityException {
        Class clazz = joinPoint.getTarget().getClass();

        String targetName = clazz.getName();
        DataSourceType dataSource = DataSourceType.chose(targetName);
        if (dataSource == null) {
            dataSource = DataSourceType.MASTER;
        }
		logger.debug("{}当前执行的库：{}", targetName, dataSource.name());
        DynamicDataSourceContextHolder.setDataSourceType(dataSource.name());
    }

    @After("checkArgs()")
    public void afterAdvice() {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
