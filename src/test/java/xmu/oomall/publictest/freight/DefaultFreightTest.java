package xmu.oomall.publictest.freight;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

/**
 * @author 24320172203141
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultFreightTest {
    @Value("http://${oomall.host}:${oomall.port}/freightService/defaultFreights")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Test
    public void tc_defaultFreight_001() throws Exception {
        /**
         * @author 24320172203141
         */
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // 发出HTTP请求
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }



}
