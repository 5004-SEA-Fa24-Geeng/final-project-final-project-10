package entry;

import controller.IPokemonController;
import controller.PokemonController;
import Model.IPokemonModel;
import Model.PokemonModel;
import view.IPokemonView;
import view.MainPokemonFrame;

/**
 * Main entry point for the Pokemon application.
 * Creates and connects the Model, View, and Controller components.
 */
public class PokemonApp {

    /**
     * Main method that launches the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        PokemonApp app = new PokemonApp();
        app.initializeApplication();
    }

    /**
     * Initializes the components of the application and starts it.
     * Creates Model, Controller, and View instances and connects them.
     * Loads initial Pokemon data.
     */
//    private void initializeApplication() {
//        // Create model
//        IPokemonModel model = new PokemonModel();
//
//        // Create controller with reference to model
//        IPokemonController controller = new PokemonController(model);
//
//        // Create view with reference to controller
//        IPokemonView view = new MainPokemonFrame(controller);
//
//        // Fetch initial data
//        System.out.println("Loading Pokemon data, please wait...");
//        controller.fetchInitialPokemon(100); // Load first 100 Pokemon
//
//        // Display the view
//        view.display();
//
//        System.out.println("Application initialized successfully!");
//    }
}