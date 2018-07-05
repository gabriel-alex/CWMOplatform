package io.github.gabriel.alex;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Main controller of the web site
 * 
 * @author Alex Gabriel
 * @version 1.0
 * @since 1.0
 */
@Controller
@SpringBootApplication
public class Main {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);

	}

	/**
	 * Mapping for the index page
	 * 
	 * @return index page
	 */
	@RequestMapping("/index")
	String index() {
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
		OntologyManager ontoMngr = new OntologyManager();
		technique = ontoMngr.getTechnique(uri);
		model.put("technique", technique);
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
	@PostMapping("/workhop/create/")
	String strategyForm(@ModelAttribute Workshop wrkshp, Map<String, Object> model) {
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		ontoMngr.setWorkshop(wrkshp);
		workshops =ontoMngr.getWorkshops();
		model.put("workshops", workshops);
		return "creativeStrategy";
	}
	
	@GetMapping("/workshop/create/")
	String createStrategy(Model model, Map<String, Object> map) {
		model.addAttribute("workshop", new Workshop());
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops = ontoMngr.getWorkshops();
		map.put("workshops", workshops);
		return "createWorkshop";
	}
	
	@RequestMapping(value = "/workshop", method = RequestMethod.GET)
	public String workshopDetail(@RequestParam String uri, Map<String, Object> model) {
		Workshop workshop = new Workshop();
		ArrayList<Workshop> workshops = new ArrayList<Workshop>();
		OntologyManager ontoMngr = new OntologyManager();
		workshops = ontoMngr.getWorkshops();
		workshop = ontoMngr.getWorkshop(uri);
		model.put("workshop", workshop);
		model.put("workshops", workshops);
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
