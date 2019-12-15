package sam.biblio.web.service;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import sam.biblio.model.PageInfo;
import sam.biblio.model.library.Copy;
import sam.biblio.model.library.Document;
import sam.biblio.web.dto.DocumentDTO;
import sam.biblio.web.dto.PageDTO;
import sam.biblio.web.webclient.CopyWebClient;
import sam.biblio.web.webclient.DocumentWebClient;
import sam.biblio.web.webclient.LendingWebClient;


@Service
public class SearchDocumentService {

    private final DocumentWebClient documentWebClient;

    private final CopyWebClient copyWebClient;

    private final LendingWebClient lendingWebClient;

    private final DozerBeanMapper mapper;

    @Autowired
    public SearchDocumentService(DocumentWebClient documentWebClient, CopyWebClient copyWebClient, LendingWebClient lendingWebClient, DozerBeanMapper mapper) {
        this.documentWebClient = documentWebClient;
        this.copyWebClient = copyWebClient;
        this.lendingWebClient = lendingWebClient;
        this.mapper = mapper;
    }

    public PageDTO<DocumentDTO> searchDocument(int pageSize, int pageNumber, String criteria) {

        PageInfo pageIn = new PageInfo(pageSize);
        pageIn.setNumber(pageNumber);

        PagedModel<EntityModel<Document>> result = documentWebClient.findByTextString(pageIn, criteria);
        PageDTO<DocumentDTO> pageDTO = new PageDTO<>();
        mapper.map(result.getMetadata(), pageDTO.getPageMetadata());

        for (EntityModel<Document> documentResource : result.getContent()) {
            DocumentDTO documentDTO = new DocumentDTO();
            mapper.map(documentResource.getContent(), documentDTO);
            pageDTO.addContent(documentDTO);

            PagedModel<EntityModel<Copy>> copiesResource = copyWebClient.findFreeCopyOfDocument(null, documentResource.getContent().getId());
            documentDTO.setNbExemplairesLibres(copiesResource.getContent().size());
        }
        return pageDTO;
    }
}
