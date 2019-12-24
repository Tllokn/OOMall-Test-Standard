package xmu.oomall.publictest.discount;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.domain.CouponRule;
import xmu.oomall.domain.CouponRulePo;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 审核：@王健
 */
@SpringBootTest
public class CouponRule {
    // 插入一条优惠券规则
    @Value("http://${oomall.host}:${oomall.port}/discountService/couponRules")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAccount userAccount;

    /**
     * @author: 24320172203262
     * 正确的优惠券规则信息，期望正确插入数据库
     * 为NumberStrategy
     */
    @Test
    public void tc_couponRule_001() throws Exception{

        CouponRulePo couponRule = new CouponRulePo();

        LocalDateTime now = LocalDateTime.now();

        couponRule.setStrategy("{\"name\":\"NumberStrategy\", \"obj\":{\"threshold\":4, \"offCash\":200}}");
        couponRule.setGoodsList1("{\"gIDs\":[321,320,327,326,299]}");
        couponRule.setBeginTime(now);
        couponRule.setBrief("满4件商品减200元");
        couponRule.setValidPeriod(30);
        couponRule.setEndTime(now.plus(30, ChronoUnit.DAYS));
        couponRule.setName("满4件减200");
        couponRule.setPicUrl("11234.jpg");
        couponRule.setTotal(100);
        couponRule.setCollectedNum(0);
        couponRule.setStatusCode(true);
        couponRule.setBeDeleted(false);
        couponRule.setGmtCreate(now);


        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(couponRule, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());//请求200

        String body=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(body,"errno");
        CouponRule couponRule1 = JacksonUtil.parseObject(body,"data",CouponRule.class);

        assertEquals(0,errno); //新增优惠券规则成功
    }

    /**
     * @author: 24320172203262
     * 正确的优惠券规则信息，期望正确插入数据库
     * 为CashOffStrategy
     */
    @Test
    public void tc_couponRule_002() throws Exception{

        CouponRulePo couponRule = new CouponRulePo();

        LocalDateTime now = LocalDateTime.now();

        couponRule.setStrategy("{\"name\":\"CashOffStrategy\", \"obj\":{\"threshold\":200.00, \"offCash\":50.00}}");
        couponRule.setGoodsList1("{\"gIDs\":[321,320,327,326,299]}");
        couponRule.setBeginTime(now);
        couponRule.setBrief("满200元减50元");
        couponRule.setValidPeriod(30);
        couponRule.setEndTime(now.plus(30, ChronoUnit.DAYS));
        couponRule.setName("满200减50大额优惠券");
        couponRule.setPicUrl("114.jpg");
        couponRule.setTotal(15);
        couponRule.setCollectedNum(0);
        couponRule.setStatusCode(true);
        couponRule.setBeDeleted(false);
        couponRule.setGmtCreate(now);


        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(couponRule, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());//请求200

        String body=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(body,"errno");
        CouponRule couponRule1 = JacksonUtil.parseObject(body,"data",CouponRule.class);

        assertEquals(0,errno); //新增优惠券规则成功
    }

    /**
     * @author: 24320172203262
     * 正确的优惠券规则信息，期望正确插入数据库
     * 为PercentageStrategy
     */
    @Test
    public void tc_couponRule_003() throws Exception{

        CouponRulePo couponRule = new CouponRulePo();

        LocalDateTime now = LocalDateTime.now();

        couponRule.setStrategy("{\"name\":\"PercentageStrategy\", \"obj\":{\"threshold\":149.99, \"percentage\":0.88}}");
        couponRule.setGoodsList1("{\"gIDs\":[]}");
        couponRule.setBeginTime(now);
        couponRule.setBrief("满149.99元打88折");
        couponRule.setValidPeriod(15);
        couponRule.setEndTime(now.plus(30, ChronoUnit.DAYS));
        couponRule.setName("满150即可享受88折优惠");
        couponRule.setPicUrl("114.jpg");
        couponRule.setTotal(99);
        couponRule.setCollectedNum(0);
        couponRule.setStatusCode(true);
        couponRule.setBeDeleted(false);
        couponRule.setGmtCreate(now);


        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(couponRule, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());//请求200

        String body=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(body,"errno");
        CouponRule couponRule1 = JacksonUtil.parseObject(body,"data",CouponRule.class);

        assertEquals(0,errno); //新增优惠券规则成功
    }


    /**
     * @author: 24320172203262
     * @throws Exception
     * 错误的优惠券规则信息，折扣金额为负数，期待插入失败
     */
    @Test
    public void tc_couponRule_004() throws Exception{

        CouponRulePo couponRule = new CouponRulePo();

        LocalDateTime now = LocalDateTime.now();

        couponRule.setStrategy("{\"name\":\"NumberStrategy\", \"obj\":{\"threshold\":4, \"offCash\":-1}}");//offCash应该为非负数
        couponRule.setGoodsList1("{\"gIDs\":[321,320,327,326,299]}");
        couponRule.setBeginTime(now);
        couponRule.setBrief("满4件商品减200元");
        couponRule.setValidPeriod(30);
        couponRule.setEndTime(now.plus(30, ChronoUnit.DAYS));
        couponRule.setName("满4件减200");
        couponRule.setPicUrl("11234.jpg");
        couponRule.setTotal(100);
        couponRule.setCollectedNum(0);
        couponRule.setStatusCode(true);
        couponRule.setBeDeleted(false);
        couponRule.setGmtCreate(now);


        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(couponRule, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());//请求200

        String body=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(body,"errno");
        CouponRule couponRule1 = JacksonUtil.parseObject(body,"data",CouponRule.class);

        assertEquals(712,errno); //新增优惠券规则失败
    }

    /**
     * @author: 24320172203262
     * @throws Exception
     * 错误的优惠券规则信息，开始时间晚于结束时间，期待插入失败
     */
    @Test
    public void tc_couponRule_005() throws Exception{

        CouponRulePo couponRule = new CouponRulePo();

        LocalDateTime now = LocalDateTime.now();

        couponRule.setStrategy("{\"name\":\"CashOffStrategy\", \"obj\":{\"threshold\":1000, \"offCash\":300}}");
        couponRule.setGoodsList1("{\"gIDs\":[]}");//全场通用
        couponRule.setBeginTime(now.plus(30, ChronoUnit.DAYS));
        couponRule.setBrief("满1000减200元");
        couponRule.setValidPeriod(30);
        couponRule.setEndTime(now);//结束时间早于开始时间
        couponRule.setName("全场通用大额神券满1000减300");
        couponRule.setPicUrl("167.jpg");
        couponRule.setTotal(10);
        couponRule.setCollectedNum(0);
        couponRule.setStatusCode(true);
        couponRule.setBeDeleted(false);
        couponRule.setGmtCreate(now);


        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(couponRule, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());//请求200

        String body=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(body,"errno");
        CouponRule couponRule1 = JacksonUtil.parseObject(body,"data",CouponRule.class);

        assertEquals(712,errno); //新增优惠券规则失败
    }



    /**
     * @author: 24320172203262
     * 错误的优惠券规则信息，策略name不合法，期待插入失败
     */
    @Test
    public void tc_couponRule_006() throws Exception{

        CouponRulePo couponRule = new CouponRulePo();

        LocalDateTime now = LocalDateTime.now();

        couponRule.setStrategy("{\"name\":\"WrongStrategy\", \"obj\":{\"threshold\":4, \"offCash\":200}}");//WrongStrategy
        couponRule.setGoodsList1("{\"gIDs\":[321,320,327,326,299]}");
        couponRule.setBeginTime(now);
        couponRule.setBrief("满4件商品减200元");
        couponRule.setValidPeriod(30);
        couponRule.setEndTime(now.plus(30, ChronoUnit.DAYS));
        couponRule.setName("满4件减200");
        couponRule.setPicUrl("11234.jpg");
        couponRule.setTotal(100);
        couponRule.setCollectedNum(0);
        couponRule.setStatusCode(true);
        couponRule.setBeDeleted(false);
        couponRule.setGmtCreate(now);


        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(couponRule, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());//请求200

        String body=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(body,"errno");
        CouponRule couponRule1 = JacksonUtil.parseObject(body,"data",CouponRule.class);

        assertEquals(712,errno); //新增优惠券规则失败
    }


}
