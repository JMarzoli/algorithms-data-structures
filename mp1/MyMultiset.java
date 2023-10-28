package it.unicam.cs.asdl2122.mp1;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.HashSet; 

/**
 * Il multiset è impelementato attraverso una mappa che associa
 * ad una chiave di tipo {@code E} un valore di tipo Integer.
 * Quest'ultimo rappresenta la molteplicità dell'elemento 
 * nell'insieme, ovvero il numero delle sue occorrenze. 
 * Questo è necessario per ottimizzare il consumo di memoria.
 * 
 * @author Luca Tesei (template) 
 *         Julian Marzoli julian.marzoli@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {

	/*
	 * La mappa rapresentante il Multiset
	 */
	private Map<E,Integer> multiset;
    
	/*
	 * Numero di occorrenze totali degli elementi nel Multiset
	 */
	private int size;
	
	/*
	 * Numero di modifiche effettuate al Multiset dal
	 * momento della sua creazione 
	 */
	private int numChanghes; 
	
	/*
     * Restituisce un iteratore per questo multinsieme. L'iteratore deve
     * presentare tutti gli elementi del multinsieme (in un ordine qualsiasi) e
     * per ogni elemento deve presentare tutte le occorrenze. Le occorrenze
     * dello stesso elemento devono essere presentate in sequenza. L'iteratore
     * restituito non implementa l'operazione {@code remove()}.
     * 
     * L'iteratore restituito deve essere <b>fail-fast</b>: se il multinsieme
     * viene modificato strutturalmente (cioè viene fatta un'aggiunta o una
     * cancellazione di almeno un'occorrenza) in qualsiasi momento dopo la
     * creazione dell'iteratore, l'iteratore dovrà lanciare una
     * {@code ConcurrentModificationException} alla chiamata successiva del
     * metodo {@code next()}.
     */
    private class Itr implements Iterator<E>{
    	
    	private int numExpectedChanghes;
    	
    	private Map.Entry<E,Integer> currEntry;
    	
    	private int occLeft = 0; 
    	
    	private E lastReturned; 
    	
    	Iterator<Map.Entry<E, Integer>> entries = multiset.entrySet().iterator();
    	
    	private Itr() {
    		this.lastReturned = null;
    		this.numExpectedChanghes = MyMultiset.this.numChanghes;
    	}
    	
    	@Override
        public boolean hasNext() {
    		// se sono alla prima iterazione
        	if(this.lastReturned == null)
        		// se il Multiset è vuoto non ci sarà un elemento da ritornare
        		// se il Multiset non è vuoto ci sarà un elemento da ritornare
        		return !MyMultiset.this.multiset.isEmpty();
        	// altrimenti vedo se l'iteratore degli elementi ha un next
        	// oppure se l'ultimo elemento corrente ha ancora molteplicità
        	return (entries.hasNext() || occLeft > 0);
    	}
    	
    	@Override
        public E next() {
    		// se il Multiset viene modificato durante l'iterazione lancio un eccezione
    		if(this.numExpectedChanghes != MyMultiset.this.numChanghes)
                throw new ConcurrentModificationException(
                		"Multiset modificato durante l'iterazione");
    		// se il Multiset non ha un altro elemento da iterare lancio un eccezione
            if(!this.hasNext())
                throw new NoSuchElementException(
                		"Richiesta di next non disponibile");
        	// se ho esaurito le occorrenze di questo elemento
            // o sono all'inizio dell'iterazione
            if(occLeft == 0) {
        		// passo al prossimo elemento
            	currEntry = entries.next();
            	// ripristino la molteplicità residua dell'elemento
        		occLeft = currEntry.getValue();
            }
            // recupero la chiave dell'elemento da ritornare
            lastReturned = currEntry.getKey();
        	// diminuisco la molteplicità residua di questo elemento
            occLeft--;
        	// ritorno l'ultimo elemento
        	return lastReturned; 
    	} 	
    }

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
    	this.multiset = new HashMap<E,Integer>();
    	this.size = 0;
    	this.numChanghes = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int count(Object element) {
    	if(element == null)
    			throw new NullPointerException(
    					"Tentativo di contare le occorrenze di un elemento nullo");
    	if(!this.contains(element))
    		return 0; 
    	return multiset.get(element);
    }

    @Override
    public int add(E element, int occurrences) {
        // TODO Implementare
    	if(element == null)
			throw new NullPointerException(
					"Tentativo di aggiungere un elemento nullo");
    	if(occurrences < 0 || count(element) + occurrences > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"Tentativo di aggiungere un numero di occorrenze non valido");
			}
        // se le occorrenze da aggiungere sono zero
    	if(occurrences == 0)
        	// non faccio nulla e torno le occorrenze attuali
    		return count(element);
    	// altrimenti aggiungo le occorrenze 
    	return setCount(element, count(element) + occurrences);
        
    }

    @Override
    public void add(E element) {
    	if(element == null)
			throw new NullPointerException(
					"Tentativo di aggiungere un elemento nullo");
    	if(count(element) > Integer.MAX_VALUE)
			throw new IllegalArgumentException(
					"Tentativo di aggiungere sforare il numero di occorrenze massimo");
    	this.add(element, 1);
    }

    @Override
    public int remove(Object element, int occurrences) {
    	if(element == null)
			throw new NullPointerException(
					"Tentativo di rimuovere un elemento nullo");
    	if(occurrences < 0)
			throw new IllegalArgumentException(
					"Tentativo di rimuovere un numero di occorrenze negativo");
    	// se l'elemento non è contenuto non faccio nulla
    	if(!this.contains(element))
        	// ritorno il numero di occorrenze 
    		return 0;
    	// recupero il numero attuale di occorenze dell'elemento
    	Integer occ = count(element);
    	// se le occorrenze da rimuovere sono uguali o più di quelle presenti
    	if(occ <= occurrences) {
    		// rimuovo l'elemento dal multiset
    		this.multiset.remove(element);
    		// aggiorno i contatori 
    		this.numChanghes++;
    		this.size -= occ; 
    		// ritorno il numero di occorenze precedenti 
    		return occ; 
    	}
    	// altrimenti tolgo dalla molteplicità le occorrenze richieste
    	return setCount((E) element, occ - occurrences);
    }

    @Override
    public boolean remove(Object element) {
    	if(element == null)
			throw new NullPointerException(
					"Tentativo di rimuovere un elemento nullo");
    	// elemento presente
    	if(this.contains(element)) {
    		//rimuovo una occorrenza
    		this.remove(element, 1);
    		return true;
    	}
    	// elemento non presente
        return false;
    }

    @Override
    public int setCount(E element, int count) {
    	if(element == null)
			throw new NullPointerException(
					"Tentativo di aggiungere/rimuovere un elemento nullo");
    	if(count < 0)
			throw new IllegalArgumentException(
					"Tentativo di settare le occorrenze ad un numero negativo");
    	// se l'elemento non è già contenuto
    	if(!contains(element)) {
    		// lo aggiungo
    		multiset.put(element, count);
    		// aggiorno i contatori
    		this.size += count;
    		this.numChanghes++;
    		// ritorno 0
    		return 0;
    		}
    	// altrimenti è già contenuto
    	// salvo il numero di occorrenze contenute
    	int occ = count(element);
    	// se le occorrenze sono già quelle richieste non faccio nulla
    	if(occ == count)
    		// ritorno il numero di occorrenze 
    		return occ;
    	// altrimenti modifico la size
    	this.size -= occ;
    	this.size += count;
    	// aggiungo una modifica
    	this.numChanghes++;
    	// ritorno le vecchie occorrenze
    	return this.multiset.replace(element, count); 
    }

    @Override
    public Set<E> elementSet() {
    	// se il multiset è vuoto
    	if(this.multiset.isEmpty())
    		// ritorno un Set vuoto
    		return new HashSet<E>();;
    	// altrimenti ritorno un Set contenente tutte le
    	// chiavi nella mappa
    	return this.multiset.keySet();
    }

    @Override
    public Iterator<E> iterator() {
        // ritorno un oggetto della classe {@code Itr}
        return new Itr();
    }

    @Override
    public boolean contains(Object element) {
    	if(element == null)
			throw new NullPointerException(
					"Tentativo di cercare un elemento nullo");
    	// verifico se la mappa contiene la chiave
    	// associata all'elemento 
    	return this.multiset.containsKey(element);
    }

    @Override
    public void clear() {
    	// elimino tutti gli elementi nella mappa
    	this.multiset.clear();	
    	// aggiungo una modifica 
    	this.numChanghes++;
    }

    @Override
    public boolean isEmpty() {
        return this.multiset.isEmpty();
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * Appunto: le righe da 305 a 315 potevano essere omesse, in quanto il
     * metodo {@code equals} chiamato sulle mappe a riga 318 effettua 
     * gli stessi controlli. Non sono però sicuro che questo vada bene ai 
     * fini dell'esame, perciò le ho lasciate.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
    	if (obj == null)
            return false;
        if (this == obj)
            return true;
    	if(!(obj instanceof MyMultiset))
    		return false;
    	MyMultiset<?> other = (MyMultiset<?>) obj;
    	// se i due Multiset hanno dimensioni diverse
    	// non possono essere uguali
    	if(this.size() != other.size())
    		return false;
    	// se i due Multiset sono entrambi vuoti sono uguali
    	if(this.isEmpty() && other.isEmpty())
    		return true;
    	// se un Multiset è vuoto e l'altro no 
    	// non possono essere uguali
    	if(this.isEmpty() && !other.isEmpty()) 
    			return false; 
    	if(!this.isEmpty() && other.isEmpty()) 
        		return false; 
    	// altrimenti devo controllare se le mappe
    	// dei due Multiset hanno gli stessi elementi
    	return this.multiset.equals(other.multiset);
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	final int prime = 31;
    	int result = 1; 
    	// hashcode calcolato tramite ogni elemento contenuto 
    	// nel Multiset e la sua molteplicità
    	for(Map.Entry<E, Integer> entry : this.multiset.entrySet()) {
    		result = prime * result + (entry.getKey().hashCode());
    		result = prime * result + (entry.getValue().hashCode());
    	}
        return result;
    }

}
