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

    /**
     * 1、检测更新商品的brandId，brandId存在的情况
     *
     * @throws Exception
     * @author 24320172203147
     */
    @Test
    public void tc_GoodsId_003() throws Exception {
        // 准备要更新的数据
        Goods goods = new Goods();
        goods.setBrandId(71);

        // 设置头部，设置登陆
        URI uri = new URI(url.replace("{id}", "275"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Goods> requestUpdate = new HttpEntity<>(goods, headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //取出状态码
        Integer errNo = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errNo);

        // 取出返回的body
        Goods responseGoods = JacksonUtil.parseObject(response.getBody(), "data", Goods.class);
        // 比较值是否相等
        assertEquals(275, responseGoods.getId());
        assertEquals(goods.getBrandId(), responseGoods.getBrandId());
    }

    /**
     * 2、检测更新商品的brandId，brandId不存在的情况
     *
     * @throws Exception
     * @author 24320172203147
     */
    @Test
    public void tc_GoodsId_004() throws Exception {
        // 准备要更新的数据
        Goods goods = new Goods();
        goods.setBrandId(1);

        // 设置头部，设置登陆
        URI uri = new URI(url.replace("{id}", "275"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Goods> requestUpdate = new HttpEntity<>(goods, headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Integer errNo = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(772, errNo);//772，商品修改失败
    }

    /**
     * 3、检测更新商品的goodsCategoryId，goodsCategoryId存在的情况
     *
     * @throws Exception
     * @author 24320172203147
     */
    @Test
    public void tc_GoodsId_005() throws Exception {
        // 准备要更新的数据
        Goods goods = new Goods();
        goods.setGoodsCategoryId(125);

        // 设置头部，设置登陆
        URI uri = new URI(url.replace("{id}", "275"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Goods> requestUpdate = new HttpEntity<>(goods, headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //取出状态码
        Integer errNo = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(0, errNo);

        // 取出返回的body
        Goods responseGoods = JacksonUtil.parseObject(response.getBody(), "data", Goods.class);
        // 比较值是否相等
        assertEquals(275, responseGoods.getId());
        assertEquals(goods.getGoodsCategoryId(), responseGoods.getGoodsCategoryId());
    }

    /**
     * 4、检测更新商品的goodsCategoryId，goodsCategoryId不存在的情况
     *
     * @throws Exception
     * @author 24320172203147
     */
    @Test
    public void tc_GoodsId_006() throws Exception {
        // 准备要更新的数据
        Goods goods = new Goods();
        goods.setGoodsCategoryId(1);

        // 设置头部，设置登陆
        URI uri = new URI(url.replace("{id}", "275"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Goods> requestUpdate = new HttpEntity<>(goods, headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Integer errNo = JacksonUtil.parseInteger(response.getBody(), "errno");
        assertEquals(772, errNo);//772，商品修改失败
    }

    /**
     * 5、检测删除未下架的商品，即status!=0的商品
     * @author 24320172203147
     * @throws Exception
     */
    @Test
    public void tc_GoodsId_007() throws Exception
    {
        URI uri = new URI(url.replace("{id}","275"));

        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(773,errno); //773，商品删除失败
    }

    /**
     * 6、检测删除已下架商品， 即status==0的商品
     * @author 24320172203147
     * @throws Exception
     */
    @Test
    public void tc_GoodsId_008() throws Exception
    {
        // 准备要更新的数据，先将商品status置为0，因为数据库中商品的status的均为1
        Goods goods = new Goods();
        goods.setStatusCode(0);

        // 设置头部，设置登陆
        URI uri = new URI(url.replace("{id}", "430"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Goods> requestUpdate = new HttpEntity<>(goods, headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //将430商品的status修改为0后，即可成功删除该商品
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        System.out.println(result);
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);//删除成功
    }

    /**
     * 7、检测删除商品后，该商品的产品是否也被删除
     * @author 24320172203147
     * @throws Exception
     */
    @Test
    public void tc_GoodsId_009() throws Exception
    {
        // 准备要更新的数据，先将商品status置为0，因为数据库中商品的status的均为1
        Goods goods = new Goods();
        goods.setStatusCode(0);

        // 设置头部，设置登陆
        URI uri = new URI(url.replace("{id}", "430"));
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity<Goods> requestUpdate = new HttpEntity<>(goods, headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        String result = responseEntity.getBody();
        System.out.println(result);
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);//删除成功

        //开始检测该商品的产品是否存在，430goods有一个id为157的product
        URI uri2 = new URI("http://${oomall.host}:${oomall.port}/goodsInfoService/user/product/157");
        responseEntity = restTemplate.exchange(uri2, HttpMethod.GET, entity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        result = responseEntity.getBody();
        errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(784, errno); //784，该产品不存在
    }
}
