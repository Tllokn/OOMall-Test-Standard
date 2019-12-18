package xmu.oomall.ad;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.test.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteAdsId {
    @Value("http://${oomall.host}:${oomall.host}/ads/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AdminAccount adminAccount;

    private HttpHeaders getHttpHeaders(URI uri) throws URISyntaxException {
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        if (!adminAccount.addToken(httpHeaders)) {
            //登录失败
            assertTrue(false);
        }
        return httpHeaders;
    }

    @Test
    public void test1() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "1"));
        HttpHeaders httpHeaders = getHttpHeaders(uri);

        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        String errmsg = JacksonUtil.parseString(body, "errmsg");
        assertEquals("成功", errmsg);
    }


}
