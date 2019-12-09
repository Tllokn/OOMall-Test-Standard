package xmu.oomall.order;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.Order;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
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
        /* 登陆 */

        URI uri = new URI(url.replace("{id}", "999"));

        String response = testRestTemplate.getForObject(uri, String.class);

        Integer errno = JacksonUtil.parseInteger(response, "errno");
        assertEquals(0, errno);

        Order order = JacksonUtil.parseObject(response, "data", Order.class);
        assertEquals(999, order.getId());
        assertEquals(9, order.getUserId());
        assertEquals("12345", order.getOrderSn());
        assertEquals("13988888888", order.getMobile());
        assertEquals("请尽快送达", order.getMessage());
        assertEquals(BigDecimal.valueOf(182.43), order.getGoodsPrice());
        assertEquals("123456789", order.getShipSn());
    }
}
