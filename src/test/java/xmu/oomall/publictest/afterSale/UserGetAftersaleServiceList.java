package xmu.ddao.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.AftersalesService;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: 何少杰(24320172203133)
 */
@SpringBootTest(classes = PublicTestApplication.class)
class UserGetAftersaleServiceList {

    @Value(value = "http://${oomall.host}:${oomall.port}/afterSaleServices")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    UserAccount userAccount;

    private HttpHeaders getHttpHeaders() throws URISyntaxException {
        HttpHeaders headers = userAccount.createHeaderWithToken();
        System.out.println("Generated Header = " + headers);
        if (headers == null) {
            //登录失败
            assertTrue(false);
        }
        return headers;
    }

    @Test
    void test() throws URISyntaxException {
        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        // 一开始售后是空的，所以应该能查到空
        List<AftersalesService> aftersalesServicesList = JacksonUtil.parseObject(body, "data", List.class);
        assertEquals(new LinkedList<>(), aftersalesServicesList);

    }
}
