package controller;

import Model.Pokemon;
import Model.PokemonType;
import service.PokemonApiService;
import service.FileHandlingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonController implements IPokemonController {
    private PokemonApiService apiService;
    private FileHandlingService fileService;
    private List<Pokemon> currentPokemonList;

    public PokemonController() {
        this.apiService = new PokemonApiService();
        this.fileService = new FileHandlingService();
        this.currentPokemonList = new ArrayList<>();
    }

    @Override
    public void fetchInitialPokemon(int count) {
        this.currentPokemonList = apiService.fetchMultiplePokemon(1, count);
    }

    @Override
    public List<Pokemon> getPokemonCollection() {
        return new ArrayList<>(currentPokemonList);
    }

    @Override
    public void saveCollection(String filename) {
        try {
            fileService.saveToJson(currentPokemonList, filename);
        } catch (IOException e) {
            System.err.println("Error saving collection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void loadCollection(String filename) {
        try {
            this.currentPokemonList = fileService.loadFromJson(filename);
        } catch (IOException e) {
            System.err.println("Error loading collection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Pokemon> searchPokemon(String searchTerm) {
        return currentPokemonList.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Pokemon> sortPokemonByName() {
        return currentPokemonList.stream()
                .sorted(Comparator.comparing(Pokemon::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Pokemon> filterPokemonByType(PokemonType type) {
        return currentPokemonList.stream()
                .filter(p -> p.getTypes().contains(type))
                .collect(Collectors.toList());
    }

    @Override
    public Pokemon getPokemonById(int id) {
        return currentPokemonList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}