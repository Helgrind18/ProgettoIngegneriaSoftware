package org.example.LibreriaTemplate;

import org.example.Biblioteca.Libreria;
import org.example.Biblioteca.Libro;

import java.io.*;

public class LibreriaCSV extends LibreriaTemplate {
    public LibreriaCSV(File fileLibreria, Libreria libreria) {
        super(fileLibreria, libreria);
    }

    private BufferedReader reader; // variabile necessaria per poter aprire il file e leggere linea per linea

    @Override
    protected void apriFile() {
        try {
            reader = new BufferedReader(new FileReader(fileLibreria));
        } catch (IOException e) {
            throw new RuntimeException("Errore nell'apertura del file CSV", e);
        }
    }

    @Override
    protected String leggiLinea() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura della linea da CSV", e);
        }
    }

    @Override
    protected Libro ottieniLibro(String linea) {
        return super.ottieniLibro(linea, ",");
    }

    @Override
    protected void chiudiFile() {
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Errore nella chiusura del file CSV", e);
        }
    }


    @Override
    public void scriviSuFile(Libro nuovo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria, true), true)) {
            pw.println(nuovo.getISBN() + "," +
                    nuovo.getTitolo() + "," +
                    nuovo.getAutore() + "," +
                    nuovo.getGenere() + "," +
                    nuovo.getValutazione() + "," +
                    nuovo.getStatoLettura());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sovrascriviFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileLibreria, false))) {
            for (Libro l : getBiblitoeca()) {
                pw.println(
                        l.getISBN() + "," +
                                l.getTitolo() + "," +
                                l.getAutore() + "," +
                                l.getGenere() + "," +
                                l.getValutazione() + "," +
                                l.getStatoLettura()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
