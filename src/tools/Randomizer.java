package tools;

import java.util.Random;

public class Randomizer {

    private final static Random rand = new Random();

    public static int nextInt() {
        return rand.nextInt();
    }

    public static short nextShort() {
        return (short) rand.nextInt();
    }

    public static int nextInt(final int arg0) {
        return rand.nextInt(arg0);
    }

    public static void nextBytes(final byte[] bytes) {
        rand.nextBytes(bytes);
    }

    public static boolean nextBoolean() {
        return rand.nextBoolean();
    }

    public static double nextDouble() {
        return rand.nextDouble();
    }

    public static float nextFloat() {
        return rand.nextFloat();
    }

    public static long nextLong() {
        return rand.nextLong();
    }

    public static int rand(final int lbound, final int ubound) {
        return (int) ((rand.nextDouble() * (ubound - lbound + 1)) + lbound);
    }

    public static int random(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static double randomDouble(double min, double max) {
        return ((max - min + 1)) + min;
    }

    public static int randomMinMax(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

    public static int MinMax(int value, int min, int max) {
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    public static double MinMaxDouble(double value, double min, double max) {
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    public static long MinMaxLong(long value, long min, long max) {
        if (value < min) {
            value = min;
        }
        if (value > max) {
            value = max;
        }
        return value;
    }

    public static int Min(int value, int min) {
        return value < min ? min : value;
    }

    public static long LongMin(long value, long min) {
        return value < min ? min : value;
    }

    public static long MaxShort(short value, short max) {
        return value > max ? max : value;
    }

    public static int Max(int value, int max) {
        return value > max ? max : value;
    }

    public static long MaxLong(long value, long max) {
        return value > max ? max : value;
    }

    public static double DoubleMax(double value, double max) {
        return value > max ? max : value;
    }

    public static double DoubleMin(double value, double min) {
        return value < min ? min : value;
    }
}
