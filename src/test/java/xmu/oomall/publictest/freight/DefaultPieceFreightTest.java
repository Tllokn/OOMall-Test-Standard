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
import static xmu.oomall.util.HttpRequest.getHttpHeaders;


/**
 * @author hsx
 * @version 1.0
 * @date 2019/12/20 22:04
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class DefaultPieceFreightTest {
    @Value("http://${oomall.host}:${oomall.port}/freightService/defaultPieceFreights")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Test
    public void tc_defaultPieceFreight_001() throws Exception {
        /**
         * @author 24320172203141
         */
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
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
