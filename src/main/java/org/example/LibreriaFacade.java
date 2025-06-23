package org.example;

import org.example.Biblioteca.Libro;
import org.example.Decorator.Ricerca.*;
import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.LibreriaTemplate.LibreriaTemplate;
import org.example.Strategy.*;

import java.io.File;
import java.util.List;

public class LibreriaFacade {
    //Questa classe deve aggiungere un grado di indirezzamento fra il client ed il resto del sistema.
    private LibreriaTemplate libreria;
    private OrdinaContext gestore;
    private RicercaDecorator ricerca;

    public LibreriaFacade(File file) {
        this.libreria = new LibreriaCSV(file);
        libreria.esegui();
        this.gestore = new OrdinaContext(libreria.getBiblitoeca());
        this.ricerca = new RicercaBase(libreria.getBiblitoeca()); // Ottengo la lista completa
    }

    public List<Libro> getAll(){
        return libreria.getBiblitoeca();
    }
    public List<Libro> cerca(String titolo, String autore, String genere){
        // Aggiungo decorator in base al campo non vuoto
        if (!titolo.isEmpty()) {
            ricerca = new RicercaTitolo(ricerca, titolo);
        }
        if (!autore.isEmpty()) {
            ricerca = new RicercaAutore(ricerca, autore);
        }
        if (!genere.isEmpty()) {
            ricerca = new RicercaGenere(ricerca, genere);
        }

        // Avvio la ricerca
        return ricerca.cerca();
    }
    public List<Libro> ordina(String criterio) {
        switch (criterio) {
            case "Valutazione":
                gestore.setStrategy(new OrdinaValutazione());
                break;
            case "Titolo":
                gestore.setStrategy(new OrdinaTitolo());
                break;
            case "Genere":
                gestore.setStrategy(new OrdinaGenere());
                break;
            case "ISBN":
                gestore.setStrategy(new OrdinaISBN());
                break;
        }
        return gestore.ordina();
    }

    public boolean aggiungiLibro(Libro l) {
        boolean aggiunto = libreria.aggiungiLibro(l);
        if (aggiunto) {
            libreria.scriviSuFile(l);
        }
        return aggiunto;
    }

    public boolean modificaLibro(Libro vecchio, Libro nuovo){
        return libreria.modificaLibro(vecchio,nuovo);
    }

    public boolean rimuoviLibro(Libro l){
        return libreria.rimuoviLibro(l);
    }

    public Libro ottieniLibro(long isbn){
        return libreria.getLibro(isbn);
    }

}
