package cn.amaging.encology.es.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Created by DuQiyu on 2018/6/15.
 */
public class BaseInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);

    private static final String UTF_8 = "utf-8";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        long startTime = (Long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String)em.nextElement();
            String value = request.getParameter(name);
            sb.append(name).append("=").append(value).append("&");
        }
        String params = null;
        if (sb.length() > 0) {
            params = sb.substring(0, sb.length() - 1);
        }
        logger.info("url:{}, params:{}, elapsed:{}ms.", request.getRequestURI(), params, (endTime - startTime));
    }
}
