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
import sam.biblio.model.library.Copy;

@Component
public class CopyWebClient extends CommonWebClient {

    @Value("${api.biblio.resource.path.copies.searchFreeCopyOfDocument}")
    private String searchFreeCopyURLFragment;
    @Value("${api.biblio.resource.path.copies.searchFreeCopyOfDocument.documentIdParam}")
    private String searchFreeCopyParamDocumentId;

    protected CopyWebClient(@Value("${api.biblio.endpoint}") String endpoint,
                            @Value("${api.biblio.resource.path.copies}") String resourcePath,
                            @Value("${api.biblio.basic-authentication.id}") String username,
                            @Value("${api.biblio.basic-authentication.password}") String password,
                            @Value("${httpclient.connectTimeout:10000}") int connectTimeout,
                            @Value("${httpclient.readTimeout:10000}") int readTimeout) {
        super(endpoint, resourcePath, username, password, connectTimeout, readTimeout);
    }

    public PagedModel<EntityModel<Copy>> findAll(PageInfo page) {
        ResponseEntity<PagedModel<EntityModel<Copy>>> response = restTemplate.exchange( setUrl(apiEndPoint + resourcePath).addParam(page).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Copy>>>() {
                });
        return response.getBody();
    }

    public EntityModel<Copy> findByResourceUrl(String resourceUrl) {
        ResponseEntity<EntityModel<Copy>> response = restTemplate.exchange(setUrl(resourceUrl).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Copy>>() {
                });
        return response.getBody();
    }

    public PagedModel<EntityModel<Copy>> findByResourcesUrl(String resourceUrl) {
        ResponseEntity<PagedModel<EntityModel<Copy>>> response = restTemplate.exchange(setUrl(resourceUrl).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Copy>>>() {
                });
        return response.getBody();
    }

    public PagedModel<EntityModel<Copy>> findFreeCopyOfDocument(PageInfo page, Long documentId) {
        ResponseEntity<PagedModel<EntityModel<Copy>>> response = restTemplate.exchange(setUrl(apiEndPoint + searchFreeCopyURLFragment).addParam(page).addParam(searchFreeCopyParamDocumentId, documentId.toString()).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Copy>>>() {
                });
        return response.getBody();
    }

}
