import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class LuceneTest {

	public static IndexWriter iwr;

	public static String filePath = "index";

	public static PrintStream ps = new PrintStream(System.out);

	public static int url_counter = 0;

	public static void start() throws IOException {

		File f=new File(filePath);


		Directory dir = FSDirectory.open(f);

		Analyzer analyzer = new IKAnalyzer();

		IndexWriterConfig conf=new IndexWriterConfig(Version.LUCENE_4_10_0,analyzer);

		iwr = new IndexWriter(dir, conf);

	}

	public static void addZhihuDocument(String url) throws IOException {

		if (( !url.contains("question") ) || ( !url.contains("answer")) || ( url.contains("delete")) ) return;

		System.out.println(url_counter++);

		org.jsoup.nodes.Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").get();

		String question = "";

		String answer = "";

		String date = "";

		String author = "";

		String like = "";

		String follower = "";

		String field = "";

		String viewer = "";

		String link = url;

		question = doc.select(".QuestionHeader-title").text();

//		System.out.println(question);
		answer = doc.select(".RichContent-inner").get(0).text();
//		System.out.println(answer);
		date = doc.select(".ContentItem-time").get(0).text();
		System.out.println(date);
		author = doc.select(".AuthorInfo-name").get(0).text();
//		System.out.println(author);
		like = doc.select(".VoteButton--up").get(0).text().substring(3);
//		System.out.println(like);
		follower = doc.select(".NumberBoard-itemValue").get(0).text();
//		System.out.println(follower);
		viewer = doc.select(".NumberBoard-itemValue").get(1).text();
//		System.out.println(viewer);
		field = doc.select(".QuestionHeader-topics").text();

//		System.out.println(field);

		Document newdoc = new Document();

		Field f1=new TextField("question",question, Field.Store.YES);
		Field f2=new TextField("answer",answer,Field.Store.YES);
		Field f3=new TextField("date",date,Field.Store.YES);
		Field f4=new TextField("author",author,Field.Store.YES);
		Field f5=new StringField("like",like,Field.Store.YES);
		Field f6=new StringField("follower",follower,Field.Store.YES);
		Field f7=new StringField("field",field,Field.Store.YES);
		Field f8=new StringField("viewer",viewer,Field.Store.YES);
		Field f9=new StringField("link",link,Field.Store.YES);

		newdoc.add(f1);
		newdoc.add(f2);
		newdoc.add(f3);
		newdoc.add(f4);
		newdoc.add(f5);
		newdoc.add(f6);
		newdoc.add(f7);
		newdoc.add(f8);
		newdoc.add(f9);

		iwr.addDocument(newdoc);

		iwr.commit();

		return;
	}

	public static void addGuokrDocument(String url) throws IOException {

		if (  ( !url.startsWith("https://www.guokr.com/question/") ) || ( url.contains("hisroty"))  ) return;

		System.out.println(url_counter++);

		org.jsoup.nodes.Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").get();

		String question = "";

		String answer = "";

		String date = "";

		String author = "";

		String like = "";

		String follower = "";

		String field = "";

		String viewer = "";

		String link = url;


		question = doc.select(".post-title").text();

		Elements answers = doc.select(".answerTxt");

		Elements dates = doc.select(".answer-date");

		Elements authors = doc.select(".answer-usr-name");

		Elements likes = doc.select(".answer-digg-num");

		int length = doc.select(".focused-num").get(0).text().length();

		follower = doc.select(".focused-num").get(0).text().substring(0,length-4);
//		System.out.println(follower);

		field = doc.select(".post-tags .tag").text();
//		System.out.println(field);


		for ( int i = 0; i<answers.size(); i++){

			answer = answers.get(i).text();

			date = dates.get(i).text();

			author = authors.get(i).text();

			like = likes.get(i).text();

			System.out.println(answer);

			System.out.println(date);

			System.out.println(author);

			System.out.println(like);


			Document newdoc = new Document();

			Field f1=new TextField("question",question, Field.Store.YES);
			Field f2=new TextField("answer",answer,Field.Store.YES);
			Field f3=new TextField("date",date,Field.Store.YES);
			Field f4=new TextField("author",author,Field.Store.YES);
			Field f5=new StringField("like",like,Field.Store.YES);
			Field f6=new StringField("follower",follower,Field.Store.YES);
			Field f7=new StringField("field",field,Field.Store.YES);
			Field f8=new StringField("viewer",viewer,Field.Store.YES);
			Field f9=new StringField("link",link,Field.Store.YES);

			newdoc.add(f1);
			newdoc.add(f2);
			newdoc.add(f3);
			newdoc.add(f4);
			newdoc.add(f5);
			newdoc.add(f6);
			newdoc.add(f7);
			newdoc.add(f8);
			newdoc.add(f9);

			iwr.addDocument(newdoc);

			iwr.commit();

		}


		return;
	}

	public static void addBaiduDocument(String url) throws IOException {

		if (  ( !url.startsWith("https://zhidao.baidu.com/question/") ) ) return;

		System.out.println(url_counter++);

		org.jsoup.nodes.Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36").get();

		String question = "";

		String answer = "";

		String date = "";

		String author = "";

		String like = "";

		String follower = "";

		String field = "";

		String viewer = "";

		String link = url;


		question = doc.select(".ask-title").text();

		Elements answers = doc.select(".gt-answers-mask");

		Elements dates = doc.select(".wgt-replyer-all-time");

		Elements authors = doc.select(".wgt-replyer-all-uname");

		Elements likes = doc.select(".evaluate-num");

		int length = doc.getElementById("v-times").text().length();

//		follower = doc.select(".focused-num").get(0).text().substring(0,length-4);
//		System.out.println(follower);

		viewer = doc.getElementById("v-times").text().substring(3,length-2);

		System.out.println(viewer);

//		field = doc.select(".post-tags .tag").text();
//		System.out.println(field);


		for ( int i = 0; i<answers.size(); i++){

			answer = answers.get(i).text();

			date = dates.get(i).text();

			author = authors.get(i).text();

			like = likes.get(i).text();

			System.out.println(answer);

			System.out.println(date);

			System.out.println(author);

			System.out.println(like);


			Document newdoc = new Document();

			Field f1=new TextField("question",question, Field.Store.YES);
			Field f2=new TextField("answer",answer,Field.Store.YES);
			Field f3=new TextField("date",date,Field.Store.YES);
			Field f4=new TextField("author",author,Field.Store.YES);
			Field f5=new StringField("like",like,Field.Store.YES);
			Field f6=new StringField("follower",follower,Field.Store.YES);
			Field f7=new StringField("field",field,Field.Store.YES);
			Field f8=new StringField("viewer",viewer,Field.Store.YES);
			Field f9=new StringField("link",link,Field.Store.YES);

			newdoc.add(f1);
			newdoc.add(f2);
			newdoc.add(f3);
			newdoc.add(f4);
			newdoc.add(f5);
			newdoc.add(f6);
			newdoc.add(f7);
			newdoc.add(f8);
			newdoc.add(f9);

			iwr.addDocument(newdoc);

			iwr.commit();

		}


		return;
	}

	public static void search(String queryword) throws ParseException, IOException{

		File f=new File(filePath);

		IndexSearcher searcher=new IndexSearcher(DirectoryReader.open(FSDirectory.open(f)));

		Analyzer analyzer = new IKAnalyzer();
		String [] fields= {"question","answer","field"};
        QueryParser parser = new MultiFieldQueryParser(fields, analyzer);

        Query query=parser.parse(queryword);
        TopDocs hits=searcher.search(query,5);//前面几行代码也是固定套路，使用时直接改field和关键词即可

//		QueryScorer scorer = new QueryScorer(query);
//
//		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
//
//		Highlighter highlighter =  new Highlighter(scorer);
//
//		highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));


		for(ScoreDoc doc:hits.scoreDocs){

            Document d=searcher.doc(doc.doc);

//            String question = d.get("question");
//
//			TokenStream tokenStream=analyzer.tokenStream("question", question);
//
//			System.out.println(highlighter.getBestFragment(tokenStream, question));

			ps.println();

            ps.println("问题: " + d.get("question"));

			ps.println("关注者: " + d.get("follower") + " | 被浏览: " + d.get("viewer"));

			ps.println("领域: " + d.get("field"));

			ps.println("作者: " + d.get("author"));

			if ( d.get("answer").length() < 70) {
				ps.println("回答: " + d.get("answer"));
			}
			else{
				ps.print("回答: " + d.get("answer").substring(0,70));
				ps.println("...");
			}

			ps.println("点赞: " + d.get("like"));

			ps.println("日期: " + d.get("date"));

			ps.println("链接: " + d.get("link"));

			ps.println();

        }


	}



}