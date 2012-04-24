/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.generators;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.ConstantFieldOptions;
import edu.cornell.mannlib.vitro.webapp.edit.n3editing.VTwo.fields.FieldOptions;

public class AddOrganizerRoleToPersonGenerator extends AddRoleToPersonTwoStageGenerator {
	
	private static String template = "addOrganizerRoleToPerson.ftl";
	
    
	@Override
	String getTemplate() {
		return template;
	}

	@Override
	String getRoleType() {
		return "http://vivoweb.org/ontology/core#OrganizerRole";
	}
	
	//Organizer role involves hard-coded options for the "right side" of the role or activity
    @Override
    FieldOptions getRoleActivityFieldOptions(VitroRequest vreq) throws Exception {
		return new ConstantFieldOptions(
        "","Select type",
        "http://purl.org/NET/c4dm/event.owl#Event","Event",
        "http://vivoweb.org/ontology/core#Competition","Competition",
        "http://purl.org/ontology/bibo/Conference","Conference",
        "http://vivoweb.org/ontology/core#Course","Course",
        "http://vivoweb.org/ontology/core#Exhibit","Exhibit",
        "http://vivoweb.org/ontology/core#Meeting","Meeting",
        "http://vivoweb.org/ontology/core#Presentation","Presentation",
        "http://vivoweb.org/ontology/core#InvitedTalk","Invited Talk",
        "http://purl.org/ontology/bibo/Workshop","Workshop",
        "http://vivoweb.org/ontology/core#EventSeries","Event Series",
        "http://vivoweb.org/ontology/core#ConferenceSeries","Conference Series",
        "http://vivoweb.org/ontology/core#SeminarSeries","Seminar Series",
        "http://vivoweb.org/ontology/core#WorkshopSeries","Workshop Series");		
	}

	@Override
	boolean isShowRoleLabelField() {
		return false;
	}
}
