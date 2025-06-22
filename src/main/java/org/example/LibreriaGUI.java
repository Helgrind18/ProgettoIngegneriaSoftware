package org.example;

import org.example.Decorator.*;
import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.LibreriaTemplate.LibreriaJSON;
import org.example.LibreriaTemplate.LibreriaTemplate;
import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.List;
import java.awt.*;
import java.io.File;

public class LibreriaGUI extends JFrame {
    private final LibreriaTemplate libreria;
    private final DefaultTableModel tableModel;

    public LibreriaGUI(File fileJson) {
        super("Libreria");
        this.libreria = new LibreriaJSON(fileJson);
        libreria.esegui();  // carica la lista una volta sola

        // colonne e tabella
        String[] colonne = {"ISBN", "Titolo", "Autore", "Genere", "Valutazione", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0);
        JTable table = new JTable(tableModel);
        riempiTabella(libreria.getBiblitoeca());

        // pulsanti
        JButton addBtn = new JButton("Aggiungi Libro");
        JButton searchBtn = new JButton("Ricerca");
        addBtn.addActionListener(e -> aggiungiLibro());
        searchBtn.addActionListener(e -> ricercaLibro());

        // layout
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(searchBtn);
        south.add(addBtn);

        this.setLayout(new BorderLayout(5, 5));
        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(south, BorderLayout.SOUTH);

        // finestra
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(800, 400);
        this.setLocationRelativeTo(null);
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
        File file = new File(Costanti.percorsoFileJSONNuovo);
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
