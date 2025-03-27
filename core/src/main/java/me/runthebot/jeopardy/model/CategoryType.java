package me.runthebot.jeopardy.model;

/**
 * Enum representing the different question categories available in the game.
 */
public enum CategoryType {
    SCIENCE("Science"),
    HISTORY("History"),
    SPORTS("Sports"),
    MOVIES("Movies"),
    GEOGRAPHY("Geography");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name of the category.
     *
     * @return The human-readable category name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Find a category by its display name.
     *
     * @param name The display name to search for.
     * @return The matching CategoryType or null if not found.
     */
    public static CategoryType fromDisplayName(String name) {
        for (CategoryType category : values()) {
            if (category.displayName.equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
