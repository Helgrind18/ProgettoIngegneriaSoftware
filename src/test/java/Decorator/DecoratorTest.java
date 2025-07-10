package Decorator;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Decorator.Ricerca.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DecoratorTest {

    private List<Libro> biblioteca;
    private RicercaDecorator ricercaDecorator;

    @BeforeEach
    public void setup() {
        biblioteca = new ArrayList<>();
        biblioteca.add(new Libro(1L, "Libro A", "Autore A", "Genere B", 3, StatoLettura.LETTO));
        biblioteca.add(new Libro(2L, "Libro B", "Autore A", "Genere B", 5, StatoLettura.DA_LEGGERE));
        biblioteca.add(new Libro(3L, "Libro C", "Autore A", "Genere C", 1, StatoLettura.IN_LETTURA));
        biblioteca.add(new Libro(4L, "Libro A", "Autore D", "Genere D", 5, StatoLettura.LETTO));
    }

    @Test
    @DisplayName("Test ricerca base")
    public void ricercaBase(){
        ricercaDecorator = new RicercaBase(biblioteca);
        List<Libro> risultato = ricercaDecorator.cerca();
        System.out.println(risultato);
        assertEquals(4,risultato.size());
    }

    @Test
    @DisplayName("Test ricerca autore")
    public void ricercaAutore(){
        String autore = "Autore A";
        ricercaDecorator = new RicercaAutore(new RicercaBase(biblioteca),autore);
        List<Libro> risultato = ricercaDecorator.cerca();
        System.out.println(risultato);
        assertEquals(3,risultato.size());

    }

    @Test
    @DisplayName("Test ricerca genere")
    public void ricercaGenere(){
        String genere = "Genere B";
        ricercaDecorator = new RicercaGenere(new RicercaBase(biblioteca),genere);
        List<Libro> risultato = ricercaDecorator.cerca();
        System.out.println(risultato);
        assertEquals(2,risultato.size());
    }

    @Test
    @DisplayName("Test ricerca titolo")
    public void ricercaTitolo(){
        String titolo = "Libro A";
        ricercaDecorator = new RicercaTitolo(new RicercaBase(biblioteca),titolo);
        List<Libro> risultato = ricercaDecorator.cerca();
        System.out.println(risultato);
        assertEquals(2,risultato.size());
    }

    @Test
    @DisplayName("Test ricerca composta")
    public void ricercaComposta(){
        String titolo = "Libro A";
        String genere = "Genere B";
        String autore = "Autore A";
        ricercaDecorator = new RicercaGenere(new RicercaAutore(new RicercaTitolo(new RicercaBase(biblioteca),titolo),autore),genere);
        List<Libro> risultato = ricercaDecorator.cerca();
        System.out.println(risultato);
        assertEquals(1,risultato.size());
    }


}
