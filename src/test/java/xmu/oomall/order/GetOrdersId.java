package xmu.oomall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetOrdersId {
    @Value("http://${host}:${port}/orders/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws Exception{
        URI uri = new URI(url.replace("{id}", "2"));

        String result = testRestTemplate.getForObject(uri, String.class);

        String data = JacksonUtil.parseString(result, "data");
        String errno = JacksonUtil.parseString(result, "errno");



        System.out.println(data);
    }
}
