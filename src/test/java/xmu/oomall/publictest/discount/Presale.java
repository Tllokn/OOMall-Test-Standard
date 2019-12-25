package xmu.oomall.publictest.discount;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.domain.PresaleRule;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.PresaleRuleVo;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * 审核：@王健
 */

@SpringBootTest
public class Presale {
    @Value("http://${oomall.host}:${oomall.port}/discountService/presaleRules")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Autowired
    private UserAccount userAccount;

    /**
     * @description 测试预售规则能否删除，传入的id<0
     * error 参数错误
     * @author 24320172203255
     */
    @Test
    public void tc_presale_001() throws Exception
    {
        URI uri = new URI(url+"/-10");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);

        //这次本来就删不了，因为id不存在，参数错误，所以没必要再次获取
    }

    /**
     * @description 测试预售规则能否删除，没有管理员权限
     * error 参数错误
     * @author 24320172203255
     */
    @Test
    public void tc_presale_002() throws Exception
    {
        URI uri = new URI(url+"/1");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(666,errno); //用户无权限

        /*再次请求，看能否获取到，应该是可以获取到的*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body_2 = response.getBody();
        Integer errno_2 = JacksonUtil.parseInteger(body_2, "errno");
        assertEquals(0, errno_2);
        PresaleRuleVo presaleRuleVo = JacksonUtil.parseObject(body_2,"data", PresaleRuleVo.class);
        assertEquals(1,presaleRuleVo.getPresaleRule().getId());
    }

    /**
     * 更新预售规则，预售开始、结束结束时间不合法
     * @author 24320172203255
     */
    @Test
    public void tc_presale_003() throws Exception{
        // 准备要更新的数据
        PresaleRule presaleRule =new PresaleRule();
        presaleRule.setStartTime(LocalDateTime.now());
        presaleRule.setEndTime(LocalDateTime.now().minusDays(5));

        // 设置头部
        URI uri = new URI(url+"/1");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        // 发出http请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(731, errno);
    }

    /**
     * 更新预售规则，付尾款时间不合法
     * @author 24320172203255
     */
    @Test
    public void tc_presale_004() throws Exception{
        // 准备要更新的数据
        PresaleRule presaleRule =new PresaleRule();
        presaleRule.setFinalStartTime(LocalDateTime.now());
        presaleRule.setAdEndTime(LocalDateTime.now().minusDays(10));

        // 设置头部
        URI uri = new URI(url+"/1");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        // 发出http请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(731, errno);
    }

    /**
     * @description 删除预售规则
     * @author 24320172203255
     */
    @Test
    public void tc_presale_005() throws Exception
    {
        URI uri = new URI(url+"/1");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        /*再次请求，看能否获取到，应该是可以获取不到的*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body_2 = response.getBody();
        Integer errno_2 = JacksonUtil.parseInteger(body_2, "errno");
        assertEquals(760, errno_2);
    }

    /**
     * @description 测试预售规则能否下架
     * error 参数错误
     * @author 24320172203255
     */
    @Test
    public void tc_presale_006() throws Exception
    {
        URI uri = new URI(url+"/-1/invalid");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }

    /**
     * @description 测试预售规则能否下架
     * error 参数错误
     * @author 24320172203255
     */
    @Test
    public void tc_presale_007() throws Exception
    {
        URI uri = new URI(url+"/2/invalid");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }


    /**
     * @description 测试获取预售规则列表时，传入错误参数
     * error limit=-1
     * @author: 24320172203255
     */
    @Test
    public void tc_presale_008() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?goodsId=1&page=2&limit=-1");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }

    /**
     * @description 测试获取预售规则列表时，传入错误参数
     * error page=-1
     * @author: 24320172203255
     */
    @Test
    public void tc_presale_009() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?goodsId=1&page=-1&limit=2");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }

    /**
     * @description 测试获取预售规则列表时，传入错误参数
     * error page=-2 limit=-2
     * @author: 24320172203255
     */
    @Test
    public void tc_presale_010() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?goodsId=1&page=-2&limit=-2");
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }


}
