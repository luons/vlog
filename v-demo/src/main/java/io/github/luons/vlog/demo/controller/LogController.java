package io.github.luons.vlog.demo.controller;

import io.github.luons.vlog.core.TraceId;
import io.github.luons.vlog.core.util.GfJsonUtil;
import io.github.luons.vlog.demo.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LogController {

    private static Logger log = LoggerFactory.getLogger(LogController.class);

    @Resource
    private LogService logService;

    @RequestMapping("/log/test")
    public String index() {
        log.info("I am MainController" + System.getProperty("user.home"));
        logService.test1("test1");
        logService.test2("test2", null, null, null, null);
        System.out.println(TraceId.logTraceID.get());
        Map<String, Object> map = new HashMap<>();
        map.put("test", "test");
        map.put("value", 123455);
        map.put("data", new ArrayList<>());
        log.info("{}", GfJsonUtil.toJSONString(map));
        return "Hello World!";
    }
}
