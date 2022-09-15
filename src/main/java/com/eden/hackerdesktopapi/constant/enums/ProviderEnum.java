package com.eden.hackerdesktopapi.constant.enums;

import lombok.Getter;


@Getter
public enum ProviderEnum {
    HACKERWEEKLYNEWS("Hacker News", "Hacker Newsletter Archive Feed"),
    OVERFLOWWEEKLYNEWS("Overflow News", "Stack Overflow Blog"),
    INFOQWEEEKLYNEWS("InfoQ News", "InfoQ - News"),
    ACCIDENTALTECHPODCAST("Accidental Tech", "Accidental Tech Podcast");

    private final String name;

    private final String feedTitle;

    ProviderEnum(String name, String feedTitle) {
        this.name = name;
        this.feedTitle = feedTitle;
    }
}
