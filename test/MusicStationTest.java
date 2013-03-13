import java.lang.Thread;

class MusicStationTest {
    public static void main(String[] args) {
        MusicStation ms = new MusicStation();
        if (ms.isReadyRun() ) {
            try {
                Thread.sleep(1000);
            } catch(Exception e) {
            }
            ms.playSound();
        }
    }
}
