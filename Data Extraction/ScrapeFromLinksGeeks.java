package FinalProject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class ScrapeFromLinksGeeks {
	private static String []columns = {"Question Title", "Question Description", "Examples", "Question Title Plus Description", "Similar Questions Titles", "Similar Questions Links","Array", "Hash Table", "Linked List", "Math", "Two Pointers", "String", "Binary Search", 
			"Divide and Conquer", "Dynamic Programming","Backtracking", "Stack", "Heap", "Greedy", "Sort", "Bit Manipulation", "Tree",
			"Depth-first Search", "Breadth-first Search", "Union Find", "Graph", "Design", "Topological Sort", "Trie",
			"Binary Indexed Tree", "Segment Tree", "Binary Search Tree", "Recursion", "Memoization", "Queue", "Minimax",
			"Map", "Random", "Sliding Window", "Searching"};

	public static void main(String[] args) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("GeeksForGeeksQuestions");
		// Create a Row
        Row headerRow = sheet.createRow(0);
        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
		List<String> questions = loadDataFromFile();
		//System.out.println(questions);
		WebDriver driver = new ChromeDriver();
		List<QuestionDetails> questionsList = new ArrayList<>();
		int i = 0;
		for(String oneQuestion : questions) {
			i++;
			if(i < 2000) continue;
			if(i == 2500) break;
			System.out.println(oneQuestion + " " + i);
			QuestionDetails newQuestion = new QuestionDetails();
			driver.get(oneQuestion);
			WebElement wb = fluentWait(By.className("problemQuestion"), driver);
			//System.out.println(wb.getText());
			String questionContent = getContextData(wb);
			//System.out.println("Question content " + questionContent);
			String examples = getExamples(wb);
			//System.out.println("Examples " + examples);
			WebElement title = driver.findElement(By.className("col-lg-12"));
			//System.out.println("Title --------" + title.getText());
			List<String> tags = getTags(driver);
			HashSet<String> tagsSet = new HashSet<>(tags);
			newQuestion.setQuestionTitleMain(title.getText());
			newQuestion.setQuestionContent (questionContent);
			newQuestion.setExamples(examples);
			String titlePluDesc = "";
			titlePluDesc = title.getText() + " " + questionContent;
			newQuestion.setTitlePluDesc(titlePluDesc);
			makeQuestionObject(newQuestion, tagsSet);
			questionsList.add(newQuestion);
			//System.out.println(newQuestion);
		}
		int rowNum = 1;
        for(QuestionDetails oneQuestion: questionsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0)
                    .setCellValue(oneQuestion.getQuestionTitleMain());
            row.createCell(1)
                    .setCellValue(oneQuestion.getQuestionContent());
            row.createCell(2)
            .setCellValue(oneQuestion.getExamples());
            row.createCell(3)
            .setCellValue(oneQuestion.getTitlePluDesc());
            row.createCell(4)
            .setCellValue(oneQuestion.getSimilarQuestionsTitles());
            row.createCell(5)
            .setCellValue(oneQuestion.getSimilarQuestionsLinks());
            row.createCell(6)
            .setCellValue(oneQuestion.getArray());
            row.createCell(7)
            .setCellValue(oneQuestion.getHashTable());
            row.createCell(8)
            .setCellValue(oneQuestion.getLinkedList());
            row.createCell(9)
            .setCellValue(oneQuestion.getMath());
            row.createCell(10)
            .setCellValue(oneQuestion.getTwoPointers());
            row.createCell(11)
            .setCellValue(oneQuestion.getString());
            row.createCell(12)
            .setCellValue(oneQuestion.getBinarySearch());
            row.createCell(13)
            .setCellValue(oneQuestion.getDivideandConquer());
            row.createCell(14)
            .setCellValue(oneQuestion.getDynamicProgramming());
            row.createCell(15)
            .setCellValue(oneQuestion.getBacktracking());
            row.createCell(16)
            .setCellValue(oneQuestion.getStack());
            row.createCell(17)
            .setCellValue(oneQuestion.getHeap());
            row.createCell(18)
            .setCellValue(oneQuestion.getGreedy());
            row.createCell(19)
            .setCellValue(oneQuestion.getSort());
            row.createCell(20)
            .setCellValue(oneQuestion.getBitManipulation());
            row.createCell(21)
            .setCellValue(oneQuestion.getTree());
            row.createCell(22)
            .setCellValue(oneQuestion.getDepthfirstSearch());
            row.createCell(23)
            .setCellValue(oneQuestion.getBreadthfirstSearch());
            row.createCell(24)
            .setCellValue(oneQuestion.getUnionFind());
            row.createCell(25)
            .setCellValue(oneQuestion.getGraph());
            row.createCell(26)
            .setCellValue(oneQuestion.getDesign());
            row.createCell(27)
            .setCellValue(oneQuestion.getTopologicalSort());
            row.createCell(28)
            .setCellValue(oneQuestion.getTrie());
            row.createCell(29)
            .setCellValue(oneQuestion.getBinaryIndexedTree());
            row.createCell(30)
            .setCellValue(oneQuestion.getSegmentTree());
            row.createCell(31)
            .setCellValue(oneQuestion.getBinarySearchTree());
            row.createCell(32)
            .setCellValue(oneQuestion.getRecursion());
            row.createCell(33)
            .setCellValue(oneQuestion.getMemoization());
            row.createCell(34)
            .setCellValue(oneQuestion.getQueue());
            row.createCell(35)
            .setCellValue(oneQuestion.getMinimax());
            row.createCell(36)
            .setCellValue(oneQuestion.getMap());
            row.createCell(37)
            .setCellValue(oneQuestion.getRandom());
            row.createCell(38)
            .setCellValue(oneQuestion.getSlidingWindow());
            row.createCell(39)
            .setCellValue(oneQuestion.getSearching());
        }

			// Resize all columns to fit the content size
        	for(int j = 0; j < columns.length; j++) {
        		sheet.autoSizeColumn(j);
        }
        	// Write the output to a file
            FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream("geeks_for_geeks_questions_5.xlsx");
				workbook.write(fileOut);
	            fileOut.close();
	            // Closing the workbook
	            workbook.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

	}
	private static void makeQuestionObject(QuestionDetails newQuestion, HashSet<String> tagsSet) {
		newQuestion.setArray(tagsSet.contains("Arrays") ? 1 : 0);
		newQuestion.setHashTable(tagsSet.contains("HashTable") ? 1 : 0);
		newQuestion.setLinkedList(tagsSet.contains("Linked List") ? 1 : 0);
		newQuestion.setMath(tagsSet.contains("Mathematical") ? 1 : 0);
		newQuestion.setTwoPointers(tagsSet.contains("two-pointer-algorithm") ? 1 : 0);
		newQuestion.setString(tagsSet.contains("Strings") ? 1 : 0);
		newQuestion.setBinarySearch(tagsSet.contains("Binary Search") ? 1 : 0);
		newQuestion.setDynamicProgramming(tagsSet.contains("Dynamic Programming") ? 1 : 0);
		newQuestion.setDivideandConquer(tagsSet.contains("Divide and Conquer") ? 1 : 0);
		newQuestion.setBacktracking(tagsSet.contains("Backtracking") ? 1 : 0);
		newQuestion.setStack(tagsSet.contains("Stack") ? 1 : 0);
		newQuestion.setHeap(tagsSet.contains("Heap") ? 1 : 0);
		newQuestion.setGreedy(tagsSet.contains("Greedy") ? 1 : 0);
		newQuestion.setSort(tagsSet.contains("Sorting") ? 1 : 0);
		newQuestion.setBitManipulation(tagsSet.contains("Bit Magic") ? 1 : 0);
		newQuestion.setTree(tagsSet.contains("Tree") ? 1 : 0);
		newQuestion.setDepthfirstSearch(tagsSet.contains("DFS") ? 1 : 0);
		newQuestion.setBreadthfirstSearch(tagsSet.contains("BFS") ? 1 : 0);
		newQuestion.setUnionFind(tagsSet.contains("union-find") ? 1 : 0);
		newQuestion.setGraph(tagsSet.contains("Graph") ? 1 : 0);
		newQuestion.setDesign(tagsSet.contains("Design Pattern") ? 1 : 0);
		newQuestion.setTopologicalSort(tagsSet.contains("Topological Sorting") ? 1 : 0);
		newQuestion.setTrie(tagsSet.contains("Trie") ? 1 : 0);
		newQuestion.setBinaryIndexedTree(tagsSet.contains("Binary Indexed Tree") ? 1 : 0);
		newQuestion.setSegmentTree(tagsSet.contains("Segment-Tree") ? 1 : 0);
		newQuestion.setBinarySearchTree(tagsSet.contains("Binary Search Tree") ? 1 : 0);
		newQuestion.setRecursion(tagsSet.contains("Recursion") ? 1 : 0);
		newQuestion.setMemoization(tagsSet.contains("Memoization") ? 1 : 0);
		newQuestion.setQueue(tagsSet.contains("Queue") ? 1 : 0);
		newQuestion.setMinimax(tagsSet.contains("Game Theory") ? 1 : 0);
		newQuestion.setMap(tagsSet.contains("Map") ? 1 : 0);
		newQuestion.setRandom(tagsSet.contains("Randomized") ? 1 : 0);
		newQuestion.setSlidingWindow(tagsSet.contains("sliding-window") ? 1 : 0);
		newQuestion.setSearching(tagsSet.contains("Searching") ? 1 : 0);
	}
	private static List<String> getTags(WebDriver driver) {
		List<String> tagsList = new ArrayList<>();
		List<WebElement> tags = driver.findElements(By.cssSelector(".btn.btn-info.btn-xs.topicTags"));
		for(WebElement oneTag : tags) {
			tagsList.add(oneTag.getAttribute("textContent"));
		}
		return tagsList;
	}
	private static String getExamples(WebElement wb) {
		String questionContent = "";
		if(wb.getText().indexOf("Example:") != -1) {
			if(wb.getText().indexOf("**") > wb.getText().indexOf("Example:")) {
				questionContent = wb.getText().substring(wb.getText().indexOf("Example:"), wb.getText().indexOf("**"));
			} else {
				questionContent = wb.getText().substring(wb.getText().indexOf("Example:"));
			}
		} else {
			questionContent = wb.getText();
		}
		return questionContent;
	}
	private static String getContextData(WebElement wb) {
		String questionContent = "";
		if(wb.getText().indexOf("Input:") != -1) {
			questionContent = wb.getText().substring(0, wb.getText().indexOf("Input:"));
		} else {
			questionContent = wb.getText();
		}
		return questionContent;
	}
	public static WebElement fluentWait(final By locator, WebDriver driver) {
		//login();
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(7, TimeUnit.SECONDS)
	            .pollingEvery(2, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class);

	    WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
	        public WebElement apply(WebDriver driver) {
	            return driver.findElement(locator);
	        }
	    });

	    return  foo;
	}
	private static List<String> loadDataFromFile() throws IOException {
		// Open the file
		List<String> questions = new ArrayList<>();
		FileInputStream fstream = new FileInputStream("GeeksForGeeksLinks.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  //System.out.println (strLine);
		  questions.add(strLine);
		}

		//Close the input stream
		fstream.close();
		return questions;
	}

}
