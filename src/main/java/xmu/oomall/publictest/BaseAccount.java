package xmu.oomall.publictest;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import xmu.oomall.util.JacksonUtil;
import xmu.oomall.vo.LoginVo;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 用户帐号的抽象类
 * @author mingqiu
 * @date 2019/12/8
 */
public abstract class BaseAccount {

    protected abstract String getUserName();
    protected abstract String getPassword();
    protected abstract String getUrl();
    protected abstract RestTemplate getRestTemplate();

    protected String token = "";

    /**
     * 登录服务器，获取JWT Token
     * @return 登录成功与否
     */
    private boolean getToken(HttpHeaders httpHeaders) throws URISyntaxException {
        LoginVo loginVo = new LoginVo();
        loginVo.setUsername(this.getUserName());
        loginVo.setPassword(this.getPassword());

        URI uri = new URI(this.getUrl());
        HttpEntity httpEntity = new HttpEntity(loginVo, httpHeaders);
        ResponseEntity<String> response = this.getRestTemplate().postForEntity(uri, httpEntity, String.class);
        String body = response.getBody();
        Integer errNo = JacksonUtil.parseInteger(body, "errno");
        if (response.getStatusCode() == HttpStatus.OK && errNo == 0){
            System.out.println("response = " + response);
            this.token = response.getHeaders().get("authorization").get(0);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建一个无JWT Token的包头
     * @return 包头
     * @throws URISyntaxException
     */
    public HttpHeaders createHeaders() throws URISyntaxException {
        HttpHeaders httpHeaders = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        return httpHeaders;
    }

    /**
     * 在获得有JWT Token的包头
     * @return 包头
     */
    public HttpHeaders createHeaderWithToken() throws URISyntaxException {
        HttpHeaders headers = this.createHeaders();
        System.out.println("Before token = "+token);
        if (this.token == "") {
            this.getToken(headers);
        }
        System.out.println("After token = "+token);
        if (this.token != ""){
            headers.add("authorization", this.token);
            return headers;
        } else{
            return null;
        }

    }
}
