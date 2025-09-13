# My Notes - Simple Text Editor

A basic text editor application built with Java Swing that provides essential text editing functionality similar to Windows Notepad.

## Features

### File Operations
- **New** - Create a new document (Ctrl+N)
- **Open** - Open an existing text file (Ctrl+O)
- **Save** - Save the current document (Ctrl+S)
- **Save As** - Save the document with a new name (Ctrl+S)
- **Exit** - Close the application (Ctrl+F4)

### Edit Operations
- **Undo** - Undo the last action (Ctrl+Z)
- **Redo** - Redo the last undone action (Ctrl+Y)
- **Cut** - Cut selected text (Ctrl+X)
- **Copy** - Copy selected text (Ctrl+C)
- **Paste** - Paste text from clipboard (Ctrl+V)
- **Select All** - Select all text in the document (Ctrl+A)

### Additional Features
- **Auto-save prompt** - Warns user about unsaved changes before closing
- **File type filtering** - Only shows .txt files in file dialogs
- **Word wrap** - Text automatically wraps to fit the window
- **Scroll bars** - Vertical and horizontal scroll bars when needed
- **Modified indicator** - Shows asterisk (*) in title when document has unsaved changes

## Requirements

- Java 8 or higher
- No external dependencies (uses only standard Java libraries)

## How to Run

1. Compile the Java files:
   ```bash
   javac src/*.java
   ```

2. Run the application:
   ```bash
   java -cp src Main
   ```

## Project Structure

```
NotepadGUI/
├── src/
│   ├── Main.java          # Entry point of the application
│   └── MyNotes.java       # Main GUI class with all functionality
├── out/                   # Compiled class files
└── README.md             # This file
```

## Usage

1. **Starting the application**: Run the Main class to launch the text editor
2. **Creating a new document**: Use File → New or Ctrl+N
3. **Opening a file**: Use File → Open or Ctrl+O, then select a .txt file
4. **Saving a document**: Use File → Save or Ctrl+S for existing files, or File → Save As for new files
5. **Editing text**: Type directly in the text area, use standard keyboard shortcuts for editing operations
6. **Exiting**: Use File → Exit or Ctrl+F4, or click the X button (you'll be prompted to save if there are unsaved changes)

## Technical Details

- **GUI Framework**: Java Swing
- **Text Component**: JTextArea with JScrollPane
- **File Handling**: BufferedReader/BufferedWriter for text file I/O
- **Undo/Redo**: UndoManager for text editing history
- **Event Handling**: ActionListener for menu items and DocumentListener for text changes

## Author

**Pipuni Senadeera**  
Student ID: 2022s19512

## Version

Version 1.0

---

*A simple and lightweight text editor for basic text editing needs.*
