/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author ccchia.2014
 */
public class LuceneSpellChecker {
    private final SpellChecker spellchecker;
    
    public LuceneSpellChecker() throws IOException{
        Directory directory = new SimpleFSDirectory(new File("data/LuceneSpellChecker").toPath()); //relative to C:\Program Files\Apache Software Foundation\Apache Tomcat 8.0.27\bin\
        
        this.spellchecker = new SpellChecker(directory);
        spellchecker.indexDictionary(new PlainTextDictionary(Paths.get("./data/dictionary.txt")), new IndexWriterConfig(), true);
    }
    
    public HashMap<String, ArrayList<String>> getSuggestions(ArrayList<String> rawText) throws IOException{
        HashMap<String, ArrayList<String>> suggestions = new HashMap<String, ArrayList<String>>();
        
        for(String word: rawText){
            if(!spellchecker.exist(word)){
                String[] wordSuggestions = spellchecker.suggestSimilar(word, 5);
                ArrayList<String> uniqueSuggestions = new ArrayList<String>();
                for(String s: wordSuggestions){
                    if(!uniqueSuggestions.contains(s) && !s.trim().equals(s)){
                        uniqueSuggestions.add(s);
                    }
                }
                if(uniqueSuggestions.size() > 0){
                    //only add to suggestions if there are unique suggestions
                    suggestions.put(word, uniqueSuggestions);
                }
            }
        }
        
        return suggestions;
    }
    
    
}
