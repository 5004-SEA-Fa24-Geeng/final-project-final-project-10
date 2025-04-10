package view;

import Model.Pokemon;
import controller.IPokemonController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main frame for the Pokemon application.
 * Implements the IPokemonView interface and acts as the primary UI container.
 */
public class MainPokemonFrame extends JFrame implements IPokemonView {

    private IPokemonController controller;

    // Uncomment and modify these lines
    private PokemonListPanel listPanel;
    private JPanel detailPanel; // Keep this until you implement PokemonDetailPanel

    // Uncomment these lines when implementing actual panels
    // private PokemonListPanel pokemonListPanel;
    // private PokemonDetailPanel pokemonDetailPanel;

    /**
     * Constructor that initializes the frame with a controller reference.
     *
     * @param controller the Pokemon controller
     */
    public MainPokemonFrame(IPokemonController controller) {
        this.controller = controller;
        initComponents();
    }

    /**
     * Initializes UI components and sets up layout.
     */
    private void initComponents() {
        // Set frame properties
        setTitle("Pokemon Collection Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Add logo with error handling
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/pokemon_logo.png"));
            if (logoIcon.getImage().getWidth(null) == -1) {
                System.err.println("Failed to load pokemon_logo.png - using text fallback");
                createStyledTitle();
                return;
            }
            
            // Scale the image to a reasonable size (adjust width as needed)
            Image scaledImage = logoIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            JPanel logoPanel = new JPanel(new BorderLayout());
            logoPanel.setBackground(new Color(245, 245, 255));
            logoPanel.add(logoLabel, BorderLayout.CENTER);
            add(logoPanel, BorderLayout.NORTH);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            createStyledTitle();
        }

        // Create split pane for list and detail panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.3); // Give 30% space to list panel

        // Initialize panels
        listPanel = new PokemonListPanel(controller);
        detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        
        // Add panels to split pane
        splitPane.setLeftComponent(listPanel);
        splitPane.setRightComponent(detailPanel);
        
        add(splitPane, BorderLayout.CENTER);

        // Set up Pokemon selection listener
        listPanel.setPokemonSelectionListener(this::showPokemonDetails);
    }

    private void createStyledTitle() {
        JLabel titleLabel = new JLabel("Pokédex", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(30, 55, 153));  // Pokemon blue color
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(245, 245, 255));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
    }

    /**
     * Creates a temporary list panel as a placeholder.
     *
     * @return a basic panel with placeholder text
     */
    //shixian 之后可以删除
    private JPanel createTemporaryListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Pokemon List"));

        JLabel placeholderLabel = new JLabel(
                "<html><center>Pokemon List Panel<br>Will be implemented soon</center></html>",
                SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(placeholderLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates a temporary detail panel as a placeholder.
     *
     * @return a basic panel with placeholder text
     */
    private JPanel createTemporaryDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Pokemon Details"));

        JLabel placeholderLabel = new JLabel(
                "<html><center>Pokemon Detail Panel<br>Will be implemented soon</center></html>",
                SwingConstants.CENTER
        );
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(placeholderLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Displays the main application window.
     */
    @Override
    public void display() {
        setVisible(true);
    }

    /**
     * Updates the Pokemon list displayed in the view.
     * Currently just prints to console since the list panel is not implemented.
     *
     * @param pokemonList the list of Pokemon to display
     */
    @Override
    public void updatePokemonList(List<Pokemon> pokemonList) {
        System.out.println("Pokemon list updated with " + pokemonList.size() + " Pokemon.");
        listPanel.updatePokemonList(pokemonList);
    }

    /**
     * Shows detailed information about a specific Pokemon.
     * Currently just prints to console since the detail panel is not implemented.
     *
     * @param pokemon the Pokemon to display details for
     */
    @Override
    public void showPokemonDetails(Pokemon pokemon) {
        System.out.println("Showing details for Pokemon: " + pokemon.getName());

        // Delegate to the actual PokemonDetailPanel if it exists
        // if (pokemonDetailPanel != null) {
        //     pokemonDetailPanel.displayPokemonDetails(pokemon);
        //     return;
        // }

        // When implementing/testing PokemonDetailPanel, comment out or delete the temporary implementation below
        // Add some basic Pokemon info to the temporary panel
        if (pokemon != null) {
            StringBuilder sb = new StringBuilder("<html><center>");
            sb.append("<h2>").append(pokemon.getName()).append("</h2>");
            sb.append("ID: ").append(pokemon.getId()).append("<br>");
            sb.append("Types: ");
            pokemon.getTypes().forEach(type -> sb.append(type).append(" "));
            sb.append("<br><br>Stats:<br>");
            sb.append("HP: ").append(pokemon.getStats().getHp()).append("<br>");
            sb.append("Attack: ").append(pokemon.getStats().getAttack()).append("<br>");
            sb.append("Defense: ").append(pokemon.getStats().getDefense()).append("<br>");
            sb.append("Special Attack: ").append(pokemon.getStats().getSpecialAttack()).append("<br>");
            sb.append("Special Defense: ").append(pokemon.getStats().getSpecialDefense()).append("<br>");
            sb.append("Speed: ").append(pokemon.getStats().getSpeed()).append("<br>");
            sb.append("</center></html>");

            JLabel detailsLabel = new JLabel(sb.toString(), SwingConstants.CENTER);
            detailPanel.removeAll();
            detailPanel.add(detailsLabel, BorderLayout.CENTER);
            detailPanel.revalidate();
            detailPanel.repaint();
        }
    }


}
