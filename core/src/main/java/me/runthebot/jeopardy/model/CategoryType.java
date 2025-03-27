package me.runthebot.jeopardy.model;

/**
 * Enum representing the different question categories available in the game.
 * Each category has a display name that is shown to the user in the game interface.
 * This enum provides methods to convert between the internal enum values and their display names.
 */
public enum CategoryType {
    SCIENCE("Science"),
    HISTORY("History"),
    SPORTS("Sports"),
    MOVIES("Movies"),
    GEOGRAPHY("Geography");

    // The human-readable name of the category
    private final String displayName;

    /**
     * Creates a new category type with the specified display name.
     * @param displayName The human-readable name for this category
     */
    CategoryType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get the display name of the category.
     * @return The human-readable category name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Find a category by its display name.
     * The search is case-insensitive.
     * @param name The display name to search for
     * @return The matching CategoryType or null if not found
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
