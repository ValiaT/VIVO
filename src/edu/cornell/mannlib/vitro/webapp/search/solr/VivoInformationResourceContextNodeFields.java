/* $This file is distributed under the terms of the license in /doc/license.txt$ */
package edu.cornell.mannlib.vitro.webapp.search.solr;

import java.util.ArrayList;
import java.util.List;

import edu.cornell.mannlib.vitro.webapp.rdfservice.RDFServiceFactory;
import edu.cornell.mannlib.vitro.webapp.search.solr.documentBuilding.ContextNodeFields;

import edu.cornell.mannlib.vitro.webapp.search.solr.documentBuilding.ContextNodeFields;

/**
 * Class that adds text from context nodes to Solr Documents for 
 * obo:IAO_0000030 individuals.
 * 
 * @author bdc34
 *
 */
public class VivoInformationResourceContextNodeFields extends ContextNodeFields{
    
    static List<String> queriesForInformationResource = new ArrayList<String>();
    
    public VivoInformationResourceContextNodeFields(RDFServiceFactory rdfServiceFactory){        
        super(queriesForInformationResource, rdfServiceFactory);
    }
      
  protected static final String prefix = 
        "prefix owl: <http://www.w3.org/2002/07/owl#> "
      + " prefix vitroDisplay: <http://vitro.mannlib.cornell.edu/ontologies/display/1.1#>  "
      + " prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  "
      + " prefix core: <http://vivoweb.org/ontology/core#>  "
      + " prefix foaf: <http://xmlns.com/foaf/0.1/> "
      + " prefix obo: <http://purl.obolibrary.org/> "
      + " prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> "
      + " prefix localNav: <http://vitro.mannlib.cornell.edu/ns/localnav#>  "
      + " prefix bibo: <http://purl.org/ontology/bibo/>  ";
  

  //queries for obo:IAO_0000030
  static {
      
        /* linked author labels */

        queriesForInformationResource
                .add(prefix
                        + "SELECT (str(?ContextNodeProperty) as ?contextNodeProperty) WHERE {"
                        + "?uri rdf:type obo:IAO_0000030 . "
                        + "?uri core:relatedBy ?a . "
                        + "?a rdf:type core:Authorship ."
                        + "?a core:relates ?b ."
                        + "?b rdf:type foaf:Agent ."
                        + "?b rdfs:label ?ContextNodeProperty .}");

        /* features */
        
        queriesForInformationResource
                .add(prefix
                        + "SELECT (str(?ContextNodeProperty) as ?contextNodeProperty) WHERE {"
                        + "?uri rdf:type obo:IAO_0000030 . "
                        + "?uri core:features ?i . ?i rdfs:label ?ContextNodeProperty ."
                        + "}");

        /* editor */ 
        
        queriesForInformationResource
                .add(prefix
                        + "SELECT (str(?ContextNodeProperty) as ?contextNodeProperty) WHERE {"
                        + "?uri rdf:type obo:IAO_0000030 . "
                        + "?uri core:relatedBy ?e . "
                        + "?e rdf:type core:Editorship  ."
                        + "?e core:relates ?i  ."
                        + "?i rdf:type foaf:Agent ."
                        + "?i rdfs:label ?ContextNodeProperty  ."
                        + "}");

        /* subject area */
        
        queriesForInformationResource
                .add(prefix
                        + "SELECT (str(?ContextNodeProperty) as ?contextNodeProperty) WHERE {"
                        + "?uri rdf:type obo:IAO_0000030 . "
                        + "?uri core:hasSubjectArea ?f . ?f rdfs:label ?ContextNodeProperty ."
                        + "}");              
    }

}
