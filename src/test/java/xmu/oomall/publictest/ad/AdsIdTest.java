package xmu.oomall.publictest.ad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Ad;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.NoAdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class AdsIdTest {

    @Value("http://${oomall.host}:${oomall.port}/adService/ads/{id}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;



    /*********************************************************
     * DELETE
     *********************************************************/
    /**
     * @author ming qiu
     */
    @Test
    public void tc_adsId_001() throws Exception {
        /* 设置请求头部*/
        System.out.println("url = " + url);
        URI uri = new URI(url.replace("{id}", "121"));
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);

        response = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(680, errNo);
    }

    /**
     * @author ming qiu
     */
    @Test
    public void tc_adsId_002() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "122"));
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(660, errNo); //用户无操作权限

        //原来的对象还在
        response = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
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
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Ad> requestUpdate = new HttpEntity<>(ad, headers);

        //发出http请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //取出返回的body
        Ad responseAd = JacksonUtil.parseObject(response.getBody(), "data", Ad.class);

        //比较值是否相等
        Integer errno = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errno);
        assertEquals(123,responseAd.getId());
        assertEquals(ad.getName(),responseAd.getName());
        assertEquals(ad.getContent(),responseAd.getContent());

    }

    @Test
    public void tc_adsId_004() throws URISyntaxException {
        URI uri = new URI(url.replace("/{id}",""));

        HttpHeaders headers = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);
        List<HashMap> adsList = JacksonUtil.parseObject(body,"data", List.class);
        assertEquals(40, adsList.size());
    }
}
