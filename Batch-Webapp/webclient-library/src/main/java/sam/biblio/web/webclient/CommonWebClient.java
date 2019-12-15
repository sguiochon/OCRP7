package sam.biblio.web.webclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.rest.webmvc.convert.UriListHttpMessageConverter;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import sam.biblio.model.PageInfo;
import sam.biblio.web.webclient.logging.LoggingRequestInterceptor;
import sam.biblio.web.webclient.messageconverter.LendingPatchMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CommonWebClient {

    RestTemplate restTemplate;
    String apiEndPoint;
    String resourcePath;
    String username;
    String password;
    private int connectTimeout;
    private int readTimeout;
    private StringBuilder stringBuilder;

    protected CommonWebClient(String apiEndpoint, String resourcePath, String username, String password, int connectTimeout, int readTimeout) {
        this.apiEndPoint = apiEndpoint;
        this.resourcePath = resourcePath;
        this.username = username;
        this.password = password;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        buildRestTemplate();
    }

    /**
     * Builds a RestTemplate instance that fixes a bug in SpringBoot Web Client: http header Accept is not
     * valid for Spring Data REST API and must be forced to 'application/hal+json'.
     */
    private void buildRestTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();
        jacksonMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes(Arrays.asList("application/hal+json;charset=utf-8", "application/json;charset=utf-8")));
        jacksonMessageConverter.setObjectMapper(objectMapper);

        List<HttpMessageConverter<?>> customMessageConverters = new ArrayList();
        customMessageConverters.add(jacksonMessageConverter);
        customMessageConverters.add(new UriListHttpMessageConverter());
        //customMessageConverters.add(new LendingPatchMessageConverter());

        restTemplate = new RestTemplate(customMessageConverters);

        SimpleClientHttpRequestFactory rf = new SimpleClientHttpRequestFactory();
        rf.setReadTimeout(readTimeout);
        rf.setConnectTimeout(connectTimeout);

        BufferingClientHttpRequestFactory brf = new BufferingClientHttpRequestFactory(rf);

        restTemplate.setRequestFactory(brf);

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
    }

    HttpHeaders createBasicAuthentHeader(String username, String password) {
        HttpHeaders authorization = new HttpHeaders();
        authorization.setBasicAuth(username, password);
        return authorization;
    }

    CommonWebClient addParam(PageInfo pageInfo) {
        if (pageInfo != null) {
            this.addParam("page", String.valueOf(pageInfo.getNumber()));
            this.addParam("size", String.valueOf(pageInfo.getSize()));
        }
        return this;
    }

    CommonWebClient addParam(String name, String value) {
        if (!stringBuilder.toString().contains("?"))
            stringBuilder.append("?");
        if (!stringBuilder.toString().endsWith("?"))
            stringBuilder.append("&");
        stringBuilder.append(name).append("=").append(value);
        return this;
    }

    CommonWebClient setUrl(String url) {
        stringBuilder = new StringBuilder(url);
        return this;
    }

    String buildURL() {
        String urlString = stringBuilder.toString();
        stringBuilder = null;
        return urlString;
    }
}
