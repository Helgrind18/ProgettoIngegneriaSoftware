package org.example;

import org.example.Biblioteca.Libro;
import org.example.Decorator.Ricerca.*;
import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.LibreriaTemplate.LibreriaJSON;
import org.example.LibreriaTemplate.LibreriaTemplate;
import org.example.Strategy.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibreriaFacade {
    //Questa classe deve aggiungere un grado di indirezzamento fra il client ed il resto del sistema.
    private LibreriaTemplate libreria;
    private OrdinaContext ctx;
    private RicercaDecorator ricerca;

    public LibreriaFacade(File file) {
        this.libreria = new LibreriaCSV(file);
        this.ctx = new OrdinaContext(libreria.getBiblitoeca());
        this.ricerca = new RicercaBase(libreria.getBiblitoeca()); // Ottengo la lista completa
    }

    public List<Libro> getAll() {
        System.out.println("LibreriaFacade: getAll()");
        libreria.esegui();
        return libreria.getBiblitoeca();
    }

    public List<Libro> cerca(String titolo, String autore, String genere) {
        System.out.println("Ricerca avviata con campi: " + titolo + " " + autore + " " + genere);
        ricerca = new RicercaBase(libreria.getBiblitoeca()); // Ricerca base a prescindere da quello che l'utente fornisce
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

    public List<Libro> ordina(List<Libro> lista, String criterio) {
        ctx = new OrdinaContext(lista);
        switch (criterio) {
            case "Valutazione":
                ctx.setStrategy(new OrdinaValutazione());
                break;
            case "Titolo":
                ctx.setStrategy(new OrdinaTitolo());
                break;
            case "Genere":
                ctx.setStrategy(new OrdinaGenere());
                break;
            case "ISBN":
                ctx.setStrategy(new OrdinaISBN());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ctx.ordina();
    }

    public boolean aggiungiLibro(Libro l) {
        boolean aggiunto = libreria.aggiungiLibro(l);
        if (aggiunto) {
            libreria.scriviSuFile(l);
        }
        return aggiunto;
    }

    public boolean modificaLibro(Libro vecchio, Libro nuovo) {
        return libreria.modificaLibro(vecchio, nuovo);
    }

    public boolean rimuoviLibro(Libro l) {
        return libreria.rimuoviLibro(l);
    }

    public Libro ottieniLibro(long isbn) {
        return libreria.getLibro(isbn);
    }

}
