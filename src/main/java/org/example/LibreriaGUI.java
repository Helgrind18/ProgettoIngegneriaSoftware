package org.example;

import org.example.Decorator.*;
import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.LibreriaTemplate.LibreriaJSON;
import org.example.LibreriaTemplate.LibreriaTemplate;
import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Strategy.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.List;
import java.awt.*;
import java.io.File;

public class LibreriaGUI extends JFrame {
    private final LibreriaTemplate libreria;
    private final DefaultTableModel tableModel;
    private final OrdinaContext gestore;  // il Context
    private final JTable table;
    public LibreriaGUI(File fileJson) {
        super("Libreria");
        this.libreria = new LibreriaCSV(fileJson);
        libreria.esegui();
        this.gestore = new OrdinaContext(libreria.getBiblitoeca());

        // Table setup
        String[] colonne = {"ISBN", "Titolo", "Autore", "Genere", "Valutazione", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0);
        table = new JTable(tableModel);
        riempiTabella(gestore.ordina());

        // Buttons
        JButton addBtn    = new JButton("Aggiungi Libro");
        JButton searchBtn = new JButton("Ricerca");
        JButton editBtn   = new JButton("Modifica selezionato");
        editBtn.setEnabled(false);

        addBtn.addActionListener(e -> aggiungiLibro());
        searchBtn.addActionListener(e -> ricercaLibro());
        editBtn.addActionListener(e -> modificaSelezionato());

        // Enable edit button on row selection
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean selected = table.getSelectedRow() >= 0;
                editBtn.setEnabled(selected);
            }
        });

        // Ordinamento combobox
        JComboBox<String> ordinamento = getOrdinamento();

        // South panel
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(new JLabel("Ordina per:"));
        south.add(ordinamento);
        south.add(searchBtn);
        south.add(addBtn);
        south.add(editBtn);

        // Frame layout
        this.setLayout(new BorderLayout(5,5));
        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(south, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
    }

    private void modificaSelezionato() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        long isbn = (Long) tableModel.getValueAt(row, 0);
        Libro vecchio = libreria.getBiblitoeca().stream()
                .filter(l -> l.getISBN() == isbn)
                .findFirst().orElse(null);
        if (vecchio == null) return;

        Libro nuovo = showEditDialog(vecchio);
        if (nuovo != null && libreria.modificaLibro(vecchio, nuovo)) {
            riempiTabella(libreria.getBiblitoeca());
        }
    }

    private Libro showEditDialog(Libro old) {
        JTextField titoloField      = new JTextField(old.getTitolo());
        JTextField autoreField      = new JTextField(old.getAutore());
        JTextField genereField      = new JTextField(old.getGenere());
        JTextField valutazioneField = new JTextField(String.valueOf(old.getValutazione()));
        JComboBox<StatoLettura> statoBox = new JComboBox<>(StatoLettura.values());
        statoBox.setSelectedItem(old.getStatoLettura());

        JPanel panel = new JPanel(new GridLayout(0,2,5,5));
        generaPanel(titoloField, autoreField, genereField, valutazioneField, statoBox, panel);

        int res = JOptionPane.showConfirmDialog(
                this, panel, "Modifica Libro",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (res != JOptionPane.OK_OPTION) return null;
        try {
            int val = Integer.parseInt(valutazioneField.getText().trim());
            return new Libro(
                    old.getISBN(),
                    titoloField.getText().trim(),
                    autoreField.getText().trim(),
                    genereField.getText().trim(),
                    val,
                    (StatoLettura) statoBox.getSelectedItem()
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Valutazione non valida", "Errore formato",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void generaPanel(JTextField titoloField, JTextField autoreField, JTextField genereField, JTextField valutazioneField, JComboBox<StatoLettura> statoBox, JPanel panel) {
        panel.add(new JLabel("Titolo:"));
        panel.add(titoloField);
        panel.add(new JLabel("Autore:"));
        panel.add(autoreField);
        panel.add(new JLabel("Genere:"));
        panel.add(genereField);
        panel.add(new JLabel("Valutazione:"));
        panel.add(valutazioneField);
        panel.add(new JLabel("Stato:"));
        panel.add(statoBox);
    }

    private JComboBox<String> getOrdinamento() {
        String[] criteri = {"Valutazione", "Titolo","Genere","ISBN"};
        JComboBox<String> sortBox = new JComboBox<>(criteri);
        sortBox.addActionListener(e -> {
            String criterio = (String) sortBox.getSelectedItem();
            System.out.println("Criterio selezionato = " + criterio);
            switch (criterio) {
                case "Valutazione":
                    gestore.setStrategy(new OrdinaValutazione());
                    break;
                case "Titolo":
                    gestore.setStrategy(new OrdinaTitolo());
                    break;
                case "Genere":
                    gestore.setStrategy(new OrdinaGenere());
                    break;
                case "ISBN":
                    gestore.setStrategy(new OrdinaISBN());
                    break;
            }
            riempiTabella(gestore.ordina());
        });
        return sortBox;
    }

    private void ricercaLibro() {
        JTextField titoloField = new JTextField(); // Campo per inserire il titolo
        JTextField autoreField = new JTextField(); // Campo per inserire l'autore
        JTextField genereField = new JTextField(); // Campo per inserire il genere
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Titolo libro:"));
        panel.add(titoloField);
        panel.add(new JLabel("Autore libro:"));
        panel.add(autoreField);
        panel.add(new JLabel("Genere libro"));
        panel.add(genereField);

        int result = JOptionPane.showConfirmDialog(
                this, panel,
                "Ricerca Libri",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String titolo = titoloField.getText().trim();
            String autore = autoreField.getText().trim();
            String genere = genereField.getText().trim();

            RicercaDecorator ricerca = new RicercaBase(libreria.getBiblitoeca()); // Ottengo la lista completa
            // Aggiungo decorator in base al campo non vuoto
            if (!titolo.isEmpty()) {
                ricerca = new RicercaTitolo(ricerca, titolo);
            }
            if (!autore.isEmpty()) {
                ricerca = new RicercaAutore(ricerca, autore);
            }
            if (!genere.isEmpty()) {
                ricerca = new RicercaGenere(ricerca, genere);
            }

            // Avvio la ricerca e popolo la tabella
            List<Libro> risultati = ricerca.cerca();
            riempiTabella(risultati);
        }
    }

    private void riempiTabella(List<Libro> lista) {
        tableModel.setRowCount(0);
        for (Libro l : lista) {
            tableModel.addRow(new Object[]{
                    l.getISBN(), l.getTitolo(), l.getAutore(),
                    l.getGenere(), l.getValutazione(), l.getStatoLettura()
            });
        }
    }


    private void aggiungiLibro() {
        // Panel di input
        JTextField isbnField = new JTextField();
        JTextField titoloField = new JTextField();
        JTextField autoreField = new JTextField();
        JTextField genereField = new JTextField();
        JTextField valutazioneField = new JTextField();
        JComboBox<StatoLettura> statoBox = new JComboBox<>(StatoLettura.values());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        generaPanel(titoloField, autoreField, genereField, valutazioneField, statoBox, panel);

        int result = JOptionPane.showConfirmDialog(
                this, panel,
                "Inserisci nuovo libro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                long isbn = Long.parseLong(isbnField.getText().trim());
                String titolo = titoloField.getText().trim();
                String autore = autoreField.getText().trim();
                String genere = genereField.getText().trim();
                int val = Integer.parseInt(valutazioneField.getText().trim());
                StatoLettura stato = (StatoLettura) statoBox.getSelectedItem();

                Libro nuovo = new Libro(isbn, titolo, autore, genere, val, stato);
                if (libreria.aggiungiLibro(nuovo)) {
                    // Salvo su file
                    libreria.scriviSuFile(nuovo);

                    // Aggiungo solo la nuova riga, senza ripopolare tutta la tabella
                    tableModel.addRow(new Object[]{
                            nuovo.getISBN(),
                            nuovo.getTitolo(),
                            nuovo.getAutore(),
                            nuovo.getGenere(),
                            nuovo.getValutazione(),
                            nuovo.getStatoLettura()
                    });
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Libro già presente (ISBN duplicato)!",
                            "Errore",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "ISBN o valutazione non valida.",
                        "Errore di formato",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        File file = new File(Costanti.percorsoFileCSV);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("File " + file + " inesistente, lo creo");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        SwingUtilities.invokeLater(() -> new LibreriaGUI(file).setVisible(true));
    }
}
