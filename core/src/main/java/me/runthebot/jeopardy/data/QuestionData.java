package me.runthebot.jeopardy.data;

import me.runthebot.jeopardy.model.CategoryType;

/**
 * Represents a single question in the Jeopardy game
 */
public class QuestionData {
    private CategoryType category;
    private int value;
    private String question;
    private String correctAnswer;
    private String[] choices;

    /**
     * Creates a new question data object
     *
     * @param category the category of the question
     * @param value the dollar value of the question
     * @param question the question text
     * @param correctAnswer the correct answer
     * @param choices the available choices
     */
    public QuestionData(CategoryType category, int value, String question, String correctAnswer, String[] choices) {
        this.category = category;
        this.value = value;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.choices = choices;
    }

    /**
     * @return the category of the question
     */
    public CategoryType getCategory() {
        return category;
    }

    /**
     * @return the dollar value of the question
     */
    public int getValue() {
        return value;
    }

    /**
     * @return the question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @return the correct answer
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * @return the available choices
     */
    public String[] getChoices() {
        return choices;
    }
}
