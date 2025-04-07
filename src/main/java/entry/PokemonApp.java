package entry;

import Model.IPokemon;
import Model.Pokemon;
import controller.IPokemonController;
import controller.PokemonController;
import view.MainPokemonFrame;
import view.IPokemonView;

public class PokemonApp {

    public static void main(String[] args) {
        PokemonApp app = new PokemonApp();
        app.initializeApplication();
    }

    private void initializeApplication() {
        // Create model
        IPokemon model = new Pokemon();

        // Create controller with model
        IPokemonController controller = new PokemonController(model);

        // Create view with controller
        IPokemonView view = new MainPokemonFrame(controller);

        // Set view in controller
        ((PokemonController)controller).setView(view);

        // Initial data fetch
        controller.fetchInitialPokemon(100);

        // Display the view
        view.display();
    }
}