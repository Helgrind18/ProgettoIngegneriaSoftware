package Strategy;

import org.example.Biblioteca.Libro;
import org.example.Biblioteca.StatoLettura;
import org.example.Strategy.OrdinaValutazione;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
public class OrdinaValutazioneTest {

    private OrdinaValutazione strategy;
    private List<Libro> biblioteca;

    @BeforeEach
    void setUp() {
        strategy = new OrdinaValutazione();
        biblioteca = Arrays.asList(
                new Libro(1111L, "Libro A", "Autore A", "Genere A", 4, StatoLettura.DA_LEGGERE),
                new Libro(2222L, "Libro B", "Autore B", "Genere B", 2, StatoLettura.LETTO),
                new Libro(3333L, "Libro C", "Autore C", "Genere C", 5, StatoLettura.IN_LETTURA),
                new Libro(4444L, "Libro D", "Autore D", "Genere D", 3, StatoLettura.LETTO)
        );
    }

    @Test
    @DisplayName("Ordina in ordine crescente di valutazione")
    public void testOrdinaValutazione_Crescente() {
        List<Libro> sorted = strategy.ordina(biblioteca);

        assertEquals(4, sorted.size());
        // Le valutazioni attese in ordine: 2 (B), 3 (D), 4 (A), 5 (C)
        assertEquals(2222L, sorted.get(0).getISBN());
        assertEquals(4444L, sorted.get(1).getISBN());
        assertEquals(1111L, sorted.get(2).getISBN());
        assertEquals(3333L, sorted.get(3).getISBN());
    }

    @Test
    @DisplayName("Non altera la lista originale")
    public void testOriginaleRestInalterata() {
        List<Libro> copia = new ArrayList<>(biblioteca);
        strategy.ordina(biblioteca);
        assertEquals(copia, biblioteca, "La lista passata deve rimanere invariata");
    }

    @Test
    @DisplayName("Gestisce lista vuota")
    public void testOrdinaListaVuota() {
        List<Libro> vuota = Collections.emptyList();
        List<Libro> result = strategy.ordina(vuota);
        assertTrue(result.isEmpty(), "Il risultato su lista vuota deve essere vuoto");
    }
}
