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
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PublicTestApplication.class)
public class LogsTest {
    @Value("http://${oomall.host}:${oomall.port}/logService/logs")
    private String url;

    @Value("http://${oomall.host}:${oomall.port}/")
    private String baseUrl;

    @Value("${oomall.adminuser}")
    private String adminUserName;

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

        URI uri = new URI(baseUrl + "admin?adminName="+adminUserName);
        System.out.println("adminUserName = " + adminUserName);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
        List users = JacksonUtil.parseObject(body, "data", List.class);
        assertEquals(1, users.size());
        HashMap user = (HashMap) users.get(0);
        Integer adminId = (Integer) user.get("id");

        uri = new URI(url + "?adminid="+adminId);

        // 发出HTTP请求
        response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 取得响应体
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @author  Ming Qiu
     */
    @Test
    public void tc_Logs_002() throws Exception {
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
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
