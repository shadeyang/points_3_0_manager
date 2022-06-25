package com.ruoyi.common.enums;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * 数据源
 * 
 * @author ruoyi
 */
public enum DataSourceType
{
    MASTER("master", null),
    SLAVE("slave", null),
    ACCOUNT("account", Pattern.compile("^com.wt2024.points.repository.account")),
    CUSTOMER("customer", Pattern.compile("^com.wt2024.points.repository.customer")),
    SYSTEM("system", Pattern.compile("^com.wt2024.points.repository.system")),

    MERCHANT("merchant",Pattern.compile("^com.wt2024.points.repository.merchant")),
    ;

    private String source;
    private Pattern pattern;

    DataSourceType(String source, Pattern pattern) {
        this.source = source;
        this.pattern = pattern;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public static DataSourceType chose(String classPath) {
        return Arrays.stream(DataSourceType.values())
                .filter(dynamic -> !dynamic.equals(MASTER) && !dynamic.equals(SLAVE) && dynamic.getPattern().matcher(classPath).find())
                .findFirst().orElse(null);
    }
}
