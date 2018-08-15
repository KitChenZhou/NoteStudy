package com.ckt.chen.fragmentexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by D22395 on 2017/12/20.
 */

public class BookContent {

    public static class Book {
        public Integer id;
        public String title;
        public String desc;
        public Book(Integer id, String title, String desc) {
            this.id = id;
            this.title = title;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return title;
        }
    }
    // 使用List集合记录系统所包含的Book对象
    public static List<Book> ITEMS = new ArrayList<>();
    public static Map<Integer, Book> ITEM_MAP = new HashMap<Integer, Book>();

    static {
        addItem(new Book(1, "Android编程权威指南", "Amazon移动开发类榜首畅销书，Android开发入门与进阶不二之选"));
        addItem(new Book(2, "第一行代码", "Android初学者的最佳入门书。全书由浅入深、系统全面地讲解了Android软件开发的方方面面"));
        addItem(new Book(3, "疯狂Android讲义", "疯狂源自梦想\n" +
                "技术成就辉煌"));
    }
    private static void addItem(Book book){
        ITEMS.add(book);
        ITEM_MAP.put(book.id, book);
    }

}
