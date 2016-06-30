package test;

import io.limithot.naver.api.NaverApi;
import io.limithot.naver.api.impl.NaverApiImpl;
import io.limithot.naver.model.NaverBook;
import io.limithot.naver.model.NaverSearch;
import jdk.internal.org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.staticFiles;

/**
 * Created by REIDEN on 2016-06-30.
 */
public class Application {



    public static void main(String args[]) {

        boolean localhost = true;

        if (localhost) {
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            staticFiles.externalLocation(projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }

        String USER_AGENT = "Mozilla/5.0";

        get("/hello", (req, res) -> {
            Map<String, Object> attributes = new HashMap<>();


//            URL obj = new URL("http://openapi.naver.com/search?key=c1b406b32dbbbbeee5f2a36ddc14067f&query=%EC%82%BC%EA%B5%AD%EC%A7%80&display=10&start=1&target=book");
//            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
//
//            con.setRequestMethod("GET");
//            con.setRequestProperty("User-Agent", USER_AGENT);
//
//            int resCode = con.getResponseCode();
//            BufferedReader in = new BufferedReader(new InputStreamReader((con.getInputStream())));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//
//            in.close();
//
//            attributes.put("message", response.toString());
//            String responseText= new String(response.toString().getBytes("UTF-8"), "UTF-8");
//
//            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(responseText);
//            XPath xpath = XPathFactory.newInstance().newXPath();
//            NodeList cols = (NodeList)xpath.compile("item").evaluate(document, XPathConstants.NODESET);
//
//            for(int index = 0; index < cols.getLength(); index++) {
//                String title = cols.item(index).getAttributes().item(0).getTextContent();
//                System.out.println("TITLE: " + title);
//            }


            NaverApi api = new NaverApiImpl();

            NaverSearch search = new NaverSearch();

            search.setIsbn("9791195618408");
            try {
                List<NaverBook> books = api.searchBook(search);

                for(NaverBook book : books){
                    System.out.println("BOOK: " + book.getTitle());
                    attributes.put("message", book.getTitle());
                }

           } catch(Exception e) {
                System.out.println("EXCEPTION");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }



            return new ModelAndView(attributes, "hello.ftl");

        }, new FreeMarkerEngine());
    }
}
