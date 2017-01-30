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

    private static Map<String, Map<String, List<String>>> categoryIdMap = new HashMap<>();
    private static Map<String, Map<String, List<String>>> categoryChildrenMap = new HashMap<>();
    private static Map<String, Set<String>> allCategoriesSet = new HashMap<>();

    public static class Tuple<X, Y> {
        public final X x;
        public final Y y;
        public Tuple(X x, Y y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "{" + x + " = " + y + '}';
        }
    }

    public static Set<String> getMatchingCategoriesSet(String categoryToMatch, String language) {
        List<Tuple<String, Integer>> fuzzyScores = new ArrayList<>();
        List<Tuple<String, Double>> jaroWinklerScores = new ArrayList<>();

        //https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html
        for (String category: allCategoriesSet.get(language)) {
            fuzzyScores.add(new Tuple<>(category, StringUtils.getFuzzyDistance(category, categoryToMatch, Locale.ENGLISH)));
            jaroWinklerScores.add(new Tuple<>(category, StringUtils.getJaroWinklerDistance(category, categoryToMatch)));
        }
        fuzzyScores.sort((tuple1, tuple2) -> tuple2.y.compareTo(tuple1.y));
        jaroWinklerScores.sort((tuple1, tuple2) -> tuple2.y.compareTo(tuple1.y));

        System.out.println(Utils.prettyList(fuzzyScores));
        System.out.println(Utils.prettyList(jaroWinklerScores));

        return null;
    }

    public static void parse() {

        /*
         *  Facts Google Categories
         *   - Each category appears exactly once at the end.
         *   - Each category may or may not have children.
         */

        Map<String, String> languages = new HashMap<>();
        languages.put("Svenska", "sv-SE");
        languages.put("English", "en-US");
        languages.put("Norsk", "no-NO");
        languages.put("Dansk", "da-DK");

        for (Map.Entry<String, String> language : languages.entrySet()) {
            String content = readFile(language.getValue());
            categoryIdMap.put(language.getKey(), buildCategoryIdMap(content));
            categoryChildrenMap.put(language.getKey(), buildCategoryChildrenMap(categoryIdMap.get(language.getKey())));
            allCategoriesSet.put(language.getKey(), buildAllCategoriesSet(categoryIdMap.get(language.getKey())));
        }

        getMatchingCategoriesSet("Shoes", "English");
    }

    private static Set<String> buildAllCategoriesSet(Map<String, List<String>> categoryIdMap) {
        Set<String> allCategories = new HashSet<>();
        for (List<String> categories: categoryIdMap.values()) {
            allCategories.addAll(categories);
        }
        return allCategories;
    }

    private static String readFile(String language) {
        String content = null;
        try {
            String fileName = "F:\\IdeaProjects\\Integrator\\src\\main\\resources\\taxonomy-with-ids." + language + ".txt";
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
            if (!line.startsWith("#")) {
                int dash = line.indexOf('-');
                String id = line.substring(0, dash).trim();
                String categoryBunch = line.substring(dash + 1);
                List<String> categories = new ArrayList<>();
                for (String category: categoryBunch.split(">")) {
                    categories.add(category.trim());
                }
                categoryIdMap.put(id, categories);
            }
        }
        return categoryIdMap;
    }
}
