package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import static java.util.Objects.isNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

// TODO:
//  1. To complete proper image implementation. Bugs with loading due to lack of it (recipe app still works though).
//  2. Add and edit text labels
//  3. Make overall program run faster. Better abstraction. Utilize design patterns.
//  4. Make GUI look pretty
/**
 * The GUI version of the recipe application
 */
public class RecipeMainFrame extends JFrame implements ActionListener {
    private static final String JSON_STORE = "./data/RecipeApp.json";
    private RecipeList recipeList;
    private RecipeListFav recipeListFav;
    private Recipe recipeSelected;
    private HashMap<Recipe, Image> recipeImgMap = new HashMap<>();
    //private BufferedImage recipeImg; // for implementing image adding feature
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private EventLog eventlog = EventLog.getInstance();

    // JSwing Components and constants
    private static final int WIDTH = 900;
    private static final int HEIGHT = 720;
    private JScrollPane listScrollPane;
    private JLabel header;
    private JInternalFrame recipeFrame = new JInternalFrame();
    private JTextArea textIngredients = new JTextArea();
    private JTextArea textSteps = new JTextArea();
    private JLabel labelRecipeName2 = new JLabel();
    private JLabel labelRecipeCat2 = new JLabel();
    private JLabel labelRecipeImage = new JLabel();
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
    private JButton btnAddImg;
    private JMenuItem saveMenu;
    private JMenuItem loadMenu;

    // Set names of keys for recipe lists for saving and loading
    private final String keyRecipeLists = "Recipe Lists";
    private final String keyRecipeListMain = "Main Recipes";
    private final String keyRecipeListFav = "Favourite Recipes";

    // Default Image (tobs.jpg)
    private BufferedImage defaultTobs; // it's Tobs

    // Default recipes. Maybe move to elsewhere, like its own class?
    protected Recipe recipe1 = new Recipe("Pasta Alla Norcina", "Pasta",
            "Pasta, Fatty Meat, Garlic, Cream",
            "1. Boil pasta. 2. Fry meats with chopped garlic. Add in heavy cream and let sauce simmer."
                    + "3. Add pasta into sauce. Serve hot");
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

    // EFFECTS: runs the recipe application
    public RecipeMainFrame() {
        super("Recipe Application"); // Actual type JFrame
        init(); // Instantiate objects and JFrame components
        createRecipeAppFrame(); // the 'main' method assembling the recipe application
    }

    // MODIFIES: this
    // EFFECTS: assembles all the panels for the default configuration of the application
    // TODO: fix the panel sizes to look better
    private void createRecipeAppFrame() {
        this.addMenu();
        this.mainButtonPanels();
        this.topButtonPanels();
        this.mainList(); // will need to be called before createRecipeFrane for now (for recipeSelected != null)
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

        this.add(splitAll);
        this.addWindowListener(createWindowListener());
        this.setGraphics();
    }

    // EFFECTS: Helper to set JFrame options for the recipe main frame
    private void setGraphics() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Add quit option
        this.pack();
        this.setResizable(true);
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes recipe list, favourite recipe list, and the JFrame components
    private void init() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        recipeList = new RecipeList(keyRecipeListMain);
        recipeListFav = new RecipeListFav(keyRecipeListFav);

        listRecipe = new JList(modelMain);
        listScrollPane = new JScrollPane(listRecipe);
        createListSelectionListener();

        textIngredients.setEditable(false);
        textSteps.setEditable(false);

        loadButtonList();
        loadDefaultRecipeLists();
        loadDefaultRecipeImageMap();
    }

    // REQUIRES: default images for default recipes exist in the data folder, else project crashes
    //           will have it so Tobs is the default no-image case
    // MODIFIES: this
    // EFFECTS: setups default map of recipe images with their recipes
    private void loadDefaultRecipeImageMap() {
        try {
            defaultTobs = ImageIO.read(new File("./data/tobs.jpg"));
            BufferedImage imgDefaultPastaAllaNorcina = ImageIO.read(new File("./data/pasta_alla_norcina.jpg"));
            BufferedImage imgDefaultMinestroneSoup = ImageIO.read(new File("./data/minestrone_soup.jpg"));
            BufferedImage imgDefaultVichyssoise = ImageIO.read(new File("./data/alfredo.jpg"));
            BufferedImage imgDefaultFriedRice = ImageIO.read(new File("./data/fried_rice.jpg"));

            recipeImgMap.put(recipe1, imgDefaultPastaAllaNorcina);
            recipeImgMap.put(recipe2, imgDefaultMinestroneSoup);
            recipeImgMap.put(recipe3, imgDefaultVichyssoise);
            recipeImgMap.put(recipe4, imgDefaultFriedRice);
            recipeImgMap.put(recipe5, defaultTobs);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "No image?",
                    "No image?", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // MODIFIES: this
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

    // MODIFIES: this
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
        btnAddImg = new JButton(new AddImageBtn());

        btnSwitchList.setBackground(new Color(229, 144, 220));
    }

    // MODIFIES: this
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

    // MODIFIES: this
    // EFFECTS: updates the buttons of the bottom panel for the main recipe list
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

    // MODIFIES: this
    // EFFECTS: updates the buttons of the bottom panel for the favourite recipe list
    private void favButtonPanels() {
        buttonBotPane.removeAll();
        buttonBotPane.add(new JButton("-")); // an empty button, just to take up space
        buttonBotPane.add(btnRemoveFromFav);
        buttonBotPane.add(btnEditRecipe);
        buttonBotPane.add(btnSwitchList);

        btnRemoveFromFav.setEnabled(false);
        buttonBotPane.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: adds the buttons to the top panel. Does not change.
    public void topButtonPanels() {
        buttonTopPane.add(btnCreateRecipe);
        // TODO: add a search bar in later updates
        buttonTopPane.updateUI();
    }

    // MODIFIES: this
    // EFFECTS: main method to create all the panels for the detailed recipe frame
    //          displays all the details of the selected recipe
    private void createRecipeFrame() {
        JPanel topPanels = new JPanel();
        JPanel rowPanels = new JPanel();

        createRecipeFramePanels(rowPanels, topPanels);
        recipeFrame.add(rowPanels);

        removeTitleInternalFrame(recipeFrame);
        recipeFrame.setVisible(true);
    }

    // EFFECTS: helper to remove the title bar of the detailed recipe frame
    // Code from first answer from StackOverflow:
    // https://stackoverflow.com/questions/7218608/hiding-title-bar-of-jinternalframe-java
    private void removeTitleInternalFrame(JInternalFrame recipeFrame) {
        ((javax.swing.plaf.basic.BasicInternalFrameUI)recipeFrame.getUI()).setNorthPane(null);
    }

    // EFFECTS: creates the panels for detailed recipe frame.
    // - panelLeft contains recipe name and category
    // - panelRight contains the img of the recipe
    // - panelCenter contains the ingredients of the recipe
    // - panelBot contains the steps of the recipe
    private JPanel createRecipeFramePanels(JPanel outsidePanel, JPanel insidePanel) {
        JPanel panelLeft = new JPanel();
        JPanel panelImg = new JPanel();
        JPanel panelCenter = new JPanel();
        JPanel panelBot = new JPanel();

        insidePanel.setLayout(new GridLayout(1, 2));
        outsidePanel.setLayout(new GridLayout(3, 1));

        // Creates the panels (4 total)
        createPanelLeft(panelLeft);
        createPanelImg(panelImg);
        panelCenter.add(createIngScrollPane());
        panelBot.add(createStepsScrollPane());

        // Adds all the panels into one, sets the values in them, and returns the assembled panels
        insidePanel.add(panelLeft);
        insidePanel.add(panelImg);
        outsidePanel.add(insidePanel);
        outsidePanel.add(panelCenter);
        outsidePanel.add(panelBot);
        setRecipeFrame();
        return outsidePanel;
    }

    // EFFECTS: creates the scroll pane containing the steps
    private JScrollPane createStepsScrollPane() {
        JScrollPane stepsScrollPane = new JScrollPane(textSteps);

        stepsScrollPane.setPreferredSize(new Dimension(500,300));
        textSteps.setLineWrap(true);
        return stepsScrollPane;
    }

    // EFFECTS: creates the scroll pane containing the ingredients
    private JScrollPane createIngScrollPane() {
        JScrollPane ingScrollPane = new JScrollPane(textIngredients);

        ingScrollPane.setPreferredSize(new Dimension(500,200));
        textIngredients.setLineWrap(true);
        return ingScrollPane;
    }

    // EFFECTS: creates the image panel
    private void createPanelImg(JPanel panelImg) {
        panelImg.add(labelRecipeImage);
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

    // MODIFIES: this
    // EFFECTS: sets the recipe frame. Called by list listener on selection of a recipe in the list
    private void setRecipeFrame() {
        // !!! maybe not have isNull for recipeSelected??
        // !!! possibly have an empty frame when no recipe is selected...
        if (!isNull(recipeSelected)) {
            labelRecipeName2.setText(recipeSelected.getRecipeName());
            labelRecipeCat2.setText(recipeSelected.getCategory());
            textIngredients.setText(recipeSelected.getIngredients());
            textSteps.setText(recipeSelected.getSteps());
            labelRecipeImage.setIcon(findRecipeImage(recipeSelected));
        }
    }

    // EFFECTS: returns the ImageIcon of the selected recipe if it exists, else returns Tobs
    // !!! BUG: after saving and loading, recipes in the map will not be in sync with that from the recipe lists
    //          i.e. different hashcodes. Likely an issue when loading, not overriding equals and hashcode
    //          Regardless, the catch statement will be called.
    private ImageIcon findRecipeImage(Recipe recipeSelected) {
        Image getImage;
        try {
            getImage = recipeImgMap.get(recipeSelected).getScaledInstance(400,250, Image.SCALE_DEFAULT);
            return new ImageIcon(getImage);
        } catch (NullPointerException e) {
            getImage = defaultTobs.getScaledInstance(400,250, Image.SCALE_DEFAULT);
            return new ImageIcon(getImage);
        }
    }

    // !!! stub for proper image setting function, paired with saving hashmap to json
    // EFFECTS: sets recipes not already in the recipeImgMap to have the defaultTobs image
    //          this would apply for all new recipes created by the user
    // BUG: because of loading bug, recipes will not be in sync with list after loading
    private void setRecipeImageToTobs(Recipe recipeSelected) {
        if (!recipeImgMap.containsKey(recipeSelected)) {
            recipeImgMap.put(recipeSelected,defaultTobs);
        }
    }

    // EFFECTS: removes the selected recipe from the img map
    // BUG: because of loading bug, recipes within the map will not be removed after loading
    private void removeRecipeFromMap(Recipe recipeSelected) {
        recipeImgMap.remove(recipeSelected);
    }

    // MODIFIES: this
    // EFFECTS: sets the JList displaying the main list of recipes
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

    // MODIFIES: this
    // EFFECTS: sets the JList displaying the favourite list of recipes
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

    /*
    // EFFECTS: assigns image with the recipe in the map
    private void updateRecipeImageMap() {
        // !!! sTUB TO check img. Currently gives dialog of unremovable tobs!
        if (!isNull(recipeSelected)) {
            JLabel imgLabel = new JLabel(new ImageIcon(defaultTobs));
            JDialog dialog = new JDialog();
            dialog.setUndecorated(true);
            dialog.add(imgLabel);
            dialog.pack();
            dialog.setVisible(true);
        }
    }
    */

    // EFFECTS: saves the current state of the recipe app to JSON file
    private void saveRecipe() {
        RecipeLists recipeLists = new RecipeLists(keyRecipeLists, recipeList, recipeListFav);

        try {
            jsonWriter.open();
            jsonWriter.write(recipeLists);
            // TODO: write hashmap to new json file (to not break command line)
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

    // !!! Bug: Loading edited file, then swapping between favourites and main recipe list does not correctly
    //          load main recipe list
    // EFFECTS: loads previously saved recipe configuration from file
    private void loadRecipe() {
        RecipeLists recipeLists;
        try {
            recipeLists = jsonReader.read(keyRecipeLists, keyRecipeListMain, keyRecipeListFav);
            // !!! load separate json file representing images
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

    // MODIFIES: this
    // EFFECTS: creates the list selection listener, which assigns the selected recipe
    private void createListSelectionListener() {
        //Adds event listener to update with selection of recipe. Uses lambda expression
        listRecipe.getSelectionModel().addListSelectionListener(r -> {
            recipeSelected = (Recipe) listRecipe.getSelectedValue();
            buttonCheck(); // updates buttons
            setRecipeFrame(); // updates the right frame detailing the selected recipe
            // if (r.getValueIsAdjusting()) {
                // !!! find a way to have listSelectionModel invoked only once
                // i.e when recipe is clicked on the list, listener does not return both true and false booleans
            // }
        });
    }

    // EFFECTS: creates a window listener that prompts users when they try to exit the recipe application
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
                    getRecipeLog();
                    eventlog.clear();
                    System.exit(0); // fully exit app
                }
            }
        };
    }

    // EFFECTS:
    private void getRecipeLog() {
        Iterator recipeLogIterator = eventlog.iterator();
        while (recipeLogIterator.hasNext()) {
            System.out.println(recipeLogIterator.next());
        }
    }

    /*
    // !!! stub for now
    // EFFECTS: creates the file chooser for selecting valid images formats
    private JFileChooser createFileImageChooser() {
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new FileNameExtensionFilter(
                "Image Files", "jpg", "png", "tif"));
        return fc;
    }
     */

    /**
     * List of buttons and actions :
     * From Main: Create new recipe (add to main), Add to fav, Remove from main, Edit recipe, Switch lists
     * From Fav: Remove from fav, edit recipe, switch lists
     * From Create and Edit Menu: Add Image (not fully implemented)
     * 7 buttons total
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

        // MODIFIES: this
        // EFFECTS: creates an optionPane and requests for user input to create a new Recipe object
        //          then adds it to the main recipe list and updates the JList upon clicking OK
        //          else displays cancel popup
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
                setRecipeImageToTobs(newRecipe);
                JOptionPane.showMessageDialog(null,
                        "Added [" + newRecipe.getRecipeName() + "] to the main list!",
                        "", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Cancelled creating new recipe",
                        "", JOptionPane.PLAIN_MESSAGE);
            }
        }

        // EFFECTS: initializes the input fields for the option pane
        private Object[] setupInputFields() {
            Object[] inputFields =
                    {"Add Image: ", btnAddImg,
                            "Name: ", textName,
                            "Category: ", textCat,
                            "Ingredients: ", ingScrollPane,
                            "Steps: ", stepsScrollPane};
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

        // MODIFIES: this
        // EFFECTS: adds selected recipe to the favourite recipe list,
        //          else displays error popup
        // BUG: displays error popup anyways cause of list listener invoking twice
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
     * Represents action to be taken when user wants to remove a recipe in the main list
     */
    public class RemoveRecipeFromMainAction extends AbstractAction {

        private RemoveRecipeFromMainAction() {
            super("Remove from main");
        }

        // MODIFIES: this
        // EFFECTS: removes selected recipe from the main list
        //          else displays error popup
        // BUG: displays error popup anyways cause of list listener invoking twice
        //      as a result, also have to call removeRecipeFromMap before removeRecipe() for now.
        @Override
        public void actionPerformed(ActionEvent evt) {
            int index = listRecipe.getSelectedIndex();

            try {
                removeRecipeFromMap(recipeSelected);
                recipeList.removeRecipe(recipeSelected);
                modelMain.removeElementAt(index);
                JOptionPane.showMessageDialog(null,
                        "Removed " + recipeSelected.getRecipeName() + " from the main list",
                        "Removed main recipe", JOptionPane.PLAIN_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                        "Can't remove nothing!",
                        "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            } finally {
                recipeSelected = null;
            }
        }
    }


    /**
     * Represents action to be taken when user wants to remove a recipe from the fav list
     */
    public class RemoveFavRecipeAction extends AbstractAction {

        private RemoveFavRecipeAction() {
            super("Remove fav");
        }

        // MODIFIES: this
        // EFFECTS: removes selected recipe from the favourite recipe list
        //          else displays error popup
        // BUG: displays error popup anyways cause of list listener invoking twice
        @Override
        public void actionPerformed(ActionEvent evt) {
            int index = listRecipe.getSelectedIndex();

            try {
                recipeListFav.removeRecipe(recipeSelected);
                modelFav.removeElementAt(index);
                JOptionPane.showMessageDialog(null,
                        "Removed " + recipeSelected.getRecipeName() + " from the favourite list",
                        "Removed favourite recipe", JOptionPane.PLAIN_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                        "Can't remove nothing!",
                        "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            } finally {
                recipeSelected = null;
            }
        }
    }

    /**
     * Represents action to be taken when user wants to edit a recipe
     * Accessible from main and favourite list
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

        // MODIFIES: this
        // EFFECTS: creates a optionPane and requests for user input to create edit the selected recipe
        //          then updates it in the JLIST
        // BUG: displays error popup anyways cause of list listener invoking twice
        // BUG: update from edit is not immediate; must switch between list before update registers
        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                Object[] inputFields = setupInputFields();
                int option = JOptionPane.showConfirmDialog(null, inputFields, "Editing Process",
                        JOptionPane.OK_CANCEL_OPTION);

                if (option == 0 && textName.getText() != null && !textName.getText().trim().isEmpty()) {
                    recipeSelected.changeRecipeName(textName.getText());
                    recipeSelected.changeCategory(textCat.getText());
                    recipeSelected.changeIngredients(textIng.getText());
                    recipeSelected.changeSteps(textSteps.getText());
                    JOptionPane.showMessageDialog(null,
                            "Recipe edited!",
                            "Edit Success", JOptionPane.PLAIN_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Cancelled editing this recipe",
                            "", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null,
                            "Can't edit nothing!",
                            "No Recipe Selected", JOptionPane.ERROR_MESSAGE);
            }
        }

        // EFFECTS: initializes the input fields for the option pane
        private Object[] setupInputFields() {
            initializeFields();
            Object[] inputFields =
                    {"Add image:", btnAddImg,
                     "Name:", textName,
                     "Category:", textCat,
                     "Ingredients:", ingScrollPane,
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

        // Helper for initializing input fields above to have the current recipe field values
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

        // EFFECTS: creates button action with a binary state
        private SwitchListAction(int state) {
            super("Switch List");
            this.state = state;
        }

        // !!! BUG: loading and switching between favourite and main list breaks switching options
        // MODIFIES: this
        // EFFECTS: switches between the favourite and main list on button click
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

    /**
     * Represents action to be taken when user wants to add an image file to the recipe in either
     * the edit or create recipe dialogue boxes
     * !!! In development
     */
    public class AddImageBtn extends AbstractAction {
        //File recipeFile; to have users be able to select from file

        private AddImageBtn() {
            super("Add Image File?");
        }

        // EFFECTS: creates the file chooser allowing user to upload their own image
        // !!! For now gives popup for unremovable Tobs
        @Override
        public void actionPerformed(ActionEvent evt) {
            JOptionPane.showMessageDialog(null,
                    "I haven't properly implemented this feature yet. \n",
                    "Sorry", JOptionPane.PLAIN_MESSAGE);
//            final JFileChooser fc = createFileImageChooser();
//            int returnVal = fc.showOpenDialog(null);
//
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                recipeFile = fc.getSelectedFile();
//                try {
//                    recipeImg = ImageIO.read(recipeFile);
//                } catch (IOException e) {
//                    JOptionPane.showMessageDialog(null,
//                            "So what were you doing exactly?",
//                            "Where's your image?", JOptionPane.PLAIN_MESSAGE);
//                }
//            }
        }
    }

    // MODIFIES: this
    // EFFECTS: update remove buttons with state of the recipe list
    // e.g. if no more recipes in the JList, disable remove button
    private void buttonCheck() {
        if (listRecipe.getSelectionModel().isSelectionEmpty()) {
            btnEditRecipe.setEnabled(false);
            btnRemoveRecipe.setEnabled(false);
            btnRemoveFromFav.setEnabled(false);
        } else {
            btnEditRecipe.setEnabled(true);
            btnRemoveRecipe.setEnabled(true);
            btnRemoveFromFav.setEnabled(true);
        }
    }

}