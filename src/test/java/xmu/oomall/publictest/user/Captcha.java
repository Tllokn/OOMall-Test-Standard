package xmu.oomall.publictest.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest(classes = PublicTestApplication.class)
public class Captcha {
    @Value("http://${oomall.host}:${oomall.port}/userService/captcha")
    String url;

    @Autowired
    private RestTemplate testRestTemplate;

    @Autowired
    private UserAccount userAccount;

    /**
     * @author 24320172203219
     */
    @Test
    public void tc_captcha_001() throws Exception {
        String telephone = "13959288888";

        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity<>(telephone, httpHeaders);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result, "errno");
        String captcha = JacksonUtil.parseString(result, "data");

        assertEquals(200, errno);
        assertNotEquals(null, captcha);
    }
}