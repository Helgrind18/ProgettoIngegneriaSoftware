package org.example.Command;

import org.example.Biblioteca.Libro;
import org.example.LibreriaFacade;
import org.example.LibreriaGUI;

import java.util.List;

public class Ordina implements Command{
    private String criterio;
    private LibreriaFacade facade;
    private LibreriaGUI gui;

    public Ordina(String criterio, LibreriaFacade facade, LibreriaGUI gui) {
        this.criterio = criterio;
        this.facade = facade; // è il receiver, sulla quale i comandi operano
        this.gui = gui; // è l'invoker del command
    }

    @Override
    public void execute() {
        if(criterio != null) {
            List<Libro> listaOrdinata = facade.ordina(gui.getCurrentList(), criterio);
            gui.riempiTabella(listaOrdinata);
        }
    }
}
