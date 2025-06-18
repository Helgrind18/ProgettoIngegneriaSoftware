package org.example;

import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.LibreriaTemplate.LibreriaJSON;
import org.example.LibreriaTemplate.LibreriaTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class LibreriaGUI extends JFrame {
    private final LibreriaTemplate libreria;
    private final DefaultTableModel tableModel;

    public LibreriaGUI(File fileCsv) {
        super("Libreria");

        // 1) Inizializzo la libreria e carico i dati
        this.libreria = new LibreriaJSON(fileCsv);
        libreria.esegui();  // chiama leggiFile()

        // 2) Costruisco la tabella
        String[] colonne = {"ISBN","Titolo","Autore","Genere","Valutazione","Stato"};
        tableModel = new DefaultTableModel(colonne, 0);
        JTable table = new JTable(tableModel);
        populateTable();

        // 3) Bottone per aggiungere
        JButton addBtn = new JButton("Aggiungi Libro");
        addBtn.addActionListener(e -> showAddDialog());

        // 4) Layout
        this.setLayout(new BorderLayout(5,5));
        this.add(new JScrollPane(table), BorderLayout.CENTER);
        this.add(addBtn, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 400);
        this.setLocationRelativeTo(null); // centro schermo
    }

    private void populateTable() {
        // svuota e ricarica da libreria.biblitoeca
        tableModel.setRowCount(0);
        for (Libro l : libreria.getBiblitoeca()) {
            tableModel.addRow(new Object[]{
                    l.getISBN(),
                    l.getTitolo(),
                    l.getAutore(),
                    l.getGenere(),
                    l.getValutazione(),
                    l.getStatoLettura()
            });
        }
    }

    private void showAddDialog() {
        // panel con campi
        JTextField isbnField       = new JTextField();
        JTextField titoloField     = new JTextField();
        JTextField autoreField     = new JTextField();
        JTextField genereField     = new JTextField();
        JTextField valutazioneField= new JTextField();
        JComboBox<StatoLettura> statoBox = new JComboBox<>(StatoLettura.values());

        JPanel panel = new JPanel(new GridLayout(0,2,5,5));
        panel.add(new JLabel("ISBN:"));        panel.add(isbnField);
        panel.add(new JLabel("Titolo:"));      panel.add(titoloField);
        panel.add(new JLabel("Autore:"));      panel.add(autoreField);
        panel.add(new JLabel("Genere:"));      panel.add(genereField);
        panel.add(new JLabel("Valutazione:")); panel.add(valutazioneField);
        panel.add(new JLabel("Stato:"));       panel.add(statoBox);

        int result = JOptionPane.showConfirmDialog(
                this, panel,
                "Inserisci nuovo libro",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                long isbn      = Long.parseLong(isbnField.getText().trim());
                String titolo  = titoloField.getText().trim();
                String autore  = autoreField.getText().trim();
                String genere  = genereField.getText().trim();
                int val        = Integer.parseInt(valutazioneField.getText().trim());
                StatoLettura stato = (StatoLettura) statoBox.getSelectedItem();

                Libro nuovo = new Libro(isbn, titolo, autore, genere, val, stato);
                if (libreria.aggiungiLibro(nuovo)) {
                    libreria.scriviSuFile();
                    populateTable();
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
        // punta al file libreria.csv nella directory corrente
        File csv = new File(Costanti.percorsoFileJSON);
        SwingUtilities.invokeLater(() -> {
            new LibreriaGUI(csv).setVisible(true);
        });
    }
}
