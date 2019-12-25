package xmu.oomall.publictest.freight;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.DefaultPieceFreightPo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author hsx
 * @version 1.0
 * @date 2019/12/20 22:04
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class DefaultPieceFreightTest {
    @Value("http://${oomall.host}:${oomall.port}/freightService/defaultPieceFreights?page=1&limit=12")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;


    @Test
    public void tc_defaultPieceFreight_001() throws Exception {
        /**
         * @author 24320172203141
         */
        // 设置请求头部
        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

    }

    @Test
    public void tc_defaultPieceFreight_002() throws Exception {
        /**
         * @author 24320172203140
         */
        // 设置请求头部

        DefaultPieceFreightPo defaultPieceFreightPo=new DefaultPieceFreightPo();
        BigDecimal UnitRate = new BigDecimal("9.00");

        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity<DefaultPieceFreightPo> httpEntity = new HttpEntity<>(defaultPieceFreightPo,httpHeaders);


        defaultPieceFreightPo.setDestination("{\"dest\":[1,2]}");
        defaultPieceFreightPo.setGmtModified(LocalDateTime.now());
        defaultPieceFreightPo.setGmtCreate(LocalDateTime.now());
        defaultPieceFreightPo.setRequireTime("次日");
        defaultPieceFreightPo.setUnitRate(UnitRate);


        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        DefaultPieceFreightPo testDefaultPieceFreightPo = JacksonUtil.parseObject(body, "data",DefaultPieceFreightPo.class);
        assertEquals(0, errno);
        assertEquals(defaultPieceFreightPo.getUnitRate(),testDefaultPieceFreightPo.getUnitRate());

    }


    @Test
    public void tc_defaultPieceFreight_003() throws Exception {
        /**
         * @author 24320172203140
         */
        // 设置请求头部
        DefaultPieceFreightPo defaultPieceFreightPo=new DefaultPieceFreightPo();
        BigDecimal UnitRate = new BigDecimal("9.00");

        URI uri = new URI(url.replace("{id}","1"));
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity<DefaultPieceFreightPo> httpEntity = new HttpEntity<>(defaultPieceFreightPo,httpHeaders);


        defaultPieceFreightPo.setDestination("{\"dest\":[1,2]}");
        defaultPieceFreightPo.setGmtModified(LocalDateTime.now());
        defaultPieceFreightPo.setGmtCreate(LocalDateTime.now());
        defaultPieceFreightPo.setRequireTime("次日");
        defaultPieceFreightPo.setUnitRate(UnitRate);


        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        DefaultPieceFreightPo testDefaultPieceFreightPo = JacksonUtil.parseObject(body, "data",DefaultPieceFreightPo.class);
        assertEquals(0, errno);
        assertEquals(defaultPieceFreightPo.getUnitRate(),testDefaultPieceFreightPo.getUnitRate());

    }



}
