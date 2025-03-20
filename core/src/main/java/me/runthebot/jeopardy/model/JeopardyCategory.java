package me.runthebot.jeopardy.model;

import java.util.ArrayList;
import java.util.List;

public class JeopardyCategory {
    private String name;
    private List<JeopardyQuestion> questions = new ArrayList<>();

    public JeopardyCategory() {}

    public JeopardyCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JeopardyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<JeopardyQuestion> questions) {
        this.questions = questions;
    }

    public void addQuestion(JeopardyQuestion question) {
        questions.add(question);
    }
}
