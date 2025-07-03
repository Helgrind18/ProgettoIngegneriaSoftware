package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libreria;
import org.example.Biblioteca.LibreriaLista;
import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;

import java.io.File;
import java.util.*;

public abstract class LibreriaTemplate {
    protected File fileLibreria;
    protected Libreria libreria;

    public LibreriaTemplate(File fileLibreria, Libreria libreria) {
        this.fileLibreria = fileLibreria;
        this.libreria = libreria;
    }

    public List<Libro> getBiblitoeca() {
        return libreria.getAll();
    }

    public void esegui() {
        apriFile();
        while (true){
            String linea = leggiLinea();
            if (linea == null){
                System.out.println("Eof");
                break;
            }
            Libro l = ottieniLibro(linea);
            if (l != null)
                libreria.aggiungiLibro(l);
        }
        chiudiFile();
    }

    protected abstract void apriFile();

    protected abstract String leggiLinea();

    protected abstract Libro ottieniLibro(String linea);

    protected abstract void chiudiFile();


    public abstract void scriviSuFile(Libro nuovo);


    //Consente di modificare il libro. Nota bene: non posso modificare un file, non posso inserire in mezzo un libro modificato
    public abstract void sovrascriviFile();

    protected Libro ottieniLibro(String linea, String delimitatore) {
        StringTokenizer st = new StringTokenizer(linea, delimitatore);
        long isbn = Long.parseLong(st.nextToken());
        String titolo = st.nextToken();
        String autore = st.nextToken();
        String genere = st.nextToken();
        int valutazione = Integer.parseInt(st.nextToken());
        StatoLettura statoLettura = StatoLettura.valueOf(st.nextToken());
        Libro l = new Libro(isbn, titolo, autore, genere, valutazione, statoLettura);
        System.out.println("Libro ricostruito = " + l);
        return l;
    }

}
