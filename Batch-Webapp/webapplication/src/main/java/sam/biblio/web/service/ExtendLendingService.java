package sam.biblio.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import sam.biblio.model.library.Lending;
import sam.biblio.web.error.LendingExtensionException;
import sam.biblio.web.webclient.LendingWebClient;

import java.time.LocalDate;

@Service
public class ExtendLendingService {

    private static final Logger log = LoggerFactory.getLogger(ExtendLendingService.class);

    @Autowired
    LendingWebClient lendingWebClient;

    public void extend(Long lendingId, String email) {

        EntityModel<Lending> lendingResource = null;
        try {
            lendingResource = lendingWebClient.findById(lendingId);
        }catch (HttpClientErrorException.NotFound e) {
            log.info("Tentative de prêt d'un document inexistant: {}", lendingId);
            throw new LendingExtensionException("Le document demandé n'existe pas");
        }

        // Nb of postponement is increased by 1
        int nbPostponement = lendingResource.getContent().getNbPostponement();
        lendingResource.getContent().setNbPostponement(nbPostponement + 1);

        // end date delayed of one month:
        LocalDate endDate = lendingResource.getContent().getEnd();
        lendingResource.getContent().setEnd(endDate.plusMonths(1));

        // TODO vérifier que le Principal est bien associé au prêt

        try {
            lendingWebClient.updateLending(lendingResource);
        } catch (HttpClientErrorException.NotAcceptable e) {
            throw new LendingExtensionException("Il n'est pas autorisé de prolonger un prêt plus d'une fois");
        }

    }
}
