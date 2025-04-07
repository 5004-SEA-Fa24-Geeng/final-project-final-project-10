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

    // These will be replaced with actual implementations later
    private JPanel listPanel; // Placeholder for PokemonListPanel
    private JPanel detailPanel; // Placeholder for PokemonDetailPanel

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
        setLocationRelativeTo(null); // Center on screen

        // Create temporary panels (will be replaced with actual implementations)
        listPanel = createTemporaryListPanel();
        detailPanel = createTemporaryDetailPanel();

        // Set up layout
        setLayout(new BorderLayout());

        // Split pane to divide list and detail views
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listPanel),
                new JScrollPane(detailPanel)
        );
        splitPane.setDividerLocation(500); // Set initial divider position

        // Add split pane to frame
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Creates a temporary list panel as a placeholder.
     *
     * @return a basic panel with placeholder text
     */
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
        // Temporary implementation until PokemonListPanel is created
        System.out.println("Pokemon list updated with " + pokemonList.size() + " Pokemon.");

        // Add a display of the first few Pokemon to the temporary panel for visual feedback
        if (!pokemonList.isEmpty()) {
            StringBuilder sb = new StringBuilder("<html><center>Pokemon Available:<br>");
            int displayCount = Math.min(50, pokemonList.size());
            for (int i = 0; i < displayCount; i++) {
                sb.append(pokemonList.get(i).getName()).append("<br>");
            }
            sb.append("</center></html>");

            JLabel infoLabel = new JLabel(sb.toString(), SwingConstants.CENTER);
            listPanel.removeAll();
            listPanel.add(infoLabel, BorderLayout.CENTER);
            listPanel.revalidate();
            listPanel.repaint();
        }
    }

    /**
     * Shows detailed information about a specific Pokemon.
     * Currently just prints to console since the detail panel is not implemented.
     *
     * @param pokemon the Pokemon to display details for
     */
    @Override
    public void showPokemonDetails(Pokemon pokemon) {
        // Temporary implementation until PokemonDetailPanel is created
        System.out.println("Showing details for Pokemon: " + pokemon.getName());

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

    /**
     * Future method to set the actual list panel once implemented.
     *
     * @param pokemonListPanel the implemented list panel
     */
    public void setListPanel(JPanel pokemonListPanel) {
        // This will be called once the PokemonListPanel is implemented
        Container parent = listPanel.getParent();
        if (parent instanceof JScrollPane) {
            ((JScrollPane) parent).setViewportView(pokemonListPanel);
        }
        this.listPanel = pokemonListPanel;
    }

    /**
     * Future method to set the actual detail panel once implemented.
     *
     * @param pokemonDetailPanel the implemented detail panel
     */
    public void setDetailPanel(JPanel pokemonDetailPanel) {
        // This will be called once the PokemonDetailPanel is implemented
        Container parent = detailPanel.getParent();
        if (parent instanceof JScrollPane) {
            ((JScrollPane) parent).setViewportView(pokemonDetailPanel);
        }
        this.detailPanel = pokemonDetailPanel;
    }
}
