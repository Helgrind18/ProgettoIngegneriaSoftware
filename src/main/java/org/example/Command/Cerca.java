package org.example.Command;


import org.example.Biblioteca.Libro;
import org.example.LibreriaFacade;
import org.example.LibreriaGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class Cerca implements Command {
    private LibreriaFacade facade;
    private LibreriaGUI gui;

    public Cerca(LibreriaFacade facade, LibreriaGUI gui) {
        this.facade = facade; // è il receiver, sulla quale i comandi operano
        this.gui = gui; // è l'invoker del command
    }

    @Override
    public void execute() {
        JTextField campoTitolo = new JTextField();
        JTextField campoAutore = new JTextField();
        JTextField campoGenere = new JTextField();
        JTextField campoStato  = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0,2,5,5));
        panel.add(new JLabel("Titolo:")); panel.add(campoTitolo);
        panel.add(new JLabel("Autore:")); panel.add(campoAutore);
        panel.add(new JLabel("Genere:")); panel.add(campoGenere);
        panel.add(new JLabel("Stato:")); panel.add(campoStato);
        int res = JOptionPane.showConfirmDialog(gui, panel, "Ricerca libro", JOptionPane.OK_CANCEL_OPTION);
        if(res == JOptionPane.OK_OPTION) {
            String t = campoTitolo.getText().trim();
            String a = campoAutore.getText().trim();
            String g = campoGenere.getText().trim();
            String s = campoStato.getText().trim().toUpperCase();
            List<Libro> result = facade.cerca(t,a,g,s);
            gui.riempiTabella(result);
        }
    }
}