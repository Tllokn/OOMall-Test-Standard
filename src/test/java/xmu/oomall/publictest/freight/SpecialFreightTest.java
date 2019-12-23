package xmu.oomall.publictest.freight;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hsx
 * @version 1.0
 * @date 2019/12/20 22:00
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class SpecialFreightTest {
    @Value("http://${oomall.host}:${oomall.port}/freightService/specialFreights?page=1&limit=12")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    /**
     * @author 24320172203141
     */
    @Test
    public void tc_specialFreight_001() throws Exception {
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

}
