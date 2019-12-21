package xmu.oomall.publictest.topic;

import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Topic;
import xmu.oomall.publictest.AdminAccount;
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

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Topics_001() throws Exception{
        Topic topic = new Topic();
        topic.setContent("测试用的Topic");
        topic.setPicUrlList("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E6%AF%8D%E4%BA%B2%E8%8A%82&step_word=&hs=0&pn=16&spn=0&di=107250&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1102082523%2C3781714004&os=773283296%2C810038009&simid=3438615684%2C318272217&adpicid=0&lpn=0&ln=1601&fr=&fmq=1575902810445_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fdealer2.autoimg.cn%2Fdealerdfs%2Fg25%2FM06%2F6D%2FA5%2F620x0_1_q87_autohomedealer__wKgHIFr1LcWAEISwAAHDybd1B78056.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bw7p5i54j_z%26e3Bv54_z%26e3BvgAzdH3F1jwsj6AzdH3Fda8bacAzdH3Fd889dm80n_z%26e3Bip4s&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined");

        /* 设置请求头部*/
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        Topic responseTopic = JacksonUtil.parseObject(body, "data", Topic.class);
        assertEquals(topic.getContent(), responseTopic.getContent());
        assertEquals(topic.getPicUrlList(), responseTopic.getPicUrlList());

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
        topic.setPicUrlList("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E6%AF%8D%E4%BA%B2%E8%8A%82&step_word=&hs=0&pn=16&spn=0&di=107250&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=1102082523%2C3781714004&os=773283296%2C810038009&simid=3438615684%2C318272217&adpicid=0&lpn=0&ln=1601&fr=&fmq=1575902810445_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fdealer2.autoimg.cn%2Fdealerdfs%2Fg25%2FM06%2F6D%2FA5%2F620x0_1_q87_autohomedealer__wKgHIFr1LcWAEISwAAHDybd1B78056.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bw7p5i54j_z%26e3Bv54_z%26e3BvgAzdH3F1jwsj6AzdH3Fda8bacAzdH3Fd889dm80n_z%26e3Bip4s&gsm=&rpstart=0&rpnum=0&islist=&querylist=&force=undefined");

        /* 设置请求头部*/
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
        HttpEntity<Topic> httpEntity = new HttpEntity<>(topic, httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

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

        response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        /*取得响应体*/
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(650, errno); //该话题是无效话题(不在数据库里的或者逻辑删除)
    }

    /**
     * 正确测试,需要数据库中topic的个数>1
     * @author: 24320172203240
     * @throws Exception
     */
    @org.junit.Test
    public void getTopics_001() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(baseUrl+"?page=2&limit=2");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
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
        assertEquals(650, errno);
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
        assertEquals(650, errno);
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
        assertEquals(650, errno);
    }
}
