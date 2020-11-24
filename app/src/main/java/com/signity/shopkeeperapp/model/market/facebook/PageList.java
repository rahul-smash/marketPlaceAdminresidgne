package com.signity.shopkeeperapp.model.market.facebook;

import java.util.List;

public class PageList {
    /**
     * data : [{"access_token":"EAAj5ZBFM95j0BAB6ZCBCxz0ZAcVCoBqfSINnZCifbcC1eJPvZBzz5uAmZCZBkDvOFvOsSiMrqja9Wln3VBsAWZBkJuZC1iObaGZA8wMnL9W5o12IUK35ZCCNVTyaz6IbXXSKm311kNCFFGAGGHS7xY04mkhiO2LjCZAPZAbLtZAll1e0RqZBDiu9p9jUZCZAOZB9JC3PGtFgMZD","category":"Beauty Salon","category_list":[{"id":"199236533423806","name":"Beauty salon"}],"name":"Marketing Tool Testing","id":"103620701053071","tasks":["ANALYZE","ADVERTISE","MODERATE","CREATE_CONTENT","MANAGE"]},{"access_token":"EAAj5ZBFM95j0BAP0HMLaGAbS6Wzjux9cjt8QWZC1sKHVtD0uuaNkrYsJudqxZCXqSsTVSk9KrYLGCD323TZA8FGIovvv6kxyBZBqobptXL6JbgRsQqa9GtXT8RGcd8dGURbZCIXqdC7lZBWeKbZCqZA9Ba78AdxuliGZAoWjti8Tw9tOPsqjuOTZBzZB1HtR6gZC27ycZD","category":"E-commerce website","category_list":[{"id":"1756049968005436","name":"E-commerce website"}],"name":"TestingSignity","id":"104126787659359","tasks":["ANALYZE","ADVERTISE","MODERATE","CREATE_CONTENT","MANAGE"]},{"access_token":"EAAj5ZBFM95j0BAL8qxKVQvrXPXZB6nQqLW6PZBlYfckSk1hAMBbbA72jFutwVTZBs29AWZAQRSW90O4mPvafcKDGrFYIMZCn7J1pZAJWFE4ihzZBQXjZCUXmtksI1VbwW2Wz4LzV7ZBdKJkzqAPGeMZA9wZAZAhxePIJQ7FNnxfZAFeUb5rk7AcEUJCG5oYPQQSb4ZCniMZD","category":"E-commerce website","category_list":[{"id":"1756049968005436","name":"E-commerce website"}],"name":"Signity page","id":"113622943373570","tasks":["ANALYZE","ADVERTISE","MODERATE","CREATE_CONTENT","MANAGE"]},{"access_token":"EAAj5ZBFM95j0BAJ6Y8vCLDOmLQCVvGu3fY5thLEZCAVa3KwCo8rfZA5XA8EkV6h0VD6uIGY2wvIZC006F3n0mzGfzulFK8UBD9cbMhmkqCFgDwDZBWv8vvrbqUERK38JIQjYBSWIWZAjZBHUGQlu5Bxt17rcLgymtyjCyeqVkWKnO8jFF8kiyZCaKnqhjj4XkyZC2Dew7fo9WZCgZDZD","category":"Education","category_list":[{"id":"2250","name":"Education"},{"id":"1602","name":"Public figure"}],"name":"Vueque Test Page","id":"388446121288182","tasks":["ANALYZE","ADVERTISE","MODERATE","CREATE_CONTENT","MANAGE"]}]
     * paging : {"cursors":{"before":"MTAzNjIwNzAxMDUzMDcx","after":"Mzg4NDQ2MTIxMjg4MTgy"}}
     */

    private PagingBean paging;
    private List<DataBean> data;

    public PagingBean getPaging() {
        return paging;
    }

    public void setPaging(PagingBean paging) {
        this.paging = paging;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PagingBean {
        /**
         * cursors : {"before":"MTAzNjIwNzAxMDUzMDcx","after":"Mzg4NDQ2MTIxMjg4MTgy"}
         */

        private CursorsBean cursors;

        public CursorsBean getCursors() {
            return cursors;
        }

        public void setCursors(CursorsBean cursors) {
            this.cursors = cursors;
        }

        public static class CursorsBean {
            /**
             * before : MTAzNjIwNzAxMDUzMDcx
             * after : Mzg4NDQ2MTIxMjg4MTgy
             */

            private String before;
            private String after;

            public String getBefore() {
                return before;
            }

            public void setBefore(String before) {
                this.before = before;
            }

            public String getAfter() {
                return after;
            }

            public void setAfter(String after) {
                this.after = after;
            }
        }
    }

    public static class DataBean {
        /**
         * access_token : EAAj5ZBFM95j0BAB6ZCBCxz0ZAcVCoBqfSINnZCifbcC1eJPvZBzz5uAmZCZBkDvOFvOsSiMrqja9Wln3VBsAWZBkJuZC1iObaGZA8wMnL9W5o12IUK35ZCCNVTyaz6IbXXSKm311kNCFFGAGGHS7xY04mkhiO2LjCZAPZAbLtZAll1e0RqZBDiu9p9jUZCZAOZB9JC3PGtFgMZD
         * category : Beauty Salon
         * category_list : [{"id":"199236533423806","name":"Beauty salon"}]
         * name : Marketing Tool Testing
         * id : 103620701053071
         * tasks : ["ANALYZE","ADVERTISE","MODERATE","CREATE_CONTENT","MANAGE"]
         */

        private String access_token;
        private String category;
        private String name;
        private String id;
        private List<CategoryListBean> category_list;
        private List<String> tasks;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<CategoryListBean> getCategory_list() {
            return category_list;
        }

        public void setCategory_list(List<CategoryListBean> category_list) {
            this.category_list = category_list;
        }

        public List<String> getTasks() {
            return tasks;
        }

        public void setTasks(List<String> tasks) {
            this.tasks = tasks;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "access_token='" + access_token + '\'' +
                    ", category='" + category + '\'' +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", category_list=" + category_list +
                    ", tasks=" + tasks +
                    '}';
        }

        public static class CategoryListBean {
            /**
             * id : 199236533423806
             * name : Beauty salon
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
