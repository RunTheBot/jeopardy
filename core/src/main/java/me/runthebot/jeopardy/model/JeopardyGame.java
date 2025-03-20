package me.runthebot.jeopardy.model;

import java.util.ArrayList;
import java.util.List;

public class JeopardyGame {
    private String title;
    private List<JeopardyCategory> categories = new ArrayList<>();

    public JeopardyGame() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<JeopardyCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<JeopardyCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(JeopardyCategory category) {
        categories.add(category);
    }
}
