package Strategy;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrategyTest {
    private List<Libro> biblioteca;
    private OrdinaContext ctx;

    @BeforeEach
    public void setup() {
        biblioteca = new ArrayList<>();
        biblioteca.add(new Libro(1L, "Libro A", "Autore A", "Genere A", 3, StatoLettura.LETTO));
        biblioteca.add(new Libro(2L, "Libro B", "Autore B", "Genere B", 5, StatoLettura.DA_LEGGERE));
        biblioteca.add(new Libro(3L, "Libro C", "Autore C", "Genere C", 1, StatoLettura.IN_LETTURA));
        biblioteca.add(new Libro(4L, "Libro D", "Autore D", "Genere D", 5, StatoLettura.LETTO));
        ctx = new OrdinaContext(biblioteca);
    }

    @Test
    @DisplayName("OrdinamentoValutazione")
    public void testOrdinamentoValutazione() {
        ctx.setStrategy(new OrdinaValutazione());
        List<Libro> ordinata = ctx.ordina();
        assertEquals(1, ordinata.get(0).getValutazione());
        assertEquals(3, ordinata.get(1).getValutazione());
        assertEquals(5, ordinata.get(2).getValutazione());
        assertEquals(5, ordinata.get(3).getValutazione());
    }

    @Test
    @DisplayName("OrdinamentoTitolo")
    public void testOrdinamentoTitolo() {
        ctx.setStrategy(new OrdinaTitolo());
        List<Libro> ordinata = ctx.ordina();
        assertEquals("Libro A", ordinata.get(0).getTitolo());
        assertEquals("Libro B", ordinata.get(1).getTitolo());
        assertEquals("Libro C", ordinata.get(2).getTitolo());
        assertEquals("Libro D", ordinata.get(3).getTitolo());
    }

    @Test
    @DisplayName("OrdinamentoGenere")
    public void testOrdinamentoGenere() {
        ctx.setStrategy(new OrdinaGenere());
        List<Libro> ordinata = ctx.ordina();
        assertEquals("Genere A", ordinata.get(0).getGenere());
        assertEquals("Genere B", ordinata.get(1).getGenere());
        assertEquals("Genere C", ordinata.get(2).getGenere());
        assertEquals("Genere D", ordinata.get(3).getGenere());
    }

    @Test
    @DisplayName("OrdinamentoISBN")
    public void testOrdinamentoISBN() {
        ctx.setStrategy(new OrdinaISBN());
        List<Libro> ordinata = ctx.ordina();
        assertEquals(1, ordinata.get(0).getISBN());
        assertEquals(2, ordinata.get(1).getISBN());
        assertEquals(3, ordinata.get(2).getISBN());
        assertEquals(4, ordinata.get(3).getISBN());
    }
}
