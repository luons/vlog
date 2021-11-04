package io.github.vlog.demo.filter;

import io.github.vlog.core.TraceId;
import io.github.vlog.core.util.IdWorker;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "LogFilter", urlPatterns = "/*")
public class LogFilter implements Filter {

    private IdWorker worker = new IdWorker(1, 1, 1);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String traceId = request.getHeader("TraceId");
        if (StringUtils.isEmpty(traceId)) {
            TraceId.logTraceID.set(String.valueOf(worker.nextId()));
        } else {
            TraceId.logTraceID.set(traceId);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        // do nothing
    }
}
