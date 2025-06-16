package org.example.GUI;

import org.example.Biblioteca.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private BibliotecaImpl biblioteca;
    private DefaultTableModel tableModel;
    private JTable table;

    public BibliotecaGUI() {
        biblioteca = new BibliotecaImpl();

        setTitle("Gestione Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabella
        String[] colonne = {"ISBN", "Titolo", "Autore", "Genere", "Valutazione", "Stato", "Azioni"};
        tableModel = new DefaultTableModel(colonne, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottoni
        JPanel buttonPanel = new JPanel();

        JButton aggiungiBtn = new JButton("Aggiungi libro");
        aggiungiBtn.addActionListener(e -> aggiungiLibro());

        JButton cercaAutoreBtn = new JButton("Cerca per autore");
        cercaAutoreBtn.addActionListener(e -> cercaPerAutore());

        JButton cercaGenereBtn = new JButton("Cerca per genere");
        cercaGenereBtn.addActionListener(e -> cercaPerGenere());

        buttonPanel.add(aggiungiBtn);
        buttonPanel.add(cercaAutoreBtn);
        buttonPanel.add(cercaGenereBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void aggiornaTabella(List<Libro> libri) {
        tableModel.setRowCount(0);
        for (Libro l : libri) {
            tableModel.addRow(new Object[]{
                    l.getISBN(), l.getTitolo(), l.getAutore(), l.getGenere(),
                    l.getValutazione(), l.getStatoLettura()
            });
        }
    }

    private void aggiungiLibro() {
        JTextField isbn = new JTextField();
        JTextField titolo = new JTextField();
        JTextField autore = new JTextField();
        JTextField genere = new JTextField();
        JTextField valutazione = new JTextField();
        JComboBox<StatoLettura> statoBox = new JComboBox<>(StatoLettura.values());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("ISBN:")); panel.add(isbn);
        panel.add(new JLabel("Titolo:")); panel.add(titolo);
        panel.add(new JLabel("Autore:")); panel.add(autore);
        panel.add(new JLabel("Genere:")); panel.add(genere);
        panel.add(new JLabel("Valutazione:")); panel.add(valutazione);
        panel.add(new JLabel("Stato lettura:")); panel.add(statoBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Nuovo Libro", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Libro nuovo = new Libro(
                    Integer.parseInt(isbn.getText()), titolo.getText(), autore.getText(), genere.getText(),
                    Integer.parseInt(valutazione.getText()), (StatoLettura) statoBox.getSelectedItem()
            );
            if (biblioteca.aggiungiLibro(nuovo)) {
                JOptionPane.showMessageDialog(this, "Libro aggiunto!");
            }
        }
    }

    private void cercaPerAutore() {
        String autore = JOptionPane.showInputDialog("Inserisci autore:");
        if (autore != null) {
            List<Libro> risultati = biblioteca.cercaPerAutore(autore);
            aggiornaTabella(risultati);
        }
    }

    private void cercaPerGenere() {
        String genere = JOptionPane.showInputDialog("Inserisci genere:");
        if (genere != null) {
            List<Libro> risultati = biblioteca.cercaPerGenere(genere);
            aggiornaTabella(risultati);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BibliotecaGUI::new);
    }
}
