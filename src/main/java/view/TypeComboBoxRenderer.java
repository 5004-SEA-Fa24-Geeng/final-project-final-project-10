package view;

import Model.PokemonType;
import javax.swing.*;
import java.awt.*;

public class TypeComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value == null) {
            setText("All Types");
        } else if (value instanceof PokemonType) {
            setText(((PokemonType) value).name());
        }
        return this;
    }
} 