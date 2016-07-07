package test;

import io.limithot.naver.model.NaverBook;
import org.sqlite.SQLiteConfig;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by REIDEN on 2016-07-03.
 */
public class SaveManager {

    private Connection connection = null;
    private String dbFileName = "";
    private boolean isOpened = false;


    //public final static String DATABASE = "comic.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public SaveManager(String dbFileName) {
        this.dbFileName = dbFileName;
    }

    private boolean open() {
        try {

            SQLiteConfig config = new SQLiteConfig();
            connection = DriverManager.getConnection("jdbc:sqlite:/" + this.dbFileName, config.toProperties());
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

        isOpened = true;
        return true;
    }

    public synchronized boolean writeBook(NaverBook book) {

        try {
            open();

            String title = book.getTitle();
            String imageUrl = book.getImageFile().toString();
            String isbn10 = book.getIsbn10();
            String isbn13 = book.getIsbn13();

            PreparedStatement stmt = connection.prepareStatement("INSERT OR IGNORE INTO BOOKS (book_title, book_img, book_isbn10, book_isbn13) VALUES(?, ?, ?, ?);");
            stmt.setString(1, title);
            stmt.setString(2, imageUrl);
            stmt.setString(3, isbn10);
            stmt.setString(4, isbn13);

            stmt.execute();
            stmt.close();
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return true;


    }

    public synchronized List<NaverBook> readList(int limit) {

        List<NaverBook> list = new ArrayList<NaverBook>();

        try {

            open();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BOOKS LIMIT ?");
            stmt.setInt(1, limit);
            ResultSet row = stmt.executeQuery();
            while(row.next()) {
                String title = row.getString(1);
                String imageUrl = row.getString(2);
                NaverBook book = new NaverBook();
                book.setTitle(title);
                book.setImageUrl(imageUrl);

                list.add(book);
            }

            close();

        } catch(Exception e) {
            e.printStackTrace();
        }


        return list;

    }

    public synchronized List<NaverBook> readBook(String searchKeyword) {

        List<NaverBook> list = new ArrayList<NaverBook>();

        try {

            open();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM BOOKS WHERE book_title LIKE ?;");
            stmt.setString(1, searchKeyword + "%");
            ResultSet row = stmt.executeQuery();
            while(row.next()) {
                String title = row.getString(1);
                String imageUrl = row.getString(2);
                NaverBook book = new NaverBook();
                book.setTitle(title);
                book.setImageUrl(imageUrl);

                list.add(book);
            }

            close();

        } catch(Exception e) {
            e.printStackTrace();
        }


        return list;
    }

    private boolean openReadOnly() {
        try {

            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(true);
            connection = DriverManager.getConnection("jdbc:sqlite:/" + this.dbFileName, config.toProperties());
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }

        isOpened = true;
        return true;
    }

    private boolean close() {
        if (this.isOpened == false) {
            return true;
        }

        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
