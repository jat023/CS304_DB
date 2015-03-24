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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((fCustomer == null) ? 0 : fCustomer.hashCode());
        result = prime * result
                + ((fFinalizedGame == null) ? 0 : fFinalizedGame.hashCode());
        result = prime * result + Float.floatToIntBits(fHours);
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
        Playtime other = (Playtime) obj;
        if (fCustomer == null) {
            if (other.fCustomer != null)
                return false;
        } else if (!fCustomer.equals(other.fCustomer))
            return false;
        if (fFinalizedGame == null) {
            if (other.fFinalizedGame != null)
                return false;
        } else if (!fFinalizedGame.equals(other.fFinalizedGame))
            return false;
        if (Float.floatToIntBits(fHours) != Float.floatToIntBits(other.fHours))
            return false;
        return true;
    }
    
    
}
