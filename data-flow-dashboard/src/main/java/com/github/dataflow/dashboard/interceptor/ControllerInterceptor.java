package com.github.dataflow.dashboard.interceptor;

import com.github.dataflow.dashboard.utils.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/7
 */
public class ControllerInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isLogin(request) || isIndex(request)) {
            return true;
        } else {
            HttpSession session = request.getSession();
            if (session == null) {
                redirectToIndex(request, response);
                return false;
            } else {
                if (session.getAttribute(Constants.SESSION_USER) == null) {
                    redirectToIndex(request, response);
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    /**
     * 重定向到index页面
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void redirectToIndex(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath());
    }

    private boolean isIndex(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/")) {
            requestURI = requestURI.substring(0, requestURI.length() - 1);
        }
        return requestURI.equals(request.getContextPath());
    }

    private boolean isLogin(HttpServletRequest request) {
        String loginUri = request.getContextPath() + "/login";
        return loginUri.equals(request.getRequestURI());
    }
}
