package challenges;

import java.util.ArrayList;
import java.util.List;

public class day14 {

    private static List<Integer> calculateRecipes(int recipes) {
        List<Integer> startScores = new ArrayList<>(30000000);
        int indexElfOne = 0;
        int indexElfTwo = 1;
        boolean found = false;

        startScores.add(3); startScores.add(7);
        List<Integer> newRecipes = new ArrayList<>();
        while (!found) {
            int newScore = startScores.get(indexElfOne) + startScores.get(indexElfTwo);
            if(newScore < 10) {
                startScores.add(newScore);
                newRecipes = checkNewRecipes(newRecipes, startScores, recipes);
            } else {
                startScores.add(1);
                newRecipes = checkNewRecipes(newRecipes, startScores, recipes);
                startScores.add(newScore % 10);
                newRecipes = checkNewRecipes(newRecipes, startScores, recipes);

            }
            found = findRecipe(startScores, recipes);
            indexElfOne = (indexElfOne + (startScores.get(indexElfOne) + 1)) % startScores.size();
            indexElfTwo = (indexElfTwo + (startScores.get(indexElfTwo) + 1)) % startScores.size();
        }
        return newRecipes;
    }

    private static List<Integer> checkNewRecipes(final List<Integer> newRecipes, final List<Integer> allScores, int recipe) {
        if(newRecipes.size() < 10 && allScores.size() > recipe) {
            newRecipes.add(allScores.get(allScores.size() - 1));
        }
        return newRecipes;
    }

    private static boolean findRecipe(List<Integer> recipes, int searchRecipe) {
        if (recipes.size() > String.valueOf(searchRecipe).length()) {
            String searchRecipeStr = Integer.toString(searchRecipe);
            List<Integer> resultSearchList = new ArrayList<>(searchRecipeStr.length());
            for (int x = 0; x < searchRecipeStr.length(); x++) {
                resultSearchList.add(Character.getNumericValue(searchRecipeStr.charAt(x)));
            }
            List<Integer> testList = recipes.subList(recipes.size() - searchRecipeStr.length(), recipes.size());
            List<Integer> testList2 = recipes.subList(recipes.size() - 1 - searchRecipeStr.length(), recipes.size() - 1);
            if(testList.equals(resultSearchList) || testList2.equals(resultSearchList)) {
                System.out.println("Found: " + (recipes.size() - resultSearchList.size() - 1));
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("The 10 following recipes are: " + calculateRecipes(635041).toString());
    }
}
