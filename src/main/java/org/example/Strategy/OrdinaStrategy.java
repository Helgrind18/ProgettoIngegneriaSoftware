package org.example.Strategy;

import org.example.Biblioteca.Libro;

import java.util.List;

public interface OrdinaStrategy {
    List<Libro> ordina (List<Libro> biblioteca);
}
