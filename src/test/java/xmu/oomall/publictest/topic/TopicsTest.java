package xmu.oomall.publictest.topic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Topic;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;


@SpringBootTest(classes = PublicTestApplication.class)
public class TopicsTest {
    @Value("http://${oomall.host}:${oomall.port}/topicService/topics/")
    String url;

    @Value("http://${oomall.host}:${oomall.port}/topicService/topics")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Autowired
    private UserAccount userAccount;


    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Topics_001() throws Exception{
        Topic topic = new Topic();
        topic.setContent("测试用的Topic");

        /* 设置请求头部*/
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        Topic responseTopic = JacksonUtil.parseObject(body, "data", Topic.class);
        assertEquals(topic.getContent(), responseTopic.getContent());

        uri = new URI(url+"/"+responseTopic.getId());
        httpHeaders = adminAccount.createHeaders();
        HttpEntity entity = new HttpEntity(httpHeaders);
        response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        /*取得响应体*/
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        responseTopic = JacksonUtil.parseObject(body, "data", Topic.class);
        assertEquals(topic.getContent(), responseTopic.getContent());
        assertEquals(topic.getPicUrlList(), responseTopic.getPicUrlList());
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Topics_002() throws Exception{
        Topic topic = new Topic();
        topic.setContent("测试用的Topic1");

        /* 设置请求头部*/
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        Topic responseTopic = JacksonUtil.parseObject(body, "data", Topic.class);
        assertEquals(topic.getContent(), responseTopic.getContent());
        assertEquals(topic.getPicUrlList(), responseTopic.getPicUrlList());

        uri = new URI(url+"/"+responseTopic.getId());
        HttpEntity entity = new HttpEntity(httpHeaders);
        response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        /*取得响应体*/
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        httpHeaders = getHttpHeaders(userAccount);
        httpEntity = new HttpEntity<>(topic, httpHeaders);
        response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        /*取得响应体*/
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(650, errno); //该话题是无效话题(不在数据库里的或者逻辑删除)
    }

    @Test
    /**
     * error limit=-2 page=2
     * @author: 24320172203240
     */
    public void getTopics_002() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(baseUrl+"?page=2&limit=-1");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }

    @Test
    /**
     * error limit=-2 page=2
     * @author: 24320172203240
     */
    public void getTopics_003() throws Exception{
        /* 设置请求头部*/
        URI uri  =new URI(baseUrl+"?page=-1&limit=2");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }

    @Test
    /**
     * error limit=-2 page=2
     * @author: 24320172203240
     */
    public void getTopics_004() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(baseUrl+"?page=-2&limit=-2");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }
}
