package xmu.oomall.ad;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.Ad;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 24320172203256
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PutAdsId {
    @Value("http://${host}:${port}/adService/ads/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test() throws Exception{
        //准备要更新的数据
        Ad ad = new Ad();
        ad.setId(1);
        ad.setName("我们太难了");
        ad.setContent("hello");

        //设置头部
        URI uri = new URI(url.replace("{id}","1"));
        HttpHeaders headers = testRestTemplate.headForHeaders(uri);
        headers.add("userId","1");
        HttpEntity<Ad> requestUpdate = new HttpEntity<>(ad, headers);

        //发出http请求
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //取出返回的body
        Ad responseAd = JacksonUtil.parseObject(response.getBody(), "data", Ad.class);

        //比较值是否相等
        Integer errno = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errno);
        assertEquals(ad.getId(),responseAd.getId());
        assertEquals(ad.getName(),responseAd.getName());
        assertEquals(ad.getContent(),responseAd.getContent());
    }

}
