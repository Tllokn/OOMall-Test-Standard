package xmu.oomall.publictest.share;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.*;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = PublicTestApplication.class)
public class GetShareRules {
    @Value("http://${oomall.host}:${oomall.port}/shareService/goods/{id}/shareRules")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAccount userAccount;

    @Autowired
    private AdminAccount adminAccount;

    /**
     * @author 24320172203139
     * @throws URISyntaxException
     */
    @Test
    public void getShareRule() throws URISyntaxException {

        /* 设置请求头部*/
        URI uri=new URI(url.replace("{id}","1006239"));
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();

        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*assert判断*/
        ShareRule shareRule = JacksonUtil.parseObject(body, "data", ShareRule.class);

        assertEquals(1001002, shareRule.getId());
        assertEquals("{\"strategy\": [{\"lowerbound\":\"0\", \"upperbound\":\"1\", \"rate\":\"0.5\"}," +
                "{\"lowerbound\":\"2\", \"upperbound\":\"10\", \"rate\":\"0.7\"},{\"lowerbound\":\"11\", \"upperbound\":\"30\", \"rate\":\"1\"}," +
                "{\"lowerbound\":\"31\", \"upperbound\":\"100\", \"rate\":\"1.5\"}],\"type\":\"0\"}",
                shareRule.getShareLevelStrategy());

    }

}
