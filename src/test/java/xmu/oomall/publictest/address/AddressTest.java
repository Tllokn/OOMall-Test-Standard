package xmu.oomall.publictest.address;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.domain.AddressPo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xmu.oomall.util.HttpRequest.getHttpHeaders;

@SpringBootTest
public class AddressTest {
    @Value("http://${oomall.host}:${oomall.port}/addressService/addresses")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;


    @Test
    public void tc_address_001() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(100);
        addressPo.setProvinceId(22);
        addressPo.setCountyId(33);
        addressPo.setAddressDetail("新疆维吾尔自治区乌鲁木齐市乌鲁木齐县");
        addressPo.setMobile("139463254");//错误的电话号码，只有9位（测试能不能插入成功）
        addressPo.setPostalCode("830063");
        addressPo.setConsignee("神无月");
        addressPo.setBeDefault(true);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = getHttpHeaders(adminAccount);;
        HttpEntity httpEntity = new HttpEntity<>(addressPo, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");
        AddressPo testAddressPo=JacksonUtil.parseObject(result,"data",AddressPo.class);

        assertEquals(0,errno);
        assertEquals(addressPo.getUserId(),testAddressPo.getUserId());
        assertEquals(addressPo.getCountyId(),testAddressPo.getCountyId());
    }
}
