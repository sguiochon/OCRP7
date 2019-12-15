package sam.biblio.web.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sam.biblio.model.library.Copy;
import sam.biblio.model.library.Lending;
import sam.biblio.web.webclient.CopyWebClient;
import sam.biblio.web.webclient.LendingWebClient;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class LendCopyServiceTest {

    @Autowired
    private LendingWebClient lendingWebClient;

    @Autowired
    private CopyWebClient copyWebClient;

    @Test
    public void testLendingCreation() {

        Lending lending = new Lending();
        lending.setStart(LocalDate.now());
        EntityModel<Lending> lendingResource = lendingWebClient.save(lending);

        EntityModel<Copy> copyResource = copyWebClient.findByResourceUrl("http://localhost:9990/copies/12");

        lendingWebClient.linkEntityToLending(lendingResource, copyResource, LinkRelation.of("/copy"));

    }

    @Test
    public void testLendingFind() {
        PagedModel<EntityModel<Lending>> lendings;
        lendings = lendingWebClient.findAll(null);
    }
}
