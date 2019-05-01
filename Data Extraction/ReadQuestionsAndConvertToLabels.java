package FinalProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class ReadQuestionsAndConvertToLabels {

	  public static void main(String[] args) throws IOException {
	    File excelFile = new File("leetcode_questions.xlsx");
	    FileInputStream fis = new FileInputStream(excelFile);

	    // we create an XSSF Workbook object for our XLSX Excel File
	    XSSFWorkbook workbook = new XSSFWorkbook(fis);
	    // we get first sheet
	    XSSFSheet sheet = workbook.getSheetAt(0);
	    int limit = 0;
	    // we iterate on rows
	    Iterator<Row> rowIt = sheet.iterator();
	    int i = 0;
	    while(rowIt.hasNext()) {
	    	limit++;
	    	//if(limit == 10) break;
	      Row row = rowIt.next();
	      // iterate on cells for the current row
	      Iterator<Cell> cellIterator = row.cellIterator();
	      StringBuffer labString = new StringBuffer();
	      StringBuffer questionString = new StringBuffer();
	      BufferedWriter bwLab = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/yashmehta/eclipse-workspace/InformationExtraction/data/" + i + ".lab"), "UTF-8"));
	      BufferedWriter bwQuestion = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/yashmehta/eclipse-workspace/InformationExtraction/data/" + i + ".txt"), "UTF-8"));
	      int j = 0;
	      while (cellIterator.hasNext()) {
	        Cell cell = cellIterator.next();
	        cell.setCellType(Cell.CELL_TYPE_STRING);
	        String exampleString = "";
	        cell.toString();
	        //System.out.println();
	        if(j == 2) {
	        	exampleString = cell.toString();
	        }
	        if(j == 3) {
	        	questionString.append(cell.toString());
	        	questionString.append(exampleString);
	        }
	        if(j == 6) {
	        	labString.append(cell.toString().equals("1") ? "Array" + "\n" : "");
	        }
	        if(j == 7) {
	        	labString.append(cell.toString().equals("1") ? "Hash Table" + "\n" : "");
	        }
	        if(j == 8) {
	        	labString.append(cell.toString().equals("1") ? "Linked List" + "\n" : "");
	        }
	        if(j == 9) {
	        	labString.append(cell.toString().equals("1") ? "Math" + "\n" : "");
	        }
	        if(j == 10) {
	        	labString.append(cell.toString().equals("1") ? "Two Pointers" + "\n" : "");
	        }
	        if(j == 11) {
	        	labString.append(cell.toString().equals("1") ? "String" + "\n" : "");
	        }
	        if(j == 12) {
	        	labString.append(cell.toString().equals("1") ? "Binary Search" + "\n" : "");
	        }
	        if(j == 13) {
	        	labString.append(cell.toString().equals("1") ? "Divide and Conquer" + "\n" : "");
	        }
	        if(j == 14) {
	        	labString.append(cell.toString().equals("1") ? "Dynamic Programming" + "\n" : "");
	        }
	        if(j == 15) {
	        	labString.append(cell.toString().equals("1") ? "Backtracking" + "\n" : "");
	        }
	        if(j == 16) {
	        	labString.append(cell.toString().equals("1") ? "Stack" + "\n" : "");
	        }
	        if(j == 17) {
	        	labString.append(cell.toString().equals("1") ? "Heap" + "\n" : "");
	        }
	        if(j == 18) {
	        	labString.append(cell.toString().equals("1") ? "Greedy" + "\n" : "");
	        }
	        if(j == 19) {
	        	labString.append(cell.toString().equals("1") ? "Sort" + "\n" : "");
	        }
	        if(j == 20) {
	        	labString.append(cell.toString().equals("1") ? "Bit Manipulation" + "\n" : "");
	        }
	        if(j == 21) {
	        	labString.append(cell.toString().equals("1") ? "Tree" + "\n" : "");
	        }
	        if(j == 22) {
	        	labString.append(cell.toString().equals("1") ? "Depth-first Search" + "\n" : "");
	        }
	        if(j == 23) {
	        	labString.append(cell.toString().equals("1") ? "Breadth-first Search" + "\n" : "");
	        }
	        if(j == 24) {
	        	labString.append(cell.toString().equals("1") ? "Union Find" + "\n" : "");
	        }
	        if(j == 25) {
	        	labString.append(cell.toString().equals("1") ? "Graph" + "\n" : "");
	        }
	        if(j == 26) {
	        	labString.append(cell.toString().equals("1") ? "Design" + "\n" : "");
	        }
	        if(j == 27) {
	        	labString.append(cell.toString().equals("1") ? "Topological Sort" + "\n" : "");
	        }
	        if(j == 28) {
	        	labString.append(cell.toString().equals("1") ? "Trie" + "\n" : "");
	        }
	        if(j == 29) {
	        	labString.append(cell.toString().equals("1") ? "Binary Indexed Tree" + "\n" : "");
	        }
	        if(j == 30) {
	        	labString.append(cell.toString().equals("1") ? "Segment Tree" + "\n" : "");
	        }
	        if(j == 31) {
	        	labString.append(cell.toString().equals("1") ? "Binary Search Tree" + "\n" : "");
	        }
	        if(j == 32) {
	        	labString.append(cell.toString().equals("1") ? "Recursion" + "\n" : "");
	        }
	        if(j == 33) {
	        	labString.append(cell.toString().equals("1") ? "Memoization" + "\n" : "");
	        }
	        if(j == 34) {
	        	labString.append(cell.toString().equals("1") ? "Queue" + "\n" : "");
	        }
	        if(j == 35) {
	        	labString.append(cell.toString().equals("1") ? "Minimax" + "\n" : "");
	        }
	        if(j == 36) {
	        	labString.append(cell.toString().equals("1") ? "Map" + "\n" : "");
	        }
	        if(j == 37) {
	        	labString.append(cell.toString().equals("1") ? "Random" + "\n" : "");
	        }
	        if(j == 38) {
	        	labString.append(cell.toString().equals("1") ? "Sliding Window" + "\n" : "");
	        }
	        if(j == 39) {
	        	labString.append(cell.toString().equals("1") ? "Searching" + "\n" : "");
	        }
	        j++;
	      }
	      i++;
	      bwLab.write(labString.toString());
	      bwQuestion.write(questionString.toString());
	      bwLab.flush();
	      bwLab.close();
	      bwQuestion.flush();
	      bwQuestion.close();
//	      System.out.println(labString.toString());
//	      System.out.println(questionString.toString());
	    }
	    workbook.close();
	    fis.close();
	  }
}
