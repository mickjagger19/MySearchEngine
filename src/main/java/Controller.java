import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

    public static CrawlController controller;

    public enum website { Zhihu, Guokr, Douban,Baidu};

    public static website current_web;

    public static void main(String[] args) throws Exception {


        current_web = website.Baidu;

        LuceneTest.start();

        String crawlStorageFolder = "/data/crawl/root";

        int numberOfCrawlers = 5;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        config.setResumableCrawling(true);


        switch (current_web){
            case Zhihu:{
                numberOfCrawlers = 10;
                break;
            }
            case Guokr:{
                numberOfCrawlers = 3;
                config.setPolitenessDelay(1300);
                break;
            }
            case Baidu:{
                numberOfCrawlers = 10;
                break;
            }
        }


        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

        controller = new CrawlController(config, pageFetcher, robotstxtServer);

        addSeedZhihu();

        switch (current_web){
            case Zhihu:{addSeedZhihu();}
            case Guokr:{addSeedGuokr();}
            case Douban:{addSeedDouban();}
        }

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);

        LuceneTest.iwr.commit();
        LuceneTest.iwr.close();
    }


    public static void addSeedZhihu(){
        controller.addSeed("https://www.zhihu.com/follow");
        controller.addSeed("https://www.zhihu.com");
        controller.addSeed("https://www.zhihu.com/hot");
        controller.addSeed("https://www.zhihu.com/explore");
        controller.addSeed("https://www.zhihu.com/topic");
    }

    public static void addSeedGuokr(){

        controller.addSeed("https://www.guokr.com/ask/");
        controller.addSeed("https://www.guokr.com/ask/hottest/");
        controller.addSeed("https://www.guokr.com/ask/newest/");
        controller.addSeed("https://www.guokr.com/ask/highlight/");
        controller.addSeed("https://www.guokr.com/ask/popular/");
//        controller.addSeed("https://www.guokr.com/ask/tag/专业/");
//        controller.addSeed("https://www.guokr.com/ask/tag/高考/");
//        controller.addSeed("https://www.guokr.com/ask/tag/心理学/");
//        controller.addSeed("https://www.guokr.com/ask/tag/生活/");
//        controller.addSeed("https://www.guokr.com/ask/tag/category/hot/");


    }

    public static void addSeedDouban(){
        controller.addSeed("https://movie.douban.com");
        controller.addSeed("https://www.zhihu.com");
        controller.addSeed("https://www.zhihu.com/hot");
        controller.addSeed("https://www.zhihu.com/explore");
        controller.addSeed("https://www.zhihu.com/topic");
    }


    public static void addSeedBaidu(){
        controller.addSeed("https://zhidao.baidu.com");
        controller.addSeed("https://zhidao.baidu.com/list?fr=daohang");
        controller.addSeed("https://zhidao.baidu.com/list?type=highScore");
        controller.addSeed("https://zhidao.baidu.com/list?type=hot");
        controller.addSeed("https://zhidao.baidu.com/list?type=feed");
    }
}