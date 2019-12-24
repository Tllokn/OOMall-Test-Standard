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
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.ResetPhoneVo;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest(classes = PublicTestApplication.class)
public class Phone {
    @Value("http://${oomall.host}:${oomall.port}/userService/phone")
    String putPhoneUrl;

    @Value("http://${oomall.host}:${oomall.port}/userService/captcha")
    String postCaptchaUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdtUserAccount adtUserAccount;

    /**
     * @author 24320172203219
     */
    @Test
    public void tc_phone_001() throws Exception {
        User user = new User();
        user.setId(2);
        user.setPassword("123456");
        user.setName("28269168388");
        user.setGender(0);
        user.setBirthday(null);
        user.setMobile("13959288888");
        user.setNickname(null);
        user.setRebate(0);
        user.setAvatar(null);
        user.setLastLoginTime(LocalDateTime.of(2019,12,18,9,39,31));
        user.setLastLoginIp("59.63.23.162");
        user.setUserLevel(0);
        user.setRoleId(0);
        user.setWxOpenId(null);
        user.setSessionKey(null);
        user.setGmtCreate(LocalDateTime.of(2019,12,18,9,39,31));
        user.setGmtModified(LocalDateTime.of(2019,12,18,9,39,31));
        user.setBeDeleted(false);

        String newTelephone = "13988888899";

        URI uri = new URI(postCaptchaUrl);
        HttpHeaders httpHeaders = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity<>(newTelephone, httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        String result = responseEntity.getBody();
        String captcha = JacksonUtil.parseString(result, "data");


        ResetPhoneVo resetVo = new ResetPhoneVo();
        resetVo.setTelephone(user.getMobile());
        resetVo.setPassword(user.getPassword());
        resetVo.setCode(captcha);
        resetVo.setNewTelephone(newTelephone);

        uri = new URI(putPhoneUrl);
        httpHeaders = getHttpHeaders(adtUserAccount);
        httpEntity = new HttpEntity<>(resetVo, httpHeaders);

        responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result, "errno");
        User testUser = JacksonUtil.parseObject(result, "data", User.class);

        assertEquals(0, errno);
        assertEquals(user.getName(), testUser.getName()); // 确定改对了人
        assertEquals(newTelephone, testUser.getMobile()); // 确定改了手机号
    }
}