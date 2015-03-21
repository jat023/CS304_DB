package ca.ubc.cs.cs304.steemproject.base.development;

import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IGame;

public class GameInDevelopment implements IGame {
    
    private final String fName;
    private final String fDescription;
    private final Genre fGenre;
    private final String fDeveloper;
    private final String fVersion;

    public GameInDevelopment(String aName, String aDescription, Genre aGenre, String aDeveloper, String aVersion) {
        
        if (aName == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        }
        if (aDescription == null) {
            throw new IllegalArgumentException("Description cannot be null.");
        }
        if (aGenre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
        if (aDeveloper == null) {
            throw new IllegalArgumentException("Developer cannot be null.");
        }
        if (aVersion == null) {
            throw new IllegalArgumentException("Version cannot be null.");
        }
        
        this.fName = aName;
        this.fDescription = aDescription;
        this.fGenre = aGenre;
        this.fDeveloper = aDeveloper;
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
    public String getDeveloper() {
        return fDeveloper;
    }
    
    public String getVersion() {
        return fVersion;
    }

}
