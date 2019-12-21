package xmu.oomall.publictest.log;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest(classes = PublicTestApplication.class)
public class LogsTest {
    @Value("http://${oomall.host}:${oomall.port}/logService/logs")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Autowired
    private UserAccount userAccount;

    /**
     * @author 24320172203213
     * @modified By Ming Qiu
     */
    @Test
    public void tc_Logs_001() throws Exception {
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @author  Ming Qiu
     */
    @Test
    public void tc_Logs_002() throws Exception {
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(666, errno); //用户无权限
    }

}
