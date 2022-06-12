package com.example.newsfeed;

public class News {
    String publishedAt,source,title,description,imageUrl;

    public String getPublishedAt() {
        return publishedAt;
    }

    public News(String publishedAt, String source, String title, String description,String imageUrl) {
        this.publishedAt = publishedAt;
        this.source = source;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
