package FunctionalProgramming;

import java.util.Arrays;

public class LetterCounter {

    static final String VOCALS = "aeiou";

    static String myText = "There also could occur errors during search or poison application Or the search could take a very long time because the ants hide very well";

    public static void main(String[] args) {
        System.out.println(Arrays.toString(myText.split("")));
        // TO DO - find 2 other ways to create a stream of chars from a string !!!!
        // ex myText.getChars();
        long vc = Arrays.stream(myText.split(""))
                .filter(l -> VOCALS.indexOf(l) >= 0)
                .count();
        System.out.println(vc);
        System.out.println(myText.length() - vc);
        System.out.println(Arrays.stream(myText.split(""))
                .filter(l -> VOCALS.indexOf(l) < 0)
                .count());


        //1st way to create a stream of chars from String
        long numOfVocals = myText.chars().
                filter(c -> VOCALS.indexOf(c) >= 0).
                count();

        System.out.println("numOfVocals: "+  numOfVocals );


        //2nd way to create a stream of chars from a String
        long numOfVocals2 = myText.codePoints().
                mapToObj(c -> (char) c).
                filter(c -> VOCALS.indexOf(c) >= 0).
                count();

        System.out.println("numOfVocals2: "+  numOfVocals2 );
    }
}
