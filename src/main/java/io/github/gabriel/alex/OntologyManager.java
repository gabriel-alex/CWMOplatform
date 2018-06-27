package io.github.gabriel.alex;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

public class OntologyManager {
	//private static final String FILE = "CWMO-05.owl";
	OWLOntology onto;
	
	public void InitOntology() {
	//public void InitOntology() {
		
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		//File fileout = new File("C:\\pizza.func.owl");
		IRI pizzaontology = IRI.create("https://raw.githubusercontent.com/gabriel-alex/cwmo/master/CWMO-05.owl");
		try {
			onto = man.loadOntology(pizzaontology);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
	
	public ArrayList<String> getInstances(){
		//ArrayList<OWLLogicalAxiom> output = new ArrayList<OWLLogicalAxiom>();
		ArrayList<String> output = new ArrayList<String>();
		//get class and its individuals
		//onto.logicalAxioms().forEach(output::add);
		onto.logicalAxioms().map(x->x.toString()).forEach(output::add);
		
		return output;
	}
	
	

}
