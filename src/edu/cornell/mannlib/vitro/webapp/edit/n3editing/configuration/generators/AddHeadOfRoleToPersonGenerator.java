/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.ConstantFieldOptions;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldOptions;

public class AddHeadOfRoleToPersonGenerator extends AddRoleToPersonTwoStageGenerator {
	
	private static String template = "addHeadOfRoleToPerson.ftl";
	
    //Should this be overridden
	@Override
	String getTemplate() {
		return template;
	}
	
	@Override
	String getRoleType() {
		return "http://vivoweb.org/ontology/core#LeaderRole";
	}	

	/** Head Of role involves hard-coded options for the "right side" of the role or activity */
    @Override
    FieldOptions getRoleActivityFieldOptions(VitroRequest vreq) throws Exception {
     
        return new ConstantFieldOptions(
        "", "Select type",
        "http://vivoweb.org/ontology/core#Association", "Association",
        "http://vivoweb.org/ontology/core#Center", "Center",
        "http://vivoweb.org/ontology/core#ClinicalOrganization", "Clinical Organization",
        "http://vivoweb.org/ontology/core#College", "College",
        "http://vivoweb.org/ontology/core#Committee", "Committee",
        "http://vivoweb.org/ontology/core#Consortium", "Consortium",
        "http://vivoweb.org/ontology/core#Department", "Department",
        "http://vivoweb.org/ontology/core#Division", "Division",
        "http://purl.org/NET/c4dm/event.owl#Event", "Event",
        "http://vivoweb.org/ontology/core#ExtensionUnit", "Extension Unit",
        "http://vivoweb.org/ontology/core#Foundation", "Foundation",
        "http://vivoweb.org/ontology/core#FundingOrganization", "Funding Organization",
        "http://vivoweb.org/ontology/core#GovernmentAgency", "Government Agency",
        "http://vivoweb.org/ontology/core#Hospital", "Hospital",
        "http://vivoweb.org/ontology/core#Institute", "Institute",
        "http://vivoweb.org/ontology/core#Laboratory", "Laboratory",
        "http://vivoweb.org/ontology/core#Library", "Library",
        "http://vivoweb.org/ontology/core#Museum", "Museum",
        "http://xmlns.com/foaf/0.1/Organization", "Organization",
        "http://vivoweb.org/ontology/core#PrivateCompany", "Private Company",
        "http://vivoweb.org/ontology/core#Program", "Program",
        "http://vivoweb.org/ontology/core#Project", "Project",
        "http://vivoweb.org/ontology/core#Publisher", "Publisher",
        "http://vivoweb.org/ontology/core#ResearchOrganization", "Research Organization",
        "http://vivoweb.org/ontology/core#School", "School",
        "http://vivoweb.org/ontology/core#Service", "Service",
        "http://vivoweb.org/ontology/core#Team", "Team",
        "http://vivoweb.org/ontology/core#StudentOrganization", "Student Organization",
        "http://vivoweb.org/ontology/core#University", "University");		
	}

	@Override
	boolean isShowRoleLabelField(){return true;}


    
}
