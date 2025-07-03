package org.example.Biblioteca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface Libreria extends Iterable<Libro> {
    boolean aggiungiLibro(Libro libro);

    default boolean rimuoviLibro(Libro libro) {
        Iterator<Libro> it = this.iterator();
        while (it.hasNext()) {
            if (it.next().equals(libro)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    boolean modificaLibro(Libro vecchio, Libro nuovo);

    default Libro getLibro(long isbn) {
        Iterator<Libro> it = this.iterator();
        while (it.hasNext()) {
            Libro curr = it.next();
            if (curr.getISBN() == isbn) {
                return curr;
            }
        }
        return null;
    }

    default List<Libro> getAll(){
        Iterator<Libro> it = this.iterator();
        List<Libro> getAll = new ArrayList<>();
        while (it.hasNext()){
            getAll.add(it.next());
        }
        return getAll;
    }
}
