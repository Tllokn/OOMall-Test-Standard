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
import java.time.LocalDateTime;


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

        HttpHeaders headers = getHttpHeaders(adminAccount);
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
        assertEquals(666,errno); //用户无权限

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

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_006() throws Exception{
        // 准备要更新的数据
        Brand brand = new Brand();
        brand.setName("wjaaa");
        brand.setGmtModified(LocalDateTime.now());

        // 设置头部，未设置登陆
        URI uri = new URI(url.replace("{id}","107"));
        HttpHeaders headers = getHttpHeaders(userAccount);
        HttpEntity<Brand> requestUpdate = new HttpEntity<>(brand, headers);

        // 发出http请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer errNo = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(666, errNo); //用户无权限

        response = restTemplate.exchange(uri, HttpMethod.GET, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String result = response.getBody();
        errNo = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errNo);

        brand = JacksonUtil.parseObject(result,"data", Brand.class);
        assertEquals("叶可思", brand.getName());
        assertEquals(107, brand.getId());

    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_BrandsId_007() throws Exception{
        // 准备要更新的数据
        Brand brand = new Brand();
        brand.setName("wjaaa");
        brand.setGmtModified(LocalDateTime.now());

        // 设置头部，未设置登陆
        URI uri = new URI(url.replace("{id}","108"));
        HttpHeaders headers = getHttpHeaders(adminAccount);
        HttpEntity<Brand> requestUpdate = new HttpEntity<>(brand, headers);

        // 发出http请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Integer errNo = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errNo);

        // 取出返回的body
        Brand responseBrand = JacksonUtil.parseObject(response.getBody(), "data", Brand.class);

        // 比较值是否相等
        assertEquals(brand.getId(), responseBrand.getId());
        assertEquals(brand.getName(), responseBrand.getName());
        assertEquals(brand.getGmtModified(), responseBrand.getGmtModified());
    }

}
