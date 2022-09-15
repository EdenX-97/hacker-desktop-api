package com.eden.hackerdesktopapi.constant.enums;

import lombok.Getter;


@Getter
public enum ProviderEnum {
    HACKERWEEKLYNEWS("Hacker News", "Hacker Newsletter Archive Feed", "News"),
    OVERFLOWWEEKLYNEWS("Overflow News", "Stack Overflow Blog", "News"),
    INFOQWEEEKLYNEWS("InfoQ News", "InfoQ - News", "News"),
    ACCIDENTALTECHPODCAST("Accidental Tech", "Accidental Tech Podcast", "Podcasts");

    private final String name;

    private final String feedTitle;

    private final String type;

    ProviderEnum(String name, String feedTitle,  String type) {
        this.name = name;
        this.feedTitle = feedTitle;
        this.type = type;
    }
}
