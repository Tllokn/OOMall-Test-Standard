package xmu.oomall.brand;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import xmu.good.oomall.domain.Brand;

import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PutBrandsId {
    @Value("http://${host}:${port}/brands/{id}")
    String url;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void test1() throws Exception{
        URI uri = new URI(url.replace("{id}","3"));
        Brand updateInstance = new Brand();
        updateInstance.setId(3);
        updateInstance.setName("wjaaa");
        HttpHeaders headers = testRestTemplate.headForHeaders(uri);
        HttpEntity<Brand> requestUpdate = new HttpEntity<>(updateInstance, headers);
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.PUT, requestUpdate, String.class);
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode errno = root.path("errno");
        JsonNode data = root.path("data");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, errno.asInt());
        assertEquals(updateInstance, mapper.readValue(data.toString(), Brand.class));
    }
}
