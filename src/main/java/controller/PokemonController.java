package controller;

import Model.Pokemon;
import Model.PokemonType;
import Model.IPokemon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PokemonController implements IPokemonController {
    private IPokemon model;
    private List<Pokemon> currentPokemonList;
    private IPokemon view;

    /**
     * Constructor for the PokemonController
     * @param model The model to use for data operations
     */
    public PokemonController(IPokemon model) {
        this.model = model;
        this.currentPokemonList = new ArrayList<>();
    }

    /**
     * Sets the view for this controller
     * @param view The view to update
     */
    public void setView(IPokemon view) {
        this.view = view;
    }

    @Override
    public void fetchInitialPokemon(int count) {
        this.currentPokemonList = model.fetchMultiplePokemon(count);
        if (view != null) {
            view.updatePokemonList(currentPokemonList);
        }
    }

    @Override
    public List<Pokemon> getPokemonCollection() {
        return new ArrayList<>(currentPokemonList);
    }

    @Override
    public void saveCollection(String filename) {
        model.saveCollection(currentPokemonList, filename);
    }

    @Override
    public void loadCollection(String filename) {
        this.currentPokemonList = model.loadCollection(filename);
        if (view != null) {
            view.updatePokemonList(currentPokemonList);
        }
    }

    @Override
    public List<Pokemon> searchPokemon(String searchTerm) {
        List<Pokemon> results = currentPokemonList.stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        return results;
    }

    /**
     * Sorts the current Pokemon list by name
     * @return Sorted list of Pokemon
     */
    public List<Pokemon> sortPokemonByName() {
        List<Pokemon> sortedList = currentPokemonList.stream()
                .sorted(Comparator.comparing(Pokemon::getName))
                .collect(Collectors.toList());

        return sortedList;
    }

    @Override
    public List<Pokemon> filterPokemonByType(PokemonType type) {
        List<Pokemon> filteredList = currentPokemonList.stream()
                .filter(p -> p.getTypes().contains(type))
                .collect(Collectors.toList());

        return filteredList;
    }

    @Override
    public Pokemon getPokemonById(int id) {
        return currentPokemonList.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}