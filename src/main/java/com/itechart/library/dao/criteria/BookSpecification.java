package com.itechart.library.dao.criteria;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BookSpecification {

    private String title;
    private String authors;
    private String genres;
    private String description;

    public BookSpecification convertSpecificationParametersToRegex() {
        String defaultValue = ".*%s.*";
        if (title == null) {
            title = "";
        }
        if (authors == null) {
            authors = "";
        }
        if (genres == null) {
            genres = "";
        }
        if (description == null) {
            description = "";
        }
        this.title = String.format(defaultValue, this.title);
        this.authors = String.format(defaultValue, String.join("|", this.authors.split(" *, *")));
        this.genres = String.format(defaultValue, String.join("|", this.genres.split(" *, *")));
        this.description = String.format(defaultValue, this.description);
        return this;
    }
}
