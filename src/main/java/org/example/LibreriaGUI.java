package org.example;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Command.*;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LibreriaGUI extends JFrame {
    private final LibreriaFacade libreriaFacade;
    private final DefaultTableModel tableModel; //Struttura che tiene traccia delle righe e delle colonne da visualizzare
    private final JTable table; // La uso per mostrare i dati in forma tabellare. Usa il defaultTableModel per sapere cosa mostrare
    private List<Libro> currentList;
    private Command aggiunta;
    private Command cerca;
    private Command modifica;
    private Command elimina;
    public LibreriaGUI(File file) {
        super("Libreria");
        // Inizializzo la facade
        libreriaFacade = new LibreriaFacade(file); // è il receiver del command (in realtà sono le classi specifiche per l'implementazione della persistenza, però ricopre il ruolo di receiver)
        // Inizializzo i vari comandi
        this.aggiunta = new Aggiunta(libreriaFacade,this);
        this.cerca = new Cerca(libreriaFacade,this);
        this.modifica = new Modifica(libreriaFacade,this);
        this.elimina = new Elimina(libreriaFacade,this);
        // Setup tabella
        String[] colonne = {"ISBN", "Titolo", "Autore", "Genere", "Valutazione", "Stato"};
        tableModel = new DefaultTableModel(colonne, 0); // Il modello contiene le colonne da visualizzare, in questo modo gliele sto passando
        table = new JTable(tableModel); // Creo una tabella che mostra i dati contenuti nel modello.
        System.out.println("=======================================");
        currentList = libreriaFacade.getAll();
        riempiTabella(currentList);
        System.out.println("=======================================");
        // Bottoni
        JButton aggiungi = new JButton("Aggiungi");
        JButton ricerca = new JButton("Ricerca");
        JButton modifica = new JButton("Modifica");
        JButton elimina = new JButton("Elimina");
        //Inizialmente i bottoni sono spenti, solo quando si tocca su un libro possono essere utilizzati
        modifica.setEnabled(false);
        elimina.setEnabled(false);
        //Azioni per i bottoni
        aggiungi.addActionListener(e -> this.aggiunta.execute()); // sono i vari invoker, non sanno come implementare un'azione, sanno solo che devono eseguire un comando
        ricerca.addActionListener(e -> this.cerca.execute());
        modifica.addActionListener(e -> this.modifica.execute());
        elimina.addActionListener(e -> this.elimina.execute());
        JComboBox<String> sortBox = new JComboBox<>(new String[]{"Valutazione", "Titolo", "Genere", "ISBN", "Stato"});
        sortBox.addActionListener(e -> {
            new Ordina((String) sortBox.getSelectedItem(),libreriaFacade,this).execute();
        });
        // Ora implemento un metodo per permette la modifica dei libri, sono visibili solo se si preme su quel libro
        table.getSelectionModel().addListSelectionListener(e -> {
            modifica.setEnabled(true);
            elimina.setEnabled(true);
        });
        // Layout
        JPanel pannello = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pannello.add(new JLabel("Ordina per:"));
        pannello.add(sortBox);
        pannello.add(ricerca);
        pannello.add(aggiungi);
        pannello.add(modifica);
        pannello.add(elimina);
        setLayout(new BorderLayout(5, 5));
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pannello, BorderLayout.SOUTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTable() {
        return table;
    }

    public List<Libro> getCurrentList() {
        return currentList;
    }

    public void riempiTabella(List<Libro> lista) {
        tableModel.setRowCount(0);
        currentList = lista; //Aggiornamento della lista corrente, serve per permettere le ricerche e gli ordinamenti sulla lista mostrata a schermo e non su tutta la libreria
        for (Libro l : lista) {
            //Devo aggiungere il libro al modello, per farlo devo prendere i campi specifici che devono riempire le colonne
            tableModel.addRow(new Object[]{
                    l.getISBN(), l.getTitolo(), l.getAutore(),
                    l.getGenere(), l.getValutazione(), l.getStatoLettura()
            });
        }
    }

    public Libro schermataModifica(Libro libro) {
        JTextField campoISBN = new JTextField();
        JTextField campoTitolo = new JTextField();
        JTextField campoAutore = new JTextField();
        JTextField campoGenere = new JTextField();
        JTextField campoValutazione = new JTextField();
        JComboBox<StatoLettura> selezioneStato = new JComboBox<>(StatoLettura.values());
        String titolo = "Modifica Libro";
        if (libro != null) {
            campoISBN.setText(String.valueOf(libro.getISBN()));
            campoTitolo.setText(libro.getTitolo());
            campoAutore.setText(libro.getAutore());
            campoGenere.setText(libro.getGenere());
            campoValutazione.setText(String.valueOf(libro.getValutazione()));
            selezioneStato.setSelectedItem(libro.getStatoLettura());
        }
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));
        p.add(new JLabel("ISBN:"));
        p.add(campoISBN);
        p.add(new JLabel("Titolo:"));
        p.add(campoTitolo);
        p.add(new JLabel("Autore:"));
        p.add(campoAutore);
        p.add(new JLabel("Genere:"));
        p.add(campoGenere);
        p.add(new JLabel("Valutazione:"));
        p.add(campoValutazione);
        p.add(new JLabel("Stato:"));
        p.add(selezioneStato);
        JOptionPane.showConfirmDialog(this, p, titolo, JOptionPane.OK_CANCEL_OPTION);
        try {
            long isbn = Long.parseLong(campoISBN.getText().trim());
            String valText = campoValutazione.getText().trim();
            int val = 0;
            StatoLettura statoLettura = StatoLettura.DA_LEGGERE;
            if (!valText.isEmpty()) {
                val = Integer.parseInt(valText);
                statoLettura = (StatoLettura) selezioneStato.getSelectedItem();
            }
            return new Libro(isbn, campoTitolo.getText().trim(), campoAutore.getText().trim(), campoGenere.getText().trim(), val, statoLettura);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dati non validi", "Errore", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    public static void main(String[] args) {
        File file = new File(Costanti.percorsoFileCSV);
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        SwingUtilities.invokeLater(() -> new LibreriaGUI(file).setVisible(true));
    }
}
