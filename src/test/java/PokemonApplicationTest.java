import static org.junit.jupiter.api.Assertions.*;
import controller.PokemonController;
import model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PokemonApiService;

import java.io.File;
import java.util.List;


public class PokemonApplicationTest {
    private PokemonController controller;
    private PokemonApiService apiService;

    @BeforeEach
    public void setUp() {
        controller = new PokemonController();
        apiService = new PokemonApiService();
    }

    @Test
    public void testFetchPokemonCollection() {
        controller.fetchPokemonCollection(1, 20);
        List<Pokemon> collection = controller.getPokemonCollection();
        
        assertNotNull(collection);
        assertEquals(20, collection.size());
        
        // Verify first Pokemon
        Pokemon firstPokemon = collection.get(0);
        assertNotNull(firstPokemon);
        assertEquals(1, firstPokemon.getId());
        assertNotNull(firstPokemon.getName());
    }

    @Test
    public void testSearchPokemon() {
        controller.fetchPokemonCollection(1, 20);
        List<Pokemon> searchResults = controller.searchPokemon("pi");
        
        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.stream().allMatch(p -> p.getName().contains("pi")));
    }

    @Test
    public void testFilterPokemonByType() {
        controller.fetchPokemonCollection(1, 50);
        List<Pokemon> fireTypes = controller.filterPokemonByType("fire");
        
        assertFalse(fireTypes.isEmpty());
        assertTrue(fireTypes.stream().allMatch(p -> p.getTypes().contains("fire")));
    }

    @Test
    public void testSaveAndLoadCollection() throws Exception {
        controller.fetchPokemonCollection(1, 20);
        
        // Save to temporary file
        File tempFile = File.createTempFile("pokemon_test", ".json");
        controller.saveCollectionToJson(tempFile.getAbsolutePath());
        
        // Create new controller and load
        PokemonController newController = new PokemonController();
        newController.loadCollectionFromJson(tempFile.getAbsolutePath());
        
        List<Pokemon> loadedCollection = newController.getPokemonCollection();
        
        assertEquals(20, loadedCollection.size());
        
        // Clean up
        tempFile.delete();
    }

    @Test
    public void testApiServiceFetchSinglePokemon() throws Exception {
        Pokemon pokemon = apiService.fetchPokemonById(25); // Pikachu
        
        assertNotNull(pokemon);
        assertEquals(25, pokemon.getId());
        assertEquals("pikachu", pokemon.getName());
        assertNotNull(pokemon.getImageUrl());
        assertNotNull(pokemon.getTypes());
        assertNotNull(pokemon.getStats());
    }
}
