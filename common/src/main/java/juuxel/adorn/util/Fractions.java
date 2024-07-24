package juuxel.adorn.util;

public final class Fractions {
    private static final char[] SUPERSCRIPTS = { '\u2070', '\u00B9', '\u00B2', '\u00B3', '\u2074', '\u2075', '\u2076', '\u2077', '\u2078', '\u2079' };
    private static final char[] SUBSCRIPTS = { '\u2080', '\u2081', '\u2082', '\u2083', '\u2084', '\u2085', '\u2086', '\u2087', '\u2088', '\u2089' };
    private static final char FRACTION_BAR = '\u2044';

    public static String toString(long numerator, long denominator) {
        if (denominator == 1L) return Long.toString(numerator);
        var realNumerator = numerator % denominator;
        var whole = (numerator - realNumerator) / denominator;
        return toString(whole, realNumerator, denominator);
    }

    public static String toString(long whole, long numerator, long denominator) {
        var sb = new StringBuilder();
        if (sign(whole) * sign(numerator) * sign(denominator) == -1) {
            sb.append('-');
        }

        sb.append(Math.abs(whole));

        if (numerator != 0L) {
            sb.append(' ');

            for (int digit : digits(numerator)) {
                sb.append(SUPERSCRIPTS[digit]);
            }
            sb.append(FRACTION_BAR);
            for (int digit : digits(denominator)) {
                sb.append(SUBSCRIPTS[digit]);
            }
        }

        return sb.toString();
    }

    private static int sign(long l) {
        if (l > 0) {
            return 1;
        } else if (l < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    private static int[] digits(long l) {
        var str = Long.toString(Math.abs(l));
        var result = new int[str.length()];

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            assert '0' <= c && c <= '9' : "Unknown digit character: " + c;
            result[i] = c - '0';
        }

        return result;
    }
}
