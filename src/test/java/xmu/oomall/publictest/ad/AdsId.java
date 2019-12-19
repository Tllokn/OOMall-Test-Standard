package xmu.oomall.publictest.ad;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import xmu.oomall.domain.Ad;
import xmu.oomall.test.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author
 */
@SpringBootTest
public class AdsId {
    @Value("http://${oomall.host}:${oomall.host}/adService/ads/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AdminAccount adminAccount;

    private HttpHeaders getHttpHeaders(URI uri) throws URISyntaxException {
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        if (!adminAccount.addToken(httpHeaders)) {
            //登录失败
            assertTrue(false);
        }
        return httpHeaders;
    }

    /*********************************************************
     * DELETE
     *********************************************************/
    /**
     * @author ming qiu
     */
    @Test
    public void tc_adsId_001() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "121"));
        HttpHeaders httpHeaders = getHttpHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = this.testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        response = this.testRestTemplate.getForEntity(url, String.class);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(680, errNo);
    }

    /**
     * @author ming qiu
     */
    @Test
    public void tc_adsId_002() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "122"));
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ResponseEntity<String> response = this.testRestTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(errNo, 666); //用户无操作权限

        response = this.testRestTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);
        Ad ad = JacksonUtil.parseObject(body,"data", Ad.class);
        assertEquals("诚德轩八头缠枝莲花套组超值精选", ad.getName());
    }

    /**
     * @author ming qiu
     */
    @Test
    public void tc_adsId_003() throws Exception{
        //准备要更新的数据
        Ad ad = new Ad();
        ad.setName("我们太难了");
        ad.setContent("hello");

        //设置头部
        URI uri = new URI(url.replace("{id}","123"));
        HttpHeaders headers = getHttpHeaders(uri);

        HttpEntity<Ad> requestUpdate = new HttpEntity<>(ad, headers);

        //发出http请求
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //取出返回的body
        Ad responseAd = JacksonUtil.parseObject(response.getBody(), "data", Ad.class);

        //比较值是否相等
        Integer errno = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errno);
        assertEquals(123,responseAd.getId());
        assertEquals(ad.getName(),responseAd.getName());
        assertEquals(ad.getContent(),responseAd.getContent());

        response = this.testRestTemplate.getForEntity(url, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(errNo, 0);

        responseAd = JacksonUtil.parseObject(body,"data", Ad.class);
        assertEquals(123,responseAd.getId());
        assertEquals(ad.getName(),responseAd.getName());
        assertEquals(ad.getContent(),responseAd.getContent());
    }
}
