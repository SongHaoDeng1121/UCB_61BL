package core;

public enum MenuState {
    MAIN_MENU,      // The initial menu with N/L/Q options
    SEED_INPUT,     // The screen where user enters a seed
    GAME_ACTIVE,    // The actual game is running
    GAME_LOADING    // Loading a saved game
}