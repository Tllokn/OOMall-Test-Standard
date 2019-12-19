package xmu.oomall.publictest.order;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import xmu.oomall.domain.Address;
import xmu.oomall.domain.CartItem;
import xmu.oomall.domain.Order;
import xmu.oomall.domain.OrderItem;
import xmu.oomall.test.AdtUserAccount;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.OrderSubmitVo;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class Orders {
    @Value("http://${oomall.host}:${oomall.port}/orderService/orders")
    String url;

    @Value("http://${oomall.host}:${oomall.port}/")
    String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AdtUserAccount userAccount;

    private HttpHeaders getHttpHeaders(URI uri) throws URISyntaxException {
        HttpHeaders httpHeaders = testRestTemplate.headForHeaders(uri);
        if (!userAccount.addToken(httpHeaders)) {
            //登录失败
            assertTrue(false);
        }
        return httpHeaders;
    }

    /**
     *
     * @author Ming Qiu
     */
    @Test
    public void tc_Orders_001() throws Exception{

        OrderSubmitVo orderSubmitVo = new OrderSubmitVo();
        List<CartItem> cartItems = new ArrayList<>(3);
        orderSubmitVo.setCartItemIds(new ArrayList<>(3));

        //user_id = 1
        userAccount.setUserName("92998201300");
        userAccount.setPassword("123456");

        String cartUrl = baseUrl +"cartService/cartItems";
        URI uri = new URI(cartUrl);
        HttpHeaders httpHeaders = this.getHttpHeaders(uri);

        /**************************************************/
        //插入购物车
        CartItem cartItem = new CartItem();
        cartItem.setBeCheck(false);
        cartItem.setGmtCreate(LocalDateTime.now());
        cartItem.setGmtModified(LocalDateTime.now());
        cartItem.setNumber(5);
        cartItem.setProductId(3069);

        /* 设置请求头部 */
        HttpEntity<CartItem> cartEntity  = new HttpEntity<>(cartItem, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.POST, cartEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(0,errNo);
        CartItem responseItem = JacksonUtil.parseObject(body, "data", CartItem.class);
        orderSubmitVo.getCartItemIds().add(responseItem.getId());
        cartItems.add(responseItem);

        /**************************************************/

        cartItem = new CartItem();
        cartItem.setBeCheck(false);
        cartItem.setGmtCreate(LocalDateTime.now());
        cartItem.setGmtModified(LocalDateTime.now());
        cartItem.setNumber(1);
        cartItem.setProductId(3067);

        /* 设置请求头部 */
        cartEntity  = new HttpEntity<>(cartItem, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        response = testRestTemplate.exchange(uri, HttpMethod.POST, cartEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(0,errNo);
        responseItem = JacksonUtil.parseObject(body, "data", CartItem.class);
        orderSubmitVo.getCartItemIds().add(responseItem.getId());
        cartItems.add(responseItem);

        /**************************************************/

        cartItem = new CartItem();
        cartItem.setBeCheck(false);
        cartItem.setGmtCreate(LocalDateTime.now());
        cartItem.setGmtModified(LocalDateTime.now());
        cartItem.setNumber(4);
        cartItem.setProductId(3070);

        /* 设置请求头部 */
        cartEntity  = new HttpEntity<>(cartItem, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        response = testRestTemplate.exchange(uri, HttpMethod.POST, cartEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body,"errno");
        assertEquals(0,errNo);
        responseItem = JacksonUtil.parseObject(body, "data", CartItem.class);
        orderSubmitVo.getCartItemIds().add(responseItem.getId());
        cartItems.add(responseItem);

        /**************************************************/

        Address address = new Address();
        address.setCity("伊犁哈萨克自治州");
        address.setProvince("新疆维吾尔自治区");
        address.setCounty("和布克赛尔蒙古自治县");
        address.setAddressDetail("和布克赛尔镇和布克东街003号和丰县宾馆");
        orderSubmitVo.setAddress(address);

        /**************************************************/

        uri = new URI(url);
        httpHeaders = getHttpHeaders(uri);
        HttpEntity<OrderSubmitVo> orderEntity = new HttpEntity<>(orderSubmitVo, httpHeaders);

        response=testRestTemplate.exchange(uri, HttpMethod.POST, orderEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        body=response.getBody();
        errNo= JacksonUtil.parseInteger(body,"errno");
        assertEquals(0, errNo);
        Order retOrder=JacksonUtil.parseObject(body,"data", Order.class);

        /**************************************************/

        url = url + "/" + retOrder.getId();
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);
        response = testRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        body=response.getBody();
        errNo= JacksonUtil.parseInteger(body,"errno");
        assertEquals(0, errNo);
        Order fetchOrder=JacksonUtil.parseObject(body,"data", Order.class);

        assertEquals(retOrder.getId(),fetchOrder.getId());
        assertEquals(1,fetchOrder.getUserId());
        assertEquals(address.getProvince()+address.getCity()+address.getCounty()+address.getAddressDetail(),fetchOrder.getAddress());
        assertEquals(cartItems.size(), retOrder.getOrderItemList().size());
        for (OrderItem item: retOrder.getOrderItemList()){
            Integer p_id = item.getProductId();
            Boolean found = false;
            for (CartItem cItem : cartItems){
                if (p_id == cItem.getProductId()){
                    found = true;
                }
                assertTrue(found);
            }
        }
    }
}
