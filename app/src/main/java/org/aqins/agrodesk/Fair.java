package org.aqins.agrodesk;


/**
 * Created by Completement on 07/11/2016.
 */

public class Fair {
    public String projName;
    public String quality;
    public String num_volun;
    public String activity1;
    public String activity2;
    public String country;
    public String meeting_city;
    public String meeting_date;
    public String meeting_desc;
    public String meeting_place;
    public Boolean isGift;
    public String gift_quality;

    public String username;
    public Long post_time;

    public Fair(){

    }
    public Fair(String projName, String quality, String num_volun, String activity1, String activity2, String country,
                String meeting_city, String meeting_date, String meeting_desc, String meeting_place, Boolean isGift, String gift_quality,
                String username, Long post_time) {
        this.projName = projName;
        this.quality = quality;
        this.num_volun = num_volun;

        this.activity1 = activity1;
        this.activity2 = activity2;
        this.country = country;
        this.meeting_city = meeting_city;
        this.meeting_date = meeting_date;
        this.meeting_desc = meeting_desc;
        this.meeting_place = meeting_place;
        this.isGift = isGift;
        this.gift_quality = gift_quality;

        this.post_time = post_time;
        this.username = username;
    }
}
