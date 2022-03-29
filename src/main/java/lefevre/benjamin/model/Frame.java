package lefevre.benjamin.model;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public record Frame(Integer firstTry, Integer secondTry, Optional<Frame> nextElement) {

    public Frame {
        requireNonNull(nextElement);
    }

    public static final int FULL_PINS = 10;
    public static final int MAX_REGULAR_ROUNDS = 10;

    public int computeScore(int round) {
        if (round > MAX_REGULAR_ROUNDS) {
            return 0;
        }
        if (isSpare()) {
            return FULL_PINS
                    + nextElement.map(Frame::firstTry).orElseThrow();
        }
        if (isStrike()) {
            return FULL_PINS
                    + nextElement.map(Frame::firstTry).orElseThrow()
                    + nextElement
                    .map(Frame::secondTry)
                    .orElseGet(() -> nextElement.flatMap(Frame::nextElement).map(Frame::firstTry).orElseThrow());
        }
        return getKnockedPins();
    }

    public int getKnockedPins() {
        return firstTry + firstNonNull(secondTry, 0);
    }

    public boolean isSpare() {
        return getKnockedPins() == FULL_PINS && secondTry != null;
    }

    public boolean isStrike() {
        return secondTry == null;
    }

    public static Frame parseRawFrame(String frameString) {
        try {
            var firstTry = getValueOfFirstTry(frameString.charAt(0));
            var secondTry = frameString.length() > 1 ? getValueOfSecondTry(frameString.charAt(1), firstTry) : null;
            assert firstTry + firstNonNull(secondTry, 0) <= FULL_PINS;
            return new Frame(firstTry, secondTry, empty());
        } catch (Throwable t) {
            throw new IllegalArgumentException("Frame " + frameString + " is not a legal frame");
        }
    }

    private static int getValueOfFirstTry(char aChar) {
        return switch (aChar) {
            case '-' -> 0;
            case 'X' -> FULL_PINS;
            default -> Integer.parseInt(String.valueOf(aChar));
        };
    }

    private static int getValueOfSecondTry(char aChar, int firstTry) {
        if (aChar == '/') {
            return FULL_PINS - firstTry;
        }
        return getValueOfFirstTry(aChar);
    }
}
