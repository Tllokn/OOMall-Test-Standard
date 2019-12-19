package xmu.oomall.publictest.cart;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import xmu.oomall.util.JacksonUtil;


import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetCartId {
    @Value("http://${host}:${port}/cartItems/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Test
    public void test32173() throws Exception
    {
        /*查询正常购物车*/
        /*24320172203217*/

        URI uri = new URI(url.replace("{id}", "1"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        httpHeaders.set("userId","10086");
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    public void test32174() throws Exception
    {
        /*查询其非个人的购物车(非法查询购物车项)*/
        /*24320172203217*/

        URI uri = new URI(url.replace("{id}", "1"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        httpHeaders.set("userId","10");
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertNotEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer status = JacksonUtil.parseInteger(body, "status");
        assertNotEquals(500,status);



    }

}
