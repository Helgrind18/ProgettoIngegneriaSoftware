package org.example.Command;

import org.example.Biblioteca.Libro;
import org.example.LibreriaFacade;
import org.example.LibreriaGUI;

public class Aggiunta implements Command{
    private LibreriaFacade facade;
    private LibreriaGUI gui;

    public Aggiunta(LibreriaFacade facade, LibreriaGUI gui) {
        this.facade = facade; // è il receiver, sulla quale i comandi operano
        this.gui = gui; // è l'invoker del command
    }

    @Override
    public void eseguiComando() {
        // Mostra schermata per aggiungere un libro
        Libro nuovo = gui.schermataModifica(null);
        if (nuovo != null && facade.aggiungiLibro(nuovo)) {
            // Aggiorna la tabella con tutti i libri
            gui.riempiTabella(facade.getAll());
        }
    }
}
