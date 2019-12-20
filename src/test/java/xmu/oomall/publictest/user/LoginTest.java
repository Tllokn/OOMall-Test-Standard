package xmu.oomall.publictest.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.User;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.LoginVo;
import xmu.oomall.vo.UserRegisterVo;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = PublicTestApplication.class)
public class LoginTest {

    @Value("http://${oomall.host}:${oomall.port}/userService/login")
    private String url;

    @Value("http://${oomall.host}:${oomall.port}/userService/")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    /**
     * @author Ming Qiu
     */
    @Test
    public void tc_Login_001() throws Exception {

        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("37485876434");
        loginVo.setPassword("1111");
        HttpHeaders httpHeaders = adtUserAccount.createHeaders();
        HttpEntity<LoginVo> entity = new HttpEntity<>(loginVo, httpHeaders);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(664, errno); //登陆密码错误
    }

    /**
     * @author Ming Qiu
     */
    @Test
    public void tc_Login_002() throws Exception {

        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("3227485876434");
        loginVo.setPassword("1111");
        HttpHeaders httpHeaders = adtUserAccount.createHeaders();
        HttpEntity<LoginVo> entity = new HttpEntity<>(loginVo, httpHeaders);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(663, errno); //用户名不存在
    }
}
