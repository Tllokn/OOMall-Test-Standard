package xmu.oomall.publictest.freight;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.DefaultFreightPo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = PublicTestApplication.class)
public class DefaultFreightTest {
    @Value("http://${oomall.host}:${oomall.port}/freightService/defaultFreights?page=1&limit=12")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    @Test
    public void tc_defaultFreight_001() throws Exception {
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
    public void tc_defaultFreight_002() throws Exception {
        /**
         * @author 24320172203140
         */
        // 设置请求头部

        DefaultFreightPo defaultFreightPo=new DefaultFreightPo();

        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity <DefaultFreightPo>httpEntity = new HttpEntity<>(defaultFreightPo,httpHeaders);

        BigDecimal Over10Price = new BigDecimal("10.00");
        BigDecimal Over50Price = new BigDecimal("20.00");
        BigDecimal Over100Price = new BigDecimal("30.00");
        BigDecimal Over300Price = new BigDecimal("40.00");
        BigDecimal ContinueHeavyPrice = new BigDecimal("50.00");
        BigDecimal FirstHeavyPrice = new BigDecimal("60.00");


        defaultFreightPo.setDestination("{\"dest\":[1,2]}");
        defaultFreightPo.setGmtCreate(LocalDateTime.now());
        defaultFreightPo.setGmtModified(LocalDateTime.now());
        defaultFreightPo.setOver50Price(Over50Price);
        defaultFreightPo.setOver10Price(Over10Price);
        defaultFreightPo.setOver100Price(Over100Price);
        defaultFreightPo.setOver300Price(Over300Price);
        defaultFreightPo.setContinueHeavyPrice(ContinueHeavyPrice);
        defaultFreightPo.setFirstHeavyPrice(FirstHeavyPrice);



        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        DefaultFreightPo testDefaultFreightPo = JacksonUtil.parseObject(body, "data",DefaultFreightPo.class);
        assertEquals(0, errno);
        assertEquals(defaultFreightPo.getOver100Price(),testDefaultFreightPo.getOver100Price());
    }


    @Test
    public void tc_defaultFreight_003() throws Exception {
        /**
         * @author 24320172203140
         */
        // 设置请求头部
        DefaultFreightPo defaultFreightPo=new DefaultFreightPo();

        URI uri = new URI(url.replace("{id}","1"));
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity<DefaultFreightPo> httpEntity = new HttpEntity<>(defaultFreightPo,httpHeaders);

        BigDecimal Over10Price = new BigDecimal("10.00");
        BigDecimal Over50Price = new BigDecimal("20.00");
        BigDecimal Over100Price = new BigDecimal("30.00");
        BigDecimal Over300Price = new BigDecimal("40.00");
        BigDecimal ContinueHeavyPrice = new BigDecimal("50.00");
        BigDecimal FirstHeavyPrice = new BigDecimal("60.00");

        defaultFreightPo.setDestination("{\"dest\":[1,2]}");
        defaultFreightPo.setGmtModified(LocalDateTime.now());
        defaultFreightPo.setOver50Price(Over50Price);
        defaultFreightPo.setOver10Price(Over10Price);
        defaultFreightPo.setOver100Price(Over100Price);
        defaultFreightPo.setOver300Price(Over300Price);
        defaultFreightPo.setContinueHeavyPrice(ContinueHeavyPrice);
        defaultFreightPo.setFirstHeavyPrice(FirstHeavyPrice);



        // 发出HTTP请求
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        // 取得响应体
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        DefaultFreightPo testDefaultFreightPo = JacksonUtil.parseObject(body, "data",DefaultFreightPo.class);
        assertEquals(0, errno);
        assertEquals(defaultFreightPo.getOver100Price(),testDefaultFreightPo.getOver100Price());

    }




}
