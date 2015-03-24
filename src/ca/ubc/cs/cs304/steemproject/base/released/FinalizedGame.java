package ca.ubc.cs.cs304.steemproject.base.released;

import ca.ubc.cs.cs304.steemproject.base.Genre;
import ca.ubc.cs.cs304.steemproject.base.IGame;

public class FinalizedGame implements IGame {

    private final String fName;
    private final String fDescription;
    private final Genre fGenre;
    private final String fDeveloper;
    private final float fRating;
    private final float fPrice;
    private final boolean fOnSpecial;
    private final float fDiscount;

    public FinalizedGame(String aName, String aDescription, Genre aGenre, String aDeveloper, float aRating, float aPrice, boolean aOnSpecial, float aDiscount) {
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
        
        fName = aName;
        fDescription = aDescription;
        fGenre = aGenre;
        fDeveloper = aDeveloper;
        fRating = aRating;
        fPrice = aPrice;
        fOnSpecial = aOnSpecial;
        fDiscount = aDiscount;
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

    public float getRating() {
        return fRating;
    }

    public float getFullPrice() {
        return fPrice;
    }

    public boolean isOnSpecial() {
        return fOnSpecial;
    }

    public float getDiscountPercentage() {
        return fDiscount;
    }

    public Float getSalePrice() {
        if (fOnSpecial) {
            return fPrice * (1 - fDiscount);
        } else {
            return fPrice;
        }
    }

    @Override
    public String toString() {
        return "FinalizedGame [fName=" + fName + ", fDescription="
                + fDescription + ", fGenre=" + fGenre + ", fDeveloper="
                + fDeveloper + ", fRating=" + fRating + ", fPrice=" + fPrice
                + ", fOnSpecial=" + fOnSpecial + ", fDiscount=" + fDiscount
                + ", getSalePrice()=" + getSalePrice() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fDescription == null) ? 0 : fDescription.hashCode());
        result = prime * result
                + ((fDeveloper == null) ? 0 : fDeveloper.hashCode());
        result = prime * result + Float.floatToIntBits(fDiscount);
        result = prime * result + ((fGenre == null) ? 0 : fGenre.hashCode());
        result = prime * result + ((fName == null) ? 0 : fName.hashCode());
        result = prime * result + (fOnSpecial ? 1231 : 1237);
        result = prime * result + Float.floatToIntBits(fPrice);
        result = prime * result + Float.floatToIntBits(fRating);
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
        FinalizedGame other = (FinalizedGame) obj;
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
        if (Float.floatToIntBits(fDiscount) != Float
                .floatToIntBits(other.fDiscount))
            return false;
        if (fGenre != other.fGenre)
            return false;
        if (fName == null) {
            if (other.fName != null)
                return false;
        } else if (!fName.equals(other.fName))
            return false;
        if (fOnSpecial != other.fOnSpecial)
            return false;
        if (Float.floatToIntBits(fPrice) != Float.floatToIntBits(other.fPrice))
            return false;
        if (Float.floatToIntBits(fRating) != Float
                .floatToIntBits(other.fRating))
            return false;
        return true;
    }
    
    
}
