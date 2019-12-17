package xmu.oomall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteOrdersId {

    @Value("http://${host}:${port}/orders/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /*
    * @Author: 24320172203139
    * */
    @Test
    public void test1() throws URISyntaxException {

        URI uri=new URI(url.replace("{id}","31392"));

        ResponseEntity<String> responseEntity=testRestTemplate.exchange(uri, HttpMethod.DELETE,null,String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String body = responseEntity.getBody();

        Integer errno= JacksonUtil.parseInteger(body,"errno");
        assertEquals(0,errno);

        String errmsg=JacksonUtil.parseString(body,"errmsg");
        assertEquals("成功",errmsg);
    }

}
