package xmu.oomall.publictest.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Order;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Autowired
    private AdminAccount adminAccount;

    /**
     * @author 24320172203146
     * @create 2019/12/23
     */
    @Test
    public void tc_ShipOrders_001() throws Exception{
        //无效订单
        //user_id = 1
        URI uri = new URI(url.replace("{id}","-1"));

        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

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
     * @create 2019/12/23
     */
    @Test
    public void tc_ShipOrders_002() throws Exception{
        //已发货的订单不能再发货
        URI uri = new URI(url.replace("{id}","1069"));

        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);


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
