import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {
    private int id;
    private String name;
    private String imageUrl;
    private List<String> types;
    private PokemonStats stats;

    // Constructor
    public Pokemon(int id, String name, String imageUrl, List<String> types, PokemonStats stats) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.types = types;
        this.stats = stats;
    }

    // Nested Stats Class
    public static class PokemonStats implements Serializable {
        private int hp;
        private int attack;
        private int defense;
        private int specialAttack;
        private int specialDefense;
        private int speed;

        public PokemonStats(int hp, int attack, int defense, 
                            int specialAttack, int specialDefense, int speed) {
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.specialAttack = specialAttack;
            this.specialDefense = specialDefense;
            this.speed = speed;
        }

        // Getters
        public int getHp() { return hp; }
        public int getAttack() { return attack; }
        public int getDefense() { return defense; }
        public int getSpecialAttack() { return specialAttack; }
        public int getSpecialDefense() { return specialDefense; }
        public int getSpeed() { return speed; }
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public List<String> getTypes() { return types; }
    public PokemonStats getStats() { return stats; }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", types=" + types +
                '}';
    }
}
