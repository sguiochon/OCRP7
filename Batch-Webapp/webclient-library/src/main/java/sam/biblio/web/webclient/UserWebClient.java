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
import sam.biblio.model.security.User;

@Component
public class UserWebClient extends CommonWebClient {

    @Value("${api.biblio.resource.path.users.searchByEmail}")
    private String findByEmailURLFragment;
    @Value("${api.biblio.resource.path.users.searchByEmail.emailParam}")
    private String findByEmailParamEmail;

    public UserWebClient(@Value("${api.biblio.endpoint}") String endpoint,
                         @Value("${api.biblio.resource.path.users}") String path,
                         @Value("${api.biblio.basic-authentication.id}") String username,
                         @Value("${api.biblio.basic-authentication.password}") String password,
                         @Value("${httpclient.connectTimeout:10000}") int connectTimeout,
                         @Value("${httpclient.readTimeout:10000}") int readTimeout) {
        super(endpoint, path, username, password, connectTimeout, readTimeout);
    }

    public PagedModel<EntityModel<User>> findAll(PageInfo page) {
        ResponseEntity<PagedModel<EntityModel<User>>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath).addParam(page).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<User>>>() {
                });
        return response.getBody();
    }

    public EntityModel<User> findByResourceUrl(String resourceUrl) {
        ResponseEntity<EntityModel<User>> response = restTemplate.exchange(setUrl(resourceUrl).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<User>>() {
                });
        return response.getBody();
    }

    public EntityModel<User> findByEmail(String email) {
        ResponseEntity<EntityModel<User>> response = restTemplate.exchange(setUrl(apiEndPoint + findByEmailURLFragment).addParam(findByEmailParamEmail, email).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<User>>() {
                });
        return response.getBody();
    }
}
