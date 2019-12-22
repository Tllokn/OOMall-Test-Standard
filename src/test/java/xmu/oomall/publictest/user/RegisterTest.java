package xmu.oomall.publictest.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.vo.LoginVo;
import xmu.oomall.domain.User;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.UserRegisterVo;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PublicTestApplication.class)
public class RegisterTest {

    @Value("http://${oomall.host}:${oomall.port}/userInfoService/registe" +
            "r")
    private String url;

    @Value("http://${oomall.host}:${oomall.port}/userInfoService/")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    /**
     * @author Ming Qiu
     * @version 1.3
     * @date 2019/12/16 22:19
     * @modified by Ming Qiu
     */
    @Test
    public void tc_Register_001() throws Exception {

        HttpHeaders httpHeaders = adtUserAccount.createHeaders();
        HttpEntity<String> captEntity = new HttpEntity<>("1299988", httpHeaders);

        URI uri = new URI(baseUrl + "captcha");
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, captEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
        String code = JacksonUtil.parseString(body, "data");

        /* 准备数据 */
        UserRegisterVo user = new UserRegisterVo();
        user.setUsername("993843333");
        user.setPassword("10086");
        user.setTelephone("1299988");
        user.setCode(code);

        /* 设置请求头部 */
        uri = new URI(url);

        HttpEntity<UserRegisterVo> httpEntity = new HttpEntity<>(user, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /* assert判断 */
        User testUser = JacksonUtil.parseObject(body, "data", User.class);
        assertEquals(user.getUsername(), testUser.getName());
        assertEquals(user.getTelephone(), testUser.getMobile());

        LoginVo loginVo = new LoginVo();
        loginVo.setUsername(user.getUsername());
        loginVo.setPassword(user.getPassword());
        HttpEntity<LoginVo> entity = new HttpEntity<>(loginVo, httpHeaders);
        uri = new URI(baseUrl + "login");
        response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @author Ming Qiu
     * @version 1.3
     * @date 2019/12/16 22:19
     * @modified by Ming Qiu
     */
    @Test
    public void tc_Register_002() throws Exception {

        HttpHeaders httpHeaders = adtUserAccount.createHeaders();
        HttpEntity<String> captEntity = new HttpEntity<>("12999887", httpHeaders);

        URI uri = new URI(baseUrl + "captcha");
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, captEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
        String code = "999999";

        /* 准备数据 */
        UserRegisterVo user = new UserRegisterVo();
        user.setUsername("334445");
        user.setPassword("10086");
        user.setTelephone("12999887");
        user.setCode(code);

        /* 设置请求头部 */
        uri = new URI(url);

        HttpEntity<UserRegisterVo> httpEntity = new HttpEntity<>(user, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        body = response.getBody();
        errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(667, errno); //验证码错误
    }


}
