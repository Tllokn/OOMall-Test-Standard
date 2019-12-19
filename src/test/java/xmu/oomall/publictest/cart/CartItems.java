package xmu.oomall.publictest.cart;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.CartItem;
import xmu.oomall.test.UserAccount;
import xmu.oomall.util.JacksonUtil;


import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author
 */
@SpringBootTest
public class CartItems {


    @Value("http://${oomall.host}:${oomall.port}/cartService/cartItems")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserAccount userAccount;

    private HttpHeaders getHttpHeaders(URI uri) throws URISyntaxException {
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        if (!userAccount.addToken(httpHeaders)) {
            //登录失败
            assertTrue(false);
        }
        return httpHeaders;
    }

    /**
     * @author 24320172203217
     * @modified by ming qiu
     */
    @Test
    public void tc_CartItems_001() throws Exception{

        URI uri = new URI(url);
        HttpHeaders httpHeaders = this.getHttpHeaders(uri);

        CartItem cartItem = new CartItem();
        cartItem.setBeCheck(false);
        cartItem.setGmtCreate(LocalDateTime.now());
        cartItem.setGmtModified(LocalDateTime.now());
        cartItem.setNumber(5);
        cartItem.setProductId(1);
        /* 设置请求头部 */
        HttpEntity<CartItem> httpEntity  = new HttpEntity<>(cartItem, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST,httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(0,errNo);

        CartItem responseItem = JacksonUtil.parseObject(body, "data", CartItem.class);
        Integer id = responseItem.getId();
        assertEquals(false, responseItem.getBeCheck() );
        assertEquals(5, responseItem.getNumber());

        httpEntity= new HttpEntity<>(httpHeaders);
        response = testRestTemplate.exchange(uri, HttpMethod.GET,httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0,errNo);
        List<CartItem> items = JacksonUtil.parseObjectList(body,"data",CartItem.class);
        Boolean found = false;
        for (CartItem item : items){
            if (item.getId() == id ){
                found = true;
                break;
            }
        }
        assertTrue(found);

    }

    @Test
    public void test32176() throws Exception
    {
        /*24320172203217*/

        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        httpHeaders.set("userId","-1");
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer status = JacksonUtil.parseInteger(body, "status");
        assertEquals(500,status);
    }
    @Test
    public void test32175() throws Exception
    {
        /*24320172203217*/

        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        httpHeaders.set("userId","10086");
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer status = JacksonUtil.parseInteger(body, "status");
        assertEquals(500,status);
        List<String> cartItems = JacksonUtil.parseObject(body,"data",List.class);

        assertEquals(cartItems,null);
        assertEquals(true,cartItems.size() > 0);
    }
}
