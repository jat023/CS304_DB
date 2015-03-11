package ca.ubc.cs.cs304.steemproject.game;

public class FinalizedGame implements IGame {
    
    private final String fName;
    private final String fDescription;
    private final String fGenre;
    private final String fPublisher;
    private final double fRating;
    private final double fPrice;

    public FinalizedGame(String aName, String aDescription, String aGenre, String aPublisher, double aRating, double aPrice) {
        fName = aName;
        fDescription = aDescription;
        fGenre = aGenre;
        fPublisher = aPublisher;
        fRating = aRating;
        fPrice = aPrice;
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
    
    public double getRating() {
        return fRating;
    }
    
    public double getPrice() {
        return fPrice;
    }

}
