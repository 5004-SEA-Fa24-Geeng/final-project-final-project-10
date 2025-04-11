package view;

import Model.Pokemon;

public class CheckBoxListItem {
    private final Pokemon pokemon;
    private boolean selected;
    
    public CheckBoxListItem(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.selected = false;
    }
    
    public Pokemon getPokemon() {
        return pokemon;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
} 