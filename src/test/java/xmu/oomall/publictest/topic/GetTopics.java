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
 * @author 24320172203240
 * @version 2.0
 */
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class);
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)

public class GetTopics {
    @Value("http://${oomall.host}:${oomall.port}/topicService/topics?page=2&limit=2")
    String url;
    @Value("http://${oomall.host}:${oomall.port}/topicService/topics?page=2&limit=-1")
    String url2;
    @Value("http://${oomall.host}:${oomall.port}/topicService/topics?page=-1&limit=2")
    String url3;
    @Value("http://${oomall.host}:${oomall.port}/topicService/topics?page=-2&limit=-2")
    String url4;

    private AdminAccount adminAccount;

    private HttpHeaders getHttpHeaders(URI uri) throws URISyntaxException {
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        if (!adminAccount.addToken(httpHeaders)) {
            //登录失败
            System.out.println("失败");
            assertTrue(false);
        }
        return httpHeaders;
    }

    @Autowired
    private TestRestTemplate testRestTemplate;



    @Test
    //正确测试,需要数据库中topic的个数>2
    //    24320172203240
    public void getTopics_001() throws Exception{
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
        PageInfo topic_1 = JacksonUtil.parseObject(body, "data", PageInfo.class);
        assertEquals(2, topic_1.getPageSize());
        assertEquals(1, topic_1.getPageNum());
    }

    @Test
    //error limit=-2 page=2
    //    24320172203240
    public void getTopics_002() throws Exception{
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
        String errmsg = JacksonUtil.parseString(body,"errmsg");
        assertEquals(650, errno);
        assertEquals("该话题是无效话题",errmsg);
    }
    @Test
    //error limit=2 page=-2
    //    24320172203240
    public void getTopics_003() throws Exception{
        /* 设置请求头部*/
        URI uri  =new URI(url3);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        String errmsg = JacksonUtil.parseString(body,"errmsg");
        assertEquals(650, errno);
        assertEquals("该话题是无效话题",errmsg);
    }

    @Test
    //error limit=2 page=-2
    //    24320172203240 与2、3同属一个等价类不算作新的测试用例
    public void getTopics_004() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url4);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        String errmsg = JacksonUtil.parseString(body,"errmsg");
        assertEquals(650, errno);
        assertEquals("该话题是无效话题",errmsg);
    }
}

