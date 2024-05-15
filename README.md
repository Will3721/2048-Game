The classic 2048 game with added features such as undo, save, load, and reset
    - Model2048
        This is the model of the game. This class includes all the methods of the game and can be tested and run
        without any of the other classes.
    - GameBoard
        This class listens to keyboard presses and button presses. It is what allows the user to interact with the game.
        It also updates the score of the game and paints the tiles onto the board.
    - RunTwentyFortyEight
        This implements Runnable and creates the physical game board that the user can see as well as the buttons.
        It adds action listeners to the buttons so that the buttons actually do something when clicked on.
    - Model2048Test
        This class contains the JUnit tests for methods in Model2048.
    - Tile
        This class contains a value field and a color field and represents each individual tile of the game.
    - FileLineIterator
        Same class from TwitterBot. Helpful for reading from the txt file.
