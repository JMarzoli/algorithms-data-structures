package it.unicam.cs.asdl2122.mp2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Classe di test per la classe UndirectedGraphConnectedComponentsComputer.
 * 
 * @author Luca Tesei
 *
 */
class UndirectedGraphConnectedComponentsComputerTest {

    @Test
    final void testComputeConnectedComponentsExceptionsAndTrivialCases() {
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<String>();
        assertThrows(NullPointerException.class,
                () -> c.computeConnectedComponents(null));
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<String>();
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        assertTrue(c.computeConnectedComponents(g).equals(result));
        g.addNode(new GraphNode<String>("a"));
        Set<GraphNode<String>> comp1 = new HashSet<>();
        comp1.add(new GraphNode<String>("a"));
        result.add(comp1);
        assertTrue(c.computeConnectedComponents(g).equals(result));
        g.addNode(new GraphNode<String>("b"));
        Set<GraphNode<String>> comp2 = new HashSet<>();
        comp2.add(new GraphNode<String>("b"));
        result.add(comp2);
        assertTrue(c.computeConnectedComponents(g).equals(result));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("a"),
                new GraphNode<String>("b"), false));
        result.remove(comp2);
        comp1.add(new GraphNode<String>("b"));
        assertTrue(c.computeConnectedComponents(g).equals(result));
    }

    @Test
    final void testComputeConnectedComponents() {
        UndirectedGraphConnectedComponentsComputer<String> c = new UndirectedGraphConnectedComponentsComputer<String>();
        Graph<String> g = new AdjacencyMatrixUndirectedGraph<String>();
        g.addNode(new GraphNode<String>("a"));
        g.addNode(new GraphNode<String>("b"));
        g.addNode(new GraphNode<String>("c"));
        g.addNode(new GraphNode<String>("d"));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("a"),
                new GraphNode<String>("b"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("d"),
                new GraphNode<String>("b"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("c"),
                new GraphNode<String>("b"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("c"),
                new GraphNode<String>("d"), false));
        Set<Set<GraphNode<String>>> result = new HashSet<>();
        Set<GraphNode<String>> comp1 = new HashSet<>();
        comp1.add(new GraphNode<String>("a"));
        comp1.add(new GraphNode<String>("b"));
        comp1.add(new GraphNode<String>("c"));
        comp1.add(new GraphNode<String>("d"));
        result.add(comp1);
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addNode(new GraphNode<String>("e"));
        g.addNode(new GraphNode<String>("f"));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("f"),
                new GraphNode<String>("e"), false));
        Set<GraphNode<String>> comp2 = new HashSet<>();
        comp2.add(new GraphNode<String>("e"));
        comp2.add(new GraphNode<String>("f"));
        result.add(comp2);
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addNode(new GraphNode<String>("g"));
        Set<GraphNode<String>> comp3 = new HashSet<>();
        comp3.add(new GraphNode<String>("g"));
        result.add(comp3);
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addEdge(new GraphEdge<String>(new GraphNode<String>("g"),
                new GraphNode<String>("c"), false));
        result.remove(comp3);
        comp1.add(new GraphNode<String>("g"));
        assertTrue(c.computeConnectedComponents(g).equals(result));

        g.addEdge(new GraphEdge<String>(new GraphNode<String>("g"),
                new GraphNode<String>("g"), false));
        g.addEdge(new GraphEdge<String>(new GraphNode<String>("g"),
                new GraphNode<String>("f"), false));
        result.remove(comp2);
        comp1.add(new GraphNode<String>("f"));
        comp1.add(new GraphNode<String>("e"));
        assertTrue(c.computeConnectedComponents(g).equals(result));

        Graph<String> g1 = new AdjacencyMatrixUndirectedGraph<String>();
        result.clear();
        ;
        g1.addNode(new GraphNode<String>("a"));
        g1.addNode(new GraphNode<String>("b"));
        g1.addEdge(new GraphEdge<String>(new GraphNode<String>("a"),
                new GraphNode<String>("b"), false));
        comp1.clear();
        ;
        comp1.add(new GraphNode<String>("a"));
        comp1.add(new GraphNode<String>("b"));
        result.add(comp1);
        assertTrue(c.computeConnectedComponents(g1).equals(result));
    }
}
