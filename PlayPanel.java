import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class PlayPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	static final int panelWidth = 500; // Width of the game panel
	static final int panelHeight = 500; // Height of the game panel
	static final int partSize = 20; // Size of each part (segment) of the snake
	static final int partCount = (panelWidth * panelHeight) / (partSize * partSize); // Number of parts that can fit in
																						// the panel

	final int posX[] = new int[partCount]; // X coordinates of each part of the snake
	final int posY[] = new int[partCount]; // Y coordinates of each part of the snake

	int snakeLength = 5; // Initial length of the snake
	int score; // Player's score
	int foodPosX; // X coordinate of the food for the snake
	int foodPosY; // Y coordinate of the food for the snake
	char moveDirection = 'D'; // Current direction of movement for the snake
	boolean gameOn = false; // Flag to indicate if the game is currently running
	Random randGenerator; // Random number generator
	Timer gameTimer; // Timer for the game loop
	JButton replayButton; // Button for replaying the game

	PlayPanel() {
		randGenerator = new Random(); // Initialize the random number generator
		this.setPreferredSize(new Dimension(panelWidth, panelHeight)); // Set the dimensions of the panel
		this.setBackground(Color.gray); // Set the background color of the panel
		this.setFocusable(true); // Allow the panel to receive keyboard focus
		this.addKeyListener(new GameKeyAdapter()); // Add a key listener to handle keyboard input

		// Create and set up the replay button
		replayButton = new JButton("Play Again");
		replayButton.setBounds((panelWidth - 100) / 2, (panelHeight - 30) / 2 + 100, 100, 30);
		replayButton.setFocusable(false);
		replayButton.setVisible(false); // Initially hide the button
		replayButton.addActionListener(e -> {
			// Reset the game
			snakeLength = 5;
			score = 0;
			moveDirection = 'D';
			gameOn = false;
			startGame();
			requestFocusInWindow();
		});
		this.add(replayButton);
		replayButton.setFocusable(false);

	}

	public void startGame() {
		replayButton.setVisible(false); // Hide the replay button
		placeFood(); // Place the food for the snake
		gameOn = true; // Set the game flag to true

		posX[0] = (panelWidth / 2 / partSize) * partSize; // Set the initial X coordinate of the snake's head
		posY[0] = (panelHeight / 2 / partSize) * partSize; // Set the initial Y coordinate of the snake's head

		// Initialize the remaining parts of the snake with invalid coordinates
		for (int i = 1; i < snakeLength; i++) {
			posX[i] = -1;
			posY[i] = -1;
		}

		gameTimer = new Timer(80, this); // Create a timer for the game loop
		gameTimer.start(); // Start the game timer
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		drawElements(graphics); // Draw the game elements
	}

	public void makeMove() {
		for (int i = snakeLength; i > 0; i--) {
			posX[i] = posX[i - 1]; // Move each part to the position of the previous part
			posY[i] = posY[i - 1];
		}

		switch (moveDirection) {
			case 'L':
				posX[0] -= partSize; // Move the head left
				break;

			case 'R':
				posX[0] += partSize; // Move the head right
				break;

			case 'U':
				posY[0] -= partSize; // Move the head up
				break;

			case 'D':
				posY[0] += partSize; // Move the head down
				break;
		}
	}

	public void checkForFood() {
		if (posX[0] == foodPosX && posY[0] == foodPosY) { // Check if the snake's head is colliding with the food
			snakeLength++; // Increase the length of the snake
			score++; // Increase the player's score
			placeFood(); // Place new food
		}
	}

	public void drawElements(Graphics graphics) {
		if (gameOn) {
			graphics.setColor(new Color(210, 115, 90));
			graphics.fillOval(foodPosX, foodPosY, partSize, partSize); // Draw the food

			for (int i = 0; i < snakeLength; i++) {
				graphics.setColor(i == 0 ? Color.white : new Color(160, 32, 240));
				graphics.fillRect(posX[i], posY[i], partSize, partSize); // Draw each part of the snake
			}

			graphics.setColor(Color.white);
			graphics.setFont(new Font("Chalkboard", Font.PLAIN, 25));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Score: " + score, (panelWidth - metrics.stringWidth("Score: " + score)) / 2,
					graphics.getFont().getSize()); // Draw the score

		} else {
			endGame(graphics); // Call the endGame method
		}
	}

	public void placeFood() {
		foodPosX = randGenerator.nextInt((int) (panelWidth / partSize)) * partSize; // Generate random X coordinate for
																					// the food
		foodPosY = randGenerator.nextInt((int) (panelHeight / partSize)) * partSize; // Generate random Y coordinate for
																						// the food
	}

	public void collisionDetection() {
		for (int i = snakeLength; i > 0; i--) {
			if (posX[0] == posX[i] && posY[0] == posY[i]) { // Check if the snake's head is colliding with its body
				gameOn = false; // Set the game flag to false
			}
			if (posX[0] < 0 || posX[0] > panelWidth || posY[0] < 0 || posY[0] > panelHeight) { // Check if the snake's
																								// head is out of bounds
				gameOn = false; // Set the game flag to false
			}
		}

		if (!gameOn) {
			gameTimer.stop(); // Stop the game timer
		}
	}

	public void endGame(Graphics graphics) {
		replayButton.setVisible(true); // Show the replay button
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Chalkboard", Font.PLAIN, 50));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Game Over", (panelWidth - metrics.stringWidth("Game Over")) / 2, panelHeight / 2); // Draw
																												// "Game
																												// Over"
																												// message

		graphics.setColor(Color.white);
		graphics.setFont(new Font("Chalkboard", Font.PLAIN, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score: " + score, (panelWidth - metrics.stringWidth("Score: " + score)) / 2,
				graphics.getFont().getSize()); // Draw the final score

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (gameOn) {
			makeMove(); // Move the snake
			checkForFood(); // Check if the snake has eaten the food
			collisionDetection(); // Check for collisions
		}
		repaint(); // Repaint the panel
	}

	public class GameKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if (moveDirection != 'R') {
						moveDirection = 'L'; // Set the snake's direction to left
					}
					break;

				case KeyEvent.VK_RIGHT:
					if (moveDirection != 'L') {
						moveDirection = 'R'; // Set the snake's direction to right
					}
					break;

				case KeyEvent.VK_UP:
					if (moveDirection != 'D') {
						moveDirection = 'U'; // Set the snake's direction to up
					}
					break;

				case KeyEvent.VK_DOWN:
					if (moveDirection != 'U') {
						moveDirection = 'D'; // Set the snake's direction to down
					}
					break;
			}
		}
	}
}
