package com.github.dataflow.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.github.dataflow.common.model.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/8
 */
public abstract class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private ResponseEntity buildInvalidResponse(BindingResult result) {
        StringBuilder stringBuilder = new StringBuilder();
        List<FieldError> list = result.getFieldErrors();
        for (FieldError fieldError : list) {
            stringBuilder.append(fieldError.getField())
                    .append(" : ")
                    .append(fieldError.getRejectedValue())
                    .append("(")
                    .append(fieldError.getDefaultMessage())
                    .append(")")
                    .append("\n\r");
        }

        ResponseEntity responseEntity = new ResponseEntity(ResponseEntity.FAILURE);
        responseEntity.setMessage(stringBuilder.toString());
        return responseEntity;
    }

    protected ResponseEntity getResponseEntity(String methodName, Callable callable) {
        ResponseEntity responseEntity = new ResponseEntity();
        try {
            callable.call(responseEntity);
        } catch (Exception e) {
            logger.error("apply {} method failure, params : {}, detail : ", methodName, JSON.toJSONString(callable.getParams()), e);
            responseEntity.setResponseStatus(ResponseEntity.FAILURE);
        }
        return responseEntity;
    }

    protected ResponseEntity getResponseEntity(String methodName, BindingResult bindingResult, Callable callable) {
        if (bindingResult.hasErrors()) {
            return buildInvalidResponse(bindingResult);
        }

        return getResponseEntity(methodName, callable);
    }
}
