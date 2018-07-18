package io.github.gabriel.alex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.dlsyntax.renderer.DLSyntaxObjectRenderer;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.formats.PrefixDocumentFormat;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import io.github.gabriel.alex.beans.CreativeStrategy;
import io.github.gabriel.alex.beans.CreativeTechnique;
import io.github.gabriel.alex.beans.Workshop;
import io.github.gabriel.alex.controller.PlatformController;

/**
 * class to manipulate the CWMO ontology
 * 
 * @author Alex Gabriel
 * @version 1.0
 * @since 1.0
 */
public class OntologyManager {
	private final static Logger logger = LoggerFactory.getLogger(PlatformController.class);
	// private static final String FILE = "CWMO-05.owl";
	/**
	 * Ontology manager of the OWL API
	 */
	private OWLOntologyManager man;
	/**
	 * Reference to the loaded ontology
	 */
	private OWLOntology onto;
	
	private PrefixManager prefix; 
	
	private boolean localOnto = true;

	/**
	 * Constructor OntologyManager - load the cwmo ontology available on github
	 * @throws IOException 
	 * 
	 */
	public OntologyManager() {
		if(localOnto) {
			//ClassPathResource classPathResource = new ClassPathResource("/CWMO-05.owl");
			Resource resource = new ClassPathResource("/CWMO-05.owl");
			//logger.debug("resource.toString(): " + resource.toString());
			InputStream inputStream =null;
			String content = null;
			//File file = null;
			try {
				//file = resource.getFile();
				inputStream = resource.getInputStream();
				//logger.debug("resource.getInputStream(): " + inputStream);
				content = IOUtils.toString(inputStream);
				//logger.debug("content: " + content );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.debug("inputStream.toString(): "+inputStream.toString());
			File file2 = new File(content);
			//IRI cwmo = IRI.create(file.toURI());
			man = OWLManager.createOWLOntologyManager();
			try {
				onto = man.loadOntologyFromOntologyDocument(new StringDocumentSource(content));
				logger.debug(onto.toString());
				//onto = man.loadOntology(content);
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}else {
			IRI cwmo = IRI.create("https://raw.githubusercontent.com/gabriel-alex/cwmo/master/CWMO-05.owl"); 
			man = OWLManager.createOWLOntologyManager();
			try {
				onto = man.loadOntology(cwmo);
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
		}
		prefix = new DefaultPrefixManager("http://purl.org/cwmo/#");
	}

	/**
	 * Constructor OntologyManager - load the ontology given as parameter
	 * 
	 * @param ontologyUrl
	 *            Url to ontology available online that will be loaded
	 */
	public OntologyManager(String ontologyUrl) {
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
		
		OWLObjectProperty isSolvedDuring = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#isSolvedDuring");
		Stream<OWLNamedIndividual> wrkshpSubject = reasoner.getObjectPropertyValues(workshopInstance, isSolvedDuring)
				.entities();
		wrkshpSubject.forEach(phase -> wrkshp.addSubject(phase.getIRI().getShortForm()));
		
		OWLObjectProperty hasProgression = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasProgression");
		Stream<OWLNamedIndividual> wrkshpStage = reasoner.getObjectPropertyValues(workshopInstance, hasProgression)
				.entities();
		wrkshpStage.forEach(phase -> wrkshp.setStage(phase.getIRI().getShortForm()));
		
		OWLObjectProperty hasStrategy = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#isImplementingStrat");
		Stream<OWLNamedIndividual> wrkshpStrategy = reasoner.getObjectPropertyValues(workshopInstance, hasStrategy)
				.entities();
		wrkshpStrategy.forEach(strat -> wrkshp.addCreativeStrat(strat.getIRI().getShortForm()));
		
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
	 * 
	 * @param shortURI
	 * @return
	 */
	public CreativeStrategy getCreativeStrategy(String shortURI) {
		CreativeStrategy creatStrat = new CreativeStrategy(shortURI);
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);

		IRI creatStratIRI = IRI.create("http://purl.org/cwmo/#" + shortURI);
		OWLDataFactory dataFact = man.getOWLDataFactory();
		OWLNamedIndividual creatStratInstance = dataFact.getOWLNamedIndividual(creatStratIRI);
		
		/*OWLDataProperty title = dataFact.getOWLDataProperty("http://purl.org/cwmo/#title");
		Set<OWLLiteral> creatStratTitle = reasoner.getDataPropertyValues(creatStratInstance, title);
		creatStratTitle.forEach(name -> creatStrat.setTitle(name.toString())); */
		
		OWLDataProperty description = dataFact.getOWLDataProperty("http://purl.org/cwmo/#description");
		Set<OWLLiteral> creatStratDescription = reasoner.getDataPropertyValues(creatStratInstance, description);
		creatStratDescription.forEach(desc -> creatStrat.setDescription(desc.toString()));
		
		OWLObjectProperty hasProgression = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#hasStartegyProgression");
		Stream<OWLNamedIndividual> creatStratStage = reasoner.getObjectPropertyValues(creatStratInstance, hasProgression)
				.entities();
		creatStratStage.forEach(phase -> creatStrat.setStage(phase.getIRI().getShortForm()));
		
		OWLObjectProperty hasWorkshop = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#isImplementingStrategy");
		Stream<OWLNamedIndividual> creatStratWorkshop = reasoner.getObjectPropertyValues(creatStratInstance, hasWorkshop)
				.entities();
		creatStratWorkshop.forEach(wrkshp -> creatStrat.setWorkshop(wrkshp.getIRI().getShortForm()));
		
		OWLObjectProperty isPlanifiedBy = dataFact.getOWLObjectProperty("http://purl.org/cwmo/#isPlanifiedBy");
		Stream<OWLNamedIndividual> facilitator = reasoner.getObjectPropertyValues(creatStratInstance, isPlanifiedBy)
				.entities();
		facilitator.forEach(fcltr -> creatStrat.addFacilitator(fcltr.getIRI().getShortForm()));
		
		return creatStrat;
	}
	
	/**
	 * Genrate a list of workshop object based on all the creative strategy available in the ontology 
	 * @return list of creative strategy
	 */
	public ArrayList<CreativeStrategy> getCreativeStrategies(){
		ArrayList<CreativeStrategy> CSList = new ArrayList<CreativeStrategy>();
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		OWLReasoner reasoner = reasonerFactory.createReasoner(onto);
		
		IRI creatStrategy = IRI.create("http://purl.org/cwmo/#CreativeStrategy");
		OWLDataFactory dataFact = man.getOWLDataFactory();
		OWLClass CreatStratpClass = dataFact.getOWLClass(creatStrategy);
		
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(CreatStratpClass, true);
		Stream<OWLNamedIndividual> individuals = individualsNodeSet.entities();
		
		individuals.forEach(x -> CSList.add(getCreativeStrategy(x.getIRI().getShortForm())));
		
		return CSList;
	}
	
	/**
	 * Set the information concerning a workshop in the ontology
	 * @param wrkshp 
	 */
	public void setWorkshop(Workshop wrkshp) {
		OWLDataFactory dataFact = man.getOWLDataFactory();
		String wrkshpURI = "#"+createRandomString(6);
		IRI wrkshopIRI = IRI.create("http://purl.org/cwmo/"+ wrkshpURI);
		OWLClass workshop = dataFact.getOWLClass(wrkshopIRI);
		OWLAnnotation commentAnno = dataFact.getOWLAnnotation(dataFact.getRDFSComment(), dataFact.getOWLLiteral(
				 "Intance of workshop", "en"));
		OWLAxiom ax = dataFact.getOWLAnnotationAssertionAxiom(workshop.getIRI(), commentAnno);
		man.applyChange(new AddAxiom(onto, ax));
		try {
			man.saveOntology(onto);
		} catch (OWLOntologyStorageException e) {
			logger.error( e.toString()); //e.printStackTrace()
		}
	}
	
	public void addTechniqueToStrategy() {
		
	}
	/**
	 * create an hex string based on the given length 
	 * @param length
	 * @return
	 */
	private static String createRandomString(int length) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		while (sb.length() < length) {
			sb.append(Integer.toHexString(random.nextInt()));
		}
		return sb.toString();
	}
	
	private void saveOnto(String filePath, OWLOntology onto) {
		File file = new File(filePath);
		try {
			OWLXMLDocumentFormat owlxmlFormat = new OWLXMLDocumentFormat();
			man.saveOntology(onto,owlxmlFormat, IRI.create(file.toURI()));
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
