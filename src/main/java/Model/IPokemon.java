package Model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IPokemon {
    // Basic property getters
    int getId();
    String getName();
    String getImageUrl();
    List<PokemonType> getTypes();
    Map<StatCategory, Integer> getStats();

    // Static Collection Management Methods (defined in implementing class)
    static List<Pokemon> getCollection() { return null; }
    static void clearCollection() {}
    static void addToCollection(Pokemon pokemon) {}
    static void addAllToCollection(List<Pokemon> pokemon) {}
    static boolean removeFromCollection(Pokemon pokemon) { return false; }

    // API Interaction Methods (defined in implementing class)
    static Pokemon fetchById(int id) throws IOException, InterruptedException { return null; }
    static List<Pokemon> fetchMultiple(int startId, int count) { return null; }

    // File Handling Methods (defined in implementing class)
    static void saveToJson(String filename) throws IOException {}
    static void loadFromJson(String filename) throws IOException {}

}
