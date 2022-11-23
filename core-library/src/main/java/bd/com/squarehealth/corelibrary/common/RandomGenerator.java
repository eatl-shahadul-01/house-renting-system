package bd.com.squarehealth.corelibrary.common;

public final class RandomGenerator {

    public static int generateIntegerInRange(int minimum, int maximum) {
        return (int)Math.floor(Math.random() * (maximum - minimum + 1) + minimum);
    }
}
