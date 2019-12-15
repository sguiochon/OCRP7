package sam.biblio.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sam.biblio.web.dto.LendCopyDTO;
import sam.biblio.web.dto.LendingDTO;
import sam.biblio.web.service.ExtendLendingService;
import sam.biblio.web.service.LendCopyService;

import java.security.Principal;

@RestController
public class LendingRestController {

    @Autowired
    LendCopyService lendCopyService;

    @Autowired
    ExtendLendingService extendLendingService;

    @PostMapping(value = "/lend")
    public String lendCopy(Principal principal, @RequestBody LendCopyDTO lendCopyDTO) {
        lendCopyService.lendCopy(lendCopyDTO.getDocumentId(), principal.getName());
        //TODO :retourner un body empty et un code 204
        return "";
    }

    @PostMapping(value = "/extendLending")
    public String extendLending(Principal principal, @RequestBody LendingDTO lendingDTO) {
        extendLendingService.extend(lendingDTO.getLendingId(), principal.getName());
        return "";
    }

}
