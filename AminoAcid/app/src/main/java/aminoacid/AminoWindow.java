package aminoacid;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;
import java.io.FileInputStream;

public class AminoWindow {

  private static int MH_COL = 0;
  private static int START_COL = 8;
  private static int END_COL = 9;
  private static int DOMAIN_COL = 10;
  private static int HPCL_COL = 13;

  private static int SEQUENCE_COL = 14;


  private JFrame frame;
  
  private JScrollPane tableScrollPane;
  private AminoModel aminoModel;
  private CommandParser commandParser = new CommandParser();
  
  private JTextField commandField;
  private JTextField minLengthField;
  private JTextField maxLengthField;
  private JTable aminoTable;
  private DefaultTableModel tableModel;
  private JTextField hydroField;

  private JTextField errorLabel;
  
  private static final Dimension WINDOW_SIZE = new Dimension(1200, 600);

  private String[] tableColNames = {"ExcelRow", "M+H", "Start", "End", "Domain", "HPLC Index", "Sequence"};

  private boolean sortByHydrophobic;

  public AminoWindow(AminoModel aminoModel) {
    this.aminoModel = aminoModel;
    frame = new JFrame();
    frame.setMinimumSize(new Dimension(1200, 400));


    JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 30));
    top.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

    JButton importButton = new JButton("Import");

    importButton.addActionListener(action -> {
      JFileChooser fileChooser = new JFileChooser();

      int result = fileChooser.showOpenDialog(frame);

      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        String fileName = selectedFile.getName();
        String extension = "";

        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0) {
          extension = fileName.substring(lastIndex + 1);
        }

        if (extension.equals("xlsx")) {
          readFile(selectedFile.getAbsolutePath());
        } else {
          errorLabel.setText("Wrong file format, needs to be a file of type \"xlsx\"");
        }


        System.out.println("Selected File: " + selectedFile.getName());
      }

    });
    
    top.add(importButton);

    commandField = new JTextField();
    commandField.setPreferredSize(new Dimension(400, 30));
    top.add(commandField);

    JButton filterButton = new JButton("Filter");
    filterButton.setPreferredSize(new Dimension(100, 40));
    top.add(filterButton);

    filterButton.addActionListener(action -> {
      filterButtonPressed();
    });

    JPanel lengthPanel = new JPanel(new GridLayout(2, 2, 20, 10));
    
    
    JLabel minLengthLabel = new JLabel("Min Length:");
    minLengthField = new JTextField("0");
    minLengthField.setPreferredSize(new Dimension(50, 30));
    lengthPanel.add(minLengthLabel);
    lengthPanel.add(minLengthField);
    //top.add(minLengthField);

    JLabel maxLengthLabel = new JLabel("Max Length:");
    maxLengthField = new JTextField("76");
    maxLengthField.setPreferredSize(new Dimension(50, 30));
    lengthPanel.add(maxLengthLabel);
    lengthPanel.add(maxLengthField);
    //top.add(maxLengthField);  
    top.add(lengthPanel);


    JPanel hydroPanel = new JPanel(new GridLayout(2, 1, 20, 10));
    JToggleButton hydroButton = new JToggleButton("Hydrophobic");
    ItemListener itemListener = new ItemListener() {

      // itemStateChanged() method is invoked automatically
      // whenever you click or unclick on the Button.
      public void itemStateChanged(ItemEvent itemEvent) {
        int state = itemEvent.getStateChange();

        sortByHydrophobic = state == ItemEvent.SELECTED; 
      }
    };
    hydroButton.addItemListener(itemListener);
    hydroField = new JTextField();
    hydroField.setPreferredSize(new Dimension(30, 30));
    hydroPanel.add(hydroButton);
    hydroPanel.add(hydroField);
    top.add(hydroPanel);


    

    JPanel center = new JPanel();
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

    JPanel errorPanel = new JPanel();
    errorPanel.setMaximumSize(new Dimension(1000, 30));
    errorPanel.setMinimumSize(new Dimension(1000, 30));

    Border panelBorder = BorderFactory.createEtchedBorder();

    errorLabel = new JTextField("Error Field");
    errorLabel.setEditable(false);
    errorLabel.setPreferredSize(new Dimension(1000, 30));
    errorLabel.setBorder(panelBorder);
    errorPanel.add(errorLabel);

    center.add(errorPanel);

    tableModel = new DefaultTableModel(tableColNames, 6);

    aminoTable = new JTable(tableModel);
    
    aminoTable.setRowHeight(25);
    aminoTable.setDefaultEditor(Object.class, null);
    

    aminoTable.getColumnModel().getColumn(0).setPreferredWidth(30);
    aminoTable.getColumnModel().getColumn(1).setPreferredWidth(10);
    aminoTable.getColumnModel().getColumn(2).setPreferredWidth(20);
    aminoTable.getColumnModel().getColumn(3).setPreferredWidth(20);
    aminoTable.getColumnModel().getColumn(4).setPreferredWidth(20);
    aminoTable.getColumnModel().getColumn(5).setPreferredWidth(20);
    aminoTable.getColumnModel().getColumn(6).setPreferredWidth(400);

    tableScrollPane = new JScrollPane(aminoTable);
    tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    center.add(tableScrollPane);
    frame.add(center);
    frame.add(top, BorderLayout.NORTH);

    
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setSize(WINDOW_SIZE);
    frame.setVisible(true);

    renderTable(aminoModel.getAminoList());

  }

  private void filterButtonPressed() {
    try {
      int minLength;
      int maxLength;
      try {
        minLength = Integer.parseInt(minLengthField.getText());
        maxLength = Integer.parseInt(maxLengthField.getText());
      } catch (Exception e) {
        throw new Exception("Symbol in length textfields needs to be a number");
      }
      int hydroCount = 0;
      try {
        if (sortByHydrophobic) {
          hydroCount = Integer.parseInt(hydroField.getText());
        }
      } catch (Exception e) {
        throw new Exception("Symbol in hydrophobic textfield needs to be a number");
      }
      System.out.println("heheh");
      List<AbstractCondition> conditionList = commandParser.parseCommand(commandField.getText());
      List<AminoEntry> filteredList = aminoModel.filterAminos(minLength, maxLength, conditionList, sortByHydrophobic, hydroCount); 
      renderTable(filteredList);
      errorLabel.setText("Error Field                 ");
    } catch (Exception e) {
      errorLabel.setText(e.getMessage());
    }
  }

  public void renderTable(List<AminoEntry> aminoList) {
    //DefaultTableModel model = (DefaultTableModel) aminoTable.getModel();
    ((DefaultTableModel) aminoTable.getModel()).setRowCount(0);
    //model.setRowCount(0);
    for (AminoEntry aminoEntry : aminoList) {
      addRow(aminoEntry);
    }
  }

  private void addRow(AminoEntry aminoEntry) {
    String[] rowData = aminoEntry.getArray();
    DefaultTableModel model = (DefaultTableModel) aminoTable.getModel();
    model.addRow(rowData);
  }

  private void readFile(String filePath) {
    try {
      FileInputStream fileInputStream = new FileInputStream(new File(filePath));
      List<AminoEntry> aminoList = new ArrayList<AminoEntry>();
      XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);
      XSSFSheet alignedSheet = workBook.getSheetAt(1);
      workBook.close();
      int count = 0;
      int i = 0;
      for (Row row : alignedSheet) {
        i++;
        if (count == 0) {
          count++;
          continue;
        }
        // Cell cell = row.getCell(MH_COL);
        double mH = (double) row.getCell(MH_COL).getNumericCellValue();
        int start = (int) row.getCell(START_COL).getNumericCellValue();
        int end = (int) row.getCell(END_COL).getNumericCellValue();
        String domain = "";
        Cell cell = row.getCell(DOMAIN_COL);
        if (cell != null) {
          domain = row.getCell(DOMAIN_COL).getStringCellValue();
        }
        double hpclIndex = (double) row.getCell(HPCL_COL).getNumericCellValue();
        String sequence = row.getCell(SEQUENCE_COL).getStringCellValue();
        System.out.println(i);
        AminoEntry aminoEntry = new AminoEntry(i, mH, start, end, domain, hpclIndex, sequence);
        aminoList.add(aminoEntry);
      }
      aminoModel.setAminoList(aminoList);
      renderTable(aminoList);
    } catch (Exception e) {
      errorLabel.setText("No file selected");
    }
    
  }
  
}
