package xmu.oomall.publictest.order;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Order;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest(classes = PublicTestApplication.class)
public class OrdersIdTest {
    @Value("http://${oomall.host}:${oomall.port}/orderService/orders/{id}")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_OrdersId_001() throws Exception{
        /* 登陆 */
        adtUserAccount.setUserName("64000302147");
        adtUserAccount.setPassword("123456");
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "502"));
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        Order order = JacksonUtil.parseObject(body, "data", Order.class);
        assertEquals(502, order.getId());
        assertEquals(60, order.getUserId());
        assertEquals("2016102995506", order.getOrderSn());
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_OrdersId_002() throws Exception{
        /* 登陆 */
        adtUserAccount.setUserName("64000302147");
        adtUserAccount.setPassword("123456");
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "504"));
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(604, errno); //订单非法操作(查看或者操作了不属于自己的订单)

    }


    /*
     * @author: 24320172203217
     * */
    @Test
    /* 本条order被逻辑删除 */
    public void tc_OrdersId_003() throws Exception {
        /* 本条order被逻辑删除 */
        /* 设置请求头部*/
        /* 24320172203217 */
        adtUserAccount.setUserName("64000302147");
        adtUserAccount.setPassword("123456");
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "502"));
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);


        /*exchange方法模拟HTTP请求*/
        response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        /*取得响应体*/
        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(600, errNo); //该订单不存在(不在数据库里的或者逻辑删除)
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_OrdersId_004() throws Exception {

        adtUserAccount.setUserName("64000302147");
        adtUserAccount.setPassword("123456");
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "504"));
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);


        /*exchange方法模拟HTTP请求*/
        response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        /*取得响应体*/
        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(604, errNo); //订单非法操作(查看或者操作了不属于自己的订单)
    }
}
