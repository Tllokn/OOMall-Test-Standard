package xmu.oomall.ad;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.Ad;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostAd {
    @Value("http://${host}:${port}/ads")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws Exception{
        URI uri = new URI(url);
        LocalDateTime a;
        a = LocalDateTime.of(2019, 12, 20, 12, 01, 55);

        Ad ad=new Ad();
    //  ad.setId(12138);//广告表单增id，不需要自己指定
        ad.setLink("http://oomall.liublack.cn/api/adminStd.html");
        ad.setName("伯爵旅拍——想去哪拍，就去哪拍");
        ad.setContent("铂爵旅拍--中国旅拍领军品牌，全球100大旅拍基地，总部设立于厦门，遍布" +
                "厦门，三亚，丽江，大理，深圳，中国香港地区，北京，桂林，杭州，大连，青岛，巴厘" +
                "岛，普吉岛，香格里拉，马尔代夫，日本，巴黎，希腊，罗马，布拉格，土耳其等地。铂爵旅拍" +
                "的口号是：想去哪拍就去哪拍！");
        ad.setBeDefault(true);//domain层代码是BeDefault属性（虽然数据库是is_default）
        ad.setBeEnable(true);//domain层代码是BeEnable属性（虽然数据库是is_enabled）
        ad.setStartTime(a);
        ad.setEndTime(a.plusDays(7));
        ad.setGmtCreate(LocalDateTime.now());
        ad.setGmtModified(LocalDateTime.now());

        ResponseEntity<String> responseEntity=testRestTemplate.postForEntity(uri,ad,String.class);
        String result=responseEntity.getBody();
        String errno= JacksonUtil.parseString(result,"errno");
        Ad testAd=JacksonUtil.parseObject(result,"data",Ad.class);
        String errmsg=JacksonUtil.parseString(result,"errmsg");

        assertEquals("0",errno);
        assertEquals("成功",errmsg);
        assertEquals(ad,testAd);
    }
}
