package xmu.oomall.address;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.AddressPo;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostAddressPo {
    @Value("http://${host}:${port}/addresses")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws Exception{
        URI uri = new URI(url);

        AddressPo addressPo=new AddressPo();
        addressPo.setId(12138);
        addressPo.setUserId(12);//domain中是String字段
        addressPo.setCityId(100);
        addressPo.setProvinceId(22);
        addressPo.setCountyId(33);
        addressPo.setAddressDetail("新疆维吾尔自治区乌鲁木齐市乌鲁木齐县");
        //      addressPo.setMobile("13945263254");//正确电话号码
        addressPo.setMobile("139463254");//错误的电话号码，只有9位（测试能不能插入成功）
        addressPo.setPostalCode("830063");
        addressPo.setConsignee("神无月");
        addressPo.setBeDefault(true);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(LocalDateTime.now());

        ResponseEntity<String> responseEntity=testRestTemplate.postForEntity(uri,addressPo,String.class);
        String result=responseEntity.getBody();
        String errno= JacksonUtil.parseString(result,"errno");
        AddressPo testAddressPo=JacksonUtil.parseObject(result,"data",AddressPo.class);
        String errmsg=JacksonUtil.parseString(result,"errmsg");

        assertEquals("0",errno);
        assertEquals("成功",errmsg);
        assertEquals(addressPo,testAddressPo);

    }
}
