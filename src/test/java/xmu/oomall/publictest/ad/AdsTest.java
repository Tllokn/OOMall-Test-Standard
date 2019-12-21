package xmu.oomall.publictest.ad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.PublicTestApplication;
import xmu.oomall.domain.Ad;
import xmu.oomall.publictest.AdminAccount;
import xmu.oomall.util.JacksonUtil;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * @author
 */
@SpringBootTest(classes = PublicTestApplication.class)
public class AdsTest {

    @Value("http://${oomall.host}:${oomall.port}/adService/ads")
    String url;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminAccount adminAccount;

    /**
     *
     * @author Ming Qiu
     */
    @Test
    public void tc_ads_001() throws Exception{

        LocalDateTime a;
        a = LocalDateTime.of(2019, 12, 20, 12, 01, 55);

        Ad ad=new Ad();
        ad.setLink("http://oomall.liublack.cn/api/adminStd.html");
        ad.setName("伯爵旅拍——想去哪拍，就去哪拍");
        ad.setContent("铂爵旅拍--中国旅拍领军品牌，全球100大旅拍基地，总部设立于厦门，遍布" +
                "厦门，三亚，丽江，大理，深圳，中国香港地区，北京，桂林，杭州，大连，青岛，巴厘" +
                "岛，普吉岛，香格里拉，马尔代夫，日本，巴黎，希腊，罗马，布拉格，土耳其等地。铂爵旅拍" +
                "的口号是：想去哪拍就去哪拍！");
        ad.setBeDefault(true);//domain层代码是BeDefault属性（虽然数据库是is_default）
        ad.setBeEnabled(true);
        ad.setStartTime(a);
        ad.setEndTime(a.plusDays(7));
        ad.setGmtCreate(LocalDateTime.now());
        ad.setGmtModified(LocalDateTime.now());

        URI uri = new URI(url);
        HttpHeaders httpHeaders = adminAccount.createHeaderWithToken();
        HttpEntity httpEntity = new HttpEntity<>(ad, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");
        Ad testAd=JacksonUtil.parseObject(result,"data", Ad.class);

        assertEquals(0,errno);
        assertEquals(ad.getLink(),testAd.getLink());
        assertEquals(ad.getName(),testAd.getName());
        assertEquals(ad.getContent(),testAd.getContent());
        assertEquals(ad.getStartTime(),testAd.getStartTime());
    }

    /**
     *
     * @author Ming Qiu
     */
    @Test
    public void tc_ads_002() throws Exception{
        URI uri = new URI(url);
        LocalDateTime a;
        a = LocalDateTime.of(2019, 12, 20, 12, 01, 55);

        Ad ad=new Ad();
        ad.setLink("http://oomall.liublack.cn/api/adminStd.html");
        ad.setName("伯爵旅拍——想去哪拍，就去哪拍");
        ad.setContent("铂爵旅拍--中国旅拍领军品牌，全球100大旅拍基地，总部设立于厦门，遍布" +
                "厦门，三亚，丽江，大理，深圳，中国香港地区，北京，桂林，杭州，大连，青岛，巴厘" +
                "岛，普吉岛，香格里拉，马尔代夫，日本，巴黎，希腊，罗马，布拉格，土耳其等地。铂爵旅拍" +
                "的口号是：想去哪拍就去哪拍！");
        ad.setBeDefault(true);//domain层代码是BeDefault属性（虽然数据库是is_default）
        ad.setBeEnabled(true);
        ad.setStartTime(a);
        ad.setEndTime(a.plusDays(7));
        ad.setGmtCreate(LocalDateTime.now());
        ad.setGmtModified(LocalDateTime.now());

        HttpHeaders httpHeaders = adminAccount.createHeaders();
        HttpEntity httpEntity = new HttpEntity<>(ad, httpHeaders);

        ResponseEntity<String> responseEntity= restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        String result=responseEntity.getBody();
        Integer errno= JacksonUtil.parseInteger(result,"errno");
        assertEquals(660,errno); //用户无权限
    }
}
