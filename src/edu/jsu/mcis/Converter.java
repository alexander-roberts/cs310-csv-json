package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and other whitespace
        have been added for clarity).  Note the curly braces, square brackets, and double-quotes!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject();
            
			//decode string into raw format
            String[] data = csvString.split("\n");
			String[][] data2 = new String[data.length][(data[0].split("\",\"")).length];
			for(int i=0;i<data2.length;i++){
				int end = data2[0].length-1;
				data2[i] = data[i].split("\",\"");
				data2[i][0] = data2[i][0].substring(1,data2[i][0].length);
				data2[i][end] = data2[i][end].substring(0,data2[0][end].length-1);
			}
			
			//re-encode string into JSON
			results = "{\"colHeaders\":[";
			for(int i=0;i<data2.length;i++){
				results = results+"\""+data2[0][i]+"\",";
			}
			results = results.substring(0,results.length-1)+"],\n\"rowHeaders\":[";
			for(int i=1;i<data2[0].length;i++){
				results = results+"\""+data2[i][0]+"\",";
			}
			results = results.substring(0,results.length-1)+"],\n\"data\":[";
			for(int i=1;i<data2.length;i++){
				results = results+"[";
				for(int j=1;j<data2[0].length;j++){
					results = results+data2[i][j]+",";
				}
				results = results.substring(0,results.length-1)+"],";
			}
			results = results.substring(0,results.length-1)+"]}";
        }
        
        catch(IOException e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            //decode string into raw format
			String[] data = jsonString.split(",");
			String[] rows = data[1].split("\",\"");
			String[] cols = data[0].split("\",\"");
			String[] array = data[2].split("],[");
			String[][] data2 = new String[rows.length][cols.length];
			for(int i=0;i<cols.length;i++){
				data2[0][i] = rows[i];
			}
			String[] temp = data2[0][0].split("\"");
			data2[0][0] = temp[temp.length-2];
			data2[0][data2[0].length-1] = data2[0][data2[0].length-1].substring(0,data2[0][data2[0].length-1].length-1);
			for(int i=1;i<rows.length;i++) {
				data2[i][0] = cols[i];
			}
			array[0] = array
			for(int i=1;i<rows.length;i++) {
				data2[i] = array[i-1].split(",");
				data2[i][0] = 
			}
			
			
            
        }
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }
	
}