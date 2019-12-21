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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @a
 * uthor 学号24320172203139
 * @create 2019/12/16 13:22
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class OrdersIdCancelTest {

    @Value("http://${oomall.host}:${oomall.port}/orderService/orders/{id}/cancel")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    @Test
    public void tc_OrdersIdCancel_001() throws URISyntaxException {

        adtUserAccount.setUserName("32893118851");
        adtUserAccount.setPassword("123456");
        Order order=new Order();
        order.setStatusCode(1);
        order.setId(31391);

        URI uri=new URI(url.replace("{id}","509"));
        HttpHeaders httpHeaders= adtUserAccount.createHeaderWithToken();
        assertNotEquals(null, httpHeaders);

        HttpEntity<Order> httpEntity=new HttpEntity<>(order,httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT,httpEntity,String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String body=  responseEntity.getBody();

        Integer errno= JacksonUtil.parseInteger(body,"errno");
        assertEquals(607,errno); //订单修改状态失败
    }

    /**
     * @author Ming Qiu
     * @throws URISyntaxException
     */
    @Test
    public void tc_OrdersIdCancel_002() throws URISyntaxException {

        adtUserAccount.setUserName("83480673081"); //id = 58
        adtUserAccount.setPassword("123456");
        Order order=new Order();
        order.setStatusCode(1);
        order.setId(31391);

        URI uri=new URI(url.replace("{id}","509"));
        HttpHeaders httpHeaders= adtUserAccount.createHeaderWithToken();
        assertNotEquals(null, httpHeaders);

        HttpEntity<Order> httpEntity=new HttpEntity<>(order,httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT,httpEntity,String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String body=  responseEntity.getBody();

        Integer errno= JacksonUtil.parseInteger(body,"errno");
        assertEquals(604,errno); //订单非法操作（已经提交的订单不能取消）

    }

}
