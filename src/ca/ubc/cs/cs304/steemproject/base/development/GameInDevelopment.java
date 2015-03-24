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

    @Override
    public String toString() {
        return "GameInDevelopment [fName=" + fName + ", fDescription="
                + fDescription + ", fGenre=" + fGenre + ", fDeveloper="
                + fDeveloper + ", fVersion=" + fVersion + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fDescription == null) ? 0 : fDescription.hashCode());
        result = prime * result
                + ((fDeveloper == null) ? 0 : fDeveloper.hashCode());
        result = prime * result + ((fGenre == null) ? 0 : fGenre.hashCode());
        result = prime * result + ((fName == null) ? 0 : fName.hashCode());
        result = prime * result
                + ((fVersion == null) ? 0 : fVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameInDevelopment other = (GameInDevelopment) obj;
        if (fDescription == null) {
            if (other.fDescription != null)
                return false;
        } else if (!fDescription.equals(other.fDescription))
            return false;
        if (fDeveloper == null) {
            if (other.fDeveloper != null)
                return false;
        } else if (!fDeveloper.equals(other.fDeveloper))
            return false;
        if (fGenre != other.fGenre)
            return false;
        if (fName == null) {
            if (other.fName != null)
                return false;
        } else if (!fName.equals(other.fName))
            return false;
        if (fVersion == null) {
            if (other.fVersion != null)
                return false;
        } else if (!fVersion.equals(other.fVersion))
            return false;
        return true;
    }
    
    
}
