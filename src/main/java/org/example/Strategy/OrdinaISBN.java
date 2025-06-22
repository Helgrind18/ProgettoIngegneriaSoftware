package org.example.Strategy;

import org.example.Biblioteca.Libro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrdinaISBN implements OrdinaStrategy{
    @Override
    public List<Libro> ordina(List<Libro> biblioteca) {
        List<Libro> copia = new ArrayList<>(biblioteca);

        Collections.sort(copia, new Comparator<Libro>() {
            // Definisco un mio comparatore
            @Override
            public int compare(Libro o1, Libro o2) {
                return Long.compare(o1.getISBN(),o2.getISBN());
            }
        });
        return copia;
    }
}
