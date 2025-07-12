package org.example.Command;

import org.example.Biblioteca.Libro;
import org.example.LibreriaFacade;
import org.example.LibreriaGUI;

public class Elimina implements Command {
    private LibreriaFacade facade;
    private LibreriaGUI gui;

    public Elimina(LibreriaFacade facade, LibreriaGUI gui) {
        this.facade = facade; // è il receiver, sulla quale i comandi operano
        this.gui = gui; // è l'invoker del command
    }

    @Override
    public void execute() {
        int row = gui.getTable().getSelectedRow();
        long isbn = (Long) gui.getTableModel().getValueAt(row, 0);
        Libro selezionato = facade.ottieniLibro(isbn);
        if (selezionato != null && facade.rimuoviLibro(selezionato)) {
            gui.riempiTabella(facade.getAll());
        }
    }
}
