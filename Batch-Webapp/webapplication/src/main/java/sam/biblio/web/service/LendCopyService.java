package sam.biblio.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sam.biblio.model.library.Copy;
import sam.biblio.model.library.Document;
import sam.biblio.model.library.Lending;
import sam.biblio.model.library.Member;
import sam.biblio.web.error.LendingCreationException;
import sam.biblio.web.webclient.CopyWebClient;
import sam.biblio.web.webclient.DocumentWebClient;
import sam.biblio.web.webclient.LendingWebClient;
import sam.biblio.web.webclient.MemberWebClient;

import java.time.LocalDate;

@Service
public class LendCopyService {

    private static final Logger log = LoggerFactory.getLogger(LendCopyService.class);

    private final DocumentWebClient documentWebClient;

    private final LendingWebClient lendingWebClient;

    private final CopyWebClient copyWebClient;

    private final MemberWebClient memberWebClient;

    @Autowired
    public LendCopyService(DocumentWebClient documentWebClient, LendingWebClient lendingWebClient, CopyWebClient copyWebClient, MemberWebClient memberWebClient) {
        this.documentWebClient = documentWebClient;
        this.lendingWebClient = lendingWebClient;
        this.copyWebClient = copyWebClient;
        this.memberWebClient = memberWebClient;
    }

    public void lendCopy(Long documentID, String userLogin) {
        log.debug("Demande de reservation du document {} par {}", documentID, userLogin);
        // Vérification que le document existe
        EntityModel<Document> resourceDocument = null;
        try {
            resourceDocument = documentWebClient.findById(documentID);
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Tentative de prêt d'un document inexistant: {}", documentID);
            throw new LendingCreationException("Le document demandé n'existe pas");
        }

        log.debug("Prêt - le document existe!");

        // Recherche d'un exemplaire disponible
        PagedModel<EntityModel<Copy>> copiesResource = copyWebClient.findFreeCopyOfDocument(null, documentID);
        if (copiesResource.getContent().isEmpty()) {
            log.debug("Prêt - pas d'exemplaire libre");
            throw new LendingCreationException("Il n'y a plus d'exemplaire disponible");
        } else
            log.debug("Prêt - un exemplaire est disponible: ");

        // Création d'un prêt
        Lending lending = new Lending();
        lending.setStart(LocalDate.now());
        lending.setEnd(LocalDate.now().plusMonths(1));
        lending.setNbPostponement(0);
        EntityModel<Lending> lendingResource = lendingWebClient.save(lending);

        EntityModel<Copy> freeCopyResource = copiesResource.getContent().iterator().next();
        // Affectation de l'exemplaire au prêt
        lendingWebClient.linkEntityToLending(lendingResource, freeCopyResource, LinkRelation.of("/copy"));
        log.debug("L'exemplaire est bien affecté au prêt");

        // Identification du membre depuis son email:L
        EntityModel<Member> memberResource = memberWebClient.findByUserEmail(userLogin);

        // Affectation de l'utilisateur au prêt
        lendingWebClient.linkEntityToLending(lendingResource, memberResource, LinkRelation.of("/member"));
    }

}
