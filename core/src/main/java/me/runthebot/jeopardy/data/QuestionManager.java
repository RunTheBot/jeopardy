package me.runthebot.jeopardy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.runthebot.jeopardy.model.CategoryType;

/**
 * Manages the questions for the Jeopardy game
 */
public class QuestionManager {
    private static Map<CategoryType, Map<Integer, List<QuestionData>>> questions = new HashMap<>();
    private static final Random random = new Random();

    static {
        // Initialize with the default questions
        initializeDefaultQuestions();
    }

    /**
     * Initializes the default set of questions
     */
    private static void initializeDefaultQuestions() {
        // Science category
        addQuestion(CategoryType.SCIENCE, 200,
            "Which planet is known as the Red Planet?",
            "Mars",
            new String[]{"Venus", "Mars", "Jupiter", "Saturn"});

        addQuestion(CategoryType.SCIENCE, 200,
            "What is the chemical symbol for water?",
            "H2O",
            new String[]{"CO2", "H2O", "O2", "N2"});

        addQuestion(CategoryType.SCIENCE, 400,
            "What is the chemical symbol for gold?",
            "Au",
            new String[]{"Ag", "Au", "Fe", "Pb"});

        addQuestion(CategoryType.SCIENCE, 400,
            "What is the hardest natural substance on Earth?",
            "Diamond",
            new String[]{"Gold", "Iron", "Diamond", "Platinum"});

        addQuestion(CategoryType.SCIENCE, 600,
            "What gas do plants primarily use for photosynthesis?",
            "Carbon Dioxide",
            new String[]{"Oxygen", "Hydrogen", "Carbon Dioxide", "Nitrogen"});

        addQuestion(CategoryType.SCIENCE, 600,
            "What is the most abundant gas in Earth's atmosphere?",
            "Nitrogen",
            new String[]{"Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"});

        addQuestion(CategoryType.SCIENCE, 800,
            "What is the powerhouse of the cell?",
            "Mitochondria",
            new String[]{"Nucleus", "Mitochondria", "Ribosome", "Endoplasmic Reticulum"});

        addQuestion(CategoryType.SCIENCE, 800,
            "What is the speed of light in a vacuum?",
            "299,792,458 meters per second",
            new String[]{"150,000,000 meters per second", "299,792,458 meters per second", "3,000,000 meters per second", "30,000 meters per second"});

        addQuestion(CategoryType.SCIENCE, 1000,
            "Which element has the highest melting point?",
            "Tungsten",
            new String[]{"Iron", "Tungsten", "Platinum", "Carbon"});

        addQuestion(CategoryType.SCIENCE, 1000,
            "Who developed the theory of general relativity?",
            "Albert Einstein",
            new String[]{"Isaac Newton", "Albert Einstein", "Galileo Galilei", "Stephen Hawking"});

        // History category
        addQuestion(CategoryType.HISTORY, 200,
            "Who was the first President of the United States?",
            "George Washington",
            new String[]{"Thomas Jefferson", "John Adams", "George Washington", "Abraham Lincoln"});

        addQuestion(CategoryType.HISTORY, 200,
            "In which year did Columbus reach the Americas?",
            "1492",
            new String[]{"1492", "1776", "1620", "1066"});

        addQuestion(CategoryType.HISTORY, 400,
            "In which year did World War II end?",
            "1945",
            new String[]{"1939", "1942", "1945", "1950"});

        addQuestion(CategoryType.HISTORY, 400,
            "Which empire was ruled by Genghis Khan?",
            "Mongol Empire",
            new String[]{"Roman Empire", "Ottoman Empire", "Mongol Empire", "Persian Empire"});

        addQuestion(CategoryType.HISTORY, 600,
            "Which ancient civilization built the pyramids?",
            "Egyptians",
            new String[]{"Romans", "Greeks", "Egyptians", "Mayans"});

        addQuestion(CategoryType.HISTORY, 600,
            "Who wrote the 'Declaration of Independence'?",
            "Thomas Jefferson",
            new String[]{"George Washington", "Thomas Jefferson", "John Adams", "Benjamin Franklin"});

        addQuestion(CategoryType.HISTORY, 800,
            "What year did the American Civil War begin?",
            "1861",
            new String[]{"1776", "1812", "1861", "1901"});

        addQuestion(CategoryType.HISTORY, 800,
            "Who was the first emperor of China?",
            "Qin Shi Huang",
            new String[]{"Wu Zetian", "Qin Shi Huang", "Kublai Khan", "Sun Yat-sen"});

        addQuestion(CategoryType.HISTORY, 1000,
            "Who was the longest-serving U.S. President?",
            "Franklin D. Roosevelt",
            new String[]{"Theodore Roosevelt", "Franklin D. Roosevelt", "Woodrow Wilson", "Harry Truman"});

        addQuestion(CategoryType.HISTORY, 1000,
            "Which treaty ended World War I?",
            "Treaty of Versailles",
            new String[]{"Treaty of Paris", "Treaty of Versailles", "Treaty of Tordesillas", "Treaty of Ghent"});

        // Geography category
        addQuestion(CategoryType.GEOGRAPHY, 200,
            "What is the capital of France?",
            "Paris",
            new String[]{"London", "Berlin", "Paris", "Rome"});

        addQuestion(CategoryType.GEOGRAPHY, 200,
            "Which is the largest continent by land area?",
            "Asia",
            new String[]{"Africa", "Asia", "North America", "Europe"});

        addQuestion(CategoryType.GEOGRAPHY, 400,
            "Which is the largest ocean on Earth?",
            "Pacific Ocean",
            new String[]{"Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean"});

        addQuestion(CategoryType.GEOGRAPHY, 400,
            "Which country is both in Europe and Asia?",
            "Russia",
            new String[]{"Turkey", "Russia", "Egypt", "Greece"});

        addQuestion(CategoryType.GEOGRAPHY, 600,
            "What is the longest river in the world?",
            "Nile River",
            new String[]{"Amazon River", "Yangtze River", "Mississippi River", "Nile River"});

        addQuestion(CategoryType.GEOGRAPHY, 600,
            "Which U.S. state has the most coastline?",
            "Alaska",
            new String[]{"California", "Florida", "Alaska", "Hawaii"});

        addQuestion(CategoryType.GEOGRAPHY, 800,
            "What is the capital of the moon?",
            "Moon",
            new String[]{"Moon", "Mars", "Jupiter", "Saturn"});


        addQuestion(CategoryType.GEOGRAPHY, 1000,
            "What is the capital of the sun?",
            "Sun",
            new String[]{"Sun", "Moon", "Jupiter", "Saturn"});

        addQuestion(CategoryType.GEOGRAPHY, 1000,
            "What is the capital of the moon?",
            "Moon",
            new String[]{"Moon", "Mars", "Jupiter", "Saturn"});


    }


    /**
     * Adds a question to the database
     *
     * @param category the category of the question
     * @param value the dollar value of the question
     * @param question the question text
     * @param correctAnswer the correct answer
     * @param choices the available choices
     */
    public static void addQuestion(CategoryType category, int value, String question, String correctAnswer, String[] choices) {
        if (!questions.containsKey(category)) {
            questions.put(category, new HashMap<>());
        }

        Map<Integer, List<QuestionData>> categoryQuestions = questions.get(category);
        if (!categoryQuestions.containsKey(value)) {
            categoryQuestions.put(value, new ArrayList<>());
        }

        List<QuestionData> questionList = categoryQuestions.get(value);
        questionList.add(new QuestionData(category, value, question, correctAnswer, choices));
    }

    /**
     * Gets a random question from the database for the specified category and value
     *
     * @param category the category of the question
     * @param value the dollar value of the question
     * @return a random question for the category/value, or a default question if none exist
     */
    public static QuestionData getQuestion(CategoryType category, int value) {
        // Check if the category exists
        if (questions.containsKey(category)) {
            Map<Integer, List<QuestionData>> categoryQuestions = questions.get(category);

            // Check if the value exists in the category
            if (categoryQuestions.containsKey(value)) {
                List<QuestionData> questionList = categoryQuestions.get(value);

                if (!questionList.isEmpty()) {
                    // Get a random question from the list
                    int randomIndex = random.nextInt(questionList.size());
                    return questionList.get(randomIndex);
                }
            }
        }

        // Return a default question if not found
        return new QuestionData(
            CategoryType.GEOGRAPHY,
            value,
            "What is the capital of France?",
            "Paris",
            new String[]{"London", "Berlin", "Paris", "Rome"}
        );
    }

    /**
     * Gets a question from the database by category name
     *
     * @param categoryName the display name of the category
     * @param value the dollar value of the question
     * @return the question data, or a default question if not found
     */
    public static QuestionData getQuestionByName(String categoryName, int value) {
        CategoryType category = CategoryType.fromDisplayName(categoryName);
        return getQuestion(category, value);
    }

    /**
     * Gets all questions for a specific category and value
     *
     * @param category the category to get questions for
     * @param value the dollar value
     * @return a list of questions or an empty list if none found
     */
    public static List<QuestionData> getAllQuestions(CategoryType category, int value) {
        if (questions.containsKey(category)) {
            Map<Integer, List<QuestionData>> categoryQuestions = questions.get(category);

            if (categoryQuestions.containsKey(value)) {
                return new ArrayList<>(categoryQuestions.get(value));
            }
        }

        return new ArrayList<>();
    }

    /**
     * Gets the total number of questions for a specific category and value
     *
     * @param category the category to count questions for
     * @param value the dollar value
     * @return the number of questions available
     */
    public static int getQuestionCount(CategoryType category, int value) {
        if (questions.containsKey(category)) {
            Map<Integer, List<QuestionData>> categoryQuestions = questions.get(category);

            if (categoryQuestions.containsKey(value)) {
                return categoryQuestions.get(value).size();
            }
        }

        return 0;
    }
}
