package org.example;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibreriaGUI extends JFrame {
    private final LibreriaFacade libreriaFacade;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private List<Libro> currentList;
    public LibreriaGUI(File file) {
        super("Libreria");
        // Inizializzo la facade (usa CSV o JSON)
        libreriaFacade = new LibreriaFacade(file);

        // Setup tabella
        String[] colonne = {"ISBN", "Titolo", "Autore", "Genere", "Valutazione", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0);
        table = new JTable(tableModel);
        riempiTabella(libreriaFacade.getAll());

        currentList = libreriaFacade.getAll();

        // Bottoni
        JButton addBtn = new JButton("Aggiungi");
        JButton searchBtn = new JButton("Ricerca");
        JButton editBtn = new JButton("Modifica");
        JButton delBtn = new JButton("Elimina");

        editBtn.setEnabled(false);
        delBtn.setEnabled(false);

        addBtn.addActionListener(e -> bottoneAggiungi());
        searchBtn.addActionListener(e -> bottoneCerca());
        editBtn.addActionListener(e -> bottoneModifica());
        delBtn.addActionListener(e -> bottoneElimina());

        // Ora implemento un metodo per permette la modifica dei libri, sono visibili solo se si preme su quel libro
        table.getSelectionModel().addListSelectionListener(e -> {
            boolean sel = table.getSelectedRow() >= 0;
            editBtn.setEnabled(sel);
            delBtn.setEnabled(sel);
        });

        // ordinamento
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Valutazione", "Titolo", "Genere", "ISBN"});
        sortBox.addActionListener(e -> {List<Libro> sorted = libreriaFacade.ordina(currentList, (String) sortBox.getSelectedItem());
            riempiTabella(sorted);});

        // Layout
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(new JLabel("Ordina per:"));
        south.add(sortBox);
        south.add(searchBtn);
        south.add(addBtn);
        south.add(editBtn);
        south.add(delBtn);

        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
    }

    private void riempiTabella(List<Libro> lista) {
        tableModel.setRowCount(0);
        currentList = lista;
        for (Libro l : lista) {
            tableModel.addRow(new Object[]{
                    l.getISBN(), l.getTitolo(), l.getAutore(),
                    l.getGenere(), l.getValutazione(), l.getStatoLettura()
            });
        }
    }

    private void bottoneAggiungi() {
        Libro nuovo = schermataModifica(null);
        if (nuovo != null && libreriaFacade.aggiungiLibro(nuovo)) {
            riempiTabella(libreriaFacade.getAll());
        }
    }

    private void bottoneCerca() {
        JTextField titoloField = new JTextField();
        JTextField autoreField = new JTextField();
        JTextField genereField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Titolo:"));
        panel.add(titoloField);
        panel.add(new JLabel("Autore:"));
        panel.add(autoreField);
        panel.add(new JLabel("Genere:"));
        panel.add(genereField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ricerca libro", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String titolo = titoloField.getText().trim();
            String autore = autoreField.getText().trim();
            String genere = genereField.getText().trim();
            List<Libro> res = libreriaFacade.cerca(titolo, autore, genere);
            riempiTabella(res);
        }
    }

    private void bottoneModifica() {
        int rigaSelezionata = table.getSelectedRow();
        if (rigaSelezionata < 0)
            return;
        long isbn = (Long) tableModel.getValueAt(rigaSelezionata, 0);
        Libro vecchio = libreriaFacade.ottieniLibro(isbn);
        if (vecchio == null)
            return;
        Libro nuovo = schermataModifica(vecchio);
        if (nuovo != null && libreriaFacade.modificaLibro(vecchio, nuovo)) {
            riempiTabella(libreriaFacade.getAll());
        }
    }

    private void bottoneElimina() {
        int rigaSelezionata = table.getSelectedRow();
        if (rigaSelezionata < 0)
            return;
        long isbn = (Long) tableModel.getValueAt(rigaSelezionata, 0);
        Libro selezionato = libreriaFacade.ottieniLibro(isbn);
        if (selezionato == null)
            return;
        if (libreriaFacade.rimuoviLibro(selezionato))
            riempiTabella(libreriaFacade.getAll());
    }

    private Libro schermataModifica(Libro libro) {
        JTextField isbnField = new JTextField();
        JTextField titoloField = new JTextField();
        JTextField autoreField = new JTextField();
        JTextField genereField = new JTextField();
        JTextField valField = new JTextField();
        JComboBox<StatoLettura> statoBox = new JComboBox<>(StatoLettura.values());
        String titolo = "Modifica Libro";
        if (libro != null) {
            isbnField.setText(String.valueOf(libro.getISBN()));
            titoloField.setText(libro.getTitolo());
            autoreField.setText(libro.getAutore());
            genereField.setText(libro.getGenere());
            valField.setText(String.valueOf(libro.getValutazione()));
            statoBox.setSelectedItem(libro.getStatoLettura());

        }
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.add(new JLabel("ISBN:"));
        p.add(isbnField);
        p.add(new JLabel("Titolo:"));
        p.add(titoloField);
        p.add(new JLabel("Autore:"));
        p.add(autoreField);
        p.add(new JLabel("Genere:"));
        p.add(genereField);
        p.add(new JLabel("Valutazione:"));
        p.add(valField);
        p.add(new JLabel("Stato:"));
        p.add(statoBox);
        JOptionPane.showConfirmDialog(this, p, titolo,JOptionPane.OK_CANCEL_OPTION);

        try {
            long isbn = Long.parseLong(isbnField.getText().trim());
            int val = Integer.parseInt(valField.getText().trim());
            return new Libro(isbn, titoloField.getText().trim(), autoreField.getText().trim(), genereField.getText().trim(), val, (StatoLettura) statoBox.getSelectedItem());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Dati non validi","Errore",JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }


    public static void main(String[] args) {
        File file = new File(Costanti.percorsoFileJSON);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        SwingUtilities.invokeLater(() -> new LibreriaGUI(file).setVisible(true));
    }
}
