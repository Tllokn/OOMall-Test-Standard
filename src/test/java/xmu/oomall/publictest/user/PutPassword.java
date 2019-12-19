package xmu.oomall.publictest.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import xmu.oomall.vo.ResetVo;
import xmu.oomall.domain.User;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 24320172203219
 * @version 1.1
 * @date 2019/12/17 01:58
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PutPassword {

    @Value("http://${host}:${port}/password")
    String putPasswordUrl;

    @Value("http://${host}:${port}/captcha")
    String postCaptchaUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void correctChangePasswordTest() throws Exception {
        /* 24320172203219 */

        /* 准备获得Captcha所需的数据 */
        User user = new User();
        user.setId(10086);
        user.setName("10086");
        user.setNickname("10086");
        user.setPassword("10086");
        user.setGender(1);
        user.setBirthday(LocalDateTime.of(1999,8,5,0,0,0));
        user.setMobile("13988888888");
        user.setRebate(0);
        user.setAvatar("https://ss3.bdstatic.com/yrwDcj7w0QhBkMak8IuT_XF5ehU5bvGh7c50/logopic/18ed73ece744554c67ed1b7897151e49_fullsize.jpg");
        user.setLastLoginTime(LocalDateTime.of(2018,2,1,0,0,0));
        user.setLastLoginIp("1.1.1.1");
        user.setUserLevel(1);
        user.setWxOpenId("wx_open_id_10086");
        user.setSessionKey("session_key_10086");
        user.setRoleId(1);
        user.setGmtCreate(LocalDateTime.of(2018,2,1,0,0,0));
        user.setGmtModified(LocalDateTime.of(2018,2,1,0,0,0));
        user.setBeDeleted(false);

        /* 设置请求头部 */
        URI uri = new URI(postCaptchaUrl);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(user.getMobile(), httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        /*取得响应体*/
        String body = response.getBody();
        String captcha = JacksonUtil.parseString(body, "data");


        /* 准备更改密码所需的数据 */
        ResetVo resetVo = new ResetVo();
        resetVo.setTelephone("13988888888");
        resetVo.setPassword("10089");
        resetVo.setCode(captcha);

        user.setPassword("10089");

        /* 设置请求头部 */
        uri = new URI(putPasswordUrl);
        httpHeaders = testRestTemplate.headForHeaders(uri);
        httpEntity = new HttpEntity(resetVo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        response = testRestTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /* assert判断 */
        User testUser = JacksonUtil.parseObject(body, "data", User.class);
        assertEquals(user.getName(), testUser.getName()); // 确定改对了人
        assertEquals(null, testUser.getPassword()); // 确定没有返回密码
    }

    @Test
    public void incorrectCaptchaChangePasswordTest() throws Exception {
        /* 24320172203219 */

        /* 准备获得Captcha所需的数据 */
        String mobile = "13988888888";

        /* 设置请求头部 */
        URI uri = new URI(postCaptchaUrl);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(mobile, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        /*取得响应体*/
        String body = response.getBody();
        String captcha = JacksonUtil.parseString(body, "data");


        /* 准备更改密码所需的数据 */
        ResetVo resetVo = new ResetVo();
        resetVo.setTelephone("13988888888");
        resetVo.setPassword("10089");
        resetVo.setCode(captcha + "error");

        /* 设置请求头部 */
        uri = new URI(putPasswordUrl);
        httpHeaders = testRestTemplate.headForHeaders(uri);
        httpEntity = new HttpEntity(resetVo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        response = testRestTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertNotEquals(0, errno);
    }

}
