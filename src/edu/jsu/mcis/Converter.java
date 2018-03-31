
package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 *
 * @author Alexander Roberts
 */
public class Converter {
    
    public static String csvToJson(String input) {
        
        String output = "";
        
        try {
            //initialize objects
            CSVReader in = new CSVReader(new StringReader(input));
            List<String[]> csvData = in.readAll();
            Iterator<String[]> ptr = csvData.iterator();
            
            JSONObject parser = new JSONObject(); //where array data will be processed and moved to output
            JSONArray colHeader = new JSONArray();
            JSONArray rowHeader = new JSONArray();
            
            //manage the 2-dimensional data as JSONArray of JSONArrays, in a sense (2 step processing)
            JSONArray outer = new JSONArray();
            JSONArray inner = new JSONArray();
            
            //process column headers
            String[] col = csvData.get(0); //first row is column headers
            colHeader.addAll(Arrays.asList(col)); //compiler like this for some reason
            
            for(int i = 1; i<csvData.size(); i++) { //start at 1 since we just dealt with col headers
                inner = new JSONArray(); //clear inner loop values
                String[] temp = csvData.get(i);
                rowHeader.add(temp[0]); //row header is first every row
                
                for(int j = 1; j<temp.length; j++) { //[1..length] bc just dealt with row headers
                    inner.add(Integer.parseInt(temp[j])); //force value from String to int
                }
                
                outer.add(inner); //move data back into bigger array
                
                parser.put("colHeaders", colHeader);
                parser.put("rowHeaders", rowHeader);
                parser.put("data",outer);
                
                output = JSONValue.toJSONString(parser);
            }  
        } catch (IOException e) { e.printStackTrace(); }
            
        return output.trim();
    }
    
    public static String jsonToCsv(String input) {
        String output = "";
       
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonData = (JSONObject) (parser.parse(input));
            
            //extract values
            JSONArray rowHeader = (JSONArray) (jsonData.get("rowHeaders"));
            JSONArray colHeader = (JSONArray) (jsonData.get("colHeaders"));
            JSONArray data = (JSONArray) (jsonData.get("data"));
            
           for(int i = 0; i<colHeader.size();i++) { //first row
               output+=("\""+colHeader.get(i)+"\""); //escaped quotes
               if(i != (colHeader.size()-1)) //add comma except on last entry
                   output+=",";
           }
           output+="\n";
           
           for(int i = 0; i<rowHeader.size();i++) { //then everything else
               JSONArray innerdata = (JSONArray) (data.get(i));
               output+=("\""+rowHeader.get(i)+"\",");
               for(int j = 0; j<innerdata.size(); j++) { //same conditionals as col headers
                    output+=("\""+innerdata.get(j)+"\""); //escaped quotes
                    if(j != (innerdata.size()-1)) //add comma except on last entry
                        output+=",";
               }
               output+="\n";
           }     
        } catch (ParseException e) { e.printStackTrace();}
         
        return output.trim();
    }
    
}
