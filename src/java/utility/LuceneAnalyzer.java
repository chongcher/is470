package utility;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class LuceneAnalyzer {
    
  public static HashMap<String, Integer> getKeywords(String rawText){
    HashMap<String, HashMap<String,Integer>> keywords = new HashMap<String, HashMap<String,Integer>>();
    
    String[] parts = rawText.split("\\W+");
    ArrayList<String> rawTextWords = (ArrayList<String>) Arrays.asList(parts);
    
    //form tokens
    EnglishAnalyzer analyzer = new EnglishAnalyzer();
    try(TokenStream stream = analyzer.tokenStream("keywords", new StringReader(rawText));) {
        stream.reset();
        TokenStream result = new LowerCaseFilter(stream);
        //result = new StopFilter(result, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        //result = new PorterStemFilter(result);
        CharTermAttribute termAtt = result.addAttribute(CharTermAttribute.class);
        // print all tokens until stream is exhausted
        while (stream.incrementToken()) {
            String keyword = termAtt.toString();
            String actualWord = rawTextWords.remove(0);
            if(keywords.containsKey(keyword)){
                HashMap<String,Integer> keywordCounter = keywords.get(keyword);
                if(keywordCounter.containsKey(actualWord)){
                    int value = keywordCounter.get(actualWord);
                    keywordCounter.put(actualWord, ++value);
                }
                else{
                    keywordCounter.put(actualWord, 1);
                }
            }
            else{
                HashMap<String,Integer> keywordCounter = new HashMap<String,Integer>();
                keywordCounter.put(actualWord, 1);
                keywords.put(keyword, keywordCounter);
            }
        }
        stream.end();
    } 
    catch(IOException e){
        System.out.println(e);
    }
    
    return getHighestOccurancePerKeyword(keywords);
  }
  
  private static HashMap<String, Integer> getHighestOccurancePerKeyword(HashMap<String, HashMap<String,Integer>> keywords){
      HashMap<String, Integer> result = new HashMap<String, Integer>();
      
      Set<String> keywordsKeys = keywords.keySet();
      for(String keyword: keywordsKeys){
          HashMap<String,Integer> unstemmedCount = keywords.get(keyword);
          Set<String> unstemmedCountKeys = unstemmedCount.keySet();
          int totalCount = 0;
          int maxCount = -1;
          String highestOccurance = "";
          for(String unstemmed: unstemmedCountKeys){
              int unstemmedOccurance = unstemmedCount.get(unstemmed); 
              totalCount += unstemmedOccurance;
              if(unstemmedOccurance > maxCount){
                  maxCount = unstemmedOccurance;
                  highestOccurance = unstemmed;
              }
              else if(unstemmedCount.get(unstemmed) == maxCount){
                  if(unstemmed.length() < highestOccurance.length()){ //only replace highestOccurance if unstemmed is shorter                      
                      highestOccurance = unstemmed;
                  }
              }
          }
          result.put(highestOccurance, totalCount);
      }
      
      return result;
  }
  
}