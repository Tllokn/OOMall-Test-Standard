package xmu.oomall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import xmu.oomall.vo.LoginVo;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author: Tens
 * @Description:
 * @Date: 2019/12/17 19:29
 */
public class Login {

    @Value("http://${host}:${port}/userInfoService/login")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    public String login() throws Exception{
        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("10086");
        loginVo.setPassword("10086");

        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(loginVo, httpHeaders);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<String> tokens = response.getHeaders().get("token");

        return tokens.get(0);
    }
}
