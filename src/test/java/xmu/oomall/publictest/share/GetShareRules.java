package xmu.oomall.publictest.share;

import xmu.oomall.domain.*;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;
import org.junit.Test;

import java.net.URI;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.net.URISyntaxException;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;
import static org.junit.jupiter.api.Assertions.*;



@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetShareRules {
    @Value("http://${oomall.host}:${oomall.port}/shareService/goods/{id}/shareRules")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserAccount userAccount;

    @Autowired
    private AdminAccount adminAccount;

    @Test
    public void getShareRule() throws URISyntaxException {

        /*24320172203139*/

        /* 设置请求头部*/
        URI uri=new URI(url.replace("{id}","1006239"));
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);


        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(),HttpStatus.OK);

        /*取得响应体*/
        String body = response.getBody();
        System.out.println(body);

        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        Integer status = JacksonUtil.parseInteger(body, "status");
        assertNotEquals(500,status);
        /*assert判断*/
        ShareRule shareRule = JacksonUtil.parseObject(body, "data", ShareRule.class);


        assertEquals(1001002, shareRule.getId());
        assertEquals("{\"strategy\": [{\"lowerbound\":\"0\", \"upperbound\":\"1\", \"rate\":\"0.5\"}," +
                "{\"lowerbound\":\"2\", \"upperbound\":\"10\", \"rate\":\"0.7\"},{\"lowerbound\":\"11\", \"upperbound\":\"30\", \"rate\":\"1\"}," +
                "{\"lowerbound\":\"31\", \"upperbound\":\"100\", \"rate\":\"1.5\"}],\"type\":\"0\"}",
                shareRule.getShareLevelStrategy());

    }

}
