package integrator;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Per Eriksson on 2017-01-29.
 */
public class Parser {

    private static Map<String, Map<String, List<String>>> categoryIdMap = new HashMap<>();
    private static Map<String, Map<String, List<String>>> categoryChildrenMap = new HashMap<>();
    private static Map<String, Set<String>> allCategoriesSet = new HashMap<>();
    public static Map<String, String> languages = new HashMap<>();
    public static final String SVENSKA = "Svenska";
    public static final String ENGLISH = "English";
    public static final String NORSK = "Norsk";
    public static final String DANSK = "Dansk";
    static {
        languages.put(SVENSKA, "sv-SE");
        languages.put(ENGLISH, "en-US");
        languages.put(NORSK, "no-NO");
        languages.put(DANSK, "da-DK");
    }

    public static List<String> getChildrenForCategory(String category, String language) {
        return categoryChildrenMap.get(language).get(category);
    }

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

    public static Map<String, List<String>> getMatchingCategoriesSet(String flawedCategory, String language) {
        List<Tuple<String, Integer>> fuzzyScores = new ArrayList<>();
        List<Tuple<String, Double>> jaroWinklerScores = new ArrayList<>();

        //https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html
        for (String category: allCategoriesSet.get(language)) {
            fuzzyScores.add(new Tuple<>(category, StringUtils.getFuzzyDistance(category, flawedCategory, Locale.ENGLISH)));
            jaroWinklerScores.add(new Tuple<>(category, StringUtils.getJaroWinklerDistance(category, flawedCategory)));
        }
        fuzzyScores.sort((tuple1, tuple2) -> tuple2.y.compareTo(tuple1.y));
        jaroWinklerScores.sort((tuple1, tuple2) -> tuple2.y.compareTo(tuple1.y));

        Map<String, List<String>> response = new HashMap<>();
        response.put("fuzzyScores", fuzzyScores.stream().map(tuple -> tuple.x).limit(10).collect(Collectors.toList()));
        response.put("jaroWinklerScores", jaroWinklerScores.stream().map(tuple -> tuple.x).limit(10).collect(Collectors.toList()));
        return response;
    }

    public static void parse() {

        /*
         *  Facts Google Categories
         *   - Each category appears exactly once at the end.
         *   - Each category may or may not have children.
         */

        for (Map.Entry<String, String> language : languages.entrySet()) {
            String content = readFile(language.getValue());
            categoryIdMap.put(language.getKey(), buildCategoryIdMap(content));
            categoryChildrenMap.put(language.getKey(), buildCategoryChildrenMap(categoryIdMap.get(language.getKey())));
            allCategoriesSet.put(language.getKey(), buildAllCategoriesSet(categoryIdMap.get(language.getKey())));
        }
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
                List<String> myChildren = categoryChildrenMap.computeIfAbsent(category, k -> new ArrayList<>());
                int index = categories.indexOf(category);
                boolean haveChild = index + 1 != categories.size();
                if (haveChild) {
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
