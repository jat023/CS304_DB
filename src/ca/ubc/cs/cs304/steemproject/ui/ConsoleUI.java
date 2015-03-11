package ca.ubc.cs.cs304.steemproject.ui;

public class ConsoleUI implements IUI {

    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage);
    }

}
