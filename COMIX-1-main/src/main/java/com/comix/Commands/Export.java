package com.comix.Commands;
import com.comix.PersonalCollection.PersonalCollection;
import com.comix.model.Comic;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Export {
    

    public static void exportCSV(String username, File file){
        try{
            FileWriter out = new FileWriter(file);
            CSVWriter writer = new CSVWriter(out);
            List<Comic> comics = PersonalCollection.getCollection(username);
            String[] header = {"Title","Series","Issue","Publisher","Release Date","Creators","Grade", "Base Value",
                    "Calculated Value", "Is Slabbed?"};
            writer.writeNext(header);
            
            for(Comic comic : comics){
                String[] com = {comic.getTitle(), comic.getSeries(), comic.getIssue(), comic.getPublisher(),
                    comic.getPublicationDate(), comic.getCreators(), Integer.toString(comic.getGrade()),
                        Double.toString(comic.getBaseValue()), Double.toString(comic.getValue()),
                        Boolean.toString(comic.getIsSlabbed())};
                writer.writeNext(com);
            }
            writer.close();

        }catch(IOException ie){
            ie.printStackTrace();
        }
        

    }
    // protected boolean exportXML(){

    // }
    // protected boolean exportJSON(){

    // }
}
