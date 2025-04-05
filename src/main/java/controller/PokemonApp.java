package controller;


import model.IPokemon;
import model.Pokemon;

public class PokemonApp {

    public static void main(String[] args) {
        PokemonApp app = new PokemonApp();
        app.initializeApplication();
    }

    private void initializeApplication() {
        // Create model
        IPokemon model = new Pokemon();

        // Create controller
        IPokemonController controller = new PokemonController();

        //to do update view
//        // Create view
//        IPokemonView view = new MainPokemonFrame(controller);
//
//        // Initial data fetch
//        controller.fetchInitialPokemon(20);
//
//        // Display the view
//        view.display();
    }
}