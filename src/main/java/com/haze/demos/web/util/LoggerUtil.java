package com.haze.demos.web.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class LoggerUtil {

    // 格式化参数
    public static String formatParameters(Object... params) {
        if (params.length < 1){
            return "";
        }
        StringBuilder printResult = new StringBuilder().append("parameters: ");
        if (params.length % 2 != 0) {
            log.error("parameters.number.invalid");
        }
        for (int i = 0; i < params.length; i++) {
            if (i % 2 == 0) {
                printResult.append(params[i]).append("=[");
            } else {
                if (i == params.length - 1) {
                    printResult.append(params[i]).append("]");
                } else {
                    printResult.append(params[i]).append("], ");
                }
            }
        }
        return String.valueOf(printResult);
    }


    // 打印方法开始时执行时的入参
    public static String printMethodStartWithParameters(Object... params) {
        return String.format("Starting, %s ", formatParameters(params));
    }

    // 打印执行远程调用开始时的入参
    public static String printRemoteProcedureCallStartWithParameters(Object... params) {
        return String.format("Remote procedure call start , %s ", formatParameters(params));
    }

    // 打印执行远程调用结束后的结果
    public static String printRemoteProcedureCallDoneStartWithParameters(Object... params) {
        return String.format("Remote procedure call completes , %s ", formatParameters(params));
    }

    // 打印数据库查询返回的集合数据个数、入参
    public static String printQueryCountWithParameters(int size, String tableName, Object... params) {
        return String.format("Retrieved %s pieces of data from table %s, based on %s", size, tableName, formatParameters(params));
    }

    // 打印数据库执行更新返回的集合数据个数、入参
    public static String printUpdateCountWithParameters(int size, String tableName, Object... params) {
        return String.format("updated %s pieces of data from table %s, based on %s", size, tableName, formatParameters(params));
    }

    // 打印数据库查询对象、参数，可以用debug、info输出
    public static String printQueryResultWithParameters(Object result, String tableName, Object... params) {
        return String.format("Retrieved %s, [%s] based on %s", tableName, result, formatParameters(params));
    }

    // 打印开始查询
    public static String printQueryWithParameters(String tableName, Object... params) {
        return String.format("Query %s,  %s", tableName, formatParameters(params));
    }

    // 打印开始更新
    public static String printUpdateWithParameters(String tableName, Object... params) {
        return String.format("Update %s,  %s", tableName, formatParameters(params));
    }

    // 打印数据库更新后的对象、参数，可以用debug、info输出
    public static String printUpdateResultWithParameters(Object result, String tableName, Object... params) {
        return String.format("Updated %s, [%s] based on %s", tableName, result, formatParameters(params));
    }

    // 打印数据库新增的对象、参数，可以用debug、info输出
    public static String printInsertedResultWithParameters(Object result, String tableName, Object... params) {
        return String.format("Inserted %s, [%s] based on %s", tableName, result, formatParameters(params));
    }

    // 打印方法执行时间
    public static String printExecutionTime(Long beginTime, Long endTime) {
        return String.format("takes %sms", endTime - beginTime);
    }

    /**
     * 打印方法执行后的返回值
     * @param value 需打印的内容
     * @param method 标识打印的内容来自何处，可为空
     * @param params 参数，可为空
     * */
    public static String printReturnValueOfMethod(Object value, Object method, Object... params) {
        StringBuilder printResult = new StringBuilder("Retrieved [").append(value).append("]");
        if (Objects.nonNull(method)){
            printResult.append(" from [").append(method).append("]");
        }
        if (Objects.nonNull(params)){
            printResult.append(" based on ").append(formatParameters(params));
        }
        return printResult.toString();
    }
}
