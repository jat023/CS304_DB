package ca.ubc.cs.cs304.steemproject.access.game;

public class FinalizedGame implements IGame {

    private final String fName;
    private final String fDescription;
    private final String fGenre;
    private final String fPublisher;
    private final float fRating;
    private final float fPrice;
    private final boolean fOnSpecial;
    private final float fDiscount;

    public FinalizedGame(String aName, String aDescription, String aGenre, String aPublisher, float aRating, float aPrice, boolean aOnSpecial, float aDiscount) {
        fName = aName;
        fDescription = aDescription;
        fGenre = aGenre;
        fPublisher = aPublisher;
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
    public String getGenre() {
        return fGenre;
    }

    @Override
    public String getPublisher() {
        return fPublisher;
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
                + fPublisher + ", fRating=" + fRating + ", fPrice=" + fPrice
                + ", fOnSpecial=" + fOnSpecial + ", getSalePrice()="
                + getSalePrice() + "]";
    }


}
