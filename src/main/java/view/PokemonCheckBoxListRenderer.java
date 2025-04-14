package view;

import javax.swing.*;
import java.awt.*;

import Model.Pokemon;
import Model.PokemonType;

public class PokemonCheckBoxListRenderer extends JCheckBox implements ListCellRenderer<CheckBoxListItem> {

    public PokemonCheckBoxListRenderer() {
        setOpaque(true);
        setFont(new Font("Arial", Font.PLAIN, 14)); // Smaller, consistent font
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends CheckBoxListItem> list,
            CheckBoxListItem value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        Pokemon pokemon = value.getPokemon();
        setText(pokemon.toString());
        setSelected(value.isSelected());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());

            // Apply type-based color tint
            Color typeColor = getTypeColor(pokemon.getTypes().get(0));
            setBackground(new Color(
                    typeColor.getRed(),
                    typeColor.getGreen(),
                    typeColor.getBlue(),
                    30
            ));
        }

        return this;
    }

    private Color getTypeColor(PokemonType type) {
        return switch (type) {
            case FIRE -> new Color(255, 100, 100);
            case WATER -> new Color(100, 100, 255);
            case GRASS -> new Color(100, 255, 100);
            case ELECTRIC -> new Color(255, 255, 100);
            default -> new Color(200, 200, 200);
        };
    }
}
