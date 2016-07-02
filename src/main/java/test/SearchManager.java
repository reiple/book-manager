package test;

import io.limithot.naver.api.NaverApi;
import io.limithot.naver.api.impl.NaverApiImpl;
import io.limithot.naver.model.NaverBook;
import io.limithot.naver.model.NaverSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by REIDEN on 2016-07-02.
 */
public class SearchManager {

    NaverApi api = null;
    NaverSearch search = null;

    public SearchManager() {
        api = new NaverApiImpl();
        search = new NaverSearch();
    }

    /**
     * Search ISBN code through NAVER API
     * @param isbn ISBN code
     * @return Book Information
     */
    public List<NaverBook> searchISBN(String isbn) {

        List<NaverBook> resultList = null;

        try {
            search.setIsbn(isbn);

            List<NaverBook> books = api.searchBook(search);

            for(NaverBook book : books){
                System.out.println("TITLE: " + book.getTitle());
            }

            resultList = books;

        } catch(Exception e) {
            System.out.println("EXCEPTION");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }


}
