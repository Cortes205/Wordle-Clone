import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Wordle extends JFrame implements ActionListener, KeyListener, FocusListener
{
	// Word & Other Game Components
	private static File words = new File("wordleWords.txt");
	private static ArrayList<String> list = new ArrayList<String>();
	private Random roll = new Random();
	private boolean win = false;
	private boolean loss = false;
	private String word;
	
	// Menu Objects
	private JMenuBar menuBar;
	private JMenu game;
	private JMenuItem restart;
	private JMenuItem hide;
	
	// Background Components
	private JPanel background;
	private Color backColor = new Color(255, 226, 162);
	
	// Top Part Components
	private JPanel top;
	private JLabel text;
	private Font customFont;
	
	// Board Components
	private JPanel board;
	private int row = 0;
	private ArrayList<Box> boxes = new ArrayList<Box>();
	private static ArrayList<Character> incorrect = new ArrayList<Character>();
	
	// Bottom Components
	private JPanel bottom;
	private static JTextField textArea;
	private static String incorrectMsg = "Some incorrect letters have already been used!";
	private static String illegal = "ERROR: Illegal Input - Please Try Again";
	private static String restarted = "Game Restarted! New Word Chosen!";
	
	// Frame Components
	ImageIcon wordle = new ImageIcon("wordle.png");
	
	public Wordle() throws IOException, FontFormatException 
	{
		// Create first word of the game
		getWords();
		word = list.get(roll.nextInt(list.size())).toUpperCase();
//		System.out.println(word);
		
		
		// Create the text area
		textArea = new JTextField("Guess Input...");
		textArea.setBounds(0, 0, 540, 115);
		textArea.setHorizontalAlignment(JTextField.CENTER);
		textArea.setFont(new Font("Arial", Font.PLAIN, 64));
		textArea.setBorder(BorderFactory.createLineBorder(backColor, 1));
		textArea.setBackground(backColor);
		textArea.setForeground(Color.gray);
		textArea.addFocusListener(this);
		textArea.addKeyListener(this);
		
		// Bottom panel that holds the text area
		bottom = new JPanel(null);
		bottom.setOpaque(false);
		bottom.setBackground(Color.green);
		bottom.setBounds(0, 570, 540, 150);
		bottom.add(textArea);
		
		// Panels For Aligning bottom area
//		JPanel bottomTL = new JPanel();
//		bottomTL.setBackground(Color.red);
//		bottomTL.setBounds(0, 570, 270, 75);
//		JPanel bottomTR = new JPanel();
//		bottomTR.setBackground(Color.green);
//		bottomTR.setBounds(270, 570, 270, 75);
//		JPanel bottomBL = new JPanel();
//		bottomBL.setBackground(Color.green);
//		bottomBL.setBounds(0, 645, 270, 75);
//		JPanel bottomBR = new JPanel();
//		bottomBR.setBackground(Color.red);
//		bottomBR.setBounds(270, 645, 270, 75);
		
		// Panel that holds all the squares in a 6x5 grid with margin spacing
		board = new JPanel(new GridLayout(6, 5, 20, 15));
		board.setOpaque(false);
		board.setBackground(Color.blue);
		board.setBounds(15, 100, 500, 470);
		
		placeBoxes();
		
		// Title of the game
		customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Lobster.ttf")).deriveFont(48f);
		text = new JLabel("Wordle Made Better");
		text.setFont(customFont);
		
		// Top panel that holds game title
		top = new JPanel(new FlowLayout(0, 80, 15));
		top.setOpaque(false);
		top.setBackground(Color.green);
		top.setBounds(0, 0, 540, 100);
		top.add(text);
		
		// Panels For Aligning top area
//		JPanel topTL = new JPanel();
//		topTL.setBackground(Color.red);
//		topTL.setBounds(0, 0, 270, 50);
//		JPanel topTR = new JPanel();
//		topTR.setBackground(Color.green);
//		topTR.setBounds(270, 0, 270, 50);
//		JPanel topBL = new JPanel();
//		topBL.setBackground(Color.green);
//		topBL.setBounds(0, 50, 270, 50);
//		JPanel topBR = new JPanel();
//		topBR.setBackground(Color.red);
//		topBR.setBounds(270, 50, 270, 50);
		
		// Background panel that holds all panels and provides colour
		background = new JPanel(null);
		background.setBackground(backColor);
		background.add(top);
		background.add(board);
		background.add(bottom);
//		background.add(bottomTL);
//		background.add(bottomTR);
//		background.add(bottomBL);
//		background.add(bottomBR);
//		background.add(topTL);
//		background.add(topTR);
//		background.add(topBL);
//		background.add(topBR);
		
		// Hide menu bar
		hide = new JMenuItem("Hide            (ALT)");
		hide.addActionListener(this);
		
		// Restart game and get new word
		restart = new JMenuItem("Restart     (CTRL)");
		restart.addActionListener(this);
		
		// Game menu option
		game = new JMenu("Game");
		game.add(restart);
		game.add(hide);
		
		// The menu bar
		menuBar = new JMenuBar();
		menuBar.add(game);
		
		// Initiate the window
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFocusable(true);
		this.setTitle("Wordle Made Better");
		this.setPreferredSize(new Dimension(540, 720));
		this.setLayout(new GridLayout());
		this.add(background);
		this.setJMenuBar(menuBar);
		this.setIconImage(wordle.getImage());
		this.pack();
		this.addKeyListener(this);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void placeBoxes() // resets game if needed and places all the boxes onto the board panel
	{
		if (!boxes.isEmpty())
		{
			loss = false;
			win = false;
			incorrect.removeAll(incorrect);
			word = list.get(roll.nextInt(list.size())).toUpperCase();
//			System.out.println(word);
			customPlacehold(restarted, 20, Color.GREEN);
			for (int i = 0; i < 30; i++)
			{
				board.remove(boxes.get(i));
			}
			boxes.removeAll(boxes);
			row = 0;
		}
		
		for (int i = 0; i < 30; i++)
		{
			boxes.add(new Box());
			board.add(boxes.get(i));
		}
		this.repaint();
		this.revalidate();
	}
	
	public void print(String text) // prints text onto the next row of squares
	{
		for (int i = 0; i < 5; i++)
		{
			boxes.get(i+(row*5)).setText(text.toUpperCase().substring(i, i+1));
		}
		row++;
	}

	@Override
	public void actionPerformed(ActionEvent e) // listens for pressiong of menu buttons
	{		
		if (e.getSource() == restart)
		{
			placeBoxes();
		}
		else if (e.getSource() == hide)
		{
			menuBar.setVisible(false);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e)  // listens for key presses
	{
		if (e.getKeyCode() == KeyEvent.VK_ALT) // hide menu bar
		{
			if (menuBar.isVisible())
			{
				menuBar.setVisible(false);
			}
			else
			{
				menuBar.setVisible(true);
			}
		}
		else if (e.getKeyCode() == KeyEvent.VK_CONTROL) // restart game
		{
			placeBoxes();
		}
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) // enter guess
		{
			String guess = textArea.getText().toUpperCase().trim();
			boolean valid = true;
			
			for (int i = 0; i < guess.length(); i++) // validates that guess is consistent of valid letters
			{
				if (guess.charAt(i) < 65 || guess.charAt(i) > 90)
				{
					valid = false;
					break;
				}				
			}
			
			if (guess.length() == 5 && valid) // validates length and validity of guess
            {
				for (int i = 0; i < 5; i++)
				{
					if (incorrect.contains(guess.charAt(i))) // validates whether or not an incorrect letter has been used or not
					{
						customPlacehold(incorrectMsg, 17, Color.RED);
						return;
					}
				}
            	ArrayList<Character> correct = new ArrayList<Character>(); // temporary array of correct letters
            	for (int i = 0; i < 5; i++)
            	{
            		if (guess.equals(word)) // immediately returns a win if the guess = the word
            		{
            			boxes.get(i+(row*5)).setColor(Color.GREEN);
            			win = true;
            			continue;
            		}
            				
            		if (guess.charAt(i) == word.charAt(i)) // validates the letters in the correct indexes first
            		{
            			boxes.get(i+(row*5)).setColor(Color.GREEN);
            			correct.add(guess.charAt(i));
            		}
            		
            		if (i == 4)
            		{
            			for (int f = 0; f < 5; f++)
            			{
            				if (guess.charAt(f) != word.charAt(f)) // validates the other letters
                    		{
            					
            					/* validates if the word contains the letter at the given index, 
            					 * and whether or not its instances of it being correct 
            					 * is less than the instances of it in the actual word (this is done to 
            					 * avoid highlighting extra instances of a letter). For example: if this condition
            					 * did not exist and the word was "spoon", if you guessed "opoon", the first o would
            					 * be highlighted yellow and the other two would be green, which makes no sense. While
            					 * that example is highly unlikely to happen, there are examples that could happen and cause confusion */
                    			if (word.contains(guess.substring(f, f+1)) && instances(correct, guess.charAt(f)) < instances(word, guess.charAt(f))) 
                    			{
                    				boxes.get(f+(row*5)).setColor(Color.YELLOW);
                    				correct.add(guess.charAt(f));
                        		}
                    			else // if totally incorrect
                    			{
                    				boxes.get(f+(row*5)).setColor(Color.RED);
                    				/* validates whether or not the correct word actually 
                    				 * contains an instance of the letter. For example: if this condition
                    				 * didn't exist and the word was "straw", and you guessed "class", 
                    				 * the program would add the last 's' to the incorrect list and not let you use 
                    				 * 's' ever again, even though the word contains an 's' */
                    				if (instances(correct, guess.charAt(f)) < 1) 
                    				{
                    					incorrect.add(guess.charAt(f));
                    				}
                    			}
                    		}	
            			}
            		}	
            	}
            	print(guess);
            	if (row == 6 && !win) // returns a loss if all rows have been used and there is no win
    			{
    				loss = true;
    			}
            }
			else // displays message if you enter an insufficient/large amount of letters or illegal characters
			{
		        customPlacehold(illegal, 17, Color.RED);
				return;
			}
            
			textAreaPlacehold(); // "Guess Input..."
            
            if (win) // displays win message
            {
            	winLosePlaceHold("You Won! Press CTRL To Restart", 25, Color.GREEN);
            }
            
            if (loss) // displays lose message
            {
            	winLosePlaceHold("The word was " + word + "! Press CTRL To Restart", 20, Color.RED);
            }
		}
		else
		{
			if (checkText()) // starts displaying letters when you start typing
			{
				textArea.setFont(new Font("Arial", Font.PLAIN, 64));
				textArea.setForeground(Color.BLACK);
				String text = "";
				text += e.getKeyChar();
				textArea.setText(text);
			}
			textArea.requestFocus();
		}
	}
	
	public static void winLosePlaceHold(String msg, int size, Color color) // message for win or loss (not typeable until restart)
	{
		textArea.setFocusable(false);
		textArea.setForeground(color);
		textArea.setFont(new Font("Arial", Font.BOLD, size));
        textArea.setText(msg);
	}
	
	public static void customPlacehold(String msg, int size, Color color) // allows for custom message on text area
	{
		textArea.setFocusable(false);
		textArea.setForeground(color);
		textArea.setFont(new Font("Arial", Font.BOLD, size));
        textArea.setText(msg);
        textArea.setFocusable(true);
	}
	
	public static void textAreaPlacehold() // default text placeholder
	{
		textArea.setFont(new Font("Arial", Font.PLAIN, 64));
		textArea.setFocusable(false);
		textArea.setForeground(Color.GRAY);
        textArea.setText("Guess Input...");
        textArea.setFocusable(true);
	}
	
	public static int instances(ArrayList<?> input, char letter) // validates instances of a letter in an array list
	{
		int total = 0;
		for (int i = 0; i < input.size(); i++)
		{
			if (input.get(i).equals(letter))
			{
				total++;
			}
		}
		return total;
	}
	
	public static int instances(String input, char letter) // validates instances of a letter in a string
	{
		int total = 0;
		for (int i = 0; i < input.length(); i++)
		{
			if (input.charAt(i) == letter)
			{
				total++;
			}
		}
		return total;
	}

	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}
	
	public static void getWords() throws FileNotFoundException // retrieves list of words
	{
		Scanner fileReader = new Scanner(words);
		while (fileReader.hasNextLine())
		{
			list.add(fileReader.nextLine());
		}
		fileReader.close();
	}
	
	public static boolean checkText() // returns whether or not the text area is equal to certain place holders before removing them
	{
		return textArea.getText().equals("Guess Input...") || textArea.getText().equals(incorrectMsg) || textArea.getText().equals(illegal) || textArea.getText().equals(restarted);
	}

	@Override
	public void focusGained(FocusEvent e) // when you click on the text area it turns blank
	{
		if (e.getSource() == textArea)
		{
			if (checkText()) 
			{
	            textArea.setText("");
	            textArea.setFont(new Font("Arial", Font.PLAIN, 64));
	            textArea.setForeground(Color.BLACK);
	        }
		}
	}

	@Override
	public void focusLost(FocusEvent e) // when you click away from the text area it says "Guess Input..."
	{
		if (e.getSource() == textArea)
		{
			if (textArea.getText().isEmpty()) 
			{
	            textArea.setForeground(Color.GRAY);
	            textArea.setFont(new Font("Arial", Font.PLAIN, 64));
	            textArea.setText("Guess Input...");
	        }
		}
	}

}