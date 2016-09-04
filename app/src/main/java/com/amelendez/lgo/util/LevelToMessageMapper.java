package com.amelendez.lgo.util;

public class LevelToMessageMapper {

    public String GetMessage(float rating)
    {
        switch (Math.round(rating))
        {
            case 0:
                return "No rating has been given.";
            case 1:
                return "When I hear it, I recognize it.";
            case 2:
                return "It's hard to remember the meaning.";
            case 3:
                return "I know what it means, but not always.";
            case 4:
                return "I completely know this word, no doubt about it.";
        }
        return null;
    }
}
