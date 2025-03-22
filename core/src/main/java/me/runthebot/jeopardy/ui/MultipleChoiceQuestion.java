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

/**
 * A component that displays a multiple choice question
 * related to a specific category and value.
 */
public class MultipleChoiceQuestion extends VisDialog {
    private String category;
    private int value;
    private String question;
    private String correctAnswer;
    private String[] choices;
    private boolean answered = false;
    private VisLabel questionLabel;
    private VisTextButton[] choiceButtons;

    // Timer related variables
    private static final float TOTAL_TIME = 20f; // 20 seconds to answer
    private float timeRemaining = TOTAL_TIME;
    private VisProgressBar timerBar;
    private VisLabel timerLabel;
    private boolean timerRunning = true;

    /**
     * Creates a new multiple choice question dialog
     *
     * @param category the category of the question
     * @param value the dollar value of the question
     */
    public MultipleChoiceQuestion(String category, int value) {
        super(category + " - $" + value);
        this.category = category;
        this.value = value;

        setModal(true);
        setMovable(false);
        setResizable(false);

        // Fetch the question (mock implementation for now)
        fetchQuestionFromAPI(category, value);

        // Create the UI
        createUI();
    }

    /**
     * Fetches a question from the API based on category and value.
     * Currently returns a static example.
     *
     * @param category the category to fetch a question for
     * @param value the value of the question to fetch
     *
     * TODO: Implement actual API integration
     */
    private void fetchQuestionFromAPI(String category, int value) {
        // TODO: Replace with actual API call

        // Mock data based on category
        if (category.equalsIgnoreCase("Science")) {
            this.question = "Which planet is known as the Red Planet?";
            this.correctAnswer = "Mars";
            this.choices = new String[]{"Venus", "Mars", "Jupiter", "Saturn"};
        }
        else if (category.equalsIgnoreCase("History")) {
            this.question = "Who was the first President of the United States?";
            this.correctAnswer = "George Washington";
            this.choices = new String[]{"Thomas Jefferson", "John Adams", "George Washington", "Abraham Lincoln"};
        }
        else if (category.equalsIgnoreCase("Sports")) {
            this.question = "Which sport uses the term 'Grand Slam'?";
            this.correctAnswer = "All of the above";
            this.choices = new String[]{"Tennis", "Baseball", "Golf", "All of the above"};
        }
        else if (category.equalsIgnoreCase("Movies")) {
            this.question = "Which actor played Iron Man in the Marvel movies?";
            this.correctAnswer = "Robert Downey Jr.";
            this.choices = new String[]{"Chris Evans", "Chris Hemsworth", "Robert Downey Jr.", "Mark Ruffalo"};
        }
        else {
            this.question = "What is the capital of France?";
            this.correctAnswer = "Paris";
            this.choices = new String[]{"London", "Berlin", "Paris", "Rome"};
        }
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
        VisTextButton closeButton = new VisTextButton("Close");
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
        contentTable.add(closeButton).padTop(20);

        add(contentTable);

        // Set the size and position
        setSize(500, 600); // Increased height to accommodate timer
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
        timerRunning = false; // Stop the timer

        // Check if the answer is correct
        boolean isCorrect = selectedAnswer.equals(correctAnswer);

        // Update UI to show result
        if (isCorrect) {
            // Correct answer - highlight in green
            choiceButtons[choiceIndex].setColor(Color.GREEN);

            // Replace question text with success message
            questionLabel.setText("CORRECT!");
            questionLabel.setColor(Color.GREEN);

            // Update timer area with reward information
            timerLabel.setText("+" + value);
            timerLabel.setColor(Color.GREEN);
            timerBar.setColor(Color.GREEN);

            // TODO: Set Score and make API call to update score

            Gdx.app.log("Question", "Correct answer! You earned $" + value);
        } else {
            // Wrong answer - highlight in red
            choiceButtons[choiceIndex].setColor(Color.RED);

            // Highlight the correct answer in green
            for (int i = 0; i < choices.length; i++) {
                if (choices[i].equals(correctAnswer)) {
                    choiceButtons[i].setColor(Color.GREEN);
                    break;
                }
            }

            // TODO: Set Score and make API call to update score

            // Replace question text with failure message
            questionLabel.setText("INCORRECT!\nThe correct answer was: " + correctAnswer);
            questionLabel.setColor(Color.RED);

            // Update timer area with negative feedback
            timerLabel.setText("$0");
            timerLabel.setColor(Color.RED);
            timerBar.setColor(Color.RED);

            Gdx.app.log("Question", "Wrong answer! The correct answer was: " + correctAnswer);
        }

        // Disable all buttons to prevent further selection
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

            // Show the correct answer
            for (int i = 0; i < choices.length; i++) {
                if (choices[i].equals(correctAnswer)) {
                    choiceButtons[i].setColor(Color.GREEN);
                    break;
                }
            }

            // Disable all buttons
            for (VisTextButton button : choiceButtons) {
                button.setDisabled(true);
            }

            // Replace question text with time's up message
            questionLabel.setText("TIME'S UP!\nThe correct answer was: " + correctAnswer);
            questionLabel.setColor(Color.RED);

            // Update timer text
            timerLabel.setText("Time's up!");
            timerLabel.setColor(Color.RED);

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
