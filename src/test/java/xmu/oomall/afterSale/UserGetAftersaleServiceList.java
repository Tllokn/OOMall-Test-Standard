package xmu.ddao.controller.user;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.ddao.domain.standard.AftersalesService;
import xmu.ddao.util.JacksonUtil;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: 何少杰
 * @StudentId: 24320172203133
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserGetAftersaleServiceList {

    @Value(value = "http://${host}:${port}/afterSaleServices")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void test() {
        URI uri = null;
        try{
            uri = new URI(url);
        }
        catch (Exception e){

        }
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
