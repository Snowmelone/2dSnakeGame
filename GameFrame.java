
// Importing necessary swing and awt libraries
import javax.swing.*;
import java.awt.*;

// Creating a new public class that extends JFrame (i.e., a window in a desktop application)
public class GameFrame extends JFrame {

    // A unique ID needed for Serializable classes. This is not directly used in the
    // code.
    private static final long serialVersionUID = 1L;

    // Declaring a CardLayout to be used as the layout manager for the JPanel
    CardLayout cardLayout;

    // Declaring a JPanel, which is a container that can be added to a JFrame
    JPanel mainPanel;

    // Constructor for the GameFrame class
    GameFrame() {

        // Setting the title of the JFrame to "Snake"
        this.setTitle("Snake");

        // Ensuring the application stops running when the JFrame is closed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Preventing the JFrame from being resized
        this.setResizable(false);

        // Initializing cardLayout to be a new CardLayout
        cardLayout = new CardLayout();

        // Initializing mainPanel to be a new JPanel using the cardLayout
        mainPanel = new JPanel(cardLayout);

        // Creating an instance of the PlayPanel and TitlePanel classes
        PlayPanel gamePanel = new PlayPanel();
        TitlePanel titlePanel = new TitlePanel();

        // Adding the titlePanel and gamePanel to mainPanel with the names "Title" and
        // "Game" respectively
        mainPanel.add(titlePanel, "Title");
        mainPanel.add(gamePanel, "Game");

        // Adding the mainPanel to the JFrame
        this.add(mainPanel);

        // Packing all the components within the JFrame
        this.pack();

        // Making the JFrame visible
        this.setVisible(true);

        // Setting the location of the JFrame to the center of the screen
        this.setLocationRelativeTo(null);

        // Adding an ActionListener to the startButton
        // When the startButton is clicked, it will display the "Game" card and start
        // the game
        titlePanel.startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Game");
            gamePanel.startGame();
            gamePanel.requestFocusInWindow();
        });
    }

    // Creating a private class that extends JPanel
    private class TitlePanel extends JPanel {

        // Declaring a JButton to be used to start the game
        JButton startButton;

        // Constructor for the TitlePanel class
        TitlePanel() {

            // Setting the layout of the TitlePanel to be a GridBagLayout
            this.setLayout(new GridBagLayout());

            // Creating a GridBagConstraints object to specify constraints for components
            GridBagConstraints gbc = new GridBagConstraints();

            // Creating a JLabel with the text "Snake" and setting its font
            JLabel titleLabel = new JLabel("Snake");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 48));

            // Initializing startButton to be a new JButton with the text "Start Game"
            startButton = new JButton("Start Game");

            // Adding titleLabel to the TitlePanel at grid position (0,0)
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(titleLabel, gbc);

            // Adding startButton to the TitlePanel at grid position (0,1)
            gbc.gridy = 1;
            this.add(startButton, gbc);
        }
    }
}
