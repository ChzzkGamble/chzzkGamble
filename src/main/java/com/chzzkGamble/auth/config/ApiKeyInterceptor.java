package com.chzzkGamble.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class ApiKeyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        RequireApiKey annotation = handlerMethod.getMethodAnnotation(RequireApiKey.class);
        if (annotation == null) {
            return true;
        }
        String apiKey = request.getHeader("Api-Key");
        if (ApiKeyHolder.validateApiKey(apiKey)) {
            return true;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid Api Key");
        return false;
    }
}
