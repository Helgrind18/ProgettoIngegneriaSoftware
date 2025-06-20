package org.example.Decorator;

import org.example.Biblioteca.Libro;

import java.util.ArrayList;
import java.util.List;

public class RicercaTitolo extends RicercaComponent {
    private String titolo;

    public RicercaTitolo(RicercaDecorator ricercaDecorator, String titolo) {
        super(ricercaDecorator);
        this.titolo = titolo;
    }

    public List<Libro> cerca() {
        System.out.println("Ricerco i libri con TITOLO "+titolo);
        List<Libro> libriTitolo = new ArrayList<>();
        for (Libro l : super.cerca()) {
            if (l.getTitolo().equalsIgnoreCase(this.titolo)) {
                libriTitolo.add(l);
            }
        }
        return libriTitolo;
    }
}
