package eu.larkc.csparql.sr4ld2014;

import eu.larkc.csparql.cep.api.RdfStream;
import eu.larkc.csparql.common.utils.CsparqlUtils;
import eu.larkc.csparql.core.engine.CsparqlEngine;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import eu.larkc.csparql.sr4ld2014.streamer.MyStreamer2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Experiment {

    private static Logger logger = LoggerFactory.getLogger(Experiment.class);

    /*
     * Example 6: Reasoning example
     */

    public static void main(String[] args) {
    	
    	int counter = 1;
    	while(counter < 17533) {
	    	String num = String.valueOf(counter);
	        File f = new File("DividedFiles\\"+num+".txt");
	        processFile(f);
	        counter++;
    	}

    }

    private static void processFile(File fil) {
        try{

            FileOutputStream file = null;
            try {
                file = new FileOutputStream("Results\\result"+fil.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            //Configure log4j logger for the csparql engine
            PropertyConfigurator.configure("log4j_configuration/csparql_readyToGoPack_log4j.properties");

            //Create csparql engine instance
            CsparqlEngine engine = new CsparqlEngineImpl();
            //Initialize the engine instance
            //The initialization creates the static engine (SPARQL) and the stream engine (CEP)
            engine.initialize(true);

            String queryBody = "REGISTER QUERY reasoning AS "
            		+ "PREFIX f: <http://larkc.eu/csparql/sparql/jena/ext#> "
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "PREFIX :<http://myexample.org#> "
                    + "SELECT ?s1 "
                    + "FROM STREAM <http://streamreasoning.org/streams/elearning> [RANGE 5m TUMBLING] "
                    + "FROM <http://sectors.org/sectors> "
                    + "WHERE { "
                    	+ "{ "
                    		+ "SELECT ?s1 (COUNT(DISTINCT ?ev1) AS ?cnt) WHERE { "
                    				+ ":traffic :has ?ev1 . ?ev1 :isType :no_moviment . ?ev1 :sector ?s1 . "
                    				+ ":traffic :has ?ev2 . ?ev2 :isType :moviment . ?ev2 :sector ?s1 . "
                    				+ ":pollution :has ?ev3 . ?ev3 :isType :normal_air . ?ev3 :sector ?s1 . "
                    				+ ":pollution :has ?ev4 . ?ev4 :isType :high_carbon . ?ev4 :sector ?s1 . "
                    				+ ":pollution :has ?ev5 . ?ev5 :isType :high_sulfure . ?ev5 :sector ?s1 . "
                    				+ "FILTER( f:timestamp(:traffic,:has,?ev1) < f:timestamp(:traffic,:has,?ev2) "
                    				+ "&& f:timestamp(:traffic,:has,?ev1) = f:timestamp(:pollution,:has,?ev3) "
                    				+ "&& f:timestamp(:pollution,:has,?ev3) < f:timestamp(:pollution,:has,?ev4) "
                    				+ "&& f:timestamp(:traffic,:has,?ev2) = f:timestamp(:pollution,:has,?ev4) "
                    				+ "&& f:timestamp(:pollution,:has,?ev4) < f:timestamp(:pollution,:has,?ev5) "
                    		+ ")} GROUP BY ?s1 "
                    	+ "}{ "
                    		+ "SELECT ?s1 (COUNT(DISTINCT ?ev6) AS ?count) WHERE { "
                    			+ "?s1 :adjacent ?s2 . "
                    			+ ":traffic :has ?ev6 . ?ev6 :isType :no_moviment . ?ev6 :sector ?s2 . "
                				+ ":traffic :has ?ev7 . ?ev7 :isType :moviment . ?ev7 :sector ?s2 . "
                				+ ":pollution :has ?ev8 . ?ev8 :isType :normal_air . ?ev8 :sector ?s2 . "
                				+ ":pollution :has ?ev9 . ?ev9 :isType :high_carbon . ?ev9 :sector ?s2 . "
                				+ ":pollution :has ?ev10 . ?ev10 :isType :high_sulfure . ?ev10 :sector ?s2 . "
                				+ "FILTER( f:timestamp(:traffic,:has,?ev6) < f:timestamp(:traffic,:has,?ev7) "
                				+ "&& f:timestamp(:traffic,:has,?ev6) = f:timestamp(:pollution,:has,?ev8) "
                				+ "&& f:timestamp(:pollution,:has,?ev8) < f:timestamp(:pollution,:has,?ev9) "
                				+ "&& f:timestamp(:traffic,:has,?ev7) = f:timestamp(:pollution,:has,?ev9) "
                				+ "&& f:timestamp(:pollution,:has,?ev9) < f:timestamp(:pollution,:has,?ev10) "
                    		+ ")} GROUP BY ?s1 "
                    	+ "}{ "
                    		+ "SELECT ?s1 (COUNT(DISTINCT ?s2) AS ?st) WHERE { "
                    			+ "?s1 :adjacent ?s2 . "
                    			//+ "?s3 :adjacent ?s2 . "
                    		+ "} GROUP BY ?s1 "
                    	+ "} " 
            	     + "} GROUP BY ?s1 ?cnt ?count ?st "
            	     + "HAVING( ?cnt > (?count / ?st) ) ";
            
            /**String queryBody = "REGISTER QUERY reasoning AS "
                    + "PREFIX f: <http://larkc.eu/csparql/sparql/jena/ext#> "
                    + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
                    + "PREFIX :<http://myexample.org#> "
                    + "SELECT ?s1 ((COUNT(DISTINCT ?ev1) + COUNT(DISTINCT ?ev2)) as ?cnt) "
                    + "FROM STREAM <http://streamreasoning.org/streams/elearning> [RANGE 5m TUMBLING] "
                    + "FROM <http://sectors.org/sectors> "
                    + "WHERE { "
	                    + ":traffic :has ?ev1. ?ev1 :isType ?t1. ?ev1 :sector ?s1. "
	                    + ":pollution :has ?ev2. ?ev2 :isType ?t2. ?ev2 :sector ?s1. "
	                    + "?s1 :zoneType ?z1. ?z1 :not_accept ?t1. ?z1 :not_accept ?t2. "
		                    
	                + "} GROUP BY ?s1 ";*/

            RdfStream s = new MyStreamer2("http://streamreasoning.org/streams/elearning", 1L, file, fil);

            //Register new streams in the engine
            engine.registerStream(s);

            Thread sThread = new Thread((Runnable) s);


            engine.putStaticNamedModel("http://sectors.org/sectors", CsparqlUtils.serializeRDFFile("examples_files/sector.rdf"));
            //engine.putStaticNamedModel("http://ports.org/ports", CsparqlUtils.serializeRDFFile("examples_files/port.rdf"));
            //Register new query in the engine
            CsparqlQueryResultProxy c = engine.registerQuery(queryBody, false);

            //Attach a result consumer to the query result proxy to print the results on the console
            c.addObserver(new ConsoleFormatter2(file));

            //Start streaming data
            sThread.start();
            //engine.updateReasoner(c.getSparqlQueryId(), CsparqlUtils.fileToString("examples_files/rdfs.rules"), ReasonerChainingType.FORWARD, CsparqlUtils.serializeRDFFile("examples_files/tbox.rdf"));
            
            
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            
          /**  Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            Thread.sleep(20000); Thread.sleep(20000); Thread.sleep(20000);
            */
            
            
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
