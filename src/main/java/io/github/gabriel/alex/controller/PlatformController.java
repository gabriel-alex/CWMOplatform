package io.github.gabriel.alex.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.github.gabriel.alex.OntologyManager;
import io.github.gabriel.alex.beans.CreativeTechnique;
import io.github.gabriel.alex.beans.Workshop;

@Controller
public class PlatformController {
	
	private final static Logger logger = LoggerFactory.getLogger(PlatformController.class);
	
	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;
	
	

	/**
	 * Mapping for the index page
	 * 
	 * @return index page
	 */
	@RequestMapping({"/index","/"})
	public String index() {
		return "index";
	}
	
	/**
	 * Mapping for accessing example of a DB page
	 * 
	 * @param model
	 * @return html page db
	 */
	@RequestMapping("/db")
	String db(Map<String, Object> model) {
		try (Connection connection = dataSource.getConnection()) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

			ArrayList<String> output = new ArrayList<String>();
			while (rs.next()) {
				output.add("Read from DB: " + rs.getTimestamp("tick"));
			}

			model.put("records", output);
			return "db";
		} catch (Exception e) {
			model.put("message", e.getMessage());
			return "error";
		}
	}

	/**
	 * Mapping for accessing creative technique
	 * 
	 * @param model
	 * @return html page techniques
	 */
	@RequestMapping("/techniques")
	String techniques(Map<String, Object> model) {
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		ArrayList<CreativeTechnique> techniques = new ArrayList<CreativeTechnique>();

		OntologyManager ontoMngr = new OntologyManager();
		techniques = ontoMngr.getTechniques();
		workshops = ontoMngr.getWorkshops();
		
		model.put("techniques", techniques);
		model.put("workshops", workshops);
		
		return "techniques";
	}

	/**
	 * Mapping for accessing detail of a creative technique
	 * 
	 * @param uri
	 *            short uri (after #) of the creative technique
	 * @param model
	 * @return html page "techniqueDetail.html"
	 */
	@RequestMapping(value = "/techniqueDetail", method = RequestMethod.GET)
	public String techniqueDetail(@RequestParam String uri, Map<String, Object> model) {
		CreativeTechnique technique = new CreativeTechnique();
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		technique = ontoMngr.getTechnique(uri);
		workshops =ontoMngr.getWorkshops();
		model.put("technique", technique);
		model.put("workshops", workshops);
		return "techniqueDetail";
	}
	
	/**
	 * Mapping for accessing to the page that define the creative strategy
	 * @param model
	 * @return html page "creative-strategy.html"
	 */
	@RequestMapping(value="/creative-strategy", method = RequestMethod.GET)
	String test(Map<String, Object> model) {
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();

		OntologyManager ontoMngr = new OntologyManager();

		workshops =ontoMngr.getWorkshops();

		model.put("workshops", workshops);
		return "creativeStrategy";
	}
	
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping("/workshop/create")
	public String workshopCreate(@ModelAttribute("workshop") Workshop wrkshp, BindingResult bindingResult,   
			Model model, RedirectAttributes ra ) {
		logger.debug("form submission.");
		if ( bindingResult.hasErrors() ) {
			return "createWorkshop";
}
		ra.addFlashAttribute("workshop", wrkshp);
		
		return "redirect:/workshoptest";
		
	}
	@RequestMapping(value="/workhop/create/", method=RequestMethod.POST)
	String strategyForm(@ModelAttribute Workshop wrkshp, Map<String, Object> model) {
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		ontoMngr.setWorkshop(wrkshp);
		workshops =ontoMngr.getWorkshops();
		model.put("workshops", workshops);
		return "creativeStrategy";
	}
	
	/**
	 * 
	 * @param model
	 * @return new workshop form with a new instance of workshop 
	 */
	@GetMapping("/workshop/create")
	public String workshopForm(Model model) {
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops =ontoMngr.getWorkshops();
		model.addAttribute("workshop",workshops);
		model.addAttribute("workshop", new Workshop());
		return "createWorkshop";
	}
	
	/*@RequestMapping(value="/workshop/create/", method=RequestMethod.GET)
	//String createStrategy(Model model, Map<String, Object> map) {
	String createStrategy(Model model) {
		model.addAttribute("workshop", new Workshop());
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops = ontoMngr.getWorkshops();
		//map.put("workshops", workshops);
		return "createWorkshop";
	}*/
	
	@GetMapping("/workshoptest")
	public String wrkshpTest(@ModelAttribute("workshop") Workshop wrkshp, Model model) {
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops = ontoMngr.getWorkshops();
		model.addAttribute("workshop", wrkshp);
		model.addAttribute("workshops", workshops);
		return "workshop";
	}
	
	/*@RequestMapping(value = "/workshop", method = RequestMethod.GET)
	public String workshopDetail(@RequestParam String uri, Map<String, Object> model) {
		Workshop workshop = new Workshop();
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops = ontoMngr.getWorkshops();
		workshop = ontoMngr.getWorkshop(uri);
		model.put("workshop", workshop);
		model.put("workshops", workshops);
		model.put("URI", uri);
		return "workshop";
	}*/
	
	@RequestMapping(value = "/workshop", method = RequestMethod.GET)
	public String workshopDetail(@RequestParam String uri, Model model) {
		Workshop workshop = new Workshop();
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops = ontoMngr.getWorkshops();
		workshop = ontoMngr.getWorkshop(uri);
		logger.debug(workshop.toString());
		model.addAttribute("workshop", workshop);
		model.addAttribute("workshops", workshops);
		model.addAttribute("URI", uri);
		return "workshop";
	}
	
	/**
	 * Beans for generating a datasource needed to access to a DB
	 * 
	 * @return datasource needed to establish a connection the DB
	 * @throws SQLException
	 */
	@Bean
	public DataSource dataSource() throws SQLException {
		if (dbUrl == null || dbUrl.isEmpty()) {
			return new HikariDataSource();
		} else {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(dbUrl);
			return new HikariDataSource(config);
		}
	}
}
