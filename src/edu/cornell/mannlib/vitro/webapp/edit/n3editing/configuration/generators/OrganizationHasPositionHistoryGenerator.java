/* $This file is distributed under the terms of the license in /doc/license.txt$ */
package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import com.hp.hpl.jena.vocabulary.XSD;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary.Precision;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeIntervalValidationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.DateTimeWithPrecisionVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.EditConfigurationVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.ChildVClassesWithParent;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldVTwo;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.IndividualsViaVClassOptions;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.validators.AntiXssValidation;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.OrganizationHasPositionValidator;

public class OrganizationHasPositionHistoryGenerator extends VivoBaseGenerator
		implements EditConfigurationGenerator {

	private final static String NS_VIVO_CORE = "http://vivoweb.org/ontology/core#";

	private static final String URI_PRECISION_NONE = Precision.NONE.uri();
	private static final String URI_PRECISION_YEAR = Precision.YEAR.uri();

	private final static String URI_POSITION_CLASS = NS_VIVO_CORE + "Position";

	private static final String QUERY_EXISTING_POSITION_TITLE = ""
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "SELECT ?existingPositionTitle WHERE { \n"
			+ "  ?position rdfs:label ?existingPositionTitle . }";

	private static final String QUERY_EXISTING_POSITION_TYPE = ""
            + "PREFIX vitro: <" + VitroVocabulary.vitroURI + "> \n"
			+ "SELECT ?existingPositionType WHERE { \n"
			+ "  ?position vitro:mostSpecificType ?existingPositionType . }";

	private static final String QUERY_EXISTING_PERSON = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "SELECT ?existingPerson WHERE { \n"
			+ "  ?position core:positionForPerson ?existingPerson .}";

	private static final String QUERY_EXISTING_PERSON_LABEL = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "SELECT ?existingPersonLabel WHERE { \n"
			+ "  ?position core:positionForPerson ?existingPerson . \n"
			+ "  ?existingPerson rdfs:label ?existingPersonLabel . }";

	private static final String QUERY_EXISTING_INTERVAL_NODE = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingIntervalNode WHERE { \n"
			+ "  ?position core:dateTimeInterval ?existingIntervalNode . \n"
			+ "  ?existingIntervalNode a core:DateTimeInterval . }";

	private static final String QUERY_EXISTING_START_NODE = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingStartNode WHERE { \n"
			+ "  ?position core:dateTimeInterval ?intervalNode .\n"
			+ "  ?intervalNode a core:DateTimeInterval ;\n"
			+ "      core:start ?existingStartNode . \n"
			+ "  ?existingStartNode a core:DateTimeValue . }";

	private static final String QUERY_EXISTING_START_VALUE = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingDateStart WHERE { \n"
			+ "  ?position core:dateTimeInterval ?intervalNode .\n"
			+ "  ?intervalNode a core:DateTimeInterval ; \n"
			+ "      core:start ?startNode . \n"
			+ "  ?startNode a core:DateTimeValue ; \n"
			+ "      core:dateTime ?existingDateStart . }";

	private static final String QUERY_EXISTING_START_PRECISION = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingStartPrecision WHERE { \n"
			+ "  ?position core:dateTimeInterval ?intervalNode .\n"
			+ "  ?intervalNode a core:DateTimeInterval ;\n"
			+ "      core:start ?startNode . \n"
			+ "  ?startNode a core:DateTimeValue ; \n"
			+ "      core:dateTimePrecision ?existingStartPrecision . }";

	private static final String QUERY_EXISTING_END_NODE = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingEndNode WHERE { \n"
			+ "  ?position core:dateTimeInterval ?intervalNode .\n"
			+ "  ?intervalNode a core:DateTimeInterval ;\n"
			+ "      core:end ?existingEndNode . \n"
			+ "  ?existingEndNode a core:DateTimeValue . }";

	private static final String QUERY_EXISTING_END_VALUE = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingDateEnd WHERE { \n"
			+ "  ?position core:dateTimeInterval ?intervalNode .\n"
			+ "  ?intervalNode a core:DateTimeInterval ; \n"
			+ "        core:end ?endNode . \n"
			+ "  ?endNode a core:DateTimeValue ; \n"
			+ "        core:dateTime ?existingDateEnd . }";

	private static final String QUERY_EXISTING_END_PRECISION = ""
			+ "PREFIX core: <http://vivoweb.org/ontology/core#> \n"
			+ "SELECT ?existingEndPrecision WHERE { \n"
			+ "  ?position core:dateTimeInterval ?intervalNode .\n"
			+ "  ?intervalNode a core:DateTimeInterval ;\n"
			+ "      core:end ?endNode . \n"
			+ "  ?endNode a core:DateTimeValue ; \n"
			+ "      core:dateTimePrecision ?existingEndPrecision . }";

	private static final String N3_NEW_POSITION = ""
			+ "@prefix core: <http://vivoweb.org/ontology/core#> . \n"
			+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . \n"
			+ "\n" //
			+ "?organization core:organizationForPosition ?position . \n"
			+ "\n" //
			+ "?position a core:Position , ?positionType ; \n"
			+ "    rdfs:label ?positionTitle ; \n"
			+ "    core:positionInOrganization ?organization ; ";

    private static final String N3_NEW_PERSON = ""
			+ "@prefix core: <http://vivoweb.org/ontology/core#> . \n"
			+ "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> . \n"
			+ "@prefix foaf: <http://xmlns.com/foaf/0.1/> . \n"
    		+ "\n" //
    		+ "?position core:positionForPerson ?person . \n" //
    		+ "?person core:personInPosition ?position . \n"
    		+ "?person a foaf:Person . \n"
    		+ "?person rdfs:label ?personLabel";

    private static final String N3_NEW_FIRST_NAME = ""
    		+ "@prefix foaf: <http://xmlns.com/foaf/0.1/> . \n"
    		+ "\n" //
        	+ "?person foaf:firstName ?firstName .";

    private static final String N3_NEW_LAST_NAME = ""
    		+ "@prefix foaf: <http://xmlns.com/foaf/0.1/> . \n"
    		+ "\n" //
        	+ "?person foaf:lastName ?lastName .";

    private static final String N3_EXISTING_PERSON = ""
			+ "@prefix core: <http://vivoweb.org/ontology/core#> . \n"
    		+ "\n" //
        	+ "?position core:positionForPerson ?existingPerson . \n" //
        	+ "?existingPerson core:personInPosition ?position . ";

	private static final String N3_NEW_START_NODE = ""
			+ "@prefix core: <http://vivoweb.org/ontology/core#> . \n"
			+ "\n" //
			+ "?position core:dateTimeInterval ?intervalNode . \n"
			+ "\n" //
			+ "?intervalNode a core:DateTimeInterval ; \n"
			+ "    core:start ?startNode . \n "
			+ "\n" //
			+ "?startNode a core:DateTimeValue ; \n"
			+ "    core:dateTime ?startField-value ; \n"
			+ "    core:dateTimePrecision ?startField-precision . ";

	private static final String N3_NEW_END_NODE = ""
			+ "@prefix core: <http://vivoweb.org/ontology/core#> . \n"
			+ "\n" //
			+ "?position core:dateTimeInterval ?intervalNode . \n"
			+ "\n" //
			+ "?intervalNode a core:DateTimeInterval ; \n"
			+ "    core:end ?endNode . \n "
			+ "\n" //
			+ "?endNode a core:DateTimeValue ; \n"
			+ "    core:dateTime ?endField-value ; \n"
			+ "    core:dateTimePrecision ?endField-precision . ";

	@Override
	public EditConfigurationVTwo getEditConfiguration(VitroRequest vreq,
			HttpSession session) throws Exception {
		EditConfigurationVTwo conf = new EditConfigurationVTwo();

		initBasics(conf, vreq);
		initPropertyParameters(vreq, session, conf);
		initObjectPropForm(conf, vreq);

		conf.setVarNameForSubject("organization");
		conf.setVarNameForPredicate("predicate");
		conf.setVarNameForObject("position");

		conf.setTemplate("organizationHasPositionHistory.ftl");

		conf.setN3Required(Arrays.asList(N3_NEW_POSITION));
		conf.setN3Optional(Arrays.asList(N3_NEW_PERSON, N3_EXISTING_PERSON, N3_NEW_START_NODE, N3_NEW_END_NODE, N3_NEW_FIRST_NAME, N3_NEW_LAST_NAME));

		conf.addNewResource("position", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("person", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("intervalNode", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("startNode", DEFAULT_NS_FOR_NEW_RESOURCE);
		conf.addNewResource("endNode", DEFAULT_NS_FOR_NEW_RESOURCE);

		conf.setUrisOnform(Arrays.asList("existingPerson", "position", "positionType"));
		conf.addSparqlForExistingUris("positionType",
				QUERY_EXISTING_POSITION_TYPE);
		conf.addSparqlForExistingUris("person", QUERY_EXISTING_PERSON);

		conf.addSparqlForExistingUris("intervalNode",
				QUERY_EXISTING_INTERVAL_NODE);
		conf.addSparqlForExistingUris("startNode", QUERY_EXISTING_START_NODE);
		conf.addSparqlForExistingUris("endNode", QUERY_EXISTING_END_NODE);

		conf.setLiteralsOnForm(Arrays.asList("positionTitle", "personLabelDisplay", "personLabel", "firstName", "lastName"));
		conf.addSparqlForExistingLiteral("positionTitle",
				QUERY_EXISTING_POSITION_TITLE);
		conf.addSparqlForExistingLiteral("personLabel",
				QUERY_EXISTING_PERSON_LABEL);
		conf.addSparqlForExistingUris("existingPerson",
				QUERY_EXISTING_PERSON);
		conf.addSparqlForExistingLiteral("startField-value",
				QUERY_EXISTING_START_VALUE);
		conf.addSparqlForExistingUris("startField-precision",
				QUERY_EXISTING_START_PRECISION);
		conf.addSparqlForExistingLiteral("endField-value",
				QUERY_EXISTING_END_VALUE);
		conf.addSparqlForExistingUris("endField-precision",
				QUERY_EXISTING_END_PRECISION);

		conf.addField(new FieldVTwo()
				.setName("positionType")
				.setValidators(list("nonempty"))
				.setOptions( 
				        new ChildVClassesWithParent(URI_POSITION_CLASS))
				);

		conf.addField(new FieldVTwo().setName("positionTitle")
				.setRangeDatatypeUri(XSD.xstring.toString())
				.setValidators(list("nonempty")));

		conf.addField(new FieldVTwo().setName("existingPerson")
		        .setOptions( 
		                new IndividualsViaVClassOptions(personClass)));

		conf.addField(new FieldVTwo().setName("personLabel")
				.setRangeDatatypeUri(XSD.xstring.toString())
				.setValidators( list("datatype:" + XSD.xstring.toString()) ));

    	conf.addField(new FieldVTwo().setName("firstName")
    			.setRangeDatatypeUri(XSD.xstring.toString())
				.setValidators( list("datatype:" + XSD.xstring.toString()) ));

    	conf.addField(new FieldVTwo().setName("lastName")
    			.setRangeDatatypeUri(XSD.xstring.toString())
				.setValidators( list("datatype:" + XSD.xstring.toString()) ));

    	conf.addField(new FieldVTwo().setName("personLabelDisplay")
    			.setRangeDatatypeUri(XSD.xstring.toString())
    			.setValidators( list("datatype:" + XSD.xstring.toString()) ));

		FieldVTwo startField = new FieldVTwo().setName("startField");
		conf.addField(startField.setEditElement(new DateTimeWithPrecisionVTwo(
				startField, URI_PRECISION_YEAR, URI_PRECISION_NONE)));

		FieldVTwo endField = new FieldVTwo().setName("endField");
		conf.addField(endField.setEditElement(new DateTimeWithPrecisionVTwo(
				endField, URI_PRECISION_YEAR, URI_PRECISION_NONE)));

        conf.addValidator(new OrganizationHasPositionValidator());
		conf.addValidator(new AntiXssValidation());
		conf.addValidator(new DateTimeIntervalValidationVTwo("startField",
				"endField"));
		
		prepare(vreq, conf);
		return conf;
	}
}
