/**
 * 
 */
package it.unicam.cs.asdl2021.slides.equazioni2g;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTestImplemented {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#EquazioneSecondoGradoModificabileConRisolutore(double, double, double)}.
     */
    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#getA()}.
     */
    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#setA(double)}.
     */
    @Test
    final void testSetA() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // controllo che il valore 0 su a lanci un'eccezione
        assertThrows(IllegalArgumentException.class, () -> e1.setA(0));
        // metto un nuovo valore
        e1.setA(3);
        // controllo che lo stato dell'equazione risulti "non risolto"
        assertFalse(e1.isSolved());
        // controllo che il valore inserito è stato effettivamente memorizzato
        assertTrue(e1.getA() == 3);
        // anche in questo caso non ci sono manipolazioni algebriche quindi non
        // ci saranno errori di approssimazione da compensare usando
        // assertTrue(Math.abs(e1.getA()-3) < EPSILON)
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#getB()}.
     */
    @Test
    final void testGetB() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, x, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getB());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getB())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#setB(double)}.
     */
    @Test
    final void testSetB() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // metto un nuovo valore
        e1.setB(3);
        // controllo che lo stato dell'equazione risulti "non risolto"
        assertFalse(e1.isSolved());
        // controllo che il valore inserito è stato effettivamente memorizzato
        assertTrue(e1.getB() == 3);
        // anche in questo caso non ci sono manipolazioni algebriche quindi non
        // ci saranno errori di approssimazione da compensare usando
        // assertTrue(Math.abs(e1.getB()-3) < EPSILON)
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#getC()}.
     */
    @Test
    final void testGetC() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, x);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getC());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getC())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#setC(double)}.
     */
    @Test
    final void testSetC() {
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // metto un nuovo valore
        e1.setC(3);
        // controllo che lo stato dell'equazione risulti "non risolto"
        assertFalse(e1.isSolved());
        // controllo che il valore inserito è stato effettivamente memorizzato
        assertTrue(e1.getC() == 3);
        // anche in questo caso non ci sono manipolazioni algebriche quindi non
        // ci saranno errori di approssimazione da compensare usando
        // assertTrue(Math.abs(e1.getC()-3) < EPSILON)
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#isSolved()}.
     */
    @Test
    final void testIsSolved() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // all’inizio controllo che l’equazione non sia risolta
        assertFalse(e3.isSolved());
        // risolvo
        e3.solve();
        // controllo che sia risolta
        assertTrue(e3.isSolved());
        // cambio un parametro
        e3.setB(45);
        // controllo che non sia risolta
        assertFalse(e3.isSolved());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#solve()}.
     */
    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl2021.slides.equazioni2g.EquazioneSecondoGradoModificabileConRisolutore#getSolution()}.
     */
    @Test
    final void testGetSolution() {
        // creo l'oggetto equazione che mi servirà per tutti i test
        EquazioneSecondoGradoModificabileConRisolutore e = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // controllo che chiamando il metodo ora venga lanciata l'eccezione
        assertThrows(IllegalStateException.class, () -> e.getSolution());
        // risolvo l'equazione
        e.solve();
        // estraggo la soluzione (non deve essere lanciata l'eccezine)
        SoluzioneEquazioneSecondoGrado s1 = e.getSolution();
        // caso senza soluzioni
        assertTrue(s1.isEmptySolution());
        // caso con due soluzioni reali distinte
        e.setB(-3);
        e.setC(2);
        e.solve();
        SoluzioneEquazioneSecondoGrado s2 = e.getSolution();
        assertFalse(s2.isEmptySolution());
        assertFalse(s2.isOneSolution());
        // non so l'ordine in cui vengono date le soluzioni, quindi provo
        // entrambe le possibilità
        assertTrue(Math.abs(s2.getS1() - 1) < EPSILON
                || Math.abs(s2.getS1() - 2) < EPSILON);
        assertTrue(Math.abs(s2.getS2() - 1) < EPSILON
                || Math.abs(s2.getS2() - 2) < EPSILON);
        // caso con due soluzioni reali coincidenti
        e.setB(-2);
        e.setC(1);
        e.solve();
        SoluzioneEquazioneSecondoGrado s3 = e.getSolution();
        assertFalse(s3.isEmptySolution());
        assertTrue(s3.isOneSolution());
        assertTrue(Math.abs(s3.getS1() - 1) < EPSILON);
        // equazione con due soluzioni reali con decimali e parametro a diverso
        // da 1
        e.setA(2);
        e.setB(-10);
        e.setC(2);
        e.solve();
        SoluzioneEquazioneSecondoGrado s4 = e.getSolution();
        assertFalse(s4.isEmptySolution());
        assertFalse(s4.isOneSolution());
        assertTrue(Math.abs(s4.getS1() - 4.7912878474779195) < EPSILON
                || Math.abs(s4.getS1() - 0.20871215252208009) < EPSILON);
        assertTrue(Math.abs(s4.getS2() - 4.7912878474779195) < EPSILON
                || Math.abs(s4.getS2() - 0.20871215252208009) < EPSILON);
    }

}
