package com.github.dataflow.dashboard.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.dataflow.common.model.ResponseEntity;
import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.dashboard.exception.DataFlowException;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/14
 */
public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static       String UTF_8  = "UTF-8";

    public static ResponseEntity post(String url) {
        return post(url, null, null);
    }

    public static ResponseEntity post(String url, String key, Object value) {
        logger.info("send [POST] request, url : {}, params : {}", url, value);
        CloseableHttpResponse httpResponse = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            httpResponse = httpClient.execute(buildHttpPost(url, key, value));
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity(), UTF_8);
            if (isSuccess(httpResponse)) {
                logger.info("receive [POST] response : {} successfully.", jsonResponse);
                return JSONObject.parseObject(jsonResponse, ResponseEntity.class);
            } else {
                logger.error("receive [POST] response failure, detail : {}", jsonResponse);
                throw new DataFlowException(jsonResponse);
            }
        } catch (IOException e) {
            throw new DataFlowException(e);
        } finally {
            Closer.closeQuietly(httpResponse);
            Closer.closeQuietly(httpClient);
        }
    }

    private static boolean isSuccess(CloseableHttpResponse httpResponse) {
        return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

    private static HttpPost buildHttpPost(String url, String key, Object value) throws UnsupportedEncodingException {
        String jsonString = null;
        if (value instanceof CharSequence) {
            jsonString = value.toString();
        } else {
            jsonString = JSONObject.toJSONString(value);
        }
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (key != null) {
            nameValuePairs.add(new BasicNameValuePair(key, jsonString));
        }
        UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs);
        httpEntity.setContentEncoding(UTF_8);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(httpEntity);
        return httpPost;
    }


}
