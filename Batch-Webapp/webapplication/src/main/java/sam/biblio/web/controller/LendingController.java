package sam.biblio.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sam.biblio.model.library.Lending;
import sam.biblio.model.library.Member;
import sam.biblio.model.security.User;
import sam.biblio.web.config.ApplicationConfig;
import sam.biblio.web.dto.*;
import sam.biblio.web.service.LendingService;
import sam.biblio.web.webclient.LendingWebClient;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

@Controller
public class LendingController {

    private final LendingService lendingService;

    private final ApplicationConfig applicationConfig;

    private final TableNavigationHelper navHelper;

    @Autowired
    public LendingController(LendingService lendingService, ApplicationConfig applicationConfig, TableNavigationHelper navHelper) {
        this.lendingService = lendingService;
        this.applicationConfig = applicationConfig;
        this.navHelper = navHelper;
    }

    @GetMapping(value = "/lendings")
    public String viewMyBorrows(Principal principal, @RequestParam(name = "p", required = false) String pageNb, Model model) {

        int currentPage = pageNb == null ? 0 : Integer.parseInt(pageNb);

        PageDTO<LendingDTO> page = lendingService.getLendingsOfMember(applicationConfig.tableSize, currentPage, principal.getName());

        NavDTO nav = null;
        if (page.getPageMetadata().getTotalPages() != 0)
            nav = navHelper.buildNavInfo(currentPage, page.getPageMetadata().getTotalPages());

        List<LendingDTO> collected = page.getContent();

        model.addAttribute("nav", nav);
        model.addAttribute("lendings", collected);

        return "lendingsPage";
    }

}
