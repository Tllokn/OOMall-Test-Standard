package xmu.oomall.publictest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class PublicTestConfig {
    @Bean
    public TestRestTemplate testRestTemplate() {
        return new TestRestTemplate();
    }
}
