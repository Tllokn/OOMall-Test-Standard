package xmu.oomall.goods;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.oomall.domain.Goods;
import xmu.oomall.util.JacksonUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostGoods {
    @Value("http://${host}:${port}/goods")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void test1() throws Exception{
        URI uri = new URI(url);
        Goods good=new Goods();
        good.setId(10086);
        good.setGmtCreate(LocalDateTime.now());
        good.setName("农夫山泉");
        good.setGoodsSn("10089");
        good.setShortName("山泉");
        good.setDescription("大自然的搬运工");
        good.setBrief("可以喝");
        good.setStatusCode(false);
        good.setGoodsCategoryId(1009);
        good.setBrandId(10086);
        good.setWeight(new BigDecimal(100.05));
        good.setVolume("500ml");
        good.setSpecialFreightId(0);
        good.setBeSpecial(false);
        good.setBeDeleted(false);


        ResponseEntity<String>responseEntity=testRestTemplate.postForEntity(uri,good,String.class);
        String result=responseEntity.getBody();
        String errno=JacksonUtil.parseString(result,"errno");
        Goods testGoods=JacksonUtil.parseObject(result,"data",Goods.class);
        String errmsg=JacksonUtil.parseString(result,"errmsg");

        assertEquals("0",errno);
        assertEquals("成功",errmsg);
        assertEquals(good,testGoods);

    }
}
