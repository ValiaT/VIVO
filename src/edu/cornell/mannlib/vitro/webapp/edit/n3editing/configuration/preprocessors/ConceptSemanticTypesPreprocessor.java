/* $This file is distributed under the terms of the license in /doc/license.txt$ */

package edu.cornell.mannlib.vitro.webapp.edit.n3editing.configuration.preprocessors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import edu.cornell.mannlib.vitro.webapp.controller.VitroRequest;
import edu.cornell.mannlib.vitro.webapp.dao.ModelAccess;
import edu.cornell.mannlib.vitro.webapp.dao.VitroVocabulary;
import edu.cornell.mannlib.vitro.webapp.dao.jena.DatasetWrapper;
import edu.cornell.mannlib.vitro.webapp.dao.jena.DatasetWrapperFactory;

//We are representing semantic types from the UMLS Semantic Network as OWL Classes
//and this preprocessor will add the appropriate class information to the TBox

public class ConceptSemanticTypesPreprocessor implements ModelChangePreprocessor {

	private static String VIVOCore = "http://vivoweb.org/ontology/core#";
	private static String SKOSConceptType = "http://www.w3.org/2004/02/skos/core#Concept";	
	private Log log = LogFactory.getLog(ConceptSemanticTypesPreprocessor.class);

	
	//Custom constructor
	public ConceptSemanticTypesPreprocessor() {
	}
	
	@Override
	public void preprocess(Model retractionsModel, Model additionsModel,
			HttpServletRequest request) {
		VitroRequest vreq = new VitroRequest(request);
		//Run a construct query against the additions model
		String prefixes = "PREFIX rdfs:<" + RDFS.getURI() + "> " + 
		"PREFIX owl:<http://www.w3.org/2002/07/owl#> " + 
		"PREFIX rdf:<" + RDF.getURI() + ">" + 
		"PREFIX skos:<http://www.w3.org/2004/02/skos/core#>";		
		String constructQuery = prefixes + " CONSTRUCT { " + 
				"?semanticType rdf:type owl:Class.  " + 
				"?semanticType rdfs:subClassOf skos:Concept .  " + 
				"?semanticType rdfs:label ?label.  " + 
			"} WHERE { " + 
				"?concept rdf:type ?semanticType.  " + 
				"?semanticType rdfs:label ?label . " + 
				"?semanticType rdfs:subClassOf skos:Concept . " + 
			"}";
		
		//Execute construct query 
		 Model constructedModel = ModelFactory.createDefaultModel();
	        
	       
        log.debug("CONSTRUCT query string " + constructQuery);
        
        Query query = null;
        try {
            query = QueryFactory.create(constructQuery, Syntax.syntaxARQ);
        } catch(Throwable th){
            log.error("Could not create CONSTRUCT SPARQL query for query " +
                      "string. " + th.getMessage());
            log.error(constructQuery);
            return;
        } 
	        
	           
	        
        additionsModel.getLock().enterCriticalSection(Lock.READ);
        QueryExecution qe = null;
        try {                           
            qe = QueryExecutionFactory.create(
                    query, additionsModel);
            qe.execConstruct(constructedModel);
        } catch (Exception e) {
            log.error("Error getting constructed model for query string " + constructQuery);
        } finally {
            if (qe != null) {
                qe.close();
            }
            additionsModel.getLock().leaveCriticalSection();
        }
        
        //Add constructed model to the designated update model
        OntModel toUpdateModel = ModelAccess.on(vreq).getOntModelSelector().getTBoxModel();
        toUpdateModel.enterCriticalSection(Lock.WRITE);
        try {
        	toUpdateModel.add(constructedModel);
        } catch (Exception e) {
            log.error("Error adding statements to update model for " + constructQuery);
        } finally {
        	toUpdateModel.leaveCriticalSection();
        }
        
        //Take this constructed model and remove from the additions model
        additionsModel.enterCriticalSection(Lock.WRITE);
        try {
        	additionsModel.remove(constructedModel.listStatements().toList());
        } catch (Exception e) {
            log.error("Error removing statements from additions model for " + constructQuery);
        } finally {
            additionsModel.leaveCriticalSection();
        }
        
    }
		


}
