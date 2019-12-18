package xmu.oomall.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import xmu.oomall.vo.LoginVo;
import xmu.oomall.domain.User;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostLogin {

    @Value("http://${host}:${port}/userService/login")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * @author 24320172203219
     * @version 1.3
     * @date 2019/12/16 22:19
     */
    @Test
    public void correctLoginTest() throws Exception {
        /* 24320172203219 */

        /* 准备数据 */
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

        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("10086");
        loginVo.setPassword("10086");

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(loginVo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /* assert判断 */
        String data = JacksonUtil.parseString(body, "data");
        User testUser = JacksonUtil.parseObject(data, "data", User.class);
        assertEquals(user.getName(), testUser.getName());
    }

    @Test
    public void incorrectPasswordLoginTest() throws Exception {
        /* 24320172203219 */

        /* 准备数据 */
        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("10086");
        loginVo.setPassword("10087");

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(loginVo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertNotEquals(0, errno);
    }


    @Test
    public void incorrectUsernameLoginTest() throws Exception {
        /* 24320172203219 */

        /* 准备数据 */
        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("errorUsername");
        loginVo.setPassword("10086");

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(loginVo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertNotEquals(0, errno);
    }

}
