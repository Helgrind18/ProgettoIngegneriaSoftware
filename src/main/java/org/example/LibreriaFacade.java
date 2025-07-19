package org.example;


import org.example.Biblioteca.Libreria;
import org.example.Biblioteca.LibreriaLista;
import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Decorator.Ricerca.*;
import org.example.LibreriaTemplate.LibreriaCSV;
import org.example.LibreriaTemplate.LibreriaJSON;
import org.example.LibreriaTemplate.LibreriaTemplate;
import org.example.Strategy.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//Questa classe deve aggiungere un grado di indirizzamento fra il client ed il resto del sistema.
//Aumentando l'indipendenza fra la rappresentazione dell'oggetto e l'utilizzo del sistema
public class LibreriaFacade {
    private LibreriaTemplate libreria;
    private Libreria biblioteca = new LibreriaLista();
    private OrdinaContext ctx;
    private RicercaDecorator ricerca;

    public LibreriaFacade(File file) {
        this.libreria = new LibreriaJSON(file,biblioteca); // Scelgo quale tipologia del file leggere
        this.ctx = new OrdinaContext(biblioteca.getAll());
        this.ricerca = new RicercaBase(biblioteca.getAll()); // Ottengo la lista completa
    }
    public List<Libro> getAll() {
        System.out.println("Libreria: getAll()");
        libreria.esegui();
        return biblioteca.getAll();
    }

    public List<Libro> cerca(String titolo, String autore, String genere, String statoLettura) {
        System.out.println("Ricerca avviata con campi: " + titolo + " " + autore + " " + genere+" "+statoLettura);
        ricerca = new RicercaBase(biblioteca.getAll()); // Ricerca base a prescindere da quello che l'utente fornisce
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
        if (!statoLettura.isEmpty()){
            ricerca = new RicercaStato(ricerca,StatoLettura.valueOf(statoLettura));
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
            case "Stato":
                ctx.setStrategy(new OrdinaStatoLettura());
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ctx.ordina();
    }

    public boolean aggiungiLibro(Libro l) {
        boolean aggiunto = biblioteca.aggiungiLibro(l);
        if (aggiunto) {
            libreria.scriviSuFile(l);
        }
        return aggiunto;
    }

    public boolean modificaLibro(Libro vecchio, Libro nuovo) {
        boolean modifica = biblioteca.modificaLibro(vecchio,nuovo);
        if (modifica)
            libreria.sovrascriviFile();
        return modifica;
    }

    public boolean rimuoviLibro(Libro l) {
        boolean rimozione = biblioteca.rimuoviLibro(l);
        if (rimozione)
            libreria.sovrascriviFile();
        return rimozione;
    }

    public Libro ottieniLibro(long isbn) {
        return biblioteca.getLibro(isbn);
    }

}
