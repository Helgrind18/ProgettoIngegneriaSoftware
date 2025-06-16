package org.example.Biblioteca;

import java.util.Iterator;
import java.util.List;

public interface Biblioteca{
    boolean aggiungiLibro(Libro l);
    boolean rimuoviLibro(Libro l);

    boolean modificaLibro(Libro l);
    boolean rimuoviLibriAutore(String autore);

    List<Libro> cercaPerAutore(String autore);

    List<Libro> cercaPerGenere(String genere);
}
