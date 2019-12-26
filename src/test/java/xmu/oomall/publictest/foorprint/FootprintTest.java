package xmu.oomall.publictest.footprint;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.domain.FootprintItem;
import xmu.oomall.domain.FootprintItemPo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FootprintTest {
    @Value("http://${oomall.host}:${oomall.port}/footprintService/footprints")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAccount userAccount;

    @Autowired
    private AdminAccount adminAccount;

    /**
     * 测试新增足迹
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_footprint_001() throws Exception{
        FootprintItemPo footprintItemPo=new FootprintItemPo();

        footprintItemPo.setGoodsId(100001);
        footprintItemPo.setUserId(10001);
        footprintItemPo.setGmtCreate(LocalDateTime.now());
        footprintItemPo.setBirthTime(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(footprintItemPo, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        FootprintItemPo testFootprintItemPo=JacksonUtil.parseObject(result,"data",FootprintItemPo.class);
        assertEquals(0,errno);
        assertEquals(footprintItemPo.getUserId(),footprintItemPo.getUserId());
        assertEquals(footprintItemPo.getBirthTime(),footprintItemPo.getBirthTime());
    }


    /**
     * 测试获取足迹列表时，传入错误参数
     * error limit=-1
     * @author: 24320172203255
     */
    @Test
    public void tc_footprint_002() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?page=2&limit=-1");
        HttpHeaders httpHeaders = userAccount.createHeaders();
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
     * @description 测试获取足迹列表
     * @author: 24320172203255
     */
    @Test
    public void tc_footprint_005() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?page=1&limit=10");
        HttpHeaders httpHeaders = userAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @description 查看足迹详情，成功情况
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_footprint_006() throws Exception{
        URI uri = new URI(url.replace("footprints","admin/footprints")+"?page=-2&limit=-2&userId=1&goodsId=100001");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @description 查看足迹详情，userId不存在
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_footprint_007() throws Exception{
        URI uri = new URI(url.replace("footprints","admin/footprints")+"?page=-2&limit=-2&goodsId=100001");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @description 查看足迹详情，goodsId不存在
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_footprint_008() throws Exception{
        URI uri = new URI(url.replace("footprints","admin/footprints")+"?page=-2&limit=-2&userId=1");
        HttpHeaders headers = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);
    }

    /**
     * @description 查看足迹，用户无权限
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_footprint_009() throws Exception{
        URI uri = new URI(url.replace("footprints","admin/footprints")+"?page=-2&limit=-2&userId=1");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String result = response.getBody();
        Integer errno = JacksonUtil.parseInteger(result,"errno");
        assertEquals(666,errno); //用户无权限
    }


}
