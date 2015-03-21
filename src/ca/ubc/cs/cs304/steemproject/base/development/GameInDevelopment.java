package ca.ubc.cs.cs304.steemproject.base.development;

import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IGame;

public class GameInDevelopment implements IGame {
    
    private final String fName;
    private final String fDescription;
    private final Genre fGenre;
    private final String fPublisher;
    private final String fVersion;

    public GameInDevelopment(String aName, String aDescription, Genre aGenre, String aPublisher, String aVersion) {
        this.fName = aName;
        this.fDescription = aDescription;
        this.fGenre = aGenre;
        this.fPublisher = aPublisher;
        this.fVersion = aVersion;
    }
    
    @Override
    public String getName() {
        return fName;
    }

    @Override
    public String getDescription() {
        return fDescription;
    }

    @Override
    public Genre getGenre() {
        return fGenre;
    }

    @Override
    public String getPublisher() {
        return fPublisher;
    }
    
    public String getVersion() {
        return fVersion;
    }

}
