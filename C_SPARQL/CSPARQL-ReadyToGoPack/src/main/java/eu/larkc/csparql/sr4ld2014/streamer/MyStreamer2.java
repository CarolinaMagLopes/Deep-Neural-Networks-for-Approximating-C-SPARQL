package eu.larkc.csparql.sr4ld2014.streamer;

import eu.larkc.csparql.cep.api.RdfQuadruple;
import eu.larkc.csparql.cep.api.RdfStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MyStreamer2 extends RdfStream implements Runnable  {

    private long sleepTime;
    private String baseUri;
    private FileOutputStream fileTuple;
    private File fil;

    public MyStreamer2(String iri, long sleepTime, FileOutputStream fileTuple, File fil) {
        super(iri);
        this.sleepTime = sleepTime;
        this.baseUri = "http://myexample.org#";
        this.fileTuple = fileTuple;
        this.fil = fil;
    }

    public void run() {

        /**String activity;
        long timestamp = 0L;
        int counter = 1;
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(fil));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = file.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        String[] tuple = line.split(" ");
        long last = Long.parseLong(tuple[tuple.length-1]);

        while(line != null){
            try{
                tuple = line.split(" ");
                activity = tuple[0];
                String sector = tuple[tuple.length-2];
            	timestamp = Long.parseLong(tuple[tuple.length-1]);
                if(timestamp != last) {
                	long elapsed = System.currentTimeMillis()-start;
                	if(elapsed < 60000)
                		Thread.sleep(60000-elapsed);
                	last = timestamp;
                	start = System.currentTimeMillis();
                }

                if(activity.equals("traffic")) {
                    String msr = tuple[1];
                    RdfQuadruple q = new RdfQuadruple(baseUri +"traffic", baseUri +"has", baseUri +"event#"+counter, timestamp);
                    this.put(q);
                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"isType", baseUri+msr, timestamp);
                    this.put(q);
                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"sector", baseUri+"sector"+sector, timestamp);
                    this.put(q);
                    counter++;
                    
                    try {
                        fileTuple.write((line+"\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    String measure = tuple[1];

                    RdfQuadruple q = new RdfQuadruple(baseUri +"pollution", baseUri +"has", baseUri +"event#"+counter, timestamp);
                    this.put(q);
                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"isType", baseUri+measure, timestamp);
                    this.put(q);
                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"sector", baseUri+"sector"+sector, timestamp);
                    this.put(q);
                    counter++;

                    try {
                        fileTuple.write((line+"\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                line = file.readLine();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    	
    	
    	String activity;
        long timestamp = 0L;
        int counter = 1;
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(fil));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            line = file.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        String[] tuple = line.split(" ");
        long last = Long.parseLong(tuple[tuple.length-1]);

        while(line != null){
            try{
                tuple = line.split(" ");
                activity = tuple[0];
                String sector = tuple[tuple.length-2];
            	timestamp = Long.parseLong(tuple[tuple.length-1]);
                if(timestamp != last) {
                	long elapsed = System.currentTimeMillis()-start;
                	if(elapsed < 60000)
                		Thread.sleep(60000-elapsed);
                	last = timestamp;
                	start = System.currentTimeMillis();
                }

                if(activity.equals("traffic")) {
                    String msr = tuple[1];
                    if(msr.equals("moviment") || msr.equals("no_moviment")) {
	                    RdfQuadruple q = new RdfQuadruple(baseUri +"traffic", baseUri +"has", baseUri +"event#"+counter, timestamp);
	                    this.put(q);
	                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"isType", baseUri+msr, timestamp);
	                    this.put(q);
	                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"sector", baseUri+"sector"+sector, timestamp);
	                    this.put(q);
	                    counter++;
	                    try {
	                        fileTuple.write((line+"\n").getBytes());
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
                    }
                    
                    

                } else {
                    String measure = tuple[1];
                    if(measure.equals("high_sulfure") || measure.equals("high_carbon") || measure.equals("normal_air")) {
	                    RdfQuadruple q = new RdfQuadruple(baseUri +"pollution", baseUri +"has", baseUri +"event#"+counter, timestamp);
	                    this.put(q);
	                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"isType", baseUri+measure, timestamp);
	                    this.put(q);
	                    q = new RdfQuadruple(baseUri +"event#"+counter, baseUri +"sector", baseUri+"sector"+sector, timestamp);
	                    this.put(q);
	                    counter++;
	                    try {
	                        fileTuple.write((line+"\n").getBytes());
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
                    }


                }
                line = file.readLine();

            } catch(Exception e){
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	
    	
    	
    	
    	
    }

}