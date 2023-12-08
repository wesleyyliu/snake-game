=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: wesliu
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Array:
  The 2D Array is used to hold the internal state of the game in the Field class. It stores GameObjects in each index
  of the 2D Array. Whenever objects are accessed in the array (through getObjectInField), the function checks that the
  array is accessed properly without any exceptions thrown. Only one 2D array is used, and the array is also
  encapsulated, since there are no getter methods returning the array, and the array is private. The 2D array is
  used because it models a 2D map of a snake game well. It makes accessing locations in the game very easy, and
  models the spatial aspect of the field well. I use a GameObject type in the array to apply inheritance well. The
  field contains many different objects of different types, so I have GameObject as the abstract class so I can
  store everything in one array, rather than having a 2D array of just the grass blocks, another just for the snake,
  etc. I can utilize the properties of abstract classes to call the same methods on each index in the array,
  even if they are of different child classes.

  2. Collections and/or Maps:
  I utilize HashSets to increase efficiency of operations, specifically when generating fruit and sinkhole
  blocks in the map. The HashSets store the list of all grass objects in the field, as well as all sinkhole
  objects in the field. This allows for easy and quick access for generation of fruits and sinkholes without
  having to iterate through each objects in the array and check what child class the object is. I iterate
  through the elements properly with for-each loops with no exceptions thrown, as well as using get methods such as
  in getSecondBlock. The information is not redundant because in order to randomly
  choose correct locations to generate fruits or sinkholes, I have to have access
  to some sort of storage of all the grass blocks. While they are in the 2D array, that would mean that every time
  I want to generate blocks, I have to iterate through the whole array and see which ones are grass blocks, and then
  randomly choose from them to generate sinkholes or fruits. This would be time-consuming and memory-consuming, when
  a HashSet that constantly stores and updates the set of sinkholes and grass blocks would be more efficient. I use
  a HashSet because the data is unordered (I just want to randomly choose grass blocks for generation). A HashSet
  is also better than an array because the data is constantly updating and changing as fruit and sinkhole blocks
  are generated or removed in the game. The HashSets are encapsulated properly, since they are private, and the
  only getter methods for them return a new HashSet with the elements copied in them.

  3. Inheritance and Subtyping:
  I use inheritance and subtyping to represent the blocks and moving objects. GameObject is an abstract class that
  represents the blocks, and MovingObject is an abstract class that represents moving objects like the snake and the
  enemy. I use abstract classes since the blocks and moving objects need to inherit functions like the getter methods
  for their locations in the field, a draw grass background (for all the fruits), and then methods like getHead,
  getNextBlock, etc., which are used by every single moving object subclass. Using abstract classes also makes it
  easier to implement the 2D array of the field, since the entries of the arrays must all be of the same type. If
  I did not use inheritance, I would have to have separate arrays for each object (grass, sinkholes, fruit, etc.),
  which would be a waste of memory. The differences between the subtypes also have distinct method implementations.
  Each of the GameObjects have different implementations for interactWith, such as a SnakeBlock killing the enemy
  when interacting with the enemy. The MovingObject also have vastly different implementations. For example, the
  enemy has functions that make it move randomly and go for fruits, while the normal snake does not have this
  implementation, since it moves based on user inputs. Finally, dynamic dispatch is also present, since the 2D
  array is made of GameObjects. When interactWith methods are called on objects in the 2D array, they use
  the overridden methods in each of the subclasses, such as Fruit, Sinkhole, etc.

  4. JUnit Testable Component:
  I use JUnit testing to test the components of my program. I have more than 5 tests, and the code is also
  designed to be unit testable, since I do not rely on GUI components and user inputs to test, beyond just
  instantiating fields to test the internal state of the game. I also have separation of concerns, since each
  test case tests a different small component of the game state. The test style is adequate, since I use correct
  assertions like AssertTrue and AssertEquals, and no two tests do the same thing. I also test edge cases. For
  example, I tested what happens when the snake runs into the head of the enemy snake (the game should end,
  rather than the enemy dying and the player continuing).

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

    - Direction: provides an enum class for the four different directions (UP, DOWN, LEFT, RIGHT), to be used
    to control the direction of the snake and enemy snake.
    - Field: controls the game state and stores all of the internal data, such as the score and the field data.
    It has a tick method to advance the game with each time interval, and moves all of the objects in the game
    according to their expected behavior. It also reacts to user inputs to the Swing components.
    - Grass: represents grass blocks in the game and its expected behavior. It stores the draw function to
    draw the right colored grass blocks in the correct locations, and the interactWith function does not do
    anything.
    - Fruit: represents fruit blocks in the game and its expected behavior. It draws the picture of an apple,
    and when interacting with the Fruit, it increases the score, generates a fruit, and also changes the speed
    if the game is in speed mode, as well as generates sinkholes if the game is in sinkhole mode.
    - SlowFruit: Same functionality as Fruit, but changes the tick speed of the game to make it slower when eaten.
    - FastFruit: Same functionality as Fruit, but changes the tick speed of the game to make it faster when eaten.
    - Sinkhole: represents sinkhole blocks in the game and its expected behavior. It draws circles as sinkholes
    in the game, and when the snake interacts with sinkholes, the game ends, while when the enemy interacts
    with sinkholes, the enemy dies.
    - SnakeBlock: represents snake blocks in the game and its expected behavior. It colors the blocks blue, and when
    the snake runs into it, the game ends, while when the enemy runs into it, the enemy dies.
    - EnemyBlock: represents enemy blocks in the game and its expected behavior. The blocks are red, and when
    the snake runs into it, the game ends, while when the enemy runs into it, the enemy dies.
    - Snake: stores the internal state of the Snake as a LinkedList. It controls the logic of how the snake moves
    and provides functions such as getting the head of the snake, getting the second object in the LinkedList, etc.
    - Enemy: stores the internal state of the Enemy as a LinkedList. It controls the logic of how the enemy moves
    and provides functions such as getting the head of the enemy, getting the second object in the LinkedList, etc.
    It also provides functions to determine the best moves and randomly choose one, with higher probabilities of
    moves that lead the enemy towards the fruit.
    - MovingObject: An abstract class for all moving objects in the game (snake, enemy). Contains functions that
    are implemented by all moving objects, such as getHead or getSecondBlock.
    - GameObject: An abstract class for all stationary objects in the game, where each subclass represents a block
    that would go in the 2D array in field. All subclasses will implement draw and interactWith, where interactWith
    is called when moving objects interact with those blocks in the 2D array.
    - RunSnake: the main class for the game that instantiates labels, buttons, panels, etc. that are used when
    the game is run.
    - Game: the class where the main method is actually run.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

One significant stumbling block was getting the internal state to operate correctly when a lot of
things were happening in each tick. For example, I had to make sure that the grasses HashSet was updated properly
when fruits, sinkholes, etc. were all being generated at the same time. Since a lot of different functions and
objects were interacting with the internal state at the same time, sometimes things were updated out of order
or caused nullPointerExceptions that I then had to debug.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I do not think the separation of functionality is great in my implementation. A lot of classes change the
internal states of other classes, so there is not great separation. The private state is mostly encapsulated
fine. While the actual GameObject objects are returned by the 2D array in the field, there are no setter methods
or anything that can actually change the internal state of the GameObject. Any HashSets or other internal fields
that are returned are also copies of the internal state, not the direct reference. If I had the chance to
refactor, I would limit how much each class is intertwined with each other. I would also probably get rid
of the LinkedList implementation of the snakes, and instead just store the head, tail, and second block within
the field itself, in order to avoid redundant uses of memory.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

Apple image: https://illustoon.com/?id=7141
Banana image: https://www.freepik.com/free-vector/banana-set-vector-flat-design-colorful_44691813.htm#query=banana%20
clipart&position=4&from_view=keyword&track=ais&uuid=f2d13e40-26e4-4271-a31f-b7fd88da8d9c
Pepper image: https://pearlyarts.com/product/red-chili-pepper-clipart/