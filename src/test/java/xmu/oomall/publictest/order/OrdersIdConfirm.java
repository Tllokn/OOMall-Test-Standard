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
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * OrderIdConfirm
 *
 * @author clines
 * @date 2019/12/24
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class OrdersIdConfirm {

    @Value("http://${oomall.host}:${oomall.port}/orderService/orders/{id}")
    private String baseUrl;

    @Value("http://${oomall.host}:${oomall.port}/orderService/orders/{id}/confirm")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    /**
     * @author 24320172203109
     */
    @Test
    public void tc_OrdersIdConfirm_001() throws URISyntaxException {

        adtUserAccount.setUserName("61951292084"); // userId = 12
        adtUserAccount.setPassword("123456");

        HttpHeaders httpHeaders = adtUserAccount.createHeaderWithToken();

        /* 查询订单 */
        Integer orderId = 19497;
        URI uri = new URI(baseUrl.replace("{id}", orderId.toString()));
        assertNotEquals(null, httpHeaders);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String body=  responseEntity.getBody();
        System.out.println(body);
        Integer errno= JacksonUtil.parseObject(body, "errno", Integer.class);
        assertEquals(0, errno);
        Order order = JacksonUtil.parseObject(body, "data", Order.class);
        assertNotNull(order);
        assertEquals(orderId, order.getId());
        assertEquals(4, order.getStatusCode()); // 确认为未收货的订单

        /* 确认收货 */
        URI uri2 = new URI(url.replace("{id}", orderId.toString()));
        responseEntity = restTemplate.exchange(uri2, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        body = responseEntity.getBody();
        errno = JacksonUtil.parseObject(body, "errno", Integer.class);
        assertEquals(0, errno);

        /* 检查状态是否改变 */
        responseEntity= restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        body=  responseEntity.getBody();
        errno= JacksonUtil.parseObject(body, "errno", Integer.class);
        assertEquals(0, errno);
        order = JacksonUtil.parseObject(body, "data", Order.class);
        assertNotNull(order);
        assertEquals(orderId, order.getId());
        assertEquals(6, order.getStatusCode()); // 是否为已收货状态
    }

    /**
     * @author 24320172203109
     */
    @Test
    public void tc_OrdersIdConfirm_002() throws URISyntaxException {

        adtUserAccount.setUserName("61951292084"); // userId = 12
        adtUserAccount.setPassword("123456");

        HttpHeaders httpHeaders = adtUserAccount.createHeaderWithToken();

        URI uri = new URI(url.replace("{id}", "19502"));
        assertNotEquals(null, httpHeaders);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseObject(body, "errno", Integer.class);
        assertEquals(607, errno);
    }

    /**
     * @author 24320172203109
     */
    @Test
    public void tc_OrdersIdConfirm_003() throws URISyntaxException {

        adtUserAccount.setUserName("61951292084"); // userId = 12
        adtUserAccount.setPassword("123456");

        HttpHeaders httpHeaders = adtUserAccount.createHeaderWithToken();

        URI uri = new URI(url.replace("{id}", "140"));
        assertNotEquals(null, httpHeaders);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseObject(body, "errno", Integer.class);
        assertEquals(604, errno);
    }

    /**
     * @author 24320172203109
     */
    @Test
    public void tc_OrdersIdConfirm_004() throws URISyntaxException {

        adtUserAccount.setUserName("61951292084"); // userId = 12
        adtUserAccount.setPassword("123456");

        HttpHeaders httpHeaders = adtUserAccount.createHeaderWithToken();

        URI uri = new URI(url.replace("{id}", "19501"));
        assertNotEquals(null, httpHeaders);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseObject(body, "errno", Integer.class);
        assertEquals(600, errno);
    }
}
