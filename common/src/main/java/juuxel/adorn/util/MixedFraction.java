package juuxel.adorn.util;

public final class MixedFraction {
    private final long whole;
    private final long numerator;
    private final long denominator;

    private MixedFraction(long whole, long numerator, long denominator) {
        if (numerator < 0) {
            throw new IllegalArgumentException("Numerator must not be negative, was " + numerator);
        } else if (denominator <= 0) {
            throw new IllegalArgumentException("Denominator must not be 0 or negative, was " + numerator);
        } else if (denominator == 1 && numerator != 0) {
            throw new IllegalArgumentException("Denominator 1 is only allowed when numerator == 0, was " + numerator);
        }

        this.whole = whole;
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public MixedFraction resizeFraction(long newDenominator) {
        if (numerator == 0L) return this;
        return new MixedFraction(whole, numerator * newDenominator / denominator, newDenominator);
    }

    @Override
    public String toString() {
        return Fractions.toString(whole, numerator, denominator);
    }

    public static MixedFraction whole(long n) {
        return new MixedFraction(n, 0, 1);
    }

    public static MixedFraction of(long numerator, long denominator) {
        if (denominator == 1L) return new MixedFraction(numerator, 0, 1);
        var realNumerator = numerator % denominator;
        var whole = (numerator - realNumerator) / denominator;
        return new MixedFraction(whole, realNumerator, denominator);
    }
}
