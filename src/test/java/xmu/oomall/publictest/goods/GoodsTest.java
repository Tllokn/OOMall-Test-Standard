package xmu.oomall.publictest.goods;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Goods;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.publictest.NoAdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = PublicTestApplication.class)
public class GoodsTest {
    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/goods")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Autowired
    private UserAccount userAccount;

    @Autowired
    private AdtUserAccount adtUserAccount;

    @Autowired
    private NoAdminAccount noAdminAccount;

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Goods_001() throws Exception{
        /* 准备数据 */
        Goods good=new Goods();
        good.setId(999999);
        good.setGmtCreate(LocalDateTime.now());
        good.setName("农夫山泉");
        good.setGoodsSn("10089");
        good.setShortName("山泉");
        good.setDescription("大自然的搬运工");
        good.setBrief("可以喝");
        good.setStatusCode(0);
        good.setGoodsCategoryId(139);
        good.setBrandId(119);
        good.setWeight(new BigDecimal(100.05));
        good.setVolume("500ml");
        good.setSpecialFreightId(0);
        good.setBeSpecial(false);
        good.setBeDeleted(false);

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(good, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /* assert判断 */
        Goods testGoods = JacksonUtil.parseObject(body, "data", Goods.class);
        assertEquals(testGoods.getBrandId(), testGoods.getBrandId());
        assertEquals(testGoods.getBrief(), testGoods.getBrief());
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Goods_002() throws Exception{
        /* 准备数据 */
        Goods good=new Goods();
        good.setId(999999);
        good.setGmtCreate(LocalDateTime.now());
        good.setName("农夫山泉");
        good.setGoodsSn("10089");
        good.setShortName("山泉");
        good.setDescription("大自然的搬运工");
        good.setBrief("可以喝");
        good.setStatusCode(0);
        good.setGoodsCategoryId(139);
        good.setBrandId(119);
        good.setWeight(new BigDecimal(100.05));
        good.setVolume("500ml");
        good.setSpecialFreightId(0);
        good.setBeSpecial(false);
        good.setBeDeleted(false);

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = noAdminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(good, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        System.out.println(body);
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(675, errno); //管理员无权限
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Goods_003() throws Exception{
        /* 准备数据 */
        Goods good=new Goods();
        good.setId(999999);
        good.setGmtCreate(LocalDateTime.now());
        good.setName("农夫山泉");
        good.setGoodsSn("10089");
        good.setShortName("山泉");
        good.setDescription("大自然的搬运工");
        good.setBrief("可以喝");
        good.setStatusCode(0);
        good.setGoodsCategoryId(139);
        good.setBrandId(119);
        good.setWeight(new BigDecimal(100.05));
        good.setVolume("500ml");
        good.setSpecialFreightId(1001000);
        good.setBeSpecial(false);
        good.setBeDeleted(false);

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(good, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(666, errno); //用户无权限
    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_Goods_004() throws Exception{
        /* 准备数据 */
        Goods good=new Goods();
        good.setId(999999);
        good.setGmtCreate(LocalDateTime.now());
        good.setName("农夫山泉");
        good.setGoodsSn("10089");
        good.setShortName("山泉");
        good.setDescription("大自然的搬运工");
        good.setBrief("可以喝");
        good.setStatusCode(0);
        good.setGoodsCategoryId(139);
        good.setBrandId(119);
        good.setWeight(new BigDecimal(100.05));
        good.setVolume("500ml");
        good.setSpecialFreightId(0);
        good.setBeSpecial(false);
        good.setBeDeleted(false);

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(good, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(660, errno); //未登录
    }

}
