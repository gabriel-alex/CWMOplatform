package io.github.gabriel.alex;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

/**
 * class to manipulate the CWMO ontology
 * 
 * @author Alex Gabriel
 * @version 1.0
 * @since 1.0
 */
public class OntologyManager {
	// private static final String FILE = "CWMO-05.owl";
	/**
	 * Ontology manager of the OWL API
	 */
	private OWLOntologyManager man;
	/**
	 * Reference to the loaded ontology
	 */
	private OWLOntology onto;

	/**
	 * Constructor OntologyManager - load the cwmo ontology available on github
	 * 
	 */
	OntologyManager() {
		IRI cwmo = IRI.create("https://raw.githubusercontent.com/gabriel-alex/cwmo/master/CWMO-05.owl");
		man = OWLManager.createOWLOntologyManager();
		try {
			onto = man.loadOntology(cwmo);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor OntologyManager - load the ontology given as parameter
	 * 
	 * @param ontologyUrl
	 *            Url to ontology available online that will be loaded
	 */
	OntologyManager(String ontologyUrl) {
		IRI cwmo = IRI.create(ontologyUrl);
		man = OWLManager.createOWLOntologyManager();
		try {
			onto = man.loadOntology(cwmo);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getInstances() {
		// ArrayList<OWLLogicalAxiom> output = new ArrayList<OWLLogicalAxiom>();
		ArrayList<String> output = new ArrayList<String>();
		// get class and its individuals
		// onto.logicalAxioms().forEach(output::add);
		onto.logicalAxioms().map(x -> x.toString()).forEach(output::add);

		return output;
	}

	/**
	 * Method that creates a creative technique object based on the information
	 * available in the ontology
	 * 
	 * @param shortURI
	 *            URI of a creative technique available in the ontology
	 * @return CreativeTechnique Object that contain all the information concerning
	 *         the creative technique specified by the URI
	 * 
	 */
	public CreativeTechnique getTechnique(String shortURI) {
		CreativeTechnique tech = new CreativeTechnique(shortURI);
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);

		IRI techIRI = IRI.create("http://purl.org/cwmo/#" + shortURI);
		OWLDataFactory dataFact = man.getOWLDataFactory();
		OWLNamedIndividual creatTechInstances = dataFact.getOWLNamedIndividual(techIRI);

		OWLAnnotationProperty rdfLabel = dataFact.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
		/*
		 * Stream<OWLAnnotation> labelAnnotation =
		 * EntitySearcher.getAnnotations(creatTechInstances, onto, rdfLabel);
		 * labelAnnotation.forEach(label -> { // IRI labelIRI = label.asIRI();
		 * OWLLiteral labelLiteral =
		 * dataFact.getOWLLiteral(label.getValue().toString());
		 * tech.setName(labelLiteral.getLiteral()); // OWLAnnotationValue val =
		 * dataFact.getOWLAnnotationProperty(labelIRI); //
		 * tech.setDifficulty(label.getValue()); });
		 */

		OWLDataProperty title = dataFact.getOWLDataProperty("http://purl.org/cwmo/#title");
		Set<OWLLiteral> techTitle = reasoner.getDataPropertyValues(creatTechInstances, title);
		techTitle.forEach(name -> tech.setName(name.toString()));

		OWLDataProperty description = dataFact.getOWLDataProperty("http://purl.org/cwmo/#description");
		Set<OWLLiteral> techDescription = reasoner.getDataPropertyValues(creatTechInstances, description);
		techDescription.forEach(desc -> tech.setDescription(desc.toString()));

		OWLObjectProperty hasDifficulty = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasDifficulty");
		Stream<OWLNamedIndividual> difficulty = reasoner.getObjectPropertyValues(creatTechInstances, hasDifficulty)
				.entities();
		difficulty.forEach(dif -> {
			Stream<OWLAnnotation> difLabelAnnot = EntitySearcher.getAnnotations(dif.getIRI(), onto, rdfLabel);
			difLabelAnnot.forEach(label -> {
				// IRI labelIRI = label.asIRI();
				OWLLiteral labelLiteral = dataFact.getOWLLiteral(label.getValue().toString());
				tech.setDifficulty(labelLiteral.getLiteral());
				// OWLAnnotationValue val = dataFact.getOWLAnnotationProperty(labelIRI);
				// tech.setDifficulty(label.getValue());
			});
		});

		OWLObjectProperty hasEnergyLevel = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasEnergyLevel");
		Stream<OWLNamedIndividual> energyLevel = reasoner.getObjectPropertyValues(creatTechInstances, hasEnergyLevel)
				.entities();
		energyLevel.forEach(energy -> tech.setEnergyLevel(energy.getIRI().getShortForm()));

		OWLObjectProperty hasQuantityLevel = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasQuantityLevel");
		Stream<OWLNamedIndividual> quantityLevel = reasoner
				.getObjectPropertyValues(creatTechInstances, hasQuantityLevel).entities();
		quantityLevel.forEach(quantity -> tech.setQuantityLevel(quantity.getIRI().getShortForm()));

		OWLObjectProperty hasNoveltyLevel = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasNoveltyLevel");
		Stream<OWLNamedIndividual> noveltyLevel = reasoner.getObjectPropertyValues(creatTechInstances, hasNoveltyLevel)
				.entities();
		noveltyLevel.forEach(novelty -> tech.setNovelty(novelty.getIRI().getShortForm()));

		OWLObjectProperty hasFunFactor = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasFunFactor");
		Stream<OWLNamedIndividual> funFactor = reasoner.getObjectPropertyValues(creatTechInstances, hasFunFactor)
				.entities();
		funFactor.forEach(fun -> tech.setFunFactor(fun.getIRI().getShortForm()));

		/*
		 * OWLObjectProperty requireStimuliType =
		 * dataFact.getOWLObjectProperty("http://purl.org/cwmo/#requireStimuliType");
		 * Stream<OWLNamedIndividual> stimuliType =
		 * reasoner.getObjectPropertyValues(creatTechInstances,requireStimuliType).
		 * entities();
		 * stimuliType.forEach(stimuli->tech.setStimuliType(stimuli.getIRI().
		 * getShortForm()));
		 */

		OWLObjectProperty hasCreativePhase = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasCreativePhase");
		Stream<OWLNamedIndividual> creatPhase = reasoner.getObjectPropertyValues(creatTechInstances, hasCreativePhase)
				.entities();
		creatPhase.forEach(phase -> tech.setCreativePhase(phase.getIRI().getShortForm()));

		OWLObjectProperty hasFunction = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasFunction");
		Stream<OWLNamedIndividual> function = reasoner.getObjectPropertyValues(creatTechInstances, hasFunction)
				.entities();
		function.forEach(phase -> tech.setFunction(phase.getIRI().getShortForm()));

		return tech;
	}

	/**
	 * Create a list of creative technique that are available in the ontology
	 * 
	 * @return ArrayList of Creative technique
	 */
	public ArrayList<CreativeTechnique> getTechniques() {
		ArrayList<CreativeTechnique> ctList = new ArrayList<CreativeTechnique>();
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);

		IRI creatTechnique = IRI.create("http://purl.org/cwmo/#CreativeTechnique");
		OWLDataFactory dataFact = man.getOWLDataFactory();
		OWLClass CreatTechClass = dataFact.getOWLClass(creatTechnique);

		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(CreatTechClass, true);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		individuals.forEach(x -> ctList.add(getTechnique(x.getIRI().getShortForm())));

		return ctList;
	}
	
	/**
	 * look up and create an instance of workshop thanks to the short URI
	 * @param shortURI 
	 * @return Workshop - object that describes workshop
	 */
	public Workshop getWorkshop(String shortURI){
		Workshop wrkshp = new Workshop(shortURI);
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);

		IRI wrkshpIRI = IRI.create("http://purl.org/cwmo/#" + shortURI);
		OWLDataFactory dataFact = man.getOWLDataFactory();
		OWLNamedIndividual workshopInstance = dataFact.getOWLNamedIndividual(wrkshpIRI);
		
		OWLDataProperty title = dataFact.getOWLDataProperty("http://purl.org/cwmo/#title");
		Set<OWLLiteral> wrkshpTitle = reasoner.getDataPropertyValues(workshopInstance, title);
		wrkshpTitle.forEach(name -> wrkshp.setTitle(name.toString()));

		OWLDataProperty description = dataFact.getOWLDataProperty("http://purl.org/cwmo/#description");
		Set<OWLLiteral> wrkshpDescription = reasoner.getDataPropertyValues(workshopInstance, description);
		wrkshpDescription.forEach(desc -> wrkshp.setDescription(desc.toString()));
		
		OWLObjectProperty hasProgression = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasProgression");
		Stream<OWLNamedIndividual> wrkshpStage = reasoner.getObjectPropertyValues(workshopInstance, hasProgression)
				.entities();
		wrkshpStage.forEach(phase -> wrkshp.setStage(phase.getIRI().getShortForm()));
		
		OWLObjectProperty hasStrategy = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#isImplementingStrat");
		Stream<OWLNamedIndividual> wrkshpStrategy = reasoner.getObjectPropertyValues(workshopInstance, hasStrategy)
				.entities();
		wrkshpStrategy.forEach(strat -> wrkshp.setStage(strat.getIRI().getShortForm()));
		
		return wrkshp;
		
	}
	
	/**
	 * Generate a list of workshop object based on all the workshops available in the ontology
	 * @return list of workshop
	 */
	public ArrayList<Workshop> getWorkshops(){
		ArrayList<Workshop> wrkshpList = new ArrayList<Workshop>();
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);
		
		IRI creatWorkshop = IRI.create("http://purl.org/cwmo/#CreativeWorkshop");
		OWLDataFactory dataFact = man.getOWLDataFactory();
		OWLClass CreatWrkshpClass = dataFact.getOWLClass(creatWorkshop);

		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(CreatWrkshpClass, true);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();

		individuals.forEach(x -> wrkshpList.add(getWorkshop(x.getIRI().getShortForm())));
		
		return wrkshpList;
		
	}
	
	/**
	 * Set the information concerning a workshop in the ontology
	 * @param wrkshp 
	 */
	public void setWorkshop(Workshop wrkshp) {
		
	}
	

}
