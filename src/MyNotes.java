import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MyNotes extends JFrame {
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private String currentFile = null ;
    private boolean isModified = false;
    private UndoManager undoManager;

    public MyNotes(){
        initializeComponent();
        setupMenuBar();
        setupEventHandler();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("My Notes - Untitled");
        setSize(800,600);
        setLocationRelativeTo(null);
    }

    private void initializeComponent(){
        // Create text area with scroll pane
        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN,12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // Initialize undo manager
        undoManager = new UndoManager();
        textArea.getDocument().addUndoableEditListener(undoManager);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        // Initialize file chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
    }

    private void setupMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As");
        JMenuItem exitItem = new JMenuItem("Exit");

        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        //Edit Menu
        JMenu editeMenu = new JMenu("Edit");

        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem selectAllItem = new JMenuItem("Select All");

        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        editeMenu.add(undoItem);
        editeMenu.add(redoItem);
        editeMenu.addSeparator();
        editeMenu.add(cutItem);
        editeMenu.add(copyItem);
        editeMenu.add(pasteItem);
        editeMenu.addSeparator();
        editeMenu.add(selectAllItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editeMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private void setupEventHandler(){
        //File Menu actions
        addActionListener("New", _ -> newFile());
        addActionListener("Open", _ -> openFile());
        addActionListener("Save", _ -> saveFile());
        addActionListener("Save As", _ -> saveAsFile());
        addActionListener("Exit", _ -> exitApplication());

        // Edit menu actions
        addActionListener("Undo", _ -> undoManager.undo());
        addActionListener("Redo", _ -> undoManager.redo());
        addActionListener("Cut", _ -> textArea.cut());
        addActionListener("Copy", _ -> textArea.copy());
        addActionListener("Paste", _ -> textArea.paste());
        addActionListener("Select All", _ -> textArea.selectAll());

        // Help menu actions
        addActionListener("About", _ -> showAboutDialog());

        //Text area change listener
        textArea.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                markAsModified();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                markAsModified();
            }
        });
        // window close handler
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
    }

    private void addActionListener(String menuItemText, ActionListener listener){
        // Find the menu item by text and add listener
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            for (int i=0; i < menuBar.getMenuCount(); i++){
                JMenu menu = menuBar.getMenu(i);
                for (int j=0; j < menu.getItemCount(); j++){
                    JMenuItem item = menu.getItem(j);
                    if (item != null && item.getText().equals(menuItemText)){
                        item.addActionListener(listener);
                        break;
                    }
                }
            }
        }
    }

    private void newFile(){
        if(checkForUnsavedChanges()){
            textArea.setText("");
            currentFile = null;
            isModified = false;
            setTitle("My Notes - Untitled");
        }
    }

    private void openFile(){
        if (checkForUnsavedChanges()){
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine())!= null){
                        content.append(line).append("\n");
                    }
                    reader.close();

                    textArea.setText(content.toString());
                    currentFile = file.getAbsolutePath();
                    isModified = false;
                    setTitle("My Notes - " + file.getName());
                } catch (IOException e){
                    JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private boolean saveFile(){
        if (currentFile == null){
            return saveAsFile();
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                writer.write(textArea.getText());
                writer.close();
                isModified = false;
                setTitle("My Notes - " + new File(currentFile).getName());
                return true;
            } catch (IOException e){
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    private boolean saveAsFile(){
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".txt")){
                file =new File(file.getAbsolutePath() + ".txt");
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(textArea.getText());
                writer.close();
                currentFile = file.getAbsolutePath();
                isModified = false;
                setTitle("My Notes - " + file.getName() );
                return true;
            } catch (IOException e){
                JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(),
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private void exitApplication(){
        if (checkForUnsavedChanges()){
            System.exit(0);
        }
    }

    private boolean checkForUnsavedChanges(){
        if (isModified){
            int result = JOptionPane.showConfirmDialog(this,
                    "Do you want save changes to" + (currentFile != null ? new File(currentFile).getName():"Untitled") + "?",
                    "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            if (result == JOptionPane.YES_OPTION){
                return saveFile();
            } else if (result == JOptionPane.NO_OPTION){
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    private void markAsModified(){
        if(!isModified){
            isModified = true;
            String title = getTitle();
            if (!title.endsWith("*")){
                setTitle(title + "*");
            }
        }
    }

    private void showAboutDialog(){
        JOptionPane.showMessageDialog(this,
                "Pipuni Senadeera(Student ID: 2022s19512)",
                "About",JOptionPane.INFORMATION_MESSAGE);
    }
}
