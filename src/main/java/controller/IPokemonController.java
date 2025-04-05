package controller;

import Model.Pokemon;
import Model.PokemonType;

import java.util.List;

public interface IPokemonController {
    /**
     * Fetches initial pokemon data
     * @param count Number of Pokemon to fetch
     */
    void fetchInitialPokemon(int count);

    /**
     * Get the current pokemon collection
     * @return List of Pokemon
     */
    List<Pokemon> getPokemonCollection();

    /**
     * Save the current collection to a JSON file
     * @param filename The file to save to
     */
    void saveCollection(String filename);

    /**
     * Load a collection from a JSON file
     * @param filename The file to load from
     */
    void loadCollection(String filename);

    /**
     * Search for Pokemon by name
     * @param searchTerm The search term
     * @return Filtered list of Pokemon
     */
    List<Pokemon> searchPokemon(String searchTerm);

    /**
     * Filter Pokemon by type
     * @param type The Pokemon type to filter by
     * @return Filtered list of Pokemon
     */
    List<Pokemon> filterPokemonByType(PokemonType type);

    /**
     * Get a specific Pokemon by ID
     * @param id The Pokemon ID
     * @return The Pokemon if found, null otherwise
     */
    Pokemon getPokemonById(int id);
}