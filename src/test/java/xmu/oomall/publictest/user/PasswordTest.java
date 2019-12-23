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
import xmu.oomall.domain.User;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.ResetPasswordVo;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest(classes = PublicTestApplication.class)
public class PasswordTest {
    @Value("http://${oomall.host}:${oomall.port}/userService/password")
    String putPasswordUrl;

    @Value("http://${oomall.host}:${oomall.port}/userService/captcha")
    String postCaptchaUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    @Autowired
    private UserAccount userAccount;

    /**
     * @author 24320172203219
     */
    @Test
    public void tc_password_001() throws Exception {
        User user = new User();
        user.setId(1);
        user.setPassword("123456");
        user.setName("86813765193");
        user.setGender(0);
        user.setBirthday(null);
        user.setMobile("13959288888");
        user.setNickname(null);
        user.setRebate(0);
        user.setAvatar(null);
        user.setLastLoginTime(LocalDateTime.of(2019,12,18,9,39,31));
        user.setLastLoginIp("218.18.157.228");
        user.setUserLevel(0);
        user.setRoleId(0);
        user.setWxOpenId(null);
        user.setSessionKey(null);
        user.setGmtCreate(LocalDateTime.of(2019,12,18,9,39,31));
        user.setGmtModified(LocalDateTime.of(2019,12,18,9,39,31));
        user.setBeDeleted(false);

        URI uri = new URI(postCaptchaUrl);
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity<>(user.getMobile(), httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        String result = responseEntity.getBody();
        String captcha = JacksonUtil.parseString(result, "data");


        ResetPasswordVo resetVo = new ResetPasswordVo();
        resetVo.setTelephone("13959288888");
        resetVo.setPassword("654321");
        resetVo.setCode(captcha);

        user.setPassword("654321");

        uri = new URI(putPasswordUrl);
        httpHeaders = getHttpHeaders(adtUserAccount);
        httpEntity = new HttpEntity<>(resetVo, httpHeaders);

        responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result, "errno");
        User testUser = JacksonUtil.parseObject(result, "data", User.class);

        assertEquals(0, errno);
        assertEquals(user.getName(), testUser.getName()); // 确定改对了人
        assertEquals(null, testUser.getPassword()); // 确定没有返回密码
    }

    /**
     * @author 24320172203219
     */
    @Test
    public void tc_password_002() throws Exception {
        ResetPasswordVo resetVo = new ResetPasswordVo();
        resetVo.setTelephone("13959288888");
        resetVo.setPassword("654321");
        resetVo.setCode("error captcha");

        URI uri = new URI(putPasswordUrl);
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity<>(resetVo, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result, "errno");

        assertEquals(667, errno);
    }

}