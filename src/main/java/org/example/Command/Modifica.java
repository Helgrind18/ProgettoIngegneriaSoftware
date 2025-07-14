package org.example.Command;

import org.example.Biblioteca.Libro;
import org.example.LibreriaFacade;
import org.example.LibreriaGUI;

public class Modifica implements Command {
    private LibreriaFacade facade;
    private LibreriaGUI gui;

    public Modifica(LibreriaFacade facade, LibreriaGUI gui) {
        this.facade = facade; // è il receiver, sulla quale i comandi operano
        this.gui = gui; // è l'invoker del command
    }

    @Override
    public void eseguiComando() {
        int row = gui.getTable().getSelectedRow();
        long isbn = (Long) gui.getTableModel().getValueAt(row, 0);
        Libro modificare = facade.ottieniLibro(isbn);
        if (modificare == null) return;
        Libro modificato = gui.schermataModifica(modificare);
        if (modificato != null && facade.modificaLibro(modificare, modificato)) {
            gui.riempiTabella(facade.getAll());
        }
    }
}
