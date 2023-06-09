package org.cubeville.cvplayerdata.playerdata;

import java.util.UUID;

public class Profiles {

    private long commentTime;
    private String comment;
    private UUID author;

    public Profiles(long commentTime, String comment, UUID author) {
        this.commentTime = commentTime;
        this.comment = comment;
        this.author = author;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public String getComment() {
        return comment;
    }

    public UUID getAuthor() {
        return author;
    }
}
