package xmu.oomall.publictest.address;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.domain.AddressPo;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.publictest.UserAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddressTest {
    @Value("http://${oomall.host}:${oomall.port}/addressService/addresses")
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
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(addressPo, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        assertEquals(751,errno); //  地址新增失败

    }

    /**
     * @author Ming Qiu
     * @throws Exception
     */
    @Test
    public void tc_address_002() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(100);
        addressPo.setProvinceId(8);
        addressPo.setCountyId(1055);
        addressPo.setAddressDetail("海韵405");
        addressPo.setMobile("13988888888");
        addressPo.setPostalCode("830063");
        addressPo.setConsignee("神无月");
        addressPo.setBeDefault(true);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
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
        /**
     * @description 测试地址region字段是否符合省市县的层级关系
     * @author 24320172203255
     */
    @Test
    public void tc_address_003() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(198);//city的pid并不是8，所以应该报错
        addressPo.setProvinceId(8);
        addressPo.setCountyId(1055);
        addressPo.setAddressDetail("海韵405");
        addressPo.setMobile("13988888888");
        addressPo.setPostalCode("830063");
        addressPo.setConsignee("神无月");
        addressPo.setBeDefault(true);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(addressPo, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        assertEquals(751,errno); //地址插入失败
    }

    /**
     * @description 测试地址region字段是否符合省市县的层级关系
     * @author 24320172203255
     */
    @Test
    public void tc_address_004() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(97);
        addressPo.setProvinceId(8);
        addressPo.setCountyId(837);//county的pid并不是97，所以应该报错
        addressPo.setAddressDetail("海韵405");
        addressPo.setMobile("13988888888");
        addressPo.setPostalCode("830063");
        addressPo.setConsignee("神无月");
        addressPo.setBeDefault(true);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(addressPo, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        assertEquals(751,errno); //地址插入失败
    }


    /**
     * @description 测试地址consignee不存在是否可以插入
     * @author 24320172203255
     */
    @Test
    public void tc_address_005() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(100);
        addressPo.setProvinceId(8);
        addressPo.setCountyId(1055);
        addressPo.setAddressDetail("海韵405");
        addressPo.setMobile("13988888888");
        addressPo.setPostalCode("830063");
        addressPo.setBeDefault(true);
        addressPo.setGmtCreate(LocalDateTime.now());
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(addressPo, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        assertEquals(751,errno); //地址插入失败
    }


    /**
     * @description 测试获取地址列表时，传入错误参数
     * error limit=-1
     * @author: 24320172203255
     */
    @Test
    public void tc_address_006() throws Exception{
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
     * @description 测试获取地址列表时，传入错误参数
     * error page=-1
     * @author: 24320172203255
     */
    @Test
    public void tc_address_007() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?page=-1&limit=2");
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
     * @description 测试获取地址列表时，传入错误参数
     * error page=-2 limit=-2
     * @author: 24320172203255
     */
    @Test
    public void tc_address_008() throws Exception{
        /* 设置请求头部*/
        URI uri = new URI(url+"?page=-2&limit=-2");
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
     * @description 测试地址能否删除，传入的id<0
     * error 参数错误
     * @author 24320172203255
     */
    @Test
    public void tc_address_009() throws Exception
    {
        URI uri = new URI(url+"/-10");

        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }

    /**
     * @description 测试地址能否删除，传入的id不存在
     * error 地址删除失败
     * @author 24320172203255
     */
    @Test
    public void tc_address_010() throws Exception
    {
        URI uri = new URI(url+"/99999");

        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(743, errno);
    }

    /**
     * @description 更新地址，省市县并不对应
     * error 地址更新失败
     * @author 24320172203255
     */
    @Test
    public void tc_address_011() throws Exception{
        // 准备要更新的数据
        AddressPo addressPo =new AddressPo();
        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(198);//city的pid并不是8，所以应该报错
        addressPo.setProvinceId(8);
        addressPo.setCountyId(1055);
        addressPo.setAddressDetail("海韵405");
        addressPo.setMobile("13988888888");
        addressPo.setPostalCode("009988");
        addressPo.setConsignee("神无日");
        addressPo.setBeDefault(true);
        addressPo.setGmtModified(LocalDateTime.now());

        // 设置头部
        URI uri = new URI(url+"/1");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        // 发出http请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(752, errno);
    }

    /**
     * @description 更新地址
     * @author 24320172203255
     */
    @Test
    public void tc_address_012() throws Exception{
        // 准备要更新的数据
        AddressPo addressPo= new AddressPo();
        addressPo.setUserId(10);//domain中是String字段
        addressPo.setCityId(100);
        addressPo.setProvinceId(22);
        addressPo.setCountyId(33);
        addressPo.setAddressDetail("海韵405");
        addressPo.setMobile("13988888888");
        addressPo.setPostalCode("009988");
        addressPo.setConsignee("神无日");
        addressPo.setBeDefault(true);
        addressPo.setGmtModified(LocalDateTime.now());

        // 设置头部
        URI uri = new URI(url+"/1");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        // 发出http请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        /*取得响应体*/
        String body = responseEntity.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        // 取出返回的body
        AddressPo responseAddress = JacksonUtil.parseObject(responseEntity.getBody(), "data", AddressPo.class);

        // 比较值是否相等
        assertEquals(1, responseAddress.getId());
        assertEquals(addressPo.getConsignee(), responseAddress.getConsignee());
        assertNotEquals(addressPo.getGmtModified(), responseAddress.getGmtModified());

    }


    /**
     * @description 查看地址详情，成功情况
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_address_013() throws Exception{
        URI uri = new URI(url+"/10");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errno);

        AddressPo addressPo =JacksonUtil.parseObject(body,"data",AddressPo.class);
        assertEquals(10,addressPo.getId());
    }

    /**
     * @description 查看地址详情，id对应的地址不存在
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_address_014() throws Exception{
        URI uri = new URI(url+"/9999");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(744, errno);
    }

    /**
     * @description 查看地址详情，id参数不对
     * @author 24320172203255
     * @throws Exception
     */
    @Test
    public void tc_address_015() throws Exception{
        URI uri = new URI(url+"/-10");
        HttpHeaders headers = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(headers);

        /*exchange方法模拟HTTP请求*/
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        /*取得响应体*/
        String body = response.getBody();
        Integer errno = JacksonUtil.parseInteger(body, "errno");
        assertEquals(580, errno);
    }
}
