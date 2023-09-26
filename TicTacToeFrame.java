import java.util.*;
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*;

/**
 * A class modelling a tic-tac-toe (noughts and crosses, Xs and Os) game in a
 * GUI window.
 * Xs starts first then Os.
 * Pressing a button disables it and the corresponding X or O is placed in its spot.
 * When a tic-tac-toe is accomplished the board disables itself from being used.
 * 
 * Additional Features:
 * -  A JPopUpMenu that pops up at the end of the game 
 *    to declare the winner
 * -  A 2 JOptionPanes which asks for and sets the name of 
 *    both players in the bottom status bar
 * -  A JLabel at the top of the tic-tac-toe board that keeps track of 
 *    the number of wins for each player and the number of ties.
 * 
 * @author Lynn Marshall
 * @version November 8, 2012
 * 
 * @author Muhammed Nabeel Azard
 * @version April 8, 2023
 */

public class TicTacToeFrame implements ActionListener
{ 
   public static void main(String[] args)
   {
       TicTacToeFrame game = new TicTacToeFrame();
   }
    
   /* The frame where the game is played on */
   private JFrame frame;
  
   /* The menu bar at the top of the game */
   private JMenuBar menubar;
   
   /* The new menu item in the list of menu options that restarts the game */
   private JMenuItem New;
   
   /* The quit menu item in the list of menu options that exits the game */
   private JMenuItem Quit;
   
   /* The grid where the buttons are placed */
   private JPanel grid;
   
   /* A 3x3 array of buttons */
   private JButton[][] buttons = new JButton[3][3];
   
   /* A 3x3 array of strings representing each button */
   private String[][] two_letters = new String[3][3];
   
   /* The number of turns it has been */
   private int turnNumber;
   
   /* The live status of the game */
   private JTextArea status;
   
   /* The status of the winners */
   private JTextArea winners;
   
   /* A popup that appears once the game has ended */
   private JPopupMenu popup;
   
   /* The text that appears on the popup */
   private JTextArea popText = new JTextArea();
    
   /* Option Pane to enter the name of both players */
   private JOptionPane pane1 = new JOptionPane();
   private JOptionPane pane2 = new JOptionPane();
   
   /* The name of both players */
   private String nameOfPlayer1;
   private String nameOfPlayer2;
   
   /* Keep track of number of wins */
   private int numPlayer1Wins;
   private int numPlayer2Wins;
   private int numTies;
   
   /** 
    * Constructs a new Tic-Tac-Toe board and sets up the basic
    * JFrame containing a JPanel of 9 buttons and a JMenu 
    */
   public TicTacToeFrame()
   { 
      // sets the number of turns to 0
      turnNumber = 0;
      
      // creates a window
      frame = new JFrame("Tic-Tac-Toe");
      
      // gets the container and sets its layout style
      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      
      // creates a grid to place buttons and sets its layout style
      grid = new JPanel();
      grid.setLayout(new GridLayout(3, 3));
      
      // loops through all the buttons, adds them to the grid and adds the actionListeners to them
      for(int i = 0; i < 3; i++) {
          for(int j = 0; j < 3; j++) {
              buttons[i][j] = new JButton();
              grid.add(buttons[i][j]);
              buttons[i][j].addActionListener(this);
          }
        }
      
     
      // adds grid onto contentPane
      contentPane.add(grid, BorderLayout.CENTER);
      
      // creates a menubar and adds it to the frame
      menubar = new JMenuBar();
      frame.setJMenuBar(menubar); 
      
      JMenu fileMenu = new JMenu("Game"); 
      menubar.add(fileMenu); 

      // creates 2 menu items and adds them to the menu
      New = new JMenuItem("New"); 
      fileMenu.add(New); 

      Quit = new JMenuItem("Quit"); 
      fileMenu.add(Quit); 
      
      // sets the shortcuts for the menu items
      final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // to save typing
      New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
      Quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
            
      // adds the menu items to the actionListeners
      New.addActionListener(this);
      Quit.addActionListener(this);
           
      // gets the name of both players from both content pane's
      nameOfPlayer1 = pane1.showInputDialog("Enter name of Player for Xs:");
      nameOfPlayer2 = pane2.showInputDialog("Enter name of Player for Os:");
      
      // defaults to Player 1 and Player 2 if name is not entered
      if(nameOfPlayer1==null || nameOfPlayer1.equals("")) {
          nameOfPlayer1 = "Player 1";
      }
      
      if(nameOfPlayer2==null || nameOfPlayer2.equals("")) {
          nameOfPlayer2 = "Player 2";
      }
      
      //creates the pop menu
      popup = new JPopupMenu();
      
      // get and sets the text for the bottom text bar
      status = new JTextArea(getText());
      contentPane.add(status, BorderLayout.SOUTH);
      
      // get and sets the text for the top text bar
      winners = new JTextArea(getWinners());
      contentPane.add(winners, BorderLayout.NORTH);
                    
      //sets the default settings
      frame.pack();
      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setResizable(false);
      frame.setSize(300,300);
      frame.setVisible(true);  
   }
   
   /** This action listener is called when the user clicks on 
    *  any of the GUI's buttons. It either responds to the menu 
    *  items or shortcuts or to the tic-tac-toe buttons 
    * 
    * @param e the action that has occured by the users input
    */
   public void actionPerformed(ActionEvent e)
   {
        Object o1 = e.getSource(); // gets the action 
         
        // checks if the action is from a button
        if (o1 instanceof JButton) {
             JButton button = (JButton)o1;
             
             // button is disabled if it has been pressed
             button.setEnabled(false);
             
             // checks which player's turn it is
             if(turnNumber%2 == 0) {
                 // sets button right player symbol
                 button.setText("X");              
                 turnNumber+=1;
                 
                 // checks winner found
                 if(this.haveWinner()) {
                     buttonsDisabled();
                     
                     // gets and sets the popup
                     popText.setText("");
                     popText.append("Congratulations " + nameOfPlayer1 + " has won!");
                     popup.insert(popText, 0);
                     popup.show(grid, 35, 110);             
                 }
             } else { // other players turn
                 // sets button right player symbol
                 button.setText("O");
                 turnNumber+=1;
                 
                 // checks winner found
                 if(this.haveWinner()) {
                     buttonsDisabled();
                     
                     // gets and sets the popup
                     popText.setText("");
                     popText.append("Congratulations " + nameOfPlayer2 + " has won!");
                     popup.insert(popText, 0);
                     popup.show(grid, 35, 110);
                 }
             }
            
             // sets the bottom status bar to show the correct live status
             status.setText("");
             status.append(getText());
        } else { // action has to be from a menu item
             JMenuItem item = (JMenuItem)o1;
              
             if (item == Quit) { // quits the game
                System.exit(0);
             } else if (item == New) { // starts new game
                this.restart();
             }
      }
   }
   
   /**
    * Returns true if filling the given square gives us a winner, and false
    * otherwise
    * 
    * @return true if we have a winner, false otherwise
    */
   private boolean haveWinner() 
   {
       // atleast 4 turns have been played
       if (turnNumber<4) {
           return false;
       }
       
       // each buttons symbol is stored in an array of two_letters       
       for(int i = 0; i < 3; i++) {
           for(int j = 0; j < 3; j++) {
               two_letters[i][j] = buttons[i][j].getText();
           }
       }
       
       // checks all possible ways of victory by checking
       // if the symbols form a win and the symbol is not set to 
       // just a blank symbol
       if(two_letters[0][0].equals(two_letters[1][0]) && two_letters[0][0].equals(two_letters[2][0]) 
            && !two_letters[0][0].equals("")) {
           return true;
       } else if(two_letters[0][1].equals(two_letters[1][1]) && two_letters[0][1].equals(two_letters[2][1])
                    && !two_letters[0][1].equals("")) {
           return true;
       } else if(two_letters[0][2].equals(two_letters[1][2]) && two_letters[0][2].equals(two_letters[2][2])
                    && !two_letters[0][2].equals("")) {
           return true;
       } else if(two_letters[0][0].equals(two_letters[0][1]) && two_letters[0][0].equals(two_letters[0][2])
                    && !two_letters[0][0].equals("")) {
           return true;
       } else if(two_letters[1][0].equals(two_letters[1][1]) && two_letters[1][0].equals(two_letters[1][2])
                    && !two_letters[1][0].equals("")) {
           return true;
       } else if(two_letters[2][0].equals(two_letters[2][1]) && two_letters[2][0].equals(two_letters[2][2])
                    && !two_letters[2][0].equals("")) {
           return true;
       } else if(two_letters[0][0].equals(two_letters[1][1]) && two_letters[0][0].equals(two_letters[2][2])
                    && !two_letters[0][0].equals("")) {
           return true;
       } else if(two_letters[0][1].equals(two_letters[1][1]) && two_letters[0][1].equals(two_letters[2][1])
                    && !two_letters[0][1].equals("")) {
           return true;
       } else if(two_letters[0][2].equals(two_letters[1][1]) && two_letters[0][2].equals(two_letters[2][0])
                    && !two_letters[0][2].equals("")) {
           return true;
       } 
        
       // no winner yet (game continues)
       return false;
   }
   
   /**
    * When a winner has been determined
    * all the buttons are disabled
    */
    private void buttonsDisabled() 
    {  
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }
    
   /**
    * Creates a new game by setting the number of turns to 0
    * and enabling all the buttons and setting them to
    * no symbol
    */
   private void restart() 
   {
       turnNumber = 0;
       
       for(int i = 0; i < 3; i++) {
          for(int j = 0; j < 3; j++) {
              buttons[i][j].setEnabled(true);
              buttons[i][j].setText("");
          }
        }
   }
   
   /**
    * Sets the text that will be written at the bottom of the game
    * to display the live status of the game 
    * 
    * @return A string that will be written at the bottom of every game
    */
   private String getText() 
   {
       String s1 = "";
       
       // checks if game as ended yet
       if(!haveWinner()) {
           s1 += "Game in progress ";
           
           // checks if its a tie
           if(turnNumber%2 == 0 && turnNumber < 9) {
               s1 += "and " + nameOfPlayer1 + "'s turn";
           } else if(turnNumber%2 != 0 && turnNumber < 9) {
               s1 += "and " + nameOfPlayer2 + "'s turn";
           } else {
               s1 = "Game over tie";
               
               // increment ties and update winners status bar
               numTies++;
               winners.setText("");
               winners.append(getWinners());
           }
       } else {
            s1 += "Game over ";
            
            if(turnNumber%2 == 0) {
               s1 += nameOfPlayer2 + " wins";
               
               // increment the winner and update winners status bar
               numPlayer2Wins++;
               winners.setText("");
               winners.append(getWinners());
            } else {
               s1 += nameOfPlayer1 + " wins";
               
               // increment the winner and update winners status bar
               numPlayer1Wins+=1;
               winners.setText("");
               winners.append(getWinners());
           }
       }
               
       return s1;
   }
   
   /**
    * Sets the text that will be displayed at the top of the game
    * to show which player has won the number of time 
    * 
    * @return A string that will be written at the top of the game
    */
   private String getWinners() 
   {
       String s = nameOfPlayer1 + ": " + numPlayer1Wins + "\t" + nameOfPlayer2 + ": " 
                  + numPlayer2Wins + "\t" + "Ties: " + numTies;
                  
       return s;
   }
}