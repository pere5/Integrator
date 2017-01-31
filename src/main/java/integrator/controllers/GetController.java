package integrator.controllers;

import integrator.Parser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GetController {

    @RequestMapping(value="/getCategorySuggestions", method=RequestMethod.POST)
    public Map<String, List<String>> greeting(@RequestBody Map<String, String> flawedCategory) {
        return Parser.getMatchingCategoriesSet(flawedCategory.get("flawedCategory"), Parser.ENGLISH);
    }
}