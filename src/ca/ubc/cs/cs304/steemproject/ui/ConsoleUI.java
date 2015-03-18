package ca.ubc.cs.cs304.steemproject.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ConsoleUI implements IUI {

    @Override
    public void printError(String errorMessage) {
        System.out.println(errorMessage);
    }

    
}
