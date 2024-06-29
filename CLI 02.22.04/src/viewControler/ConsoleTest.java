package viewControler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleTest {

    private CLI console;

    @BeforeEach
    void setUp() {
        console = new CLI();
        console.initialize();
    }

    @Test
    void initialize() {
        assertNotNull(console.getAudioManager(), "AudioManager sollte initialisiert sein");
        assertEquals(0, console.getAudioManager().getMediaList().size(), "Es sollten 0 Audiodateien initialisiert sein");
    }

    @Test
    void showMenu() {
        assertDoesNotThrow(() -> console.showMenu(), "showMenu sollte keine Ausnahmen werfen");
    }


}