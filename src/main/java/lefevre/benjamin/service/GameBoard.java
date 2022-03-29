package lefevre.benjamin.service;

import lefevre.benjamin.model.Frame;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;
import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;

public class GameBoard {

    private final List<Pair<Integer, Frame>> roundFrames = new ArrayList<>();

    public static GameBoard parseFrames(String framesString) {
        var rawFrames = stream(framesString.split("\\|"))
                .map(Frame::parseFrame)
                .collect(toList());
        reverse(rawFrames);
        var frames = new LinkedList<Frame>();
        rawFrames.forEach(frame -> {
            var nextFrame = frames.isEmpty() ? Optional.<Frame>empty() : Optional.of(frames.getFirst());
            frames.addFirst(new Frame(frame.firstTry(), frame.secondTry(), nextFrame));
        });
        var turnAndFrameList = IntStream.range(0, frames.size())
                .mapToObj(i -> Pair.of(i + 1, frames.get(i)))
                .toList();

        var gameBoard = new GameBoard();
        gameBoard.roundFrames.addAll(turnAndFrameList);
        return gameBoard;
    }

    public int computeScore() {
        return roundFrames.stream()
                .mapToInt(value -> value.getRight().computeScore(value.getLeft()))
                .sum();
    }
}
