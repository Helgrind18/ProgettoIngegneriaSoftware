package org.example.Decorator.Ricerca;

import org.example.Biblioteca.Libro;

import java.util.ArrayList;
import java.util.List;

public class RicercaAutore extends RicercaComponent {
    private String autore;

    public RicercaAutore(RicercaDecorator ricercaDecorator, String autore) {
        super(ricercaDecorator);
        this.autore = autore;
    }

    public List<Libro> cerca() {
        System.out.println("Ricerco i libri con AUTORE "+autore);
        List<Libro> libriTitolo = new ArrayList<>();
        for (Libro l : super.cerca()) {
            if (l.getAutore().equalsIgnoreCase(this.autore)) {
                libriTitolo.add(l);
            }
        }
        return libriTitolo;
    }
}
