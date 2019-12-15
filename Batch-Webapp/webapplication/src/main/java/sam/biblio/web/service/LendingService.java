package sam.biblio.web.service;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import sam.biblio.model.PageInfo;
import sam.biblio.model.library.Copy;
import sam.biblio.model.library.Document;
import sam.biblio.model.library.Lending;
import sam.biblio.web.Application;
import sam.biblio.web.dto.DocumentDTO;
import sam.biblio.web.dto.LendingDTO;
import sam.biblio.web.dto.PageDTO;
import sam.biblio.web.webclient.CopyWebClient;
import sam.biblio.web.webclient.DocumentWebClient;
import sam.biblio.web.webclient.LendingWebClient;

import java.time.LocalDate;
import java.util.List;

@Service
public class LendingService {

    private static final Logger LOG = LoggerFactory.getLogger(LendingService.class);

    @Autowired
    private LendingWebClient lendingWebClient;

    @Autowired
    private DocumentWebClient documentWebClient;

    @Autowired
    private CopyWebClient copyWebClient;

    @Autowired
    private DozerBeanMapper mapper;

    public PageDTO<LendingDTO> getLendingsOfMember(int pageSize, int pageNumber, String email){

        PageInfo pageIn = new PageInfo(pageSize);
        pageIn.setNumber(pageNumber);

        PagedModel<EntityModel<Lending>> result = lendingWebClient.findAllByUserEmail(pageIn, email);
        PageDTO<LendingDTO> pageDTO = new PageDTO<>();
        mapper.map(result.getMetadata(), pageDTO.getPageMetadata());

        LOG.debug("number:{}, taille:{}, totalPages:{}, totalElements:{}",
                pageDTO.getPageMetadata().getNumero()
                ,pageDTO.getPageMetadata().getTaille()
                ,pageDTO.getPageMetadata().getTotalPages()
                ,pageDTO.getPageMetadata().getTotalElements()
        );

        for (EntityModel<Lending> lendingResource : result.getContent()) {

            LendingDTO lendingDTO = new LendingDTO();
            mapper.map(lendingResource.getContent(), lendingDTO);

            // Business Logic: borrow can not be extended more than once
            if (lendingResource.getContent().getNbPostponement().equals(0))
                lendingDTO.setExtendable(Boolean.TRUE);
            else
                lendingDTO.setExtendable(Boolean.FALSE);

            if (lendingResource.getContent().getEnd().isAfter(LocalDate.now()))
                lendingDTO.setOverDue(Boolean.FALSE);
            else
                lendingDTO.setOverDue(Boolean.TRUE);

            LOG.debug("Lending: id:{}, extendable:{}, overdue:{}",
                    lendingDTO.getLendingId(),
                    lendingDTO.getExtendable(),
                    lendingDTO.getOverDue());

            pageDTO.addContent(lendingDTO);

            Link copyLink = lendingResource.getLink("copy").orElseThrow(()->new IllegalArgumentException("Lending resoruce has no related copy"));

            EntityModel<Copy> copyResource = copyWebClient.findByResourceUrl(copyLink.getHref());

            EntityModel<Document> documentResource = documentWebClient.findByCopyId(copyResource.getContent().getId());

            DocumentDTO documentDTO = new DocumentDTO();
            mapper.map(documentResource.getContent(), documentDTO);

            lendingDTO.setDocument(documentDTO);
        }
        return pageDTO;
    }

}
