package Model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing collections of Pokemon objects and interacting with the PokeAPI.
 */
public class PokemonCollection {
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Collection to store multiple Pokemon instances
    private static List<Pokemon> pokemonCollection = new ArrayList<>();

    /**
     * Returns a copy of the current Pokemon collection.
     *
     * @return list of Pokemon objects
     */
    public static List<Pokemon> getCollection() {
        return new ArrayList<>(pokemonCollection);
    }

    /**
     * Clears the current Pokemon collection.
     */
    public static void clearCollection() {
        pokemonCollection.clear();
    }

    /**
     * Adds a Pokemon to the collection.
     *
     * @param pokemon the Pokemon to add
     */
    public static void addToCollection(Pokemon pokemon) {
        if (pokemon != null) {
            pokemonCollection.add(pokemon);
        }
    }

    /**
     * Adds multiple Pokemon to the collection.
     *
     * @param pokemon list of Pokemon to add
     */
    public static void addAllToCollection(List<Pokemon> pokemon) {
        if (pokemon != null) {
            pokemonCollection.addAll(pokemon);
        }
    }

    /**
     * Removes a Pokemon from the collection.
     *
     * @param pokemon the Pokemon to remove
     * @return true if the Pokemon was removed, false otherwise
     */
    public static boolean removeFromCollection(Pokemon pokemon) {
        return pokemonCollection.remove(pokemon);
    }

    /**
     * Fetches a Pokemon by its ID from the PokeAPI.
     *
     * @param id the Pokemon ID
     * @return the Pokemon object
     * @throws IOException if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    public static Pokemon fetchById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(POKEAPI_BASE_URL + id))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode jsonNode = objectMapper.readTree(response.body());

        // Extract name
        String name = jsonNode.get("name").asText();

        // Extract image URL
        String imageUrl = jsonNode.get("sprites").get("front_default").asText();

        // Extract types
        List<PokemonType> types = new ArrayList<>();
        jsonNode.get("types").forEach(type -> {
            String typeName = type.get("type").get("name").asText();
            types.add(PokemonType.fromApiName(typeName));
        });

        // Extract stats
        int hp = 0, attack = 0, defense = 0, specialAttack = 0, specialDefense = 0, speed = 0;

        JsonNode statsNode = jsonNode.get("stats");
        for (JsonNode statNode : statsNode) {
            String statName = statNode.get("stat").get("name").asText();
            int value = statNode.get("base_stat").asInt();

            switch (statName) {
                case "hp":
                    hp = value;
                    break;
                case "attack":
                    attack = value;
                    break;
                case "defense":
                    defense = value;
                    break;
                case "special-attack":
                    specialAttack = value;
                    break;
                case "special-defense":
                    specialDefense = value;
                    break;
                case "speed":
                    speed = value;
                    break;
            }
        }

        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(
                hp, attack, defense, specialAttack, specialDefense, speed
        );

        return new Pokemon(id, name, imageUrl, types, stats);
    }

    /**
     * Fetches multiple Pokemon by ID range.
     *
     * @param startId the starting ID
     * @param count the number of Pokemon to fetch
     * @return list of fetched Pokemon
     */
    public static List<Pokemon> fetchMultiple(int startId, int count) {
        List<Pokemon> result = new ArrayList<>();
        for (int i = startId; i < startId + count; i++) {
            try {
                Pokemon pokemon = fetchById(i);
                result.add(pokemon);
                addToCollection(pokemon);
            } catch (Exception e) {
                System.err.println("Error fetching Pokemon " + i + ": " + e.getMessage());
            }
        }
        return result;
    }

    /**
     * Saves the current Pokemon collection to a JSON file.
     *
     * @param filename the file to save to
     * @throws IOException if an I/O error occurs
     */
    public static void saveToJson(String filename) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filename), pokemonCollection);
    }

    /**
     * Loads Pokemon from a JSON file into the collection.
     *
     * @param filename the file to load from
     * @throws IOException if an I/O error occurs
     */
    public static void loadFromJson(String filename) throws IOException {
        List<Pokemon> loadedPokemon = objectMapper.readValue(
                new File(filename),
                new TypeReference<List<Pokemon>>() {}
        );

        // Replace current collection with loaded data
        clearCollection();
        addAllToCollection(loadedPokemon);
    }
}