package Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class Pokemon implements IPokemon, Serializable {
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private int id;
    private String name;
    private String imageUrl;
    private List<PokemonType> types;
    private Map<StatCategory, Integer> stats;

    // Collection to store multiple Pokemon instances
    private static List<Pokemon> pokemonCollection = new ArrayList<>();

    // Constructor
    public Pokemon(int id, String name, String imageUrl, List<PokemonType> types, Map<StatCategory, Integer> stats) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.types = types;
        this.stats = stats;
    }

    // Default constructor for Jackson deserialization
    public Pokemon() {
        // Required for Jackson
    }

    // Getters from interface
    @Override
    public int getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getImageUrl() { return imageUrl; }

    @Override
    public List<PokemonType> getTypes() { return types; }

    @Override
    public Map<StatCategory, Integer> getStats() { return stats; }

    // Setters for Jackson deserialization
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setTypes(List<PokemonType> types) { this.types = types; }
    public void setStats(Map<StatCategory, Integer> stats) { this.stats = stats; }

    // Collection Management
    public static List<Pokemon> getCollection() {
        return new ArrayList<>(pokemonCollection);
    }

    public static void clearCollection() {
        pokemonCollection.clear();
    }

    public static void addToCollection(Pokemon pokemon) {
        if (pokemon != null) {
            pokemonCollection.add(pokemon);
        }
    }

    public static void addAllToCollection(List<Pokemon> pokemon) {
        if (pokemon != null) {
            pokemonCollection.addAll(pokemon);
        }
    }

    public static boolean removeFromCollection(Pokemon pokemon) {
        return pokemonCollection.remove(pokemon);
    }

    // API Interaction Methods
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
        Map<StatCategory, Integer> stats = new EnumMap<>(StatCategory.class);
        jsonNode.get("stats").forEach(element -> {
            String statName = element.get("stat").get("name").asText();
            int statValue = element.get("base_stat").asInt();
            stats.put(StatCategory.fromApiName(statName), statValue);
        });

        return new Pokemon(id, name, imageUrl, types, stats);
    }

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

    // File Handling Methods
    public static void saveToJson(String filename) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filename), pokemonCollection);
    }

    public static void loadFromJson(String filename) throws IOException {
        List<Pokemon> loadedPokemon = objectMapper.readValue(
                new File(filename),
                new TypeReference<List<Pokemon>>() {}
        );

        // Replace current collection with loaded data
        clearCollection();
        addAllToCollection(loadedPokemon);
    }


    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", types=" + types +
                ", stats=" + stats +
                '}';
    }
}