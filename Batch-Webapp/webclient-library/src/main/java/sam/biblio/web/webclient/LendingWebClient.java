package sam.biblio.web.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import sam.biblio.model.PageInfo;
import sam.biblio.model.library.Lending;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class LendingWebClient extends CommonWebClient {

    @Value("${api.biblio.resource.path.lendings.searchByEndDateBefore}")
    private String findByEndBeforeURLFragment;
    @Value("${api.biblio.resource.path.lendings.searchByEndDateBefore.dateParam}")
    private String findByEndBeforeParamDate;

    @Value("${api.biblio.resource.path.lendings.searchByUserEmail}")
    private String findByUserEmailURLFragment;
    @Value("${api.biblio.resource.path.lendings.searchByUserEmail.param}")
    private String findByUserEmailParam;

    protected LendingWebClient(@Value("${api.biblio.endpoint}") String endpoint,
                               @Value("${api.biblio.resource.path.lendings}") String resourcePath,
                               @Value("${api.biblio.basic-authentication.id}") String username,
                               @Value("${api.biblio.basic-authentication.password}") String password,
                               @Value("${httpclient.connectTimeout:10000}") int connectTimeout,
                               @Value("${httpclient.readTimeout:10000}") int readTimeout) {
        super(endpoint, resourcePath, username, password, connectTimeout, readTimeout);
    }

    public PagedModel<EntityModel<Lending>> findByEndDateBefore(PageInfo page, LocalDate limitDate) {
        ResponseEntity<PagedModel<EntityModel<Lending>>> response = restTemplate.exchange(setUrl(apiEndPoint + findByEndBeforeURLFragment).addParam(page).addParam(findByEndBeforeParamDate, limitDate.toString()).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Lending>>>() {
                });
        return response.getBody();
    }

    public PagedModel<EntityModel<Lending>> findAll(PageInfo page) {
        ResponseEntity<PagedModel<EntityModel<Lending>>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath).addParam(page).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Lending>>>() {
                });
        return response.getBody();
    }

    public EntityModel<Lending> findByResourceUrl(String resourceUrl) {
        ResponseEntity<EntityModel<Lending>> response = restTemplate.exchange(setUrl(resourceUrl).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Lending>>() {
                });
        return response.getBody();
    }

    public EntityModel<Lending> save(Lending lending) {
        ResponseEntity<EntityModel<Lending>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath).buildURL(),
                HttpMethod.POST,
                new HttpEntity<Lending>(lending, createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Lending>>() {
                });
        return response.getBody();
    }

    public PagedModel<EntityModel<Lending>> findAllByUserEmail(PageInfo page, String email) {
        ResponseEntity<PagedModel<EntityModel<Lending>>> response = restTemplate.exchange(setUrl(apiEndPoint + findByUserEmailURLFragment).addParam(page).addParam(findByUserEmailParam, email).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<PagedModel<EntityModel<Lending>>>() {
                });
        return response.getBody();
    }

    public <T> void linkEntityToLending(EntityModel<Lending> lendingResource, EntityModel<T> otherResource, LinkRelation linkRelation) {
        HttpHeaders headers = createBasicAuthentHeader(username, password);
        headers.setContentType(new MediaType("text", "uri-list")); // Obligatoire! Sinon, l'api rejette la requête avec un 404

        // UN SEUL lien doit être présent sinon l'api ne pourra pas faire le PUT et retournera un 500
        Link resourceSelfLink = otherResource.getLink(LinkRelation.of("self")).orElseThrow(() -> new IllegalArgumentException("Missing self link"));
        EntityModel<T> data = new EntityModel<>(Objects.requireNonNull(otherResource.getContent()), resourceSelfLink);

        Link lendingSelfLink = lendingResource.getLink(LinkRelation.of("self")).orElseThrow(() -> new IllegalArgumentException("Missing self link"));

        restTemplate.exchange(lendingSelfLink.getHref() + linkRelation.toString(),
                HttpMethod.PUT,
                new HttpEntity<EntityModel<T>>(data, headers),
                new ParameterizedTypeReference<EntityModel<Lending>>() {
                });
    }

    public void updateLending(EntityModel<Lending> lendingResource) {
        HttpHeaders headers = createBasicAuthentHeader(username, password);
        //headers.set("Content-Type", "application/merge-patch+json");
        headers.set("X-HTTP-Method-Override", "PATCH");
        Link lendingSelfLink = lendingResource.getLink(LinkRelation.of("self")).orElseThrow(() -> new IllegalArgumentException("Missing self link"));

        //int nbPostponement = lendingResource.getContent().getNbPostponement();
        //lendingResource.getContent().setNbPostponement(nbPostponement + 1);

        Lending data = lendingResource.getContent();//new EntityModel<>(Objects.requireNonNull(lendingResource.getContent()));//, lendingSelfLink);
        restTemplate.exchange(lendingSelfLink.getHref(),
                HttpMethod.PUT,
                new HttpEntity<Lending>(data, headers),
                new ParameterizedTypeReference<EntityModel<Lending>>() {
                });
    }

    public EntityModel<Lending> findById(Long lendingId) {
        ResponseEntity<EntityModel<Lending>> response = restTemplate.exchange(setUrl(apiEndPoint + resourcePath + "/" + lendingId.toString()).buildURL(),
                HttpMethod.GET,
                new HttpEntity(createBasicAuthentHeader(username, password)),
                new ParameterizedTypeReference<EntityModel<Lending>>() {
                });
        return response.getBody();
    }
}
