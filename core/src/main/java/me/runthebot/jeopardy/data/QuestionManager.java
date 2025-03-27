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

        // Geography category (600 points)
        addQuestion(CategoryType.GEOGRAPHY, 600,
        "What is the longest river in the world?",
        "Nile River",
        new String[]{"Amazon River", "Mississippi River", "Nile River", "Yangtze River"});

        addQuestion(CategoryType.GEOGRAPHY, 600,
        "Which desert is the largest in the world?",
        "Antarctic Desert",
        new String[]{"Sahara Desert", "Gobi Desert", "Antarctic Desert", "Kalahari Desert"});

        // Geography category (800 points)
        addQuestion(CategoryType.GEOGRAPHY, 800,
        "Which country has the most natural lakes?",
        "Canada",
        new String[]{"United States", "Canada", "Russia", "Brazil"});

        addQuestion(CategoryType.GEOGRAPHY, 800,
        "What is the smallest country in the world by land area?",
        "Vatican City",
        new String[]{"Monaco", "San Marino", "Vatican City", "Liechtenstein"});

        // Geography category (1000 points)
        addQuestion(CategoryType.GEOGRAPHY, 1000,
        "What is the highest mountain in the world?",
        "Mount Everest",
        new String[]{"K2", "Mount Kilimanjaro", "Mount Everest", "Denali"});

        addQuestion(CategoryType.GEOGRAPHY, 1000,
        "Which city is located on two continents?",
        "Istanbul",
        new String[]{"Moscow", "Istanbul", "Cairo", "Tokyo"});

        // Sports category (200 points)
        addQuestion(CategoryType.SPORTS, 200,
"Which sport uses the term 'Grand Slam'?",
"All of the above",
        new String[]{"Tennis", "Baseball", "Golf", "All of the above"});

        addQuestion(CategoryType.SPORTS, 200,
"How many players are there in a basketball team on the court?",
"5",
        new String[]{"4", "5", "6", "7"});

        // Sports category (400 points)
        addQuestion(CategoryType.SPORTS, 400,
"How many players are on a standard soccer team?",
"11",
        new String[]{"9", "10", "11", "12"});

        addQuestion(CategoryType.SPORTS, 400,
"Which country won the 2018 FIFA World Cup?",
"France",
        new String[]{"Brazil", "Germany", "France", "Spain"});

        // Sports category (600 points)
        addQuestion(CategoryType.SPORTS, 600,
"What is the only country to have played in every FIFA World Cup?",
"Brazil",
        new String[]{"Germany", "Argentina", "Brazil", "Italy"});

        addQuestion(CategoryType.SPORTS, 600,
"Which sport is known as the 'King of Sports'?",
"Soccer (Football)",
        new String[]{"Basketball", "Cricket", "Soccer (Football)", "Tennis"});

        // Sports category (800 points)
        addQuestion(CategoryType.SPORTS, 800,
"What is the maximum score a player can achieve in a single game of bowling?",
"300",
        new String[]{"150", "200", "250", "300"});

        addQuestion(CategoryType.SPORTS, 800,
"Which athlete has won the most Olympic gold medals?",
"Michael Phelps",
        new String[]{"Usain Bolt", "Michael Phelps", "Simone Biles", "Carl Lewis"});

        // Sports category (1000 points)
        addQuestion(CategoryType.SPORTS, 1000,
"Which NFL team has won the most Super Bowls as of 2024?",
"Pittsburgh Steelers & New England Patriots",
        new String[]{"Dallas Cowboys", "San Francisco 49ers", "Pittsburgh Steelers & New England Patriots", "Green Bay Packers"});

        addQuestion(CategoryType.SPORTS, 1000,
"What is the length of a marathon in miles?",
"26.2 miles",
        new String[]{"13.1 miles", "20 miles", "26.2 miles", "30 miles"});
        
        // Movies category (200 points)
        addQuestion(CategoryType.MOVIES, 200,
"Which actor played Iron Man in the Marvel movies?",
"Robert Downey Jr.",
        new String[]{"Chris Evans", "Chris Hemsworth", "Robert Downey Jr.", "Mark Ruffalo"});

        addQuestion(CategoryType.MOVIES, 200,
"Which movie features a character named Luke Skywalker?",
"Star Wars",
        new String[]{"Star Trek", "Star Wars", "Interstellar", "Avatar"});

        // Movies category (400 points)
        addQuestion(CategoryType.MOVIES, 400,
"Which film won the Academy Award for Best Picture in 2020?",
"Parasite",
        new String[]{"1917", "Joker", "Parasite", "Little Women"});

        addQuestion(CategoryType.MOVIES, 400,
"Who directed the movie 'Inception'?",
"Christopher Nolan",
        new String[]{"Steven Spielberg", "James Cameron", "Christopher Nolan", "Quentin Tarantino"});

        // Movies category (600 points)
        addQuestion(CategoryType.MOVIES, 600,
"What is the highest-grossing movie of all time (without adjusting for inflation)?",
"Avatar",
        new String[]{"Titanic", "Avengers: Endgame", "Avatar", "Star Wars: The Force Awakens"});

        addQuestion(CategoryType.MOVIES, 600,
"Which animated movie features a character named Woody?",
"Toy Story",
        new String[]{"Shrek", "Finding Nemo", "Toy Story", "Frozen"});

        // Movies category (800 points)
        addQuestion(CategoryType.MOVIES, 800,
"Which 1994 movie features the quote 'Life is like a box of chocolates'?",
"Forrest Gump",
        new String[]{"The Shawshank Redemption", "Forrest Gump", "Pulp Fiction", "The Green Mile"});

        addQuestion(CategoryType.MOVIES, 800,
"Who was the first actor to play James Bond in a feature film?",
"Sean Connery",
        new String[]{"Roger Moore", "Sean Connery", "Daniel Craig", "Pierce Brosnan"});

        // Movies category (1000 points)
        addQuestion(CategoryType.MOVIES, 1000,
"Which film holds the record for the most Academy Award wins?",
"Titanic, Ben-Hur, and The Lord of the Rings: The Return of the King",
        new String[]{"Titanic, Ben-Hur, and The Lord of the Rings: The Return of the King", "Gone with the Wind", "Avatar", "The Godfather"});

        addQuestion(CategoryType.MOVIES, 1000,
"Which director is known for films such as 'Pulp Fiction' and 'Kill Bill'?",
"Quentin Tarantino",
        new String[]{"Martin Scorsese", "Quentin Tarantino", "Christopher Nolan", "Francis Ford Coppola"});







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
