package ca.ubc.cs.cs304.steemproject.base.released;

public class Playtime {
    
    private final Customer fCustomer;
    private final FinalizedGame fFinalizedGame;
    private final float fHours;

    public Playtime(Customer aCustomer, FinalizedGame aFinalizedGame, float aHours) {
        fCustomer = aCustomer;
        fFinalizedGame = aFinalizedGame;
        fHours = aHours;
    }
    
    public Customer getUser() {
        return fCustomer;
    }
    
    public FinalizedGame getGame() {
        return fFinalizedGame;
    }
    
    public float getHoursSpent() {
        return fHours;
    }

    @Override
    public String toString() {
        return "Playtime [fCustomer=" + fCustomer + ", fFinalizedGame="
                + fFinalizedGame + ", fHours=" + fHours + "]";
    }
}
