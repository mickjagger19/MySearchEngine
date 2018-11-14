import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
//import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;

import java.io.IOException;
import java.util.Scanner;

//package mysearchengine;
public class Search {


    public static void main(String args[]) throws IOException, ParseException, InvalidTokenOffsetsException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to MySearchEngine!");

        System.out.println("Please enter the query word:(enter quit to exit)");

        while (true) {


            String QueryWord = scan.nextLine();

            if (QueryWord.equals("quit")) return;

            if ( QueryWord.equals("")) continue;

            if ( QueryWord.equals("\n")) continue;


            LuceneTest.search(QueryWord);

        }



    }


}
