package model;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PokemonModel.
 * This test suite uses a subclass of PokemonModel to override the HTTP-dependent methods
 * to avoid requiring network connections during tests.
 */
public class PokemonModelTest {

    private PokemonModel pokemonModel;
    private ObjectMapper objectMapper;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Create a test instance that overrides network-dependent methods
        pokemonModel = new TestPokemonModel();
        objectMapper = new ObjectMapper();
    }

    /**
     * Test implementation of PokemonModel that overrides network-dependent methods.
     */
    private static class TestPokemonModel extends PokemonModel {
        @Override
        public Pokemon fetchPokemonById(int id) throws IOException, InterruptedException {
            // For testing, return predefined Pokemon without making HTTP calls
            if (id == 1) {
                return createTestBulbasaur();
            } else if (id == 2) {
                return createTestIvysaur();
            } else {
                throw new IOException("Pokemon not found with id: " + id);
            }
        }
    }

    /**
     * Creates a test Bulbasaur Pokemon object.
     */
    private static Pokemon createTestBulbasaur() {
        List<PokemonType> types = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(45, 49, 49, 65, 65, 45);
        return new Pokemon(1, "bulbasaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", types, stats);
    }

    /**
     * Creates a test Ivysaur Pokemon object.
     */
    private static Pokemon createTestIvysaur() {
        List<PokemonType> types = Arrays.asList(PokemonType.GRASS, PokemonType.POISON);
        Pokemon.PokemonStats stats = new Pokemon.PokemonStats(60, 62, 63, 80, 80, 60);
        return new Pokemon(2, "ivysaur", "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/2.png", types, stats);
    }

    @Test
    void testFetchPokemonById() throws IOException, InterruptedException {
        // Test fetching a Pokemon by ID
        Pokemon bulbasaur = pokemonModel.fetchPokemonById(1);

        // Verify the Pokemon properties
        assertEquals(1, bulbasaur.getId());
        assertEquals("bulbasaur", bulbasaur.getName());
        assertEquals("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png", bulbasaur.getImageUrl());
        assertEquals(2, bulbasaur.getTypes().size());
        assertTrue(bulbasaur.getTypes().contains(PokemonType.GRASS));
        assertTrue(bulbasaur.getTypes().contains(PokemonType.POISON));

        // Verify stats
        assertEquals(45, bulbasaur.getStats().getHp());
        assertEquals(49, bulbasaur.getStats().getAttack());
        assertEquals(49, bulbasaur.getStats().getDefense());
        assertEquals(65, bulbasaur.getStats().getSpecialAttack());
        assertEquals(65, bulbasaur.getStats().getSpecialDefense());
        assertEquals(45, bulbasaur.getStats().getSpeed());
    }

    @Test
    void testFetchPokemonByIdNotFound() {
        // Test fetching a Pokemon with an invalid ID
        Exception exception = assertThrows(IOException.class, () -> {
            pokemonModel.fetchPokemonById(999);
        });

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testFetchMultiplePokemon() throws IOException, InterruptedException {
        // Test fetching multiple Pokemon
        List<Pokemon> pokemon = pokemonModel.fetchMultiplePokemon(2);

        // Verify the list size and Pokemon properties
        assertEquals(2, pokemon.size());

        // Verify first Pokemon (Bulbasaur)
        Pokemon bulbasaur = pokemon.get(0);
        assertEquals(1, bulbasaur.getId());
        assertEquals("bulbasaur", bulbasaur.getName());

        // Verify second Pokemon (Ivysaur)
        Pokemon ivysaur = pokemon.get(1);
        assertEquals(2, ivysaur.getId());
        assertEquals("ivysaur", ivysaur.getName());
    }

    @Test
    void testSaveAndLoadCollection() throws IOException {
        // Create some test Pokemon
        List<Pokemon> pokemonList = Arrays.asList(createTestBulbasaur(), createTestIvysaur());

        // Create a temporary file for testing
        File tempFile = tempDir.resolve("testCollection.json").toFile();
        String filename = tempFile.getAbsolutePath();

        // Save the collection
        pokemonModel.saveCollection(pokemonList, filename);

        // Verify the file exists
        assertTrue(tempFile.exists());

        // Load the collection back
        List<Pokemon> loadedPokemon = pokemonModel.loadCollection(filename);

        // Verify the loaded collection
        assertEquals(2, loadedPokemon.size());

        // Check first Pokemon
        Pokemon bulbasaur = loadedPokemon.get(0);
        assertEquals(1, bulbasaur.getId());
        assertEquals("bulbasaur", bulbasaur.getName());
        assertEquals(2, bulbasaur.getTypes().size());

        // Check second Pokemon
        Pokemon ivysaur = loadedPokemon.get(1);
        assertEquals(2, ivysaur.getId());
        assertEquals("ivysaur", ivysaur.getName());
    }

    @Test
    void testSaveCollectionWithoutJsonExtension() throws IOException {
        // Test saving with a filename that doesn't have .json extension
        List<Pokemon> pokemonList = Arrays.asList(createTestBulbasaur());

        // Create a filename without .json extension
        File tempFile = tempDir.resolve("testCollection").toFile();
        String filename = tempFile.getAbsolutePath();

        // Save the collection
        pokemonModel.saveCollection(pokemonList, filename);

        // Verify that the file was created with .json extension
        File expectedFile = new File(filename + ".json");
        assertTrue(expectedFile.exists());

        // Load the file to verify content
        List<Pokemon> loadedPokemon = pokemonModel.loadCollection(filename);
        assertEquals(1, loadedPokemon.size());
        assertEquals("bulbasaur", loadedPokemon.get(0).getName());
    }

    @Test
    void testSaveEmptyCollection() {
        // Test saving an empty collection
        List<Pokemon> emptyList = new ArrayList<>();

        // Create a temporary file
        File tempFile = tempDir.resolve("emptyCollection.json").toFile();
        String filename = tempFile.getAbsolutePath();

        // Attempt to save an empty collection should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.saveCollection(emptyList, filename);
        });

        assertTrue(exception.getMessage().contains("Cannot save empty or null"));
    }

    @Test
    void testSaveNullCollection() {
        // Test saving a null collection
        List<Pokemon> nullList = null;

        // Create a temporary file
        File tempFile = tempDir.resolve("nullCollection.json").toFile();
        String filename = tempFile.getAbsolutePath();

        // Attempt to save a null collection should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.saveCollection(nullList, filename);
        });

        assertTrue(exception.getMessage().contains("Cannot save empty or null"));
    }

    @Test
    void testLoadNonExistentFile() {
        // Test loading a file that doesn't exist
        String nonExistentFile = tempDir.resolve("nonExistent.json").toString();

        // Attempt to load a non-existent file should throw an exception
        Exception exception = assertThrows(IOException.class, () -> {
            pokemonModel.loadCollection(nonExistentFile);
        });

        assertTrue(exception.getMessage().contains("File does not exist"));
    }

    @Test
    void testLoadWithEmptyFilename() {
        // Test loading with an empty filename
        String emptyFilename = "";

        // Attempt to load with an empty filename should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.loadCollection(emptyFilename);
        });

        assertTrue(exception.getMessage().contains("Filename cannot be null or empty"));
    }

    @Test
    void testLoadWithNullFilename() {
        // Test loading with a null filename
        String nullFilename = null;

        // Attempt to load with a null filename should throw an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            pokemonModel.loadCollection(nullFilename);
        });

        assertTrue(exception.getMessage().contains("Filename cannot be null or empty"));
    }
}