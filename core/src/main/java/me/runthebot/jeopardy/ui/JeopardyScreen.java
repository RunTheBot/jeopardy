package me.runthebot.jeopardy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import me.runthebot.jeopardy.model.JeopardyCategory;
import me.runthebot.jeopardy.model.JeopardyGame;
import me.runthebot.jeopardy.model.JeopardyQuestion;

public class JeopardyScreen {
    private Stage stage;
    private JeopardyGame game;
    private VisTable mainTable;
    private int currentScore = 0;
    private VisLabel scoreLabel;

    public JeopardyScreen(Stage stage, JeopardyGame game) {
        this.stage = stage;
        this.game = game;

        buildUI();
    }

    private void buildUI() {
        mainTable = new VisTable();
        mainTable.setFillParent(true);

        // Title
        VisLabel titleLabel = new VisLabel(game.getTitle());
        titleLabel.setFontScale(2.0f);
        mainTable.add(titleLabel).colspan(game.getCategories().size()).pad(20).row();

        // Score display
        scoreLabel = new VisLabel("Score: " + currentScore);
        mainTable.add(scoreLabel).colspan(game.getCategories().size()).pad(10).row();

        // Categories
        VisTable categoriesTable = new VisTable();
        for (JeopardyCategory category : game.getCategories()) {
            VisLabel categoryLabel = new VisLabel(category.getName());
            categoryLabel.setAlignment(Align.center);
            categoryLabel.setWrap(true);
            categoriesTable.add(categoryLabel).width(150).height(60).pad(5);
        }
        mainTable.add(categoriesTable).colspan(game.getCategories().size()).row();

        // Questions Grid
        // Find the maximum number of questions in any category
        int maxQuestions = 0;
        for (JeopardyCategory category : game.getCategories()) {
            maxQuestions = Math.max(maxQuestions, category.getQuestions().size());
        }

        // Create rows for each value level
        for (int i = 0; i < maxQuestions; i++) {
            VisTable rowTable = new VisTable();

            for (JeopardyCategory category : game.getCategories()) {
                if (i < category.getQuestions().size()) {
                    JeopardyQuestion question = category.getQuestions().get(i);
                    final VisTextButton questionButton = new VisTextButton("$" + question.getValue());
                    questionButton.setUserObject(question);

                    questionButton.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            showQuestion((JeopardyQuestion) questionButton.getUserObject(), questionButton);
                        }
                    });

                    if (question.isAnswered()) {
                        questionButton.setDisabled(true);
                        questionButton.setText("");
                    }

                    rowTable.add(questionButton).width(150).height(80).pad(5);
                } else {
                    rowTable.add().width(150).height(80).pad(5);
                }
            }

            mainTable.add(rowTable).colspan(game.getCategories().size()).row();
        }

        stage.addActor(mainTable);
    }

    private void showQuestion(final JeopardyQuestion question, final VisTextButton button) {
        final VisWindow questionWindow = new VisWindow("Question for $" + question.getValue());
        questionWindow.setModal(true);

        VisLabel questionLabel = new VisLabel(question.getQuestion());
        questionLabel.setWrap(true);
        questionLabel.setAlignment(Align.center);

        VisTextButton showAnswerButton = new VisTextButton("Show Answer");
        showAnswerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showAnswer(question, button, questionWindow);
            }
        });

        questionWindow.add(questionLabel).width(400).pad(20).row();
        questionWindow.add(showAnswerButton).pad(10);

        questionWindow.pack();
        questionWindow.centerWindow();
        stage.addActor(questionWindow.fadeIn());
    }

    private void showAnswer(final JeopardyQuestion question, final VisTextButton button, final VisWindow questionWindow) {
        final VisWindow answerWindow = new VisWindow("Answer");
        answerWindow.setModal(true);

        VisLabel answerLabel = new VisLabel(question.getAnswer());
        answerLabel.setWrap(true);
        answerLabel.setAlignment(Align.center);

        VisTable buttonsTable = new VisTable();

        VisTextButton correctButton = new VisTextButton("Correct");
        correctButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentScore += question.getValue();
                updateScore();
                questionCompleted(question, button);
                answerWindow.fadeOut();
                questionWindow.fadeOut();
            }
        });

        VisTextButton incorrectButton = new VisTextButton("Incorrect");
        incorrectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentScore -= question.getValue();
                updateScore();
                questionCompleted(question, button);
                answerWindow.fadeOut();
                questionWindow.fadeOut();
            }
        });

        buttonsTable.add(correctButton).pad(10);
        buttonsTable.add(incorrectButton).pad(10);

        answerWindow.add(answerLabel).width(400).pad(20).row();
        answerWindow.add(buttonsTable).pad(10);

        answerWindow.pack();
        answerWindow.centerWindow();
        stage.addActor(answerWindow.fadeIn());
    }

    private void questionCompleted(JeopardyQuestion question, VisTextButton button) {
        question.setAnswered(true);
        button.setDisabled(true);
        button.setText("");

        // Check if all questions are answered
        boolean allAnswered = true;
        for (JeopardyCategory category : game.getCategories()) {
            for (JeopardyQuestion q : category.getQuestions()) {
                if (!q.isAnswered()) {
                    allAnswered = false;
                    break;
                }
            }
            if (!allAnswered) break;
        }

        if (allAnswered) {
            showGameOver();
        }
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + currentScore);
    }

    private void showGameOver() {
        Dialogs.showOKDialog(stage, "Game Over", "Final Score: " + currentScore);
    }
}
