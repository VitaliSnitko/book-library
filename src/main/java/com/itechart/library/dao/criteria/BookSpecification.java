package com.itechart.library.dao.criteria;

import lombok.Builder;
import lombok.Getter;

/**
 * Contains search parameters
 */
@Builder
@Getter
public class BookSpecification {

    public static final String DEFAULT_REGEX = ".*%s.*";
    public static final String REGEX_DELIMITER = "|";
    public static final String REGEX_SPLITTER = " *, *";

    private String title;
    private String authors;
    private String genres;
    private String description;
    private boolean onlyAvailable;

    /**
     * Convert specification parameters in regex form
     * that can be used for soft-searching with multiple parameters in one field.
     * E.g. typing "Andrew, Bob" in «Authors» field will return books that contains
     * in authors either Andrew or Bob. Matches all strings if parameters were not specified
     * @return formatted specification
     */
    public BookSpecification convertSpecificationParametersToRegex() {
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
        this.title = String.format(DEFAULT_REGEX, this.title);
        this.authors = String.format(DEFAULT_REGEX, String.join(REGEX_DELIMITER, this.authors.split(REGEX_SPLITTER)));
        this.genres = String.format(DEFAULT_REGEX, String.join(REGEX_DELIMITER, this.genres.split(REGEX_SPLITTER)));
        this.description = String.format(DEFAULT_REGEX, this.description);
        return this;
    }
}
