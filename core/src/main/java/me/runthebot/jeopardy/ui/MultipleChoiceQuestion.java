package me.runthebot.jeopardy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisProgressBar;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import me.runthebot.jeopardy.screens.GameScreen;
import me.runthebot.jeopardy.data.QuestionData;
import me.runthebot.jeopardy.data.QuestionManager;
import me.runthebot.jeopardy.model.CategoryType;

/**
 * A component that displays a multiple choice question
 * related to a specific category and value.
 */
public class MultipleChoiceQuestion extends VisDialog {
    private CategoryType category;
    private int value;
    private String question;
    private String correctAnswer;
    private String[] choices;
    private boolean answered = false;
    private VisLabel questionLabel;
    private VisTextButton[] choiceButtons;
    private VisTextButton closeButton;
    private GameScreen gameScreen;

    // Timer related variables
    private static final float TOTAL_TIME = 20f; // 20 seconds to answer
    private float timeRemaining = TOTAL_TIME;
    private VisProgressBar timerBar;
    private VisLabel timerLabel;
    private boolean timerRunning = true;

    /**
     * Creates a new multiple choice question dialog
     *
     * @param categoryName the display name of the category
     * @param value the dollar value of the question
     */
    public MultipleChoiceQuestion(String categoryName, int value, GameScreen gameScreen) {
        super(categoryName + " - $" + value);
        this.category = CategoryType.fromDisplayName(categoryName);
        this.value = value;
        this.gameScreen = gameScreen;

        setModal(true);
        setMovable(false);
        setResizable(false);

        // Load the question
        loadQuestion(category, value);

        // Create the UI
        createUI();
    }

    /**
     * Loads a question based on category and value.
     *
     * @param category the category to fetch a question for
     * @param value the value of the question to fetch
     */
    private void loadQuestion(CategoryType category, int value) {
        // Get question data from the QuestionManager
        QuestionData questionData = QuestionManager.getQuestion(category, value);

        this.question = questionData.getQuestion();
        this.correctAnswer = questionData.getCorrectAnswer();
        this.choices = questionData.getChoices();
    }

    /**
     * Creates the UI for the question dialog
     */
    private void createUI() {
        VisTable contentTable = new VisTable();
        contentTable.pad(20);

        // Add timer components
        VisTable timerTable = new VisTable();

        // Create progress bar for timer
        timerBar = new VisProgressBar(0f, 1f, 0.01f, false);
        timerBar.setValue(1f); // Start full

        // Create timer label - Fix: use Integer.toString instead of String.format
        timerLabel = new VisLabel(Integer.toString((int)timeRemaining));

        // Add components to timer table
        timerTable.add(timerLabel).padRight(10);
        timerTable.add(timerBar).growX().height(15);

        // Add timer to content table
        contentTable.add(timerTable).growX().padBottom(20).row();

        // Add question text
        questionLabel = new VisLabel(question, Align.center);
        questionLabel.setWrap(true);
        contentTable.add(questionLabel).width(400).padBottom(30).row();

        // Add multiple choice options
        choiceButtons = new VisTextButton[choices.length];
        for (int i = 0; i < choices.length; i++) {
            final int choiceIndex = i;
            final String choice = choices[i];

            VisTextButton choiceButton = new VisTextButton(choice);
            choiceButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (!answered) {
                        handleAnswer(choiceIndex, choice);
                    }
                }
            });

            choiceButtons[i] = choiceButton;
            contentTable.add(choiceButton).width(350).padBottom(10).row();
        }

        // Add a close button at the bottom
        closeButton = new VisTextButton("Close");
        closeButton.setDisabled(true);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        contentTable.add(closeButton).padTop(20);

        add(contentTable);

        // Set the size and position
        setSize(500, 600);
        centerWindow();
    }

    /**
     * Handles the user's answer selection
     *
     * @param choiceIndex the index of the selected choice
     * @param selectedAnswer the text of the selected answer
     */
    private void handleAnswer(int choiceIndex, String selectedAnswer) {
        answered = true;
        timerRunning = false;
        closeButton.setDisabled(false);

        boolean isCorrect = selectedAnswer.equals(correctAnswer);

        if (isCorrect) {
            choiceButtons[choiceIndex].setColor(Color.GREEN);
            questionLabel.setText("CORRECT!");
            questionLabel.setColor(Color.GREEN);
            timerLabel.setText("+" + value);
            timerLabel.setColor(Color.GREEN);
            timerBar.setColor(Color.GREEN);

            // Update the score in GameScreen
            gameScreen.updateScore(value);

            Gdx.app.log("Question", "Correct answer! You earned $" + value);
        } else {
            choiceButtons[choiceIndex].setColor(Color.RED);

            for (int i = 0; i < choices.length; i++) {
                if (choices[i].equals(correctAnswer)) {
                    choiceButtons[i].setColor(Color.GREEN);
                    break;
                }
            }

            questionLabel.setText("INCORRECT!\nThe correct answer was: " + correctAnswer);
            questionLabel.setColor(Color.RED);
            timerLabel.setText("$0");
            timerLabel.setColor(Color.RED);
            timerBar.setColor(Color.RED);

            // Update score with 0 points for wrong answer
            gameScreen.updateScore(0);

            Gdx.app.log("Question", "Wrong answer! The correct answer was: " + correctAnswer);
        }

        for (VisTextButton button : choiceButtons) {
            button.setDisabled(true);
        }
    }

    /**
     * Handle time expired event
     */
    private void handleTimeExpired() {
        if (!answered) {
            answered = true;
            closeButton.setDisabled(false);

            for (int i = 0; i < choices.length; i++) {
                if (choices[i].equals(correctAnswer)) {
                    choiceButtons[i].setColor(Color.GREEN);
                    break;
                }
            }

            for (VisTextButton button : choiceButtons) {
                button.setDisabled(true);
            }

            questionLabel.setText("TIME'S UP!\nThe correct answer was: " + correctAnswer);
            questionLabel.setColor(Color.RED);
            timerLabel.setText("Time's up!");
            timerLabel.setColor(Color.RED);

            // Update score with 0 points for time expired
            gameScreen.updateScore(0);

            Gdx.app.log("Question", "Time expired! The correct answer was: " + correctAnswer);
        }
    }

    /**
     * Update the timer each frame
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        if (timerRunning && !answered) {
            // Update remaining time
            timeRemaining -= delta;

            if (timeRemaining <= 0) {
                // Time's up
                timeRemaining = 0;
                timerRunning = false;
                handleTimeExpired();
            }

            // Update progress bar
            float progress = timeRemaining / TOTAL_TIME;
            timerBar.setValue(progress);

            // Update timer text - Fix: use Integer.toString instead of String.format
            timerLabel.setText(Integer.toString((int)timeRemaining));

            // Update colors based on time remaining
            if (timeRemaining <= 5) {
                timerBar.setColor(Color.RED);
                timerLabel.setColor(Color.RED);
            } else if (timeRemaining <= 10) {
                timerBar.setColor(Color.ORANGE);
            } else {
                timerBar.setColor(Color.GREEN);
            }
        }
    }

    /**
     * Checks if the question has been answered
     *
     * @return true if the question has been answered, false otherwise
     */
    public boolean isAnswered() {
        return answered;
    }
}
