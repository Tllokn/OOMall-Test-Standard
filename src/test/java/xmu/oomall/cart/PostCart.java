package xmu.oomall.cart;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.CartItem;


import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostCart {


    @Value("http://${host}:${port}/cartItems")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test32177() throws Exception{


        /*24320172203217*/

        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        httpHeaders.set("userId","5");

        CartItem cartItem = new CartItem();
        cartItem.setBeCheck(false);
        cartItem.setGmtCreate(LocalDateTime.now());
        cartItem.setGmtModified(LocalDateTime.now());
        cartItem.setNumber(5);
        cartItem.setProductId(1);
        /* 设置请求头部 */
        httpHeaders.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<CartItem> httpEntity  = new HttpEntity<>(cartItem, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST,httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
