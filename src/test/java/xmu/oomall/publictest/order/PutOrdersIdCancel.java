package xmu.oomall.publictest.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.Order;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author 学号24320172203139
 * @create 2019/12/16 13:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PutOrdersIdCancel {

    @Value("http://${host}:${port}/orders/{id}/cancel")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws URISyntaxException {

        Order order=new Order();
        order.setStatusCode(1);
        order.setId(31391);

        URI uri=new URI(url.replace("{id}","31391"));
        HttpHeaders httpHeaders=testRestTemplate.headForHeaders(uri);
        HttpEntity<Order> httpEntity=new HttpEntity<>(order,httpHeaders);

        ResponseEntity<String> responseEntity=testRestTemplate.exchange(uri, HttpMethod.PUT,httpEntity,String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String body=  responseEntity.getBody();

        Integer errno= JacksonUtil.parseInteger(body,"errno");
        assertEquals(0,errno);

        Order testOrder= JacksonUtil.parseObject(body,"data",Order.class);

        System.out.println(testOrder.toString());
        assertEquals(31391,testOrder.getId());
        assertEquals(1,testOrder.getStatusCode());
    }

}
