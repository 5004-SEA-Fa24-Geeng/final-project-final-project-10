import model.Pokemon;
import service.PokemonApiService;
import service.FileHandlingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonController {
    private PokemonApiService apiService;
    private FileHandlingService fileService;
    private List<Pokemon> pokemonCollection;

    public PokemonController() {
        this.apiService = new PokemonApiService();
        this.fileService = new FileHandlingService();
        this.pokemonCollection = new ArrayList<>();
    }

    public void fetchPokemonCollection(int startId, int count) {
        this.pokemonCollection = apiService.fetchMultiplePokemon(startId, count);
    }

    public List<Pokemon> getPokemonCollection() {
        return new ArrayList<>(pokemonCollection);
    }

    public void saveCollectionToJson(String filename) throws IOException {
        fileService.saveToJson(pokemonCollection, filename);
    }

    public void saveCollectionToCSV(String filename) throws IOException {
        fileService.saveToCSV(pokemonCollection, filename);
    }

    public void loadCollectionFromJson(String filename) throws IOException {
        this.pokemonCollection = fileService.loadFromJson(filename);
    }

    public List<Pokemon> searchPokemon(String searchTerm) {
        return pokemonCollection.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Pokemon> sortPokemonByName() {
        return pokemonCollection.stream()
                .sorted(Comparator.comparing(Pokemon::getName))
                .collect(Collectors.toList());
    }

    public List<Pokemon> filterPokemonByType(String type) {
        return pokemonCollection.stream()
                .filter(p -> p.getTypes().contains(type.toLowerCase()))
                .collect(Collectors.toList());
    }
}
