package service;

import Model.Pokemon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonApiService {
    private static final String POKEAPI_BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private HttpClient httpClient;
    private Gson gson;

    public PokemonApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    // to do udpate data init
    public Pokemon fetchPokemonById(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(POKEAPI_BASE_URL + id))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

        List<String> types = jsonObject.getAsJsonArray("types").asList().stream()
                .map(type -> type.getAsJsonObject().get("type").getAsJsonObject().get("name").getAsString())
                .collect(Collectors.toList());

        JsonObject statsJson = jsonObject.getAsJsonArray("stats").asList().get(0).getAsJsonObject();
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(
                jsonObject.getAsJsonArray("stats").get(0).getAsJsonObject().get("base_stat").getAsInt(),
                jsonObject.getAsJsonArray("stats").get(1).getAsJsonObject().get("base_stat").getAsInt(),
                jsonObject.getAsJsonArray("stats").get(2).getAsJsonObject().get("base_stat").getAsInt(),
                jsonObject.getAsJsonArray("stats").get(3).getAsJsonObject().get("base_stat").getAsInt(),
                jsonObject.getAsJsonArray("stats").get(4).getAsJsonObject().get("base_stat").getAsInt(),
                jsonObject.getAsJsonArray("stats").get(5).getAsJsonObject().get("base_stat").getAsInt()
        );

        return new Pokemon(
                id,
                jsonObject.get("name").getAsString(),
                jsonObject.getAsJsonObject("sprites").get("front_default").getAsString(),
                types,
                stats
        );
    }

    public List<Pokemon> fetchMultiplePokemon(int startId, int count) {
        List<Pokemon> pokemonList = new ArrayList<>();
        for (int i = startId; i < startId + count; i++) {
            try {
                pokemonList.add(fetchPokemonById(i));
            } catch (Exception e) {
                System.err.println("Error fetching Pokemon " + i + ": " + e.getMessage());
            }
        }
        return pokemonList;
    }
}