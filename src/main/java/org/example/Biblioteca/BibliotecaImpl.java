package org.example.Biblioteca;

import java.util.*;

public class BibliotecaImpl implements Biblioteca{
    private Map<String, List<Libro>> biblioteca = new HashMap<>();
    @Override
    public boolean aggiungiLibro(Libro l) {
        if (l == null){
            System.out.println("Libro = "+l);
            return false;
        }
        if (esisteISBN(l)){
            System.out.println("Esiste già un libro con quell'isbn");
            return false;
        }
        System.out.println("Devo aggiungere il libro "+l);
        if (!biblioteca.containsKey(l.getAutore())){
            System.out.println("Non esiste l'autore per il libro "+l);
            List<Libro> libri = new LinkedList<>();
            libri.add(l);
            biblioteca.put(l.getAutore(),libri);
            return true;
        }
        System.out.println("Esiste l'autore per il libro, devo modificare la mappa");
        for (Libro libro : biblioteca.get(l.getAutore())){
            if ((libro.getAutore().equalsIgnoreCase(l.getAutore())) && (l.getGenere().equalsIgnoreCase(libro.getGenere()))){
                System.out.println("Esiste già un libro identico");
                return false;
            }
        }
        System.out.println("Aggiunta ok");
        biblioteca.get(l.getAutore()).add(l);
        return true;
    }

    private boolean esisteISBN(Libro l) {
        for (String autore : biblioteca.keySet()){
            for (Libro libro: biblioteca.get(autore)){
                if (libro.getISBN() == l.getISBN())
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean rimuoviLibro(Libro l) {
        ListIterator<Libro> listIterator = biblioteca.get(l.getAutore()).listIterator();
        while (listIterator.hasNext()){
            Libro libro = listIterator.next();
            if (libro.equals(l)){
                System.out.println("Libro da eliminare trovato!");
                listIterator.remove();
                return true;
            }
        }
        System.out.println("Non ho trovato il libro da eliminare");
        return false;
    }

    @Override
    public boolean modificaLibro(Libro l) {
        for (Libro libro: biblioteca.get(l.getAutore())){
            if (libro.getISBN() == l.getISBN()){
                libro.setTitolo(l.getTitolo());
                libro.setAutore(l.getAutore());
                libro.setGenere(l.getGenere());
                libro.setValutazione(l.getValutazione());
                libro.setStatoLettura(l.getStatoLettura());
                System.out.println("Libro aggiornato!");
                return true;
            }
        }
        System.out.println("Libro inesistente");
        return false;
    }

    @Override
    public boolean rimuoviLibriAutore(String autore) {
        if (!biblioteca.containsKey(autore)){
            System.out.println("Autore non esistente!");
            return false;
        }
        biblioteca.remove(autore);
        System.out.println("Libri eliminati!");
        return true;
    }

    @Override
    public List<Libro> cercaPerAutore(String autore) {
        return List.of();
    }

    @Override
    public List<Libro> cercaPerGenere(String genere) {
        return List.of();
    }
}
