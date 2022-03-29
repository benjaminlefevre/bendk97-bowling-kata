package lefevre.benjamin;


import lefevre.benjamin.service.GameBoard;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class GameBoardTest {

    @ParameterizedTest(name = "frames: \"{0}\"")
    @CsvSource(value = {
            "9-|9-|9-|9-|9-|9-|9-|9-|9-|9-,90",
            "X|X|X|X|X|X|X|X|X|X|X|X,300",
            "5/|5/|5/|5/|5/|5/|5/|5/|5/|5/|5,150",
            "14|45|6/|5/|X|01|7/|6/|X|2/|6,133"
    })
    void compute_frames_score(String frames, int expectedScore) {
        var gameBoard = GameBoard.parseFrames(frames);

        assertThat(gameBoard.computeScore()).isEqualTo(expectedScore);
    }

    @ParameterizedTest(name = "frames: \"{0}\"")
    @CsvSource(value = {
            "9-|9-|9-|9-|9-|93|9-|9-|9-|9-",
            "X|X|X|X|X|X|X|XX|X|X|X|X",
            "5/|5/|5/|5/|5/|/5|5/|5/|5/|5/|5"
    })
    void illegal_frames_score_leads_to_exception(String frames) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> GameBoard.parseFrames(frames))
                .withMessageMatching("Frame .* is not a legal frame");

    }


}
