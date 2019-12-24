package xmu.oomall.publictest.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import xmu.aftersales.domain.AftersalesService;
import xmu.aftersales.test.AdtUserAccount;
import xmu.aftersales.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author: 24320172203133
 */
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AftersalesTest {

    @Value("http://${oomall.host}:${oomall.port}/afterSaleService/afterSaleServices")
    String postUrl;

    @Value("http://${oomall.host}:${oomall.port}/afterSaleService/afterSaleServices/{id}")
    String cancelUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AdtUserAccount userAccount;

    private HttpHeaders getHttpHeaders() throws URISyntaxException {
        HttpHeaders headers = userAccount.createHeaderWithToken();
        if (headers == null) {
            //登录失败
            assertTrue(false);
        }
        return headers;
    }

    @Test
    public void tc_afterSaleServices_001() throws Exception {

        AftersalesService aftersalesService = new AftersalesService();

        //user_id = 4
        userAccount.setUserName("79396283327");
        userAccount.setPassword("123456");

        //创建新的售后申请
        aftersalesService.setApplyReason("物品损坏，求换货");
        aftersalesService.setOrderItemId(38);
        aftersalesService.setType(1);
        aftersalesService.setNumber(1);
        aftersalesService.setBeApplied(true);
        aftersalesService.setApplyTime(LocalDateTime.now());
        aftersalesService.setGmtCreate(LocalDateTime.now());
        aftersalesService.setGmtModified(LocalDateTime.now());

        URI uri = new URI(postUrl);
        HttpHeaders httpHeaders = this.getHttpHeaders();
        System.out.println("Generated httpheaders = " + httpHeaders);

        /* 设置请求头部 */
        HttpEntity<AftersalesService> aftersalesServiceHttpEntity = new HttpEntity<>(aftersalesService, httpHeaders);
        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, aftersalesServiceHttpEntity, String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(0, errNo);

        AftersalesService responseItem = JacksonUtil.parseObject(body, "data", AftersalesService.class);

        assertEquals(4, responseItem.getUserId());
        assertEquals(1, responseItem.getType());
        assertEquals(38, responseItem.getOrderItemId());
        assertEquals("物品损坏，求换货", responseItem.getApplyReason());
    }

    @Test
    public void tc_afterSaleServices_002() throws Exception {

        AftersalesService aftersalesService = new AftersalesService();

        //user_id = 1
        userAccount.setUserName("92998201300");
        userAccount.setPassword("123456");

        //创建新的售后申请
        aftersalesService.setApplyReason("物品损坏，求换货");
        aftersalesService.setOrderItemId(38); // 该物品不属于该用户
        aftersalesService.setType(1);
        aftersalesService.setNumber(1);
        aftersalesService.setBeApplied(true);
        aftersalesService.setApplyTime(LocalDateTime.now());
        aftersalesService.setGmtCreate(LocalDateTime.now());
        aftersalesService.setGmtModified(LocalDateTime.now());

        URI uri = new URI(postUrl);
        HttpHeaders httpHeaders = this.getHttpHeaders();
        System.out.println("Generated httpheaders = " + httpHeaders);

        /* 设置请求头部 */
        HttpEntity<AftersalesService> aftersalesServiceHttpEntity = new HttpEntity<>(aftersalesService, httpHeaders);
        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, aftersalesServiceHttpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(692, errNo);

    }

    @Test
    public void tc_afterSaleServices_003() throws Exception {

        AftersalesService aftersalesService = new AftersalesService();

        //user_id = 1
        userAccount.setUserName("92998201300");
        userAccount.setPassword("123456");

        //创建新的售后申请
        aftersalesService.setApplyReason("物品损坏，求换货");
        aftersalesService.setOrderItemId(45);
        aftersalesService.setType(1);
        aftersalesService.setNumber(3); // 数量超过订单明细中上限
        aftersalesService.setBeApplied(true);
        aftersalesService.setApplyTime(LocalDateTime.now());
        aftersalesService.setGmtCreate(LocalDateTime.now());
        aftersalesService.setGmtModified(LocalDateTime.now());

        URI uri = new URI(postUrl);
        HttpHeaders httpHeaders = this.getHttpHeaders();
        System.out.println("Generated httpheaders = " + httpHeaders);

        /* 设置请求头部 */
        HttpEntity<AftersalesService> aftersalesServiceHttpEntity = new HttpEntity<>(aftersalesService, httpHeaders);
        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, aftersalesServiceHttpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(692, errNo);

    }

}
