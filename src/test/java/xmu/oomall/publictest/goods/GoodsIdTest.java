package xmu.oomall.publictest.goods;
/**
 * @author 24320172203264
 * @version 1.0
 * @date 2019/12/10 20:04
 */


import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PublicTestApplication.class)
public class GoodsIdTest {
    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/goods/{id}")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAccount userAccount;

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_GoodsId_001() throws Exception{
        URI uri = new URI(url.replace("{id}","288"));
        HttpHeaders httpHeaders = userAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response);
        /*取得响应体*/
        String body = response.getBody();

        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        Goods goods =JacksonUtil.parseObject(body,"data",Goods.class);
        assertEquals(288,goods.getId());
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_GoodsId_002() throws Exception{
        URI uri = new URI(url.replace("{id}","10000223"));
        HttpHeaders httpHeaders = userAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(775, errno); //该商品不存在

    }


}
