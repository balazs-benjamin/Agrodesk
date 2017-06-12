package org.aqins.agrodesk;


/**
 * Created by Completement on 07/11/2016.
 */

public class Blog {
    private String title;
    private String desc;
    private String image;
     private String username;
   private Long post_time;

    public Blog(){

    }
    public Blog(String title, String desc, String image, String username, Long post_time) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.post_time = post_time;
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPost_time() {
        return post_time;
    }
    public void setPost_time(Long post_time) {
        this.post_time = post_time;
    }

}
