package org.example.Decorator;

import org.example.Biblioteca.Libro;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class RicercaBase implements RicercaDecorator {
    /*
     * Fornisce il comportamento di base (ossia restituire la lista completa)
     * */

    private List<Libro> biblioteca;

    public RicercaBase(List<Libro> biblioteca) {
        this.biblioteca = biblioteca;
    }

    @Override
    public List<Libro> cerca() {
        System.out.println("Ottengo tutti i libri");
        return biblioteca;
    }
}
