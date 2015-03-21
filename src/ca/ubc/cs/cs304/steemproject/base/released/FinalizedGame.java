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
        return "PurchasableGame [fName=" + fName + ", fDescription="
                + fDescription + ", fGenre=" + fGenre + ", fPublisher="
                + fDeveloper + ", fRating=" + fRating + ", fPrice=" + fPrice
                + ", fOnSpecial=" + fOnSpecial + ", getSalePrice()="
                + getSalePrice() + "]";
    }


}
