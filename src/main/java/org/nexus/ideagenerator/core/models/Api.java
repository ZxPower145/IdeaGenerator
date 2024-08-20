package org.nexus.ideagenerator.core.models;

import jakarta.persistence.*;

@Entity
@Table
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_sequence")
    @SequenceGenerator(name = "expense_sequence", sequenceName = "expense_sequence", allocationSize = 1)
    private Long id;
    private String title;
    private String category;
    private String url;

    public Api() {}

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
