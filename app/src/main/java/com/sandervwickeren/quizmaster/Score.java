package com.sandervwickeren.quizmaster;

/***********************************************************************
 Score class that is used to combine name and score and send it to
 the firebase. Retrieved score information from Firebase 'll also be
 converted to an instance of this class.
 ***********************************************************************/
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