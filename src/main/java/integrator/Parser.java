package integrator;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Per Eriksson on 2017-01-29.
 */
public class Parser {

    private static Map<String, List<String>> categoryIdMap = null;
    private static Map<String, List<String>> categoryChildrenMap = null;

    public static Set<String> getMatchingCategoriesSet(String categoryToMatch) {



        return null;
    }

    public static void parse() {

        /*
         *  Facts Google Categories
         *   - Each category appears exactly once at the end.
         *   - Each category may or may not have children.
         */

        String content = readFile();

        categoryIdMap = buildCategoryIdMap(content);
        categoryChildrenMap = buildCategoryChildrenMap(categoryIdMap);
        //System.out.println(content);
        //System.out.println(Utils.prettyMap(categoryChildrenMap));

        /*
         * Levenshtein distance
         * This is the number of changes needed to change one String into another,
         * where each change is a single character modification (deletion, insertion or substitution).
        */
        //System.out.println(StringUtils.getLevenshteinDistance("abcdd", "abcf"));                  // high bad

        /*
         * Jaro Winkler Distance
         * The Jaro measure is the weighted sum of percentage of matched characters from each file and transposed characters.
         */
        //System.out.println(StringUtils.getJaroWinklerDistance("abcd", "abcf"));                         // high good

        /*
         * Fuzzy Distance
         * This string matching algorithm is similar to the algorithms of editors such as Sublime Text, TextMate, Atom and others.
         * One point is given for every matched character. Subsequent matches yield two bonus points.
         * A higher score indicates a higher similarity.
         */
        //System.out.println(StringUtils.getFuzzyDistance("abcd", "abcf", Locale.ENGLISH));   // high good
    }

    private static String readFile() {
        String content = null;
        try {
            String fileName = "F:\\IdeaProjects\\Integrator\\src\\main\\resources\\taxonomy-with-ids.en-US.txt";
            content = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert content != null;
        return content;
    }

    private static Map<String, List<String>> buildCategoryChildrenMap(Map<String, List<String>> categoryIdMap) {
        Map<String, List<String>> categoryChildrenMap = new HashMap<>();
        for (List<String> categories: categoryIdMap.values()) {
            for (String category: categories) {
                int index = categories.indexOf(category);
                boolean haveChild = index + 1 != categories.size();
                if (haveChild) {
                    List<String> myChildren = categoryChildrenMap.computeIfAbsent(category, k -> new ArrayList<>());
                    String newChild = categories.get(index + 1);
                    if (!myChildren.contains(newChild)) {
                        myChildren.add(newChild);
                    }
                }
            }
        }
        return categoryChildrenMap;
    }

    private static Map<String, List<String>> buildCategoryIdMap(String content) {
        Map<String, List<String>> categoryIdMap = new HashMap<>();
        assert content != null;
        for (String line: content.split("\n")) {
            int dash = line.indexOf('-');
            String id = line.substring(0, dash).trim();
            String categoryBunch = line.substring(dash + 1);
            List<String> categories = new ArrayList<>();
            for (String category: categoryBunch.split(">")) {
                categories.add(category.trim());
            }
            categoryIdMap.put(id, categories);
        }
        return categoryIdMap;
    }
}
