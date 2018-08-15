package com.ckt.test.criminal.database;

/**
 * Created by D22395 on 2017/10/12.
 */
public class CrimeDbSchema {

    /*
        内部类：定义描述数据表元素的String常量
     */
    public static final class CrimeTable { // 内部类
        public static final String NAME = "crimes";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
        }
    }
}
