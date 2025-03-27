import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Pokemon;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class FileHandlingService {
    private Gson gson;

    public FileHandlingService() {
        this.gson = new Gson();
    }

    public void saveToJson(List<Pokemon> pokemonList, String filename) throws IOException {
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(pokemonList, writer);
        }
    }

    public List<Pokemon> loadFromJson(String filename) throws IOException {
        try (Reader reader = new FileReader(filename)) {
            Type pokemonListType = new TypeToken<List<Pokemon>>(){}.getType();
            return gson.fromJson(reader, pokemonListType);
        }
    }

    public void saveToCSV(List<Pokemon> pokemonList, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // CSV Header
            writer.println("ID,Name,Types,HP,Attack,Defense,Special Attack,Special Defense,Speed");
            
            for (Pokemon pokemon : pokemonList) {
                writer.println(String.format("%d,%s,%s,%d,%d,%d,%d,%d,%d", 
                    pokemon.getId(), 
                    pokemon.getName(), 
                    String.join("|", pokemon.getTypes()),
                    pokemon.getStats().getHp(),
                    pokemon.getStats().getAttack(),
                    pokemon.getStats().getDefense(),
                    pokemon.getStats().getSpecialAttack(),
                    pokemon.getStats().getSpecialDefense(),
                    pokemon.getStats().getSpeed()
                ));
            }
        }
    }
}
