package com.github.dataflow.dashboard.controller;

import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.dashboard.service.DataUserService;
import com.github.dataflow.dubbo.model.DataUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.github.dataflow.dashboard.utils.Constants.SESSION_USER;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/6
 */
@Controller
public class IndexController {
    private Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private DataUserService dataUserService;

    @RequestMapping("/")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object username = session.getAttribute(SESSION_USER);
        if (username == null) {
            return "index";
        } else {
            return "redirect:/dataInstance/";
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity login(HttpServletRequest request, @RequestParam String username, @RequestParam String password) {
        ResponseEntity responseEntity = new ResponseEntity();
        DataUser dataUser = null;
        try {
            dataUser = dataUserService.getUserByUsername(username);
        } catch (Exception e) {
            logger.error("apply DataUserService[getUserByUsername] failure, params : {}. detail : ", username, e);
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage("系统异常");
            return responseEntity;
        }
        if (dataUser == null) {
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
            responseEntity.setMessage("用户不存在");
            return responseEntity;
        } else {
            if (password.equals(dataUser.getPassword())) {
                HttpSession session = request.getSession();
                if (session == null) {
                    session = request.getSession(true);
                }
                session.setAttribute(SESSION_USER, dataUser);
                return responseEntity;
            } else {
                responseEntity.setResponseStatus(ResponseEntity.FAILURE);
                responseEntity.setMessage("用户名或密码输入错误");
                return responseEntity;
            }
        }
    }

    @RequestMapping(value = "logout")
    public String logout(HttpServletRequest request) {
        ResponseEntity responseEntity = new ResponseEntity();
        HttpSession session = request.getSession();
        if (session == null) {
            return "redirect:/";
        } else {
            session.removeAttribute(SESSION_USER);
            return "redirect:/";
        }
    }
}
