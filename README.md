# My Personal Project - Student's Survival and Inspiration Recipe Cookbook
## By David N
## _A recipe manager application for students_

***

## Instructions for Grader
- First required action related to adding Xs to a Y:
To create a new recipe, simply press the "Create New Recipe" button on the top left. A new window will popup where you
can fill in the details of your new recipe. Click "OK" to create the recipe adding the new recipe to the main list,
or click "Cancel" or leave the recipe name blank to cancel creating the new recipe.

To add an existing recipe to the favourite list, press the "Favourite this Recipe" button on the bottom button panel.
This will add the recipe into the favourite list. 
Press the magenta "Switch List" button to switch between the favourite and main list.

- Second required action related to adding Xs to a Y:
An existing recipe in the list can be edited. Click on the recipe in the list and press "Edit Recipe" on the
bottom button panels and a new window will pop-up. Type in whatever you wish and click "OK" to apply new changes to
the recipe. If you click "Cancel" or leave the recipe name null, the edit process will cancel.

- Remove action:
To remove a recipe from the favourite list, first switch to the favourite list, then click on the recipe on the list
and press "Remove fav" on the bottom button panel. This will remove the recipe from the favourite list.
To remove a recipe from the main list, first switch to the main list, then click "Remove from main" on
the bottom button panel. This will remove the recipe from the main list. 

Though do note I have not properly implemented my intended remove feature where removing a recipe from the main list 
will also remove it from the favourite list if it's present. However, removing recipes still works.

- Visual component:
Visual components are the images of the recipe, located on the top right panel. 
If the recipe does not have an image associated with it, Tobs will be assigned to it.

Unfortunately for now there is no adding image option.

- Saving the state of application:
To save your current recipe config, press the save option in the menu bar or hit Ctrl+S.

- Reloading the state of application:
To load your previously saved recipe config, press the load option in the menu bar or hit Ctrl+L. 

Note, there is currently a bug in loading as I have not fully implemented saving recipe images for the application, 
where the default recipes will no longer display their associated images from the map but instead Tobs. 
Regardless, the program should still keep running.


### Phase 4: Task 3
If I had more time, I would:

- Implement an appropriate design pattern such as the Composite pattern to improve interacting with adding, removing, 
and editing recipes in the recipe lists, as well as to improve cohesion and reduce coupling.
- Or perhaps also implement the Iterator pattern to iterate through my recipe lists, abstracting code relating to 
interacting with a recipe in a list, as well as improving cohesion and reducing coupling
- Refactor my recipe class so that the ingredients and steps fields better handle longer text, as recipes often have. 
One option might include declaring them as a collection of string such as an arraylist.
- Refactor my RecipeMainFrame class (the GUI app); maybe implement the Observer pattern, having panels update 
based on changes received from a subject.
- Remove the association of RecipeLists with the RecipeApp (console-based recipe app) to be like my GUI where it only
creates the RecipeLists object when saving and loading from file. No reason for it to be a field, and changing it to
be declared locally 'hides' it from outside the saving and loading methods.

Other than refactoring, I would like to implement my intended feature where deleting a recipe from the main list 
removes that recipe from the favourite list if it's present. 

Furthermore, I would like to properly implement an image adding and saving feature where a user can add their own images
from a file chooser, and then have that chosen image saved to that recipe via a map. To do so I will need to also
save the map into a JSON file, which required time that I currently do not have. So for now there's a bug in the app
where upon loading the previous app state the images for the default recipes will no longer properly show.


***

## Information:

### Summary List:
- **What:** A recipe cookbook or library which users can refer to for recipes and ideas.
- **Who:** College/University students, with an interest in cooking but are inexperienced or 
  have minimal experience in cooking, and who want to reduce food waste.
- **Why:** For the past three weeks since I've arrived in Vancouver, having only studied online beforehand, 
  I've taken a great interest in cooking. Making my own meals for home and for school really engages me, and I find 
  much enjoyment in assembling ingredients I have into fully-feldge food. 
  Though I've been having difficulties sometimes in deciding on what meal to make, as well as keeping track of 
  what ingredients I have or should get without breaking bank, so this project works to tackle these issues.

### User stories
__As a college/university student, I would want to do things such as...__
 - viewing a list of recipes, with recipe steps and pictures included.
 - filter through a list of recipes by its category such as vegetarian, vegan, and so on 
 - adding, deleting, or editing my own recipes. Also, if I mess something up, I want to be able to revert my changes 
   or go back to default configuration.
 - saving my favourite recipes in a dedicated favourites list.
 - adding other things in a recipe which I might find useful like additional comments, ratings,
   or cost of ingredients and the meal itself (cost per ingredients and per serving for meal).

 - being able to save my recipe lists to file (if I so choose), and load it when I relaunch the program
 - when quiting the application, I want to be reminded to save my current recipe list and have the option not to do so

### Ideal recipe criteria
As students generally have little time to spare from their studies, a recipe is recommended to follow these criteria.
Although, a student may always want to add more complex recipes, to challenge themselves for example, so the recipe app
will not penalize students for not following these criteria exactly.

1. Generally have less than 10 main ingredients (sauces, spices, condiments, garnish, and others do not count)
2. Have ingredients that are relatively accessible to get and not too expensive
3. Have as little steps as possible for the type of dish (as some dishes are naturally more complex)
4. Have the meal not take too long to prepare overall (though must consider other things such as prepping overnight etc.)

### Recipe format
- Name of the recipe; no duplicate names
- Category of the recipe. Examples include vegetarian, vegan, seafood, etc.
  - (A recipe could have multiple categories, e.g. vegetarian, soup)
- Ingredients needed for the recipe, with amounts for each
- Steps to follow for the recipe
- Notes, for the user to give additional information about the recipe

### Extras for further improvement
- make recipe book considering for many types of students: 
  - those money-conscious, those time-crunched, those who like to experiment, etc.
- have separate user accounts which have their own recipe lists
- have a meal plan schedule planner where users can plan ahead for the week, month, or anytime they so wish
- factor in time (prep+cook) and total time, considering the time-crunch student who can't invest much time in cooking
- track individual ingredients in possession
  - then perhaps also track date of purchase, best before dates, and remind me of expiration date of ingredients
  - warn the user when their ingredients for their favourite recipes are running low
- allow greater user creativity in formatting their recipes
  - allow users to edit font colours, font sizes, font styles, etc.
- consider weights and amount of food items
  - reasonable units depending on the food item: ml or l for liquids, # of garlic cloves
  - then also allow users to edit theses to their liking. Conversion from metric to imperial units, etc.