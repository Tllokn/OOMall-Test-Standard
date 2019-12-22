package xmu.oomall.publictest.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Address;
import xmu.oomall.domain.CartItem;
import xmu.oomall.domain.Order;
import xmu.oomall.domain.OrderItem;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.OrderSubmitVo;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

/**
 * @author 24320172203146
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class OrdersIdShip {
    @Value("http://${oomall.host}:${oomall.port}/orderService/orders/{id}/ship")
    String url;

    @Value("http://${oomall.host}:${oomall.port}")
    String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount userAccount;


    /**
     * @author 24320172203146
     * @create 2019/12/21
     */
    @Test
    public void tc_Orders_001() throws Exception{

        //user_id = 1
        userAccount.setUserName("userA");
        userAccount.setPassword("qwerty888");

        URI uri = new URI(url.replace("{id}", "7"));


        HttpHeaders httpHeaders = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        Order order = JacksonUtil.parseObject(body, "data", Order.class);
        assertEquals(5, order.getStatusCode());
        assertEquals(502, order.getId());

    }

    /**
     * @author 24320172203146
     * @create 2019/12/21
     */
    @Test
    public void tc_Orders_002() throws Exception{
        //无效订单
        //user_id = 1
        userAccount.setUserName("userA");
        userAccount.setPassword("qwerty888");

        URI uri = new URI(url.replace("{id}", "-1"));


        HttpHeaders httpHeaders = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");

        assertEquals(600, errno);
    }

    /**
     * @author 24320172203146
     * @create 2019/12/21
     */
    @Test
    public void tc_Orders_003() throws Exception{
        //已发货的订单不能再发货
        //user_id = 1
        userAccount.setUserName("userA");
        userAccount.setPassword("qwerty888");

        URI uri = new URI(url.replace("{id}", "25"));


        HttpHeaders httpHeaders = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");

        //修改状态失败
        assertEquals(607, errno);
    }


}
