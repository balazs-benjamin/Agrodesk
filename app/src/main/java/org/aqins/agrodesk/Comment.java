package org.aqins.agrodesk;


import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Axelle on 06/03/2017.
 */
// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    private Object timestamp;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid,  String author, String text, Object timestamp) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
    }

    public Object getTimestamp() {
        return timestamp;
    }

}
// [END comment_class]

