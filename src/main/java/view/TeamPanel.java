package view;

import Model.Pokemon;
import Model.PokemonType;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamPanel extends JPanel {
    public final List<Pokemon> teamMembers;
    private final JPanel cardsPanel;
    private final JButton clearButton;
    private final JButton returnButton;
    private Runnable onReturnToFullList;
    private Runnable onPokemonRemoved;
    
    public TeamPanel() {
        teamMembers = new ArrayList<>();
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Header with controls
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setOpaque(false);
        
        // Left side: Title and team count
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("My Team");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        headerPanel.add(titlePanel, BorderLayout.WEST);
        
        // Right side: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);
        
        clearButton = new JButton("Clear Team");
        clearButton.setEnabled(false);
        
        returnButton = new JButton("Return to Full List");
        returnButton.setVisible(false);
        
        buttonPanel.add(clearButton);
        buttonPanel.add(returnButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Cards panel with grid layout
        cardsPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        cardsPanel.setOpaque(false);
        add(new JScrollPane(cardsPanel), BorderLayout.CENTER);
        
        setOpaque(false);
    }
    
    private JPanel createPokemonCard(Pokemon pokemon) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(5, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Pokemon info panel (top)
        JPanel infoPanel = new JPanel(new BorderLayout(5, 2));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(pokemon.getName().toUpperCase());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        
        // Type badges
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        typePanel.setOpaque(false);
        pokemon.getTypes().forEach(type -> {
            JLabel typeLabel = new JLabel(type.name());
            typeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            typeLabel.setOpaque(true);
            typeLabel.setBackground(getTypeColor(type));
            typeLabel.setForeground(Color.WHITE);
            typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
            typePanel.add(typeLabel);
        });
        infoPanel.add(typePanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Remove button
        JButton removeButton = new JButton("×");
        removeButton.setFont(new Font("Arial", Font.BOLD, 12));
        removeButton.addActionListener(e -> {
            removePokemon(pokemon);
            if (onPokemonRemoved != null) {
                onPokemonRemoved.run();
            }
        });
        card.add(removeButton, BorderLayout.EAST);
        
        return card;
    }
    
    public void updateTeam(List<Pokemon> newTeam) {
        teamMembers.clear();
        teamMembers.addAll(newTeam);
        refreshDisplay();
    }
    
    private void removePokemon(Pokemon pokemon) {
        teamMembers.remove(pokemon);
        refreshDisplay();
    }
    
    private void refreshDisplay() {
        cardsPanel.removeAll();
        teamMembers.forEach(pokemon -> cardsPanel.add(createPokemonCard(pokemon)));
        clearButton.setEnabled(!teamMembers.isEmpty());
        revalidate();
        repaint();
    }
    
    public void setClearAction(Runnable action) {
        clearButton.addActionListener(e -> {
            teamMembers.clear();
            refreshDisplay();
            action.run();
        });
    }
    
    public void setTeamMode(boolean isTeamMode) {
        returnButton.setVisible(isTeamMode);
        revalidate();
        repaint();
    }
    
    public void setOnReturnToFullList(Runnable action) {
        onReturnToFullList = action;
        returnButton.addActionListener(e -> {
            if (onReturnToFullList != null) {
                onReturnToFullList.run();
            }
        });
    }
    
    public void setOnPokemonRemoved(Runnable action) {
        onPokemonRemoved = action;
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
    
    public List<Pokemon> getTeamMembers() {
        return new ArrayList<>(teamMembers);
    }
} 