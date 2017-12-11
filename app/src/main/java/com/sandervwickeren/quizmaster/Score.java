package com.sandervwickeren.quizmaster;


public class Score {
    public String name;
    public Integer score;

    // Required for FB
    public Score() {}

    public Score(String name, Integer score) {
        this.name = name;
        this.score = score;
    }
}