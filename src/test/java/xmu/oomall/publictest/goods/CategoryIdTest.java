package xmu.oomall.publictest.goods;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.GoodsCategoryPo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author 24320172203264
 * @version 1.0
 * @date 2019/12/11 23:28
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class CategoryIdTest {

    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/categories/{id}")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_CategoryId_001() throws Exception
    {
        URI uri = new URI(url.replace("{id}","122"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals("0",errno);

        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(804, errno);

        uri = new URI(url.replace("{id}","123"));
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0, errno);
        GoodsCategoryPo categoryPo = JacksonUtil.parseObject(result, "data", GoodsCategoryPo.class);
        assertEquals(123, categoryPo.getId());
        assertEquals("大师原作", categoryPo.getName());
        assertEquals(0, categoryPo.getPid());

        uri = new URI(url.replace("{id}","124"));
        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0, errno);
        categoryPo = JacksonUtil.parseObject(result, "data", GoodsCategoryPo.class);
        assertEquals(124, categoryPo.getId());
        assertEquals("艺术衍生品", categoryPo.getName());
        assertEquals(0, categoryPo.getPid());

    }

}
