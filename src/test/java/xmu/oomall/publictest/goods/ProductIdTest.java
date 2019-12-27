package xmu.oomall.publictest.goods;
/**
 * @author 24320172203147
 * @version 1.0
 * @date 2019/12/24 20:04
 */


import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import xmu.oomall.domain.Goods;
import xmu.oomall.domain.GoodsPo;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PublicTestApplication.class)
public class ProductIdTest {
    @Value("http://${oomall.host}:${oomall.port}/goodsInfoService/goods/{id}")
    String url;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserAccount userAccount;


    /**
     * 8、检测删除产品
     * @author 24320172203147
     * @throws Exception
     */
    @Test
    public void tc_ProductId_00008() throws Exception
    {
        URI uri = new URI("http://${oomall.host}:${oomall.port}/goodsInfoService/products/158");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        System.out.println(result);
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);
    }

    /**
     * 9、检测删除产品(该product为其goods一个产品)后，此时该product的goods是否下架
     * @author 24320172203147
     * @throws Exception
     */
    @Test
    public void tc_ProductId_00009() throws Exception
    {
        URI uri = new URI("http://${oomall.host}:${oomall.port}/goodsInfoService/products/159");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        System.out.println(result);
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);

        //productID为159的产品，其goodsId为432
        // 设置头部，设置登陆
        URI uri2 = new URI("http://${oomall.host}:${oomall.port}/goodsInfoService/admin/goods/432");

        /*exchange方法模拟HTTP请求*/
        responseEntity = restTemplate.exchange(uri2, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //取出状态码
        Integer errNo = JacksonUtil.parseInteger(responseEntity.getBody(), "errno");
        assertEquals(0, errNo);

        // 取出返回的body
        Goods responseGoods = JacksonUtil.parseObject(responseEntity.getBody(), "data", Goods.class);
        // 比较值是否相等,432商品此时的status应为0(下架状态)
        assertEquals(432, responseGoods.getId());
        assertEquals(0, responseGoods.getStatusCode());
    }

    /**
     * 10、检测删除产品(该product不为其goods的最后一个产品)后，此时应该更新其goods的最低价格
     * 该测试用例需事先在数据库中product表中，将productId为161的产品的goodsId修改为433
     * @author 24320172203147
     * @throws Exception
     */
    @Test
    public void tc_ProductId_00010() throws Exception
    {
        URI uri = new URI("http://${oomall.host}:${oomall.port}/goodsInfoService/products/160");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        System.out.println(result);
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);

        //productID为160的产品，其goodsId为433
        // 设置头部，设置登陆
        URI uri2 = new URI("http://${oomall.host}:${oomall.port}/goodsInfoService/admin/goods/433");

        /*exchange方法模拟HTTP请求*/
        responseEntity = restTemplate.exchange(uri2, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //取出状态码
        Integer errNo = JacksonUtil.parseInteger(responseEntity.getBody(), "errno");
        assertEquals(0, errNo);

        // 取出返回的body
        Goods responseGoods = JacksonUtil.parseObject(responseEntity.getBody(), "data", Goods.class);
        // 比较值是否相等
        // 433商品此时的product只剩下一个id为161的product(161 product的价格为9999)，所以goods的最低价应为9999.00
        assertEquals(433, responseGoods.getId());
        assertEquals(new BigDecimal("9999.00"), responseGoods.getPrice());
    }
}
