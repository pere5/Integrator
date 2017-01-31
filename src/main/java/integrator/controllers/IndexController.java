package integrator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Per Eriksson on 2017-01-30.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String greeting(Model model) {
        model.addAttribute("name", "");
        return "index";
    }


}
