package Model;

public enum StatCategory {
    HP, ATTACK, DEFENSE, SPECIAL_ATTACK, SPECIAL_DEFENSE, SPEED;

    public static StatCategory fromApiName(String apiName) {
        return switch (apiName) {
            case "hp" -> HP;
            case "attack" -> ATTACK;
            case "defense" -> DEFENSE;
            case "special-attack" -> SPECIAL_ATTACK;
            case "special-defense" -> SPECIAL_DEFENSE;
            case "speed" -> SPEED;
            default -> throw new IllegalArgumentException("Unknown stat: " + apiName);
        };
    }
}
