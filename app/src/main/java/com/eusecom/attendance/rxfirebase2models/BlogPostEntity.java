package com.eusecom.attendance.rxfirebase2models;

import java.util.HashMap;
import java.util.Map;

/**
 * The entity for blog posts
 */
public class BlogPostEntity {

  private String author;

  private String title;

  public BlogPostEntity() {
  }

  public BlogPostEntity(String author, String title) {
    this.author = author;
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("BlogPost{");
    sb.append("author='");
    sb.append(author).append('\'');
    sb.append(", title='");
    sb.append(title);
    sb.append('\'');
    sb.append('}');
    return sb.toString();
  }

  public Map<String, Object> toMap() {
    HashMap<String, Object> result = new HashMap<>();
    result.put("author", author);
    result.put("title", title);

    return result;
  }


}
