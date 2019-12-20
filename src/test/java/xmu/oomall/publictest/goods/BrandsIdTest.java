package xmu.oomall.publictest.goods;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Brand;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest(classes = PublicTestApplication.class)
public class BrandsIdTest {

    @Value("http://${oomall.host}:${oomall.port}/goodInfoService/brands/{id}")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAccount userAccount;

    @Autowired
    private AdminAccount adminAccount;

    @Autowired
    private AdtUserAccount adtUserAccount;

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_001() throws Exception
    {
        URI uri = new URI(url.replace("{id}","71"));

        HttpHeaders headers = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);

        Brand brand = JacksonUtil.parseObject(result,"data", Brand.class);
        assertEquals("戴荣华", brand.getName());
        assertEquals(71, brand.getId());

    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_002() throws Exception
    {
        URI uri = new URI(url.replace("{id}","0"));

        HttpHeaders headers = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(794,errno); //品牌不存在
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_003() throws Exception
    {
        URI uri = new URI(url.replace("{id}","72"));

        HttpHeaders headers = getHttpHeaders(userAccount);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(675,errno); //管理员无权限

        headers = userAccount.createHeaders();
        httpEntity = new HttpEntity(headers);

        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);

        Brand brand = JacksonUtil.parseObject(result,"data", Brand.class);
        assertEquals("范敏祺", brand.getName());
        assertEquals(72, brand.getId());
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_004() throws Exception
    {
        URI uri = new URI(url.replace("{id}","72"));

        HttpHeaders headers = getHttpHeaders(adminAccount);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(675,errno); //管理员无权限

        headers = userAccount.createHeaders();
        httpEntity = new HttpEntity(headers);

        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);

        Brand brand = JacksonUtil.parseObject(result,"data", Brand.class);
        assertEquals("范敏祺", brand.getName());
        assertEquals(72, brand.getId());

    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_005() throws Exception
    {
        URI uri = new URI(url.replace("{id}","72"));

        adtUserAccount.setUserName("nan");
        adtUserAccount.setUserName("nan123");
        HttpHeaders headers = getHttpHeaders(adtUserAccount);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno); //管理员有权限

        headers = userAccount.createHeaders();
        httpEntity = new HttpEntity(headers);

        responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(794,errno); //品牌不存在
    }

}
