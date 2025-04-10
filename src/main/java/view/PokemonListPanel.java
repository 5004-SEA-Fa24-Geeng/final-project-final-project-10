package view;

import Model.Pokemon;
import Model.PokemonType;
import controller.IPokemonController;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Panel for displaying a list of Pokemon.
 * Uses a JList to show Pokemon names and allows for selection.
 */
public class PokemonListPanel extends JPanel {
    private final IPokemonController controller;
    private final DefaultListModel<Pokemon> listModel;
    private final JList<Pokemon> pokemonList;
    private Consumer<Pokemon> selectionListener;
    private List<Pokemon> fullPokemonList; // Store the complete list
    private JTextField searchField;
    private JComboBox<PokemonType> typeFilter;
    private JComboBox<SortOption> sortOptions;
    private JButton loadMoreButton;
    private JButton exportButton;
    private int currentPage = 0;
    private static final int PAGE_SIZE = 20;

    // Add enum for sort options
    private enum SortOption {
        NAME_ASC("Name A-Z"),
        NAME_DESC("Name Z-A"),
        ID_ASC("ID ↑"),
        ID_DESC("ID ↓"),
        HP_DESC("HP ↓");

        private final String label;
        SortOption(String label) { this.label = label; }
        @Override
        public String toString() { return label; }
    }

    /**
     * Constructs a new PokemonListPanel.
     *
     * @param controller the Pokemon controller
     */
    public PokemonListPanel(IPokemonController controller) {
        this.controller = controller;
        this.listModel = new DefaultListModel<>();
        this.pokemonList = new JList<>(listModel);
        
        initializeComponents();
        setupListeners();
    }

    /**
     * Initializes the panel components.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 255)); // Light blue-tinted background

        // Top control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Configure the JList with custom renderer
        configureList();

        // Bottom panel with load more and export buttons
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        // Title and search panel
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        // Stylized title
        JLabel titleLabel = new JLabel("Pokédex", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Search panel with icon
        JPanel searchPanel = createSearchPanel();
        topPanel.add(searchPanel, BorderLayout.CENTER);

        panel.add(topPanel, BorderLayout.NORTH);

        // Filter and sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        filterSortPanel.setOpaque(false);

        // Type filter
        typeFilter = new JComboBox<>();
        typeFilter.addItem(null); // "All" option
        for (PokemonType type : PokemonType.values()) {
            typeFilter.addItem(type);
        }
        typeFilter.setRenderer(new TypeComboBoxRenderer());
        filterSortPanel.add(new JLabel("Type:"));
        filterSortPanel.add(typeFilter);

        // Sort options
        sortOptions = new JComboBox<>(SortOption.values());
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortOptions);

        panel.add(filterSortPanel, BorderLayout.CENTER);
        return panel;
    }

    private void configureList() {
        pokemonList.setCellRenderer(new PokemonListCellRenderer());
        pokemonList.setFixedCellHeight(50);
        
        JScrollPane scrollPane = new JScrollPane(pokemonList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add custom styling to the scroll pane
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);

        loadMoreButton = createStyledButton("Load More", "📥");
        exportButton = createStyledButton("Export", "💾");

        panel.add(loadMoreButton);
        panel.add(exportButton);
        return panel;
    }

    private JButton createStyledButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(51, 51, 51));
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(5, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Sets up event listeners for the panel.
     */
    private void setupListeners() {
        typeFilter.addActionListener(e -> filterAndSortList());
        sortOptions.addActionListener(e -> filterAndSortList());
        loadMoreButton.addActionListener(e -> loadMorePokemon());
        exportButton.addActionListener(e -> exportCurrentList());
    }

    private void filterAndSortList() {
        if (fullPokemonList == null) return;

        List<Pokemon> filtered = fullPokemonList.stream()
            .filter(pokemon -> typeFilter.getSelectedItem() == null ||
                             pokemon.getTypes().contains(typeFilter.getSelectedItem()))
            .collect(Collectors.toList());

        SortOption selectedSort = (SortOption) sortOptions.getSelectedItem();
        switch (selectedSort) {
            case NAME_ASC -> filtered.sort(Comparator.comparing(Pokemon::getName));
            case NAME_DESC -> filtered.sort(Comparator.comparing(Pokemon::getName).reversed());
            case ID_ASC -> filtered.sort(Comparator.comparingInt(Pokemon::getId));
            case ID_DESC -> filtered.sort(Comparator.comparingInt(Pokemon::getId).reversed());
            case HP_DESC -> filtered.sort(Comparator.comparingInt((Pokemon p) -> p.getStats().getHp()).reversed());
        }

        updateListContent(filtered);
    }

    private void setupSearch() {
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterList(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterList(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterList(); }
        });
    }

    private void filterList() {
        String searchText = searchField.getText().toLowerCase().trim();
        
        if (fullPokemonList != null) {
            listModel.clear();
            
            if (searchText.isEmpty()) {
                fullPokemonList.forEach(listModel::addElement);
            } else {
                fullPokemonList.stream()
                    .filter(pokemon -> pokemon.getName().toLowerCase().contains(searchText) ||
                                     String.valueOf(pokemon.getId()).contains(searchText))
                    .forEach(listModel::addElement);
            }
            
            // Select first item if list is not empty
            if (listModel.size() > 0) {
                pokemonList.setSelectedIndex(0);
            }
        }
    }

    /**
     * Updates the list with new Pokemon data.
     *
     * @param pokemonList the list of Pokemon to display
     */
    public void updatePokemonList(List<Pokemon> pokemonList) {
        this.fullPokemonList = new ArrayList<>(pokemonList); // Store full list
        listModel.clear();
        pokemonList.forEach(listModel::addElement);
        
        if (!listModel.isEmpty()) {
            this.pokemonList.setSelectedIndex(0);
        }
    }

    /**
     * Gets the currently selected Pokemon.
     *
     * @return the selected Pokemon, or null if none is selected
     */
    public Pokemon getSelectedPokemon() {
        return pokemonList.getSelectedValue();
    }

    /**
     * Sets up a listener for Pokemon selection events.
     *
     * @param listener the consumer that will handle the selected Pokemon
     */
    public void setPokemonSelectionListener(Consumer<Pokemon> listener) {
        this.selectionListener = listener;
        // Update the list selection listener
        pokemonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Pokemon selectedPokemon = pokemonList.getSelectedValue();
                if (selectedPokemon != null && selectionListener != null) {
                    selectionListener.accept(selectedPokemon);
                }
            }
        });
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setOpaque(false);
        
        // Search icon
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        searchPanel.add(searchIcon, BorderLayout.WEST);

        // Search field
        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.putClientProperty("JTextField.placeholderText", "Search Pokémon...");
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        setupSearch(); // Initialize search functionality
        return searchPanel;
    }

    private void loadMorePokemon() {
        currentPage++;
        controller.fetchInitialPokemon((currentPage + 1) * PAGE_SIZE);
    }

    private void exportCurrentList() {
        // For now, just print the list
        System.out.println("Exporting Pokemon list...");
        fullPokemonList.forEach(System.out::println);
    }

    private void updateListContent(List<Pokemon> filtered) {
        listModel.clear();
        filtered.forEach(listModel::addElement);
    }
} 