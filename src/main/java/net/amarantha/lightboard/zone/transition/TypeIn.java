package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.entity.Pattern;

import java.util.HashMap;
import java.util.Map;

public class TypeIn extends AbstractTransition {

    private Map<Integer, Letter> letters;

    @Override
    public void reset() {
        Pattern pattern = zone.getPattern();
        letters = new HashMap<>();

        currentLetter = 0;

        letterCount = 0;

        boolean inLetter = false;
        int lastLetterStartX = 0;

        for ( int x=0; x<pattern.getWidth(); x++ ) {

            boolean isEmptyCol = true;

            for ( int y=0; y<pattern.getHeight(); y++ ) {
                if ( pattern.getBinaryPoint(y, x) ) {
                    isEmptyCol = false;
                    if ( !inLetter ) {
                        inLetter = true;
                        lastLetterStartX = x;
                    }
                    break;
                }
            }
            if ( isEmptyCol ) {
                inLetter = false;
                letters.put(letterCount, new Letter(pattern.subPattern(lastLetterStartX, 0, x-lastLetterStartX, pattern.getHeight()), lastLetterStartX, 0));
                letterCount++;
            }
        }

        if ( inLetter ) {
            letters.put(letterCount, new Letter(pattern.subPattern(lastLetterStartX, 0, pattern.getWidth()-lastLetterStartX, pattern.getHeight()), lastLetterStartX, 0));
            letterCount++;
        }

        delay = getDuration() / letterCount;
        lastDrawn = System.currentTimeMillis();

    }

    private long delay;
    private int currentLetter;
    private int letterCount;
    private long lastDrawn;

    @Override
    public void tick() {
        if ( System.currentTimeMillis() - lastDrawn >= delay ) {
            if ( currentLetter >= letterCount ) {
                zone.clear();
                complete();
            } else {
                zone.clear();
                for ( int i = 0; i <= currentLetter; i++ ) {
                    Letter l = letters.get(i);
                    zone.drawPattern(zone.getRestX()+l.x, zone.getRestY()+l.y, l.pattern);
                }
                lastDrawn = System.currentTimeMillis();
                currentLetter++;
            }
        }

    }

    private static class Letter {
        Pattern pattern;
        int x;
        int y;
        public Letter(Pattern pattern, int x, int y) {
            this.pattern = pattern;
            this.x = x;
            this.y = y;
        }
    }

}
