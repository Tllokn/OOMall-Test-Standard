package xmu.oomall.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetLog {
    @Value("http://${host}:${port}/logService/logs")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * @author 24320172203213
     */
    @Test
    public void test11111() throws Exception {
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        httpHeaders.add("userId","1");
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // 发出HTTP请求
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        Integer status = JacksonUtil.parseInteger(body, "status");
        assertNotEquals(new Integer(500),status);
        List<HashMap> lists = JacksonUtil.parseObject(body, "data",List.class);
        for(HashMap item : lists){
            assertNotEquals(null,item.get("id"));
        }

    }
}
