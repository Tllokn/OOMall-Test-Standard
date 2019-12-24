//package com.tardybird.topic;
package xmu.oomall.publictest.topic;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Authorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import xmu.oomall.util.JacksonUtil;
//import com.tardybird.topic.util.JacksonUtil;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.net.URI;
import java.net.URISyntaxException;
/**
 * @author 24320172203116
 */
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class);
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class GetBrandTest {
    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/brands?page=2&limit=1")
    String url;
    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/brands?page=2&limit=-1")
    String url2;
    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/brands?page=-1&limit=2")
    String url3;

    @Autowired
    private AdminAccount adminAccount;

    private HttpHeaders getHttpHeaders(URI uri) throws URISyntaxException {
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        if (headers == null) {
            //登录失败
            assertTrue(false);
        }
        return headers;
    }

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    //    正确测试
    //    24320172203116
    public void getBrands_001() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        PageInfo brand_1 = JacksonUtil.parseObject(body, "data", PageInfo.class);
        assertEquals(2, brand_1.getPageSize());
        assertEquals(1, brand_1.getPageNum());
    }

    @Test
    //error limit=-2 page=1
    //    24320172203116
    public void getBrands_002() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url2);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        String errmsg = JacksonUtil.parseString(body, "errmsg");
        assertEquals(580, errno);
       // assertEquals("页数page或页面limit错误", errmsg);
    }

    @Test
    //error limit=1 page=-2
    //    24320172203116
    public void getBrands_003() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url3);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        String errmsg = JacksonUtil.parseString(body, "errmsg");
        assertEquals(580, errno);
        //assertEquals("页数page或页面limit错误", errmsg);
    }
}
