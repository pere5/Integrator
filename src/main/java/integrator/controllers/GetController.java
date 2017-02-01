package integrator.controllers;

import integrator.Parser;
import integrator.Utils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GetController {

    @RequestMapping(value="/getCategorySuggestions", method=RequestMethod.POST)
    public Map<String, List<String>> getCategorySuggestions(@RequestBody Map<String, String> flawedCategoryMap) {
        return Parser.getMatchingCategoriesSet(flawedCategoryMap.get("flawedCategory"), Parser.ENGLISH);
    }

    @RequestMapping(value="/getChildrenForCategory", method=RequestMethod.POST)
    public List<String> getChildrenForCategory(@RequestBody Map<String, String> categoryMap) {
        return Parser.getChildrenForCategory(categoryMap.get("category"), Parser.ENGLISH);
    }
}