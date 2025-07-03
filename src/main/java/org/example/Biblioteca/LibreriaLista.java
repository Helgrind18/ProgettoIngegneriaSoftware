package org.example.Biblioteca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibreriaLista implements Libreria {
    private List<Libro> libreria = new ArrayList<>();

    @Override
    public boolean aggiungiLibro(Libro libro) {
        if (libro == null || getLibro(libro.getISBN()) != null) {
            return false;
        }
        System.out.println(" ---------- Libreria -------------- ");
        libreria.add(libro);
        System.out.println(libreria);
        return true;
    }


    @Override
    public boolean modificaLibro(Libro vecchio, Libro nuovo) {
        int index = libreria.indexOf(vecchio);
        if (index == -1 || nuovo == null)
            return false;
        libreria.set(index, nuovo);
        return true;
    }


    public List<Libro> getAll(){
        System.out.println("-------- getAll() -----------");
        System.out.println(libreria);
        return this.libreria;
    }

    @Override
    public Iterator<Libro> iterator() {
        return libreria.iterator();
    }
}
