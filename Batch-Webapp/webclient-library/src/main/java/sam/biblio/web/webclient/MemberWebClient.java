package sam.biblio.web.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sam.biblio.model.PageInfo;
import sam.biblio.model.library.Member;

@Component
public class MemberWebClient extends CommonWebClient {
    @Value("${api.biblio.resource.path.members.searchByUserEmail}")
    private String findByUserEmailURLFragment;
    @Value("${api.biblio.resource.path.members.searchByUserEmail.param}")
    private String findByUserEmailParam;

    protected MemberWebClient(@Value("${api.biblio.endpoint}") String endpoint,
                              @Value("${api.biblio.resource.path.members}") String resourcePath,
                              @Value("${api.biblio.basic-authentication.id}") String username,
                              @Value("${api.biblio.basic-authentication.password}") String password,
                              @Value("${httpclient.connectTimeout:10000}") int connectTimeout,
                              @Value("${httpclient.readTimeout:10000}") int readTimeout) {
        super(endpoint, resourcePath, username, password, connectTimeout, readTimeout);
    }

    public PagedModel<EntityModel<Member>> findAll(PageInfo page) {
        ResponseEntity<PagedModel<EntityModel<Member>>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath).addParam(page).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Member>>>() {
                });
        return response.getBody();
    }

    public EntityModel<Member> findByResourceUrl(String resourceUrl) {
        ResponseEntity<EntityModel<Member>> response = restTemplate.exchange(setUrl(resourceUrl).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Member>>() {
                });
        return response.getBody();
    }

    public EntityModel<Member> findByUserEmail(String userEmail) {
        ResponseEntity<EntityModel<Member>> response;
        response = restTemplate.exchange(setUrl(apiEndPoint + findByUserEmailURLFragment).addParam(findByUserEmailParam, userEmail).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Member>>() {
                });
        return response.getBody();
    }
}