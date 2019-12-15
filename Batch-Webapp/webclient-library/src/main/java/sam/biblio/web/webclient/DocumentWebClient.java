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
import sam.biblio.model.library.Document;

@Component
public class DocumentWebClient extends CommonWebClient {

    @Value("${api.biblio.resource.path.documents.searchByText}")
    private String searchByTextURLFragment;
    @Value("${api.biblio.resource.path.documents.searchByText.paramText}")
    private String searchByTextParamText;

    @Value("${api.biblio.resource.path.documents.searchByCopyURLFragment}")
    private String searchByCopyURLFragment;
    @Value("${api.biblio.resource.path.documents.searchByCopy.paramCopyId}")
    private String searchBuCopyParamCopyId;

    protected DocumentWebClient(@Value("${api.biblio.endpoint}") String endpoint,
                                @Value("${api.biblio.resource.path.documents}") String resourcePath,
                                @Value("${api.biblio.basic-authentication.id}") String username,
                                @Value("${api.biblio.basic-authentication.password}") String password,
                                @Value("${httpclient.connectTimeout:10000}") int connectTimeout,
                                @Value("${httpclient.readTimeout:10000}") int readTimeout) {
        super(endpoint, resourcePath, username, password, connectTimeout, readTimeout);
    }

    public PagedModel<EntityModel<Document>> findAll(PageInfo page) {
        ResponseEntity<PagedModel<EntityModel<Document>>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath).addParam(page).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Document>>>() {
                });
        return response.getBody();
    }

    public EntityModel<Document> findByResourceUrl(String resourceUrl) {
        ResponseEntity<EntityModel<Document>> response = restTemplate.exchange(setUrl(resourceUrl).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Document>>() {
                });
        return response.getBody();
    }

    public PagedModel<EntityModel<Document>> findByTextString(PageInfo page, String criteria) {
        ResponseEntity<PagedModel<EntityModel<Document>>> response = restTemplate.exchange(setUrl(apiEndPoint + searchByTextURLFragment).addParam(page).addParam(searchByTextParamText, criteria).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Document>>>() {
                });
        return response.getBody();
    }

    public EntityModel<Document> findById(Long documentID) {
        ResponseEntity<EntityModel<Document>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath + "/" + documentID).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Document>>() {
                });
        return response.getBody();
    }

    public EntityModel<Document> findByCopyId(Long copyId) {
        ResponseEntity<EntityModel<Document>> response = restTemplate.exchange(setUrl(apiEndPoint + searchByCopyURLFragment).addParam(searchBuCopyParamCopyId, copyId.toString()).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Document>>() {
                });
        return response.getBody();
    }


}
