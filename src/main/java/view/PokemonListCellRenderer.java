package view;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import model.Pokemon;
import model.PokemonType;

public class PokemonListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof Pokemon pokemon) {
            // Get primary type color
            Color typeColor = getTypeColor(pokemon.getTypes().get(0));
            
            // Create gradient background
            if (!isSelected) {
                setBackground(new Color(
                    typeColor.getRed(), 
                    typeColor.getGreen(), 
                    typeColor.getBlue(), 
                    30  // Very light tint
                ));
            }
            
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            
            setFont(new Font("Arial", Font.BOLD, 16));
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