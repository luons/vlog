package io.github.vlog.core.constant;

import java.util.Arrays;
import java.util.List;

public class LogMessageConstant {

    /**
     * 链路日志前缀
     */
    public final static String TRACE_PRE = "V_LOG_TRACE:";
    /**
     * 当前链路开始标志
     */
    public final static String TRACE_START = "<";
    /**
     * 当前链路结束标志
     */
    public final static String TRACE_END = ">";
    /**
     * 日志 Key Topic 名称
     */
    public final static String LOG_KEY = "kafka_topic_test";
    /**
     * 链路日志存入索引后缀
     */
    public final static String LOG_KEY_TRACE = "vlog_trace_list";

    public final static String LOG_TYPE_RUN = "run";

    public final static String LOG_TYPE_TRACE = "trace";

    public final static String DELIM_STR = "{}";

    public final static String TRACE_ID = "traceId";
    /**
     * 默认扩展
     */
    public final static String DEFAULT_EXPAND = "V_LOG";
    /**
     * Sleuth 扩展
     */
    public final static String SLEUTH_EXPAND = "sleuth";
    /**
     * 所有支持的扩展
     */
    public final static List<String> EXPANDS = Arrays.asList("V_LOG", "sleuth");
    /**
     * 1 高性能模式，2 全信息模式
     */
    public static int RUN_MODEL = 1;
    /**
     * 默认扩展 可变参数
     */
    public static String EXPAND = "V_LOG";

}
