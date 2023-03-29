package ui;

import model.Recipe;
import model.RecipeList;
import model.RecipeListFav;
import model.RecipeLists;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static java.util.Objects.isNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * Represents the recipe application's main window
 */
public class RecipeMainFrame extends JFrame implements ActionListener {
    private static final String JSON_STORE = "./data/RecipeApp.json";
    private RecipeList recipeList;
    private RecipeListFav recipeListFav;
    private Recipe recipeSelected;
    private HashMap<Recipe, Image> recipeImgMap = new HashMap<Recipe, Image>(); // to be implemented
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // JFrame components are constants
    private static final int WIDTH = 720;
    private static final int HEIGHT = 720;
    private JScrollPane listScrollPane;
    private JLabel header;
    private JInternalFrame recipeFrame = new JInternalFrame();
    private JTextArea textIngredients = new JTextArea();
    private JTextArea textSteps = new JTextArea();
    private JLabel labelRecipeName2 = new JLabel();
    private JLabel labelRecipeCat2 = new JLabel();
    private JList listRecipe;
    private DefaultListModel<Recipe> modelMain = new DefaultListModel<>();
    private DefaultListModel<Recipe> modelFav = new DefaultListModel<>();
    private JPanel buttonTopPane = new JPanel();
    private JPanel buttonBotPane = new JPanel();
    private JButton btnCreateRecipe;
    private JButton btnAddToFav;
    private JButton btnRemoveRecipe;
    private JButton btnRemoveFromFav;
    private JButton btnEditRecipe;
    private JButton btnSwitchList;
    private JMenuItem saveMenu;
    private JMenuItem loadMenu;

    // Set names of keys for recipe lists
    private final String keyRecipeLists = "Recipe Lists";
    private final String keyRecipeListMain = "Main Recipes";
    private final String keyRecipeListFav = "Favourite Recipes";

    // Default recipes. Maybe move to elsewhere, like its own class?
    protected Recipe recipe1 = new Recipe("Pasta Primavera", "Vegetarian", "Pasta, Veggies",
            "1. Boil pasta. 2. Add veggies. 3. Serve hot");
    protected Recipe recipe2 = new Recipe("Minestrone Soup", "Soup", "Veggies",
            "1. Add chopped veggies to boiling water. 2. When veggies are soft, serve");
    protected Recipe recipe3 = new Recipe("Vichyssoise", "Soup", "Leeks, Potatoes, Heavy Cream",
            "1. Fry chopped leeks and potatoes. Do not brown. 2. Add in broth to just fill chopped veggies. "
                    + "3. When veggies are tender, blend or mash, then add heavy cream");
    protected Recipe recipe4 = new Recipe("Fried Rice", "Rice", "Rice, Leftover Veggies or "
            + "Meats", "1. Add rice in a pan with some oil. Fry for a bit. 2. Add in leftovers. 3. Serve hot");
    protected Recipe recipe5 = new Recipe("Smoked Paprika Curry Sauce", "Sauce",
            "Butter, Onions, Paprika, Tomato Paste, Heavy Cream",
            "1. Combine chopped with melted butter in a pot. Fry onions until they're soft "
                    + "2.Add in water, let it boil and reduce to half. 3. Turn off heat and add in tomato paste."
                    + "4. Add in heavy cream and heat it up to temperature. Mix well.");

    /*
    // TO DO LIST:
    1. Make GUI able to do ALL RecipeApp methods.
    2. Format layout to be readable
    3. Have picture and recipe fields on the side. Maybe Hashmap for associating recipe with img
    4. Make it look nice
    * Initialize the recipe application
    */
    public RecipeMainFrame() {
        super("Recipe Application"); // Actual type JFrame
        init(); // Instantiate objects and JFrame components
        createRecipeAppFrame(); // the 'main' method assembling the recipe application
    }

    private void createRecipeAppFrame() {
        this.addMenu();
        this.mainButtonPanels();
        this.topButtonPanels();
        this.mainList();
        this.createRecipeFrame();

        this.add(buttonTopPane);
        this.add(buttonBotPane);
        this.add(listScrollPane);
        this.add(recipeFrame);

        JSplitPane splitTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonTopPane, listScrollPane);
        JSplitPane splitList = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitTop, buttonBotPane);
        JSplitPane splitAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitList, recipeFrame);
        // Combined all panels to splitAll
        splitTop.setDividerLocation(30);
        splitList.setDividerLocation(HEIGHT - 120);
        splitAll.setDividerLocation(WIDTH / 4);
        splitAll.setDividerSize(5);
        // FIX: minimum panel size constraints when resizing

        this.add(splitAll);
        this.addWindowListener(createWindowListener());
        this.setGraphics();
    }

    // Helper to set the recipe main frame
    private void setGraphics() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Add quit option
        this.pack();
        this.setResizable(true);
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes recipe list and favourite recipe list, and the JFrame components
    private void init() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        recipeList = new RecipeList(keyRecipeListMain);
        recipeListFav = new RecipeListFav(keyRecipeListFav);

        listRecipe = new JList(modelMain);
        listScrollPane = new JScrollPane(listRecipe);
        createListSelectionListener(); // creates list selection listener

        textIngredients.setEditable(false);
        textSteps.setEditable(false);

        loadButtonList();
        loadDefaultRecipeLists();
    }

    // EFFECTS: Loads default recipes in both list
    private void loadDefaultRecipeLists() {
        recipeList.addRecipe(recipe1); // Pasta Primavera
        recipeList.addRecipe(recipe2); // Minestrone Soup
        recipeList.addRecipe(recipe3); // Vichyssoise
        recipeList.addRecipe(recipe4); // Fried Rice
        recipeList.addRecipe(recipe5); // Smoked Paprika Curry Sauce
        recipeListFav.addRecipe(recipe3); // Vichyssoise
        recipeListFav.addRecipe(recipe4); // Fried Rice
    }

    // EFFECTS: loads the button list
    private void loadButtonList() {
        buttonTopPane.setLayout(new GridLayout(1,1));
        buttonBotPane.setLayout(new GridLayout(2,2));
        btnCreateRecipe = new JButton(new CreateRecipeAction());
        btnAddToFav = new JButton(new AddRecipeToFavAction());
        btnRemoveRecipe = new JButton(new RemoveRecipeFromMainAction());
        btnRemoveFromFav = new JButton(new RemoveFavRecipeAction());
        btnEditRecipe = new JButton(new EditRecipeAction());
        btnSwitchList = new JButton(new SwitchListAction(0));

        btnSwitchList.setBackground(new Color(229, 144, 220));
    }

    // EFFECTS: helper that creates the menu bar. Contains the save and load options
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();

        saveMenu = new JMenuItem("Save");
        saveMenu.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveMenu.addActionListener(this);
        menuBar.add(saveMenu);

        loadMenu = new JMenuItem("Load");
        loadMenu.setAccelerator(KeyStroke.getKeyStroke("control L"));
        loadMenu.addActionListener(this);
        menuBar.add(loadMenu);

        setJMenuBar(menuBar);
    }

    // EFFECTS: creates the buttons and button panel for the main recipe list
    private void mainButtonPanels() {
        buttonBotPane.removeAll();
        buttonBotPane.add(btnAddToFav);
        buttonBotPane.add(btnRemoveRecipe);
        buttonBotPane.add(btnEditRecipe);
        buttonBotPane.add(btnSwitchList);

        btnAddToFav.setMnemonic(KeyEvent.VK_F); // to set to CTRL+F to fav

        btnRemoveRecipe.setEnabled(false);
        buttonBotPane.updateUI();
    }

    // EFFECTS: creates the buttons and button panel for the favourite recipe list
    private void favButtonPanels() {
        buttonBotPane.removeAll();
        buttonBotPane.add(new JButton("-")); // an empty button, just to take up space
        buttonBotPane.add(btnRemoveFromFav);
        buttonBotPane.add(btnEditRecipe);
        buttonBotPane.add(btnSwitchList);

        btnRemoveFromFav.setEnabled(false);
        buttonBotPane.updateUI();
    }

    // EFFECTS: creates the top button panel. Does not change.
    public void topButtonPanels() {
        buttonTopPane.add(btnCreateRecipe);
        buttonTopPane.updateUI();
    }

    // MODIFIES: recipeFrame
    // EFFECTS: main method called to create and assemble all the frame displaying details of the selected recipe
    private void createRecipeFrame() {
        JPanel topPanels = new JPanel();
        JPanel rowPanels = new JPanel();

        createAllPanels(rowPanels, topPanels);
        recipeFrame.add(rowPanels);

        removeTitleInternalFrame(recipeFrame);
        recipeFrame.setVisible(true);
    }

    // EFFECTS: helper to remove the title bar of the JInternalFrame
    // Code from first answer from StackOverflow:
    // https://stackoverflow.com/questions/7218608/hiding-title-bar-of-jinternalframe-java
    private void removeTitleInternalFrame(JInternalFrame recipeFrame) {
        ((javax.swing.plaf.basic.BasicInternalFrameUI)recipeFrame.getUI()).setNorthPane(null);
    }

    // EFFECTS: creates all the panels.
    // - panelLeft contains recipe name and category
    // - panelRight contains the img of the recipe
    // - panelCenter contains the ingredients of the recipe
    // - panelBot contains the steps of the recipe
    private JPanel createAllPanels(JPanel outsidePanel, JPanel insidePanel) {
        JPanel panelLeft = new JPanel();
        JPanel panelImg = new JPanel();
        JPanel panelCenter = new JPanel();
        JPanel panelBot = new JPanel();
        JScrollPane ingScrollPane = new JScrollPane(textIngredients);
        JScrollPane stepsScrollPane = new JScrollPane(textSteps);

        insidePanel.setLayout(new GridLayout(1, 2));
        outsidePanel.setLayout(new GridLayout(3, 1));

        ingScrollPane.setPreferredSize(new Dimension(500,300));
        stepsScrollPane.setPreferredSize(new Dimension(500,400));
        textSteps.setLineWrap(true);
        textIngredients.setLineWrap(true);
        panelCenter.add(ingScrollPane);
        panelBot.add(stepsScrollPane);
        panelImg.setBackground(new Color(244, 0, 144)); // stub
        createPanelLeft(panelLeft);

        insidePanel.add(panelLeft);
        insidePanel.add(panelImg);
        outsidePanel.add(insidePanel);
        outsidePanel.add(panelCenter);
        outsidePanel.add(panelBot);

        setRecipeFrame();
        return outsidePanel;
    }

    // EFFECTS: creates the leftmost top panel, containing the name and category of the recipe
    private void createPanelLeft(JPanel panelLeft) {
        JLabel labelRecipeName1 = new JLabel("Name: ");
        JLabel labelRecipeCat1 = new JLabel("Category: ");
        panelLeft.add(labelRecipeName1);
        panelLeft.add(labelRecipeName2);
        panelLeft.add(labelRecipeCat1);
        panelLeft.add(labelRecipeCat2);
        panelLeft.setLayout(new GridLayout(2,2));

        labelRecipeName1.setHorizontalAlignment(SwingConstants.RIGHT);
        labelRecipeCat1.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    // EFFECTS: updates the recipe frame. Called by list listener to update on selection of a recipe
    private void setRecipeFrame() {
        if (!isNull(recipeSelected)) {
            labelRecipeName2.setText(recipeSelected.getRecipeName());
            labelRecipeCat2.setText(recipeSelected.getCategory());
            textIngredients.setText(recipeSelected.getIngredients());
            textSteps.setText(recipeSelected.getSteps());
        }
    }

    // EFFECTS: creates a JList displaying the main list of recipes
    private void mainList() {
        header = new JLabel("Main Recipe List", SwingConstants.CENTER);

        for (Recipe recipe : recipeList.getRecipeList()) {
            modelMain.addElement(recipe);
        }
        listRecipe.setModel(modelMain);

        listRecipe.setSelectionMode(SINGLE_SELECTION);
        listRecipe.setLayoutOrientation(JList.VERTICAL);
        listRecipe.setSelectedIndex(0);
        listRecipe.setVisibleRowCount(-1);
        listRecipe.revalidate();
        listRecipe.repaint();

        listScrollPane.setColumnHeaderView(header);
    }

    // EFFECTS: creates the JList displaying the favourite list of recipes
    private void favList() {
        header = new JLabel("Favourite Recipe List", SwingConstants.CENTER);

        for (Recipe recipe : recipeListFav.getRecipeList()) {
            modelFav.addElement(recipe);
        }
        listRecipe.setModel(modelFav);

        listRecipe.setSelectionMode(SINGLE_SELECTION);
        listRecipe.setLayoutOrientation(JList.VERTICAL);
        listRecipe.setSelectedIndex(0);
        listRecipe.setVisibleRowCount(-1);

        listScrollPane.setColumnHeaderView(header);
        listRecipe.revalidate();
        listRecipe.repaint();
    }

    // EFFECTS: saves the current state of the recipe app to JSON file
    private void saveRecipe() {
        RecipeLists recipeLists = new RecipeLists(keyRecipeLists, recipeList, recipeListFav);

        try {
            jsonWriter.open();
            jsonWriter.write(recipeLists);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,
                    "Saved main and favourite recipes to " + JSON_STORE,
                    "Save message", JOptionPane.PLAIN_MESSAGE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Unable to write to file: " + JSON_STORE,
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // EFFECTS: loads previously saved recipe configuration from file
    private void loadRecipe() {
        RecipeLists recipeLists;

        try {
            recipeLists = jsonReader.read(keyRecipeLists, keyRecipeListMain, keyRecipeListFav);
            recipeList = recipeLists.getRecipeList();
            recipeListFav = recipeLists.getRecipeListFav();

            JOptionPane.showMessageDialog(null,
                    "Loaded " + recipeList.getName() + " & " + recipeListFav.getName() + " from " + JSON_STORE,
                    "Load message", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Unable to read to file: " + JSON_STORE,
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    // EFFECTS: calls the appropriate method on menu clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == saveMenu) {
            saveRecipe();
        }
        if (o == loadMenu) {
            loadRecipe();
        }
    }

    // EFFECTS: creates the list selection listener, which assigns the selected recipe
    private void createListSelectionListener() {
        //Adds event listener to update with selection of recipe. Uses lambda expression
        listRecipe.getSelectionModel().addListSelectionListener(r -> {
            recipeSelected = (Recipe) listRecipe.getSelectedValue();
            buttonCheck();
            setRecipeFrame();
            if (r.getValueIsAdjusting()) {
                r.getValueIsAdjusting(); // find a way to not have listSelectionModel invoked twice
            }
        });
    }

    // EFFECTS: creates a window listener that prompts users when they try to exit the application
    // Adapted from Stackoverflow response:
    // https://stackoverflow.com/questions/15449022/show-prompt-before-closing-jframe
    private WindowAdapter createWindowListener() {
        return new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                super.windowOpened(e);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                String[] objButtons = new String[]{"Yes", "No"};
                int promptResult = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit? \nBe sure to save your recipe lists first!",
                        "Recipe Application",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        objButtons, objButtons[1]);
                if (promptResult == 0) {
                    System.exit(0);
                }
            }
        };
    }


    /**
     * List of buttons and actions:
     * From Main: Create new recipe (add to main), Add to fav, Remove from main, Edit recipe, switch lists
     * From Fav: Remove from fav, edit recipe, switch lists
     * 6 buttons total
     */

    /**
     * Represents action to be taken when user wants to add a new recipe to the main list
     * Creates a new recipe object and adds it into the main list
     */
    private class CreateRecipeAction extends AbstractAction {
        JTextField textName = new JTextField();
        JTextField textCat = new JTextField();
        JTextArea textIng = new JTextArea();
        JTextArea textSteps = new JTextArea();
        JScrollPane ingScrollPane = new JScrollPane(textIng);
        JScrollPane stepsScrollPane = new JScrollPane(textSteps);

        private CreateRecipeAction() {
            super("Create New Recipe");
        }

        // EFFECTS: creates a new optionPane and requests for user input to create a new Recipe object
        // then adds it to the main recipe list and the JList
        @Override
        public void actionPerformed(ActionEvent evt) {
            Object[] inputFields = setupInputFields();
            int option = JOptionPane.showConfirmDialog(null, inputFields, "Create New Recipe!",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == 0 && textName.getText() != null && !textName.getText().trim().isEmpty()) {
                Recipe newRecipe = new Recipe(textName.getText(), textCat.getText(),
                        textIng.getText(), textSteps.getText());
                recipeList.addRecipe(newRecipe);
                modelMain.addElement(newRecipe);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cancelled creating new recipe",
                        "", JOptionPane.PLAIN_MESSAGE);
            }
        }

        private Object[] setupInputFields() {
            Object[] inputFields =
                    {"Name", textName,
                            "Category", textCat,
                            "Ingredients", ingScrollPane,
                            "Steps:", stepsScrollPane};
            ingScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            ingScrollPane.setPreferredSize(new Dimension(400, 200));

            stepsScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            stepsScrollPane.setPreferredSize(new Dimension(400, 300));
            textIng.setLineWrap(true);
            textSteps.setLineWrap(true);
            return inputFields;
        }
    }

    /**
     * Represents action to be taken when user wants to add a recipe to the favourite list
     * from the main list
     */
    public class AddRecipeToFavAction extends AbstractAction {

        private AddRecipeToFavAction() {
            super("Favourite this recipe");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                recipeListFav.addRecipe(recipeSelected);
                JOptionPane.showMessageDialog(null,
                        "Added [" + recipeSelected.getRecipeName() + "] to the favourite recipe!",
                        "Favourite Success", JOptionPane.PLAIN_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                        "Can't add nothing to your favourites list!",
                        "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to remove a recipe from the
     * main list from the main list
     */
    public class RemoveRecipeFromMainAction extends AbstractAction {

        private RemoveRecipeFromMainAction() {
            super("Remove from main");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int index = listRecipe.getSelectedIndex();

            try {
                recipeList.removeRecipe(recipeSelected);
                recipeSelected = null;
                modelMain.removeElementAt(index);
                JOptionPane.showMessageDialog(null,
                        "Removed " + recipeSelected.getRecipeName() + " from the main list",
                        "Removed main recipe", JOptionPane.PLAIN_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                        "Can't remove nothing!",
                        "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Represents action to be taken when user wants to remove a recipe from the
     * fav list from the fav list
     */
    public class RemoveFavRecipeAction extends AbstractAction {

        private RemoveFavRecipeAction() {
            super("Remove fav");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int index = listRecipe.getSelectedIndex();

            try {
                recipeListFav.removeRecipe(recipeSelected);
                recipeSelected = null;
                modelFav.removeElementAt(index);
                JOptionPane.showMessageDialog(null,
                        "Removed " + recipeSelected.getRecipeName() + " from the favourite list",
                        "Removed favourite recipe", JOptionPane.PLAIN_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                        "Can't remove nothing!",
                        "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Represents action to be taken when user wants to edit a recipe
     * Can be done from either main or favourite list
     */
    public class EditRecipeAction extends AbstractAction {
        JTextField textName = new JTextField();
        JTextField textCat = new JTextField();
        JTextArea textIng = new JTextArea();
        JTextArea textSteps = new JTextArea();

        JScrollPane ingScrollPane = new JScrollPane(textIng);
        JScrollPane stepsScrollPane = new JScrollPane(textSteps);

        private EditRecipeAction() {
            super("Edit Recipe");
        }

        // EFFECTS: creates a new optionPane and requests for user input to create a new Recipe object
        // then adds it to the main recipe list and the JList
        @Override
        public void actionPerformed(ActionEvent evt) {
            Object[] inputFields = setupInputFields();
            int option = JOptionPane.showConfirmDialog(null, inputFields, "Editing Process",
                    JOptionPane.OK_CANCEL_OPTION);

            if (option == 0 && textName.getText() != null && !textName.getText().trim().isEmpty()) {
                recipeSelected.changeRecipeName(textName.getText());
                recipeSelected.changeCategory(textCat.getText());
                recipeSelected.changeIngredients(textIng.getText());
                recipeSelected.changeSteps(textSteps.getText());
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cancelled editing this recipe",
                        "", JOptionPane.PLAIN_MESSAGE);
            }
        }

        private Object[] setupInputFields() {
            initializeFields();
            Object[] inputFields =
                    {"Name", textName,
                     "Category", textCat,
                     "Ingredients", ingScrollPane,
                     "Steps:", stepsScrollPane};
            ingScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            ingScrollPane.setPreferredSize(new Dimension(400, 200));

            stepsScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            stepsScrollPane.setPreferredSize(new Dimension(400, 300));

            textIng.setLineWrap(true);
            textSteps.setLineWrap(true);
            return inputFields;
        }

        private void initializeFields() {
            textName.setText(recipeSelected.getRecipeName());
            textCat.setText(recipeSelected.getCategory());
            textIng.setText(recipeSelected.getIngredients());
            textSteps.setText(recipeSelected.getSteps());
        }
    }

    /**
     * Represents action to be taken when user wants to switch to between lists
     * Switches between button layouts of main and favourite's list
     */
    public class SwitchListAction extends AbstractAction {
        private int state; // 0 = Main, 1 = Favourite

        private SwitchListAction(int state) {
            super("Switch List");
            this.state = state;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (state == 0) {
                modelMain.removeAllElements();
                favList();
                favButtonPanels();
                this.state = 1;
            } else {
                modelFav.removeAllElements();
                mainList();
                mainButtonPanels();
                this.state = 0;
            }
        }
    }

    // EFFECTS: update remove buttons with state of the recipe list
    // e.g. if no more recipes in the JList, disable remove button
    private void buttonCheck() {
        if (listRecipe.getSelectionModel().isSelectionEmpty()) {
            btnRemoveRecipe.setEnabled(false);
            btnRemoveFromFav.setEnabled(false);
        } else {
            btnRemoveRecipe.setEnabled(true);
            btnRemoveFromFav.setEnabled(true);
        }
    }

    //
}