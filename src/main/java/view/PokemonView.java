package view;

import controller.PokemonController;
import model.Pokemon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class PokemonView extends JFrame {
    private PokemonController controller;
    private JTable pokemonTable;
    private JTextField searchField;
    private JComboBox<String> typeFilterComboBox;
    private JButton fetchButton, saveJsonButton, saveCSVButton, loadButton;

    public PokemonView() {
        controller = new PokemonController();
        initComponents();
    }

    private void initComponents() {
        setTitle("Pokemon Collection Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control Panel
        JPanel controlPanel = new JPanel();
        fetchButton = new JButton("Fetch Pokemon");
        saveJsonButton = new JButton("Save to JSON");
        saveCSVButton = new JButton("Save to CSV");
        loadButton = new JButton("Load Collection");
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        // todo 更新为 fetch data 里面的所有type
        typeFilterComboBox = new JComboBox<>(new String[]{"All", "Fire", "Water", "Grass", "Electric"});

        controlPanel.add(fetchButton);
        controlPanel.add(saveJsonButton);
        controlPanel.add(saveCSVButton);
        controlPanel.add(loadButton);
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(new JLabel("Filter Type:"));
        controlPanel.add(typeFilterComboBox);

        add(controlPanel, BorderLayout.NORTH);

        // Table
        //todo。更新col
        String[] columnNames = {"ID", "Name", "Types", "HP", "Attack", "Defense", "Image"};
        pokemonTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(pokemonTable);
        add(scrollPane, BorderLayout.CENTER);

        // Event Listeners
        fetchButton.addActionListener(e -> fetchPokemon());
        saveJsonButton.addActionListener(e -> saveToJson());
        saveCSVButton.addActionListener(e -> saveToCSV());
        loadButton.addActionListener(e -> loadCollection());
        searchButton.addActionListener(e -> searchPokemon());
        typeFilterComboBox.addActionListener(e -> filterByType());
    }

    private void fetchPokemon() {
        //todo 更新调用的number
        controller.fetchPokemonCollection(1, 200);
        updateTable(controller.getPokemonCollection());
    }

    private void updateTable(List<Pokemon> pokemonList) {
        //todo 更新这个model
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Name", "Types", "HP", "Attack", "Defense", "Image"}, 0);
        
        for (Pokemon pokemon : pokemonList) {
            try {
                ImageIcon icon = new ImageIcon(new URL(pokemon.getImageUrl()));
                Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                
                model.addRow(new Object[]{
                    pokemon.getId(),
                    pokemon.getName(),
                    String.join(", ", pokemon.getTypes()),
                    pokemon.getStats().getHp(),
                    pokemon.getStats().getAttack(),
                    pokemon.getStats().getDefense(),
                    new ImageIcon(scaledImage)
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        pokemonTable.setModel(model);
        pokemonTable.setRowHeight(60);
    }

    private void saveToJson() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.saveCollectionToJson(fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Collection saved successfully!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving collection: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveToCSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.saveCollectionToCSV(fileChooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Collection saved successfully!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving collection: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCollection() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                controller.loadCollectionFromJson(fileChooser.getSelectedFile().getAbsolutePath());
                updateTable(controller.getPokemonCollection());
                JOptionPane.showMessageDialog(this, "Collection loaded successfully!");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading collection: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchPokemon() {
        String searchTerm = searchField.getText();
        List<Pokemon> searchResults = controller.searchPokemon(searchTerm);
        updateTable(searchResults);
    }

    private void filterByType() {
        String selectedType = (String) typeFilterComboBox.getSelectedItem();
        if ("All".equals(selectedType)) {
            updateTable(controller.getPokemonCollection());
        } else {
            List<Pokemon> filteredPokemon = controller.filterPokemonByType(selectedType);
            updateTable(filteredPokemon);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PokemonView view = new PokemonView();
            view.setVisible(true);
        });
    }
}
