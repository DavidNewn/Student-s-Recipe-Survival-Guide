package ui;

import model.Recipe;
import model.RecipeList;
import model.RecipeListFav;
import model.RecipeLists;
import org.junit.platform.commons.util.StringUtils;
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
    private JInternalFrame recipeFrame;
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
        this.addRecipeFrame();
        this.mainList();

        this.add(buttonTopPane);
        this.add(buttonBotPane);
        this.add(listScrollPane);
        this.add(recipeFrame);

        JSplitPane splitTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buttonTopPane, listScrollPane);
        JSplitPane splitList = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listScrollPane, buttonBotPane);
        JSplitPane splitAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitList, recipeFrame);
        splitTop.setDividerLocation(60);
        splitList.setDividerLocation(HEIGHT - 120);
        splitAll.setDividerLocation(WIDTH / 4);
        splitAll.setDividerSize(5);
        // Combined all panels to splitAll
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

        buttonBotPane.setLayout(new GridLayout(2,2));
        btnCreateRecipe = new JButton(new CreateRecipeAction());
        btnAddToFav = new JButton(new AddRecipeToFavAction());
        btnRemoveRecipe = new JButton(new RemoveRecipeFromMainAction());
        btnRemoveFromFav = new JButton(new RemoveFavRecipeAction());
        btnEditRecipe = new JButton(new EditRecipeAction());
        btnSwitchList = new JButton(new SwitchListAction(0));

        listRecipe = new JList(modelMain);
        listScrollPane = new JScrollPane(listRecipe);
        createListSelectionListener(); // creates list selection listener

        //Load default recipes
        recipeList.addRecipe(recipe1); // Pasta Primavera
        recipeList.addRecipe(recipe2); // Minestrone Soup
        recipeList.addRecipe(recipe3); // Vichyssoise
        recipeList.addRecipe(recipe4); // Fried Rice
        recipeList.addRecipe(recipe5); // Smoked Paprika Curry Sauce
        recipeListFav.addRecipe(recipe3); // Vichyssoise
        recipeListFav.addRecipe(recipe4); // Fried Rice
    }


    // EFFECTS: helper that creates the menu bar. Contains the save and load options
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();

        saveMenu = new JMenuItem("Save");
        saveMenu.setMnemonic(KeyEvent.VK_S);
        saveMenu.addActionListener(this);
        menuBar.add(saveMenu);

        loadMenu = new JMenuItem("Load");
        loadMenu.setMnemonic(KeyEvent.VK_L);
        loadMenu.addActionListener(this);
        menuBar.add(loadMenu);

        setJMenuBar(menuBar);
    }

    // EFFECTS: creates the buttons and button panel for the main recipe list
    public void mainButtonPanels() {
        btnAddToFav.setMnemonic(KeyEvent.VK_F); // to set to CTRL+F to fav
        buttonBotPane.removeAll();
        buttonBotPane.add(btnCreateRecipe);
        buttonBotPane.add(btnRemoveRecipe);
        buttonBotPane.add(btnEditRecipe);
        buttonBotPane.add(btnSwitchList);

        btnRemoveRecipe.setEnabled(false);
        buttonBotPane.updateUI();
    }

    // EFFECTS: creates the buttons and button panel for the favourite recipe list
    public void favButtonPanels() {
        buttonBotPane.removeAll();
        buttonBotPane.add(new JButton("-")); // an empty button, just to take up space
        buttonBotPane.add(btnRemoveFromFav);
        buttonBotPane.add(btnEditRecipe);
        buttonBotPane.add(btnSwitchList);

        btnRemoveFromFav.setEnabled(false);
        buttonBotPane.updateUI();
    }

    // MODIFIES: recipeFrame
    // EFFECTS: creates and assembles all the frame displaying details of the selected recipe
    private void addRecipeFrame() {
        recipeFrame = new JInternalFrame();
        JPanel outsidePanel = new JPanel();
        JPanel insidePanel = new JPanel();

        createAllPanels(outsidePanel, insidePanel);
        recipeFrame.add(outsidePanel);

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
    // panelLeft contains recipe name and category
    // panelRight contains the img of the recipe
    // panelCenter contains the ingredients of the recipe
    // panelBot contains the steps of the recipe
    private JPanel createAllPanels(JPanel outsidePanel, JPanel insidePanel) {
        JPanel panelLeft = new JPanel();
        JPanel panelRight = new JPanel();
        JPanel panelCenter = new JPanel();
        JPanel panelBot = new JPanel();

        insidePanel.setLayout(new GridLayout(1, 2));
        outsidePanel.setLayout(new GridLayout(3, 1));

        panelLeft.setBackground(new Color(244, 0,200));
        panelRight.setBackground(new Color(0, 224,200));
        panelCenter.setBackground(new Color(0,0,0));
        panelBot.setBackground(new Color(0, 0,200));

        insidePanel.add(panelLeft);
        insidePanel.add(panelRight);
        outsidePanel.add(insidePanel);
        outsidePanel.add(panelCenter);
        outsidePanel.add(panelBot);

        return outsidePanel;
    }

    // EFFECTS: helper to create the bottom panels of the recipe frame
    private JPanel recipeFramePanelBot() {
        JPanel panelBot = new JPanel();
        panelBot.setBounds(0, 300, 600, 600);
        panelBot.setBorder(BorderFactory.createLineBorder(new Color(100, 255, 100)));
        // somehow make it 2/3 of panel, the rest of the bottom

        JLabel ingredients = new JLabel("dfjioageaugaoi");
        JLabel steps = new JLabel(":adadadwadawda");

        panelBot.add(ingredients);
        panelBot.add(steps);

        return panelBot;
    }

    // EFFECTS: helper to create the img panel of the recipe frame
    private JPanel recipeFramePanelImg() {
        JPanel panelImg = new JPanel();
        ImageIcon image = null; // get hashmap img
        //panelImg.setBounds(WIDTH, 0, ); somehow make it 1/3 of panel, to the top right
        panelImg.setBackground(new Color(244, 0, 144)); // stub

        panelImg.setVisible(true);
        return panelImg;
    }

    // EFFECTS: helper to create the right panel of the recipe frame
    private JPanel recipeFramePanelLeft() {
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new GridLayout());
        panelLeft.setBorder(BorderFactory.createLineBorder(Color.CYAN));
        panelLeft.setBounds(0, 0, 300, 300);

        JLabel labelRecipeName1 = new JLabel("Name: ");
        JLabel labelRecipeName2 = new JLabel(recipeSelected.getRecipeName());
        labelRecipeName1.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel labelRecipeCat1 = new JLabel("Category: ");
        JLabel labelRecipeCat2 = new JLabel(recipeSelected.getCategory());



        panelLeft.add(labelRecipeName1);
        panelLeft.add(labelRecipeName2);
        panelLeft.add(labelRecipeCat1);
        panelLeft.add(labelRecipeCat2);


        System.out.println(panelLeft.getSize());
        System.out.println(labelRecipeCat1.getSize());
        return panelLeft;
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
        listRecipe.setSelectedIndex(-1);
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
        listRecipe.setSelectedIndex(-1);
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
        JTextField textIng = new JTextField();
        JTextField textSteps = new JTextField();

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
                            "Ingredients", textIng,
                            "Steps:", textSteps};
            textIng.setPreferredSize(new Dimension(300,200));
            textSteps.setPreferredSize(new Dimension(500,300));
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

        private EditRecipeAction() {
            super("Edit recipe");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                recipeSelected.changeRecipeName("LOL");
                JOptionPane.showMessageDialog(null,
                        "Changed recipe name",
                        "Edited recipe!", JOptionPane.PLAIN_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                        "Can't edit nothing!",
                        "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            }
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
