package xmu.oomall.publictest.discount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.GrouponRule;
import xmu.oomall.domain.GrouponRulePo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.AdtUserAccount;
import xmu.oomall.publictest.NoAdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 审核：@王健
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class Groupon {
    @Value("http://${oomall.host}:${oomall.port}/discountService/grouponRules")
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
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_001() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("[{\"rate\":0.7,\"lowerbound\":5,\"upperbound\":30},{\"rate\":0.6,\"lowerbound\":30,\"upperbound\":70},{\"rate\":0.5,\"lowerbound\":70}]");//不标准的JSON结构
        grouponRulePo.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 00));
        grouponRulePo.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 05));


        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(722, errno);

        /* assert判断 */
        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
        assertEquals(testGroupon.getId(), testGroupon.getId());
        assertEquals(true,testGroupon.getStatusCode());
    }

    /**
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_002() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("{\"stretagy\":[{\"rate\":0.7,\"lowerBound\":5,\"upperBound\":30},{\"rate\":0.6,\"lowerBound\":30,\"upperBound\":70},{\"rate\":0.5,\"lowerBound\":70}]}");//不标准的JSON结构
        grouponRulePo.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 00));
        grouponRulePo.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 05));


        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(722, errno);

        /* assert判断 */
        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
        assertEquals(testGroupon.getId(), testGroupon.getId());
        assertEquals(true,testGroupon.getStatusCode());
    }

    /**
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_003() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("满20人七折，满40人六折");//非法的策略
        grouponRulePo.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 00));
        grouponRulePo.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 05));


        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(722, errno);

        /* assert判断 */
        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
        assertEquals(testGroupon.getId(), testGroupon.getId());
        assertEquals(true,testGroupon.getStatusCode());
    }

    /**
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_004() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("{\"strategy\":[{\"rate\":0.7,\"lowerbound\":5,\"upperbound\":30},{\"rate\":0.6,\"lowerbound\":30,\"upperbound\":70},{\"rate\":0.5,\"lowerbound\":70}]}");
        grouponRulePo.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 05));
        grouponRulePo.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 00));//非法的开始结束时间设置,结束时间比开始时间早


        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(722, errno);

        /* assert判断 */
        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
        assertEquals(testGroupon.getId(), testGroupon.getId());
        assertEquals(true,testGroupon.getStatusCode());
    }

    /**
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_005() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("{\"strategy\":[{\"rate\":0.7,\"lowerbound\":5,\"upperbound\":30},{\"rate\":0.6,\"lowerbound\":30,\"upperbound\":70},{\"rate\":0.5,\"lowerbound\":70}]}");
        grouponRulePo.setStartTime(LocalDateTime.now().minusYears(2));
        grouponRulePo.setEndTime(LocalDateTime.now().minusYears(1));//非法的开始结束时间设置，活动结束时间比当前时间要早


        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(722, errno);

        /* assert判断 */
        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
        assertEquals(testGroupon.getId(), testGroupon.getId());
        assertEquals(true,testGroupon.getStatusCode());
    }

    /**
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_006() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("{\"strategy\":[{\"rate\":0.7,\"lowerbound\":5,\"upperbound\":30},{\"rate\":0.6,\"lowerbound\":30,\"upperbound\":70},{\"rate\":0.5,\"lowerbound\":70}]}");
        grouponRulePo.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 05));
        grouponRulePo.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 10));
        grouponRulePo.setBeDeleted(true);//非法传入项
        grouponRulePo.setStatusCode(false);//非法传入项

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /* assert判断 */
        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
        assertEquals(testGroupon.getId(), testGroupon.getId());
        assertEquals(true,testGroupon.getStatusCode());
        assertEquals(false,testGroupon.getBeDeleted());
    }

    /**
     * @author 24320172203238
     * @throws Exception exception
     * @date 2019/12/24
     */
    @Test
    public void tc_Groupon_007() throws Exception {
        /* 准备数据 */
        GrouponRulePo grouponRulePo = new GrouponRulePo();
        grouponRulePo.setGoodsId(20);
        grouponRulePo.setGrouponLevelStrategy("{\"strategy\":[{\"rate\":0.7,\"lowerbound\":5,\"upperbound\":30},{\"rate\":0.6,\"lowerbound\":30,\"upperbound\":70},{\"rate\":0.5,\"lowerbound\":70}]}");
        grouponRulePo.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 05));
        grouponRulePo.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 10));

        GrouponRulePo grouponRulePo1=new GrouponRulePo();
        grouponRulePo1.setGoodsId(20);
        grouponRulePo1.setGrouponLevelStrategy("{\"strategy\":[{\"rate\":0.7,\"lowerbound\":5,\"upperbound\":30},{\"rate\":0.6,\"lowerbound\":30,\"upperbound\":70},{\"rate\":0.5,\"lowerbound\":70}]}");
        grouponRulePo1.setStartTime(LocalDateTime.of(2020, 12, 24, 17, 9));
        grouponRulePo1.setEndTime(LocalDateTime.of(2020, 12, 24, 17, 20));

        /* 设置请求头部 */
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(grouponRulePo, httpHeaders);
        URI uri1 = new URI(url);
        HttpHeaders httpHeaders1 = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity1 = new HttpEntity(grouponRulePo1, httpHeaders1);

        /* exchange方法模拟HTTP请求 */
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response1 = restTemplate.exchange(uri1, HttpMethod.POST, httpEntity1, String.class);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        /* 取得响应体 */
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
        String body1 = response1.getBody();
        Integer errno1 = JacksonUtil.parseInteger(body, "errno");
        assertEquals(722, errno);

        /* assert判断 */
//        GrouponRulePo testGroupon = JacksonUtil.parseObject(body, "data", GrouponRulePo.class);
//        assertEquals(testGroupon.getId(), testGroupon.getId());
//        assertEquals(true,testGroupon.getStatusCode());
//        assertEquals(false,testGroupon.getBeDeleted());
    }
}
