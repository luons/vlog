package io.github.luons.vlog.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.luons.vlog.core.TraceId;
import io.github.luons.vlog.core.TraceMessage;
import io.github.luons.vlog.core.dto.RunLogMessage;
import io.github.luons.vlog.core.dto.TraceLogMessage;

public class TraceLogMessageFactory<T> {

    public static TraceLogMessage getTraceLogMessage(TraceMessage traceMessage, String appName, long time) {
        TraceLogMessage traceLogMessage = new TraceLogMessage();
        traceLogMessage.setAppName(appName);
        traceLogMessage.setTraceId(traceMessage.getTraceId());
        traceLogMessage.setMethod(traceMessage.getMessageType());
        traceLogMessage.setTime(time);
        // traceLogMessage.setPosition(traceMessage.getPosition());
        // traceLogMessage.setPositionNum(traceMessage.getPositionNum().get());
        traceLogMessage.setServerName(IpGetter.CURRENT_IP);
        return traceLogMessage;
    }

    public static RunLogMessage getLogMessage(String appName, String message, long time) {
        RunLogMessage logMessage = new RunLogMessage();
        logMessage.setServerName(IpGetter.CURRENT_IP);
        logMessage.setAppName(appName);
        logMessage.setContent(message.trim());
        // 判断 message 类型
        logMessage.setIsJson(getDataType(message));
        logMessage.setDtTime(time);
        logMessage.setTraceId(TraceId.logTraceID.get());
        return logMessage;
    }

    public static String packageMessage(String message, Object[] args) {
        StringBuilder builder = new StringBuilder(128);
        builder.append(message);
        for (int i = 0; i < args.length; i++) {
            builder.append("\n").append(args[i]);
        }
        return builder.toString();
    }

    private static int getDataType(String message) {
        if (message == null || message.isEmpty() || !(message.contains("{") && message.contains("}"))) {
            return 0;
        }
        Object obj;
        try {
            obj = JSON.parse(message.trim());
        } catch (Exception e) {
            return 0;
        }
        if (obj instanceof JSONObject) {
            return 1;
        } else if (obj instanceof JSONArray) {
            return 2;
        } else {
            return 0;
        }

    }

}
