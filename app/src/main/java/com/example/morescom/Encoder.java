package com.example.morescom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Encoder {
    public List<String> encode(String sentence) {
        ArrayList<String> encodedWords = new ArrayList<String>();
        for (String words : separateSentence(sentence)
        ) {
            encodedWords.add(encodeString(words));
        }
        return encodedWords;
    }

    private List<String> separateSentence(String sentence) {
        return new ArrayList<String>(Arrays.asList(sentence.split(" ")));
    }

    private String encodeString(String str) {
        String encoded = "";
        str = str.toUpperCase();

        for (int i = 0; i < str.length(); i++) {
            encoded += encodeWord(str.charAt(i)) + " ";
        }
        return encoded;
    }

    private String encodeWord(char c) {
        switch (c) {

            case 'A':
                return ".-"; // 101110
            case 'B':
                return "-..."; // 1110101010
            case 'C':
                return "-.-."; // 111010111010
            case 'D':
                return "-.."; // 11101010
            case 'E':
                return ".";  // 10
            case 'F':
                return "..-.";
            case 'G':
                return "--.";
            case 'H':
                return "....";
            case 'I':
                return "..";
            case 'J':
                return ".---";
            case 'K':
                return "-.-";
            case 'L':
                return ".-..";
            case 'M':
                return "--";
            case 'N':
                return "-.";
            case 'O':
                return "---";
            case 'P':
                return ".--.";
            case 'Q':
                return "--.-";
            case 'R':
                return ".-.";
            case 'S':
                return "...";
            case 'T':
                return "-";
            case 'U':
                return "..-";
            case 'V':
                return "...-";
            case 'W':
                return ".--";
            case 'X':
                return "-..-";
            case 'Y':
                return "-.--";
            case 'Z':
                return "--..";
            case '0':
                return "-----";
            case '1':
                return ".----";
            case '2':
                return "..---";
            case '3':
                return "...--";
            case '4':
                return "....-";
            case '5':
                return ".....";
            case '6':
                return "-....";
            case '7':
                return "--...";
            case '8':
                return "---..";
            case '9':
                return "----.";
            case '.':
                return "------";
            default:
                return "";
        }
    }

}
