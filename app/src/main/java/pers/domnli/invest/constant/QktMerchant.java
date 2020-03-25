package pers.domnli.invest.constant;

import java.util.HashMap;
import java.util.Map;

public class QktMerchant {

    private static Map<String,Item> items = new HashMap<>();

    static {
        items.put("11",new Item("11","8:00-12:00","01分-10分","5812餐饮"));
        items.put("12",new Item("12","8:00-12:00","11分-20分","7011酒店住宿"));
        items.put("13",new Item("13","8:00-12:00","21分-30分","5311百货、5977化妆品、5943办公用品、5948箱包"));
        items.put("14",new Item("14","8:00-12:00","31分-40分","5699服饰、5691成人服饰、5661妇女服饰、5641儿童服饰"));
        items.put("15",new Item("15","8:00-12:00","41分-50分","5251五金销售、5211建材、8912装修工程、5714窗帘装修"));
        items.put("16",new Item("16","8:00-12:00","51分-60分","7230美容理发、7311广告、4722旅行社"));
        items.put("21",new Item("21","13:00-15:00","01分-10分","5812餐饮"));
        items.put("22",new Item("22","13:00-15:00","11分-20分","4722旅行社、7997俱乐部、7311广告、7221摄影"));
        items.put("23",new Item("23","13:00-15:00","21分-30分","5541加油"));
        items.put("24",new Item("24","13:00-15:00","31分-40分","5977化妆品、5941体育用品、5311百货、5948箱包"));
        items.put("25",new Item("25","13:00-15:00","41分-50分","5699服饰、5691成人服饰、5661妇女服饰、5641儿童服饰"));
        items.put("26",new Item("26","13:00-15:00","51分-60分","5251五金销售、5211建材、8912装修工程、5712家具"));
        items.put("31",new Item("31","16:00-17:00","01分-10分","5812餐饮、5499食品"));
        items.put("32",new Item("32","16:00-17:00","11分-20分","4722旅行社、7997俱乐部、7311广告、7221摄影"));
        items.put("33",new Item("33","16:00-17:00","21分-30分","7298美容保健、7297足浴按摩、7011酒店住宿"));
        items.put("34",new Item("34","16:00-17:00","31分-40分","5977化妆品、5941体育用品、5311百货、5984箱包"));
        items.put("35",new Item("35","16:00-17:00","41分-50分","5699服饰、5691成人服饰、5661妇女服饰、5641儿童服饰"));
        items.put("36",new Item("36","16:00-17:00","51分-60分","5251五金销售、5211建材、8912装修工程、5712家具"));
        items.put("41",new Item("41","18:00-22:00","01分-10分","5812餐饮"));
        items.put("42",new Item("42","18:00-22:00","11分-20分","7011酒店住宿、7911KTV娱乐、7297足浴按摩、7298美容保健"));
        items.put("43",new Item("43","18:00-22:00","21分-30分","5311百货、5977化妆品、5943办公用品、5948箱包"));
        items.put("44",new Item("44","18:00-22:00","31分-40分","7230美容理发、7311广告、4722旅行社"));
        items.put("45",new Item("45","18:00-22:00","41分-50分","5699服饰、5691成人服饰、5661妇女服饰、5641儿童服饰"));
        items.put("46",new Item("46","18:00-22:00","51分-60分","5094钟表首饰、5722电器、5732电子设备、5533汽配"));
        items.put("51",new Item("51","23:00-02:00","01分-10分","5812餐饮"));
        items.put("52",new Item("52","23:00-02:00","11分-20分","7011酒店住宿"));
        items.put("53",new Item("53","23:00-02:00","21分-30分","5813酒吧"));
        items.put("54",new Item("54","23:00-02:00","31分-40分","7911KTV娱乐"));
        items.put("55",new Item("55","23:00-02:00","41分-50分","5921烟酒、5993烟草"));
        items.put("56",new Item("56","23:00-02:00","51分-60分","7297足浴按摩、7298美容保健"));
        items.put("61",new Item("61","03:00-07:00","01分-10分","5813酒吧"));
        items.put("62",new Item("62","03:00-07:00","11分-20分","7911KTV娱乐"));
        items.put("63",new Item("63","03:00-07:00","21分-30分","5921烟酒、5993烟草"));
        items.put("64",new Item("64","03:00-07:00","31分-40分","5331便利杂货、5411超市"));
        items.put("65",new Item("65","03:00-07:00","41分-50分","7011酒店住宿"));
        items.put("66",new Item("66","03:00-07:00","51分-60分","7298美容保健、7297足浴按摩"));
    }

    public static String get(String id){
        Item item = items.get(id);
        return item == null ? "":item.description;
    }

    static class Item{
        private String id;
        private String hour;
        private String minute;
        private String description;

        Item(String id,String hour,String minute,String description){
            this.id = id;
            this.hour = hour;
            this.minute = minute;
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
