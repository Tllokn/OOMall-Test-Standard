package xmu.oomall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.Application;

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetOrders {
    final static String url = "http://localhost:30020/orders";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws Exception{
        URI uri = new URI(url);

        String result = testRestTemplate.getForObject(uri,String.class);

        System.out.println(result);
    }
}
