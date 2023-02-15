# My Personal Project - Student's Survival and Inspiration Recipe Cookbook
## _A recipe manager application for students_

### Summary List:
- **What:** A recipe cookbook or library which users can refer to for recipes and ideas.
- **Who:** College/University Students, with an interest in cooking but are inexperienced or 
  have minimal experience in cooking, and who want to reduce food waste.
- **Why:** For the past three weeks since I've arrived in Vancouver, having only studied online beforehand, 
  I've taken a great interest in cooking. Making my own meals for home and for school really engages me, and I find 
  much enjoyment in assembling ingredients I have into fully-feldge food. 
  Though I've been having difficulties sometimes in deciding on what meal to make, as well as keeping track of 
  what ingredients I have or should get without breaking bank, so this project works to tackle these issues.

### User Stories
__As a college/university student, I would want to do things such as...__
 - viewing a list of recipes, with recipe steps and pictures included.
 - filter through a list of recipes by its category such as vegetarian, vegan, and so on 
 - adding, deleting, or editing my own recipes. Also, if I mess something up, I want to be able to revert my changes 
   or go back to default configuration.
 - saving my favourite recipes in a dedicated favourites list.
 - adding other things in a recipe which I might find useful like additional comments, ratings,
   or cost of ingredients and the meal itself (cost per ingredients and per serving for meal?).

### Recipe Criteria
1. Generally have less than 10 main ingredients (sauces, spices, condiments, garnish, and others will not count)
2. Have ingredients not be too expensive and relatively accessible to get (in this case, for students in Van, Canada)
3. Have as little steps as possible for the type of dish (as some dishes are naturally more complex)
4. Have the meal not take too long to prepare overall (though must consider prep overnight as well)

### Recipe Format
- Name of the recipe
- Category of the recipe. Examples include vegetarian, vegan, seafood, etc.
  - (Recipe could have multiple categories, e.g. vegetarian, soup)
- Ingredients needed for the recipe, with amounts for each
  - (Maybe convert measurements between metric and imperial)
- Steps to follow for the recipe
- Notes, for the user to give additional information about the recipe
- id, for tracking the recipe within the app

### Additions
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
- weights and amount of food items
  - reasonable units depending on the food item: ml or l for liquids, # of garlic cloves
  - then also allow users to edit theses to their liking. Conversion from metric to imperial units, etc.