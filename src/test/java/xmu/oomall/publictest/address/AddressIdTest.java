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
public class AddressIdTest {
    @Value("http://${oomall.host}:${oomall.port}/addressService/addresses/{id}")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserAccount userAccount;

    /*********************************************************
     * GET
     *********************************************************/
    /**
     * @author 24320172203230
     * @throws Exception
     */
    @Test
    public void tc_addressId_001() throws Exception{


        URI uri = new URI(url.replace("{id}","3"));
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        System.out.println(responseEntity);

        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");
        assertEquals(0,errno);

        Address address= JacksonUtil.parseObject(result,"data",Address.class);

        assertEquals(3,address.getId());

    }



    /*********************************************************
     * PUT
     *********************************************************/
    /**
     * @author 24320172203230
     * @throws Exception
     */
    @Test
    public void tc_addressId_002() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setCityId(354);
        addressPo.setProvinceId(12);
        addressPo.setCountyId(1344);
        addressPo.setAddressDetail("安徽省黄山市徽州区");
        addressPo.setMobile("13946325488");
        addressPo.setPostalCode("45000");
        addressPo.setConsignee("端木磊");
        addressPo.setBeDefault(false);
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url.replace("{id}","2"));
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        assertEquals(752,errno); //  地址修改失败

    }

    /**
     * @author 24320172203230
     * @throws Exception
     */
    @Test
    public void tc_addressId_003() throws Exception{
        AddressPo addressPo=new AddressPo();

        addressPo.setCityId(354);
        addressPo.setProvinceId(12);
        addressPo.setCountyId(1344);
        addressPo.setAddressDetail("安徽省黄山市徽州区");
        addressPo.setMobile("13946325488");
        addressPo.setPostalCode("45000");
        addressPo.setConsignee("端木磊");
        addressPo.setBeDefault(false);
        addressPo.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url.replace("{id}","2"));
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(addressPo,httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");

        assertEquals(752,errno); //  地址修改失败

    }


    /*********************************************************
     * DELETE
     *********************************************************/

    /**
     * @author 24320172203230
     * @throws Exception
     */
    @Test
    public void tc_addressId_004() throws Exception {
        /* 设置请求头部*/
        URI uri = new URI(url.replace("{id}", "2"));
        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(675, errNo); //管理员无操作权限

        //原来的对象还在
        response = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);
        Address address = JacksonUtil.parseObject(body,"data", Address.class);
        assertEquals(2, address.getId());
    }

    /**
     * @author 24320172203230
     * @throws Exception
     */

    @Test
    public void tc_adsId_005() throws Exception {
        /* 设置请求头部*/
        System.out.println("url = " + url);
        URI uri = new URI(url.replace("{id}", "2"));
        HttpHeaders httpHeaders = userAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        ResponseEntity<String> response = this.restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(0, errNo);

        response = this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        body = response.getBody();
        errNo = JacksonUtil.parseInteger(body, "errno");
        assertEquals(744, errNo);
    }


}
