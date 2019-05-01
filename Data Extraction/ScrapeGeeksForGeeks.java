package FinalProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ScrapeGeeksForGeeks {

	public final static String ALL_ALGO_LINK = "https://practice.geeksforgeeks.org/topic-tags";
	private static String []columns = {"Arrays", "HashTable", "Linked List", "Mathematical", "two-pointer-algorithm", "Strings", "Binary Search", 
			"Divide and Conquer", "Dynamic Programming","Backtracking", "Stack", "Heap", "Greedy", "Sorting", "Bit Magic", "Tree",
			"DFS", "BFS", "union-find", "Graph", "Design Pattern", "Topological Sorting", "Trie",
			"Binary Indexed Tree", "Segment-Tree", "Binary Search Tree", "Recursion", "Memoization", "Queue", "Game Theory",
			"Map", "Randomized", "sliding-window", "Searching"};
	private static HashSet<String> questionTags = new HashSet<>(Arrays.asList(columns));
	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new ChromeDriver();
		driver.get(ALL_ALGO_LINK);
		//driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS); 
		//login(driver);

		WebElement dataStructuresTab = driver.findElement(By.id("algorithms"));
		List<WebElement> allDSLinks = dataStructuresTab.findElements(By.tagName("a"));
		int i = 0;
		for (WebElement link : allDSLinks) {
			
			System.out.println(link.getAttribute("href"));
			if(i < 26) {
				i++;
				continue;
			}
			driver.get(link.getAttribute("href"));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			//js.executeScript("window.scrollTo(0,Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight));");
//			Actions actions = new Actions(driver);
//			actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
			long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
			while(true) {
				js.executeScript("window.scrollTo(0,document.documentElement.scrollHeight);");
				Thread.sleep(500);
				long newHeight = (long) js.executeScript("return document.body.scrollHeight");
				if(newHeight == lastHeight) break;
				lastHeight = newHeight;
			}
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 
//			Long value = (Long) js.executeScript("return window.pageYOffset;");
//			if(value == 0) break;
			List<WebElement> panelProblems = driver.findElements(By.cssSelector(".panel.problem-block"));
			for(WebElement oneProblem : panelProblems) {
				System.out.println(oneProblem.findElement(By.tagName("a")).getAttribute("href"));
//				driver.get(oneProblem.findElement(By.tagName("a")).getAttribute("href"));
//				List<WebElement> allTags = driver.findElements(By.cssSelector(".btn.btn-info.btn-xs.topicTags"));
//				List<String> tags = new ArrayList<>();
//				for(WebElement oneTag : allTags) {
//					tags.add(oneTag.getAttribute("textContent"));
//				}
			}
//			  try{
//				  System.out.println(link.getAttribute("href"));
//				  driver.get(link.getAttribute("href"));
//			  } 
//			  catch(StaleElementReferenceException e){
//			    continue;
//			  }
//			if(isCrawlable(driver)) {
//				List<WebElement> tags = driver.findElements(By.className("practiceButton"));
//				for(WebElement oneTag : tags) {
//					System.out.println(oneTag.findElement(By.tagName("a")).getAttribute("textContent"));
//				}
//			}
		}
	}
	private static boolean footerIsNotPresent() {
		
		return false;
	}
	private static void login(WebDriver driver) {
		WebDriverWait wait;
		wait = new WebDriverWait(driver, 20);
        Actions actions = new Actions(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".header--nav__link.login-modal-btn")));
		driver.findElement(By.id("luser")).sendKeys("yashmehta894");     
	    driver.findElement(By.id("password")).sendKeys("rohit9969539040");
	    actions.moveToElement(driver.findElement(By.cssSelector(".btn.btn-green.signin-button"))).click().perform();
	}
	private static boolean isCrawlable(WebDriver driver) {
		if(driver.findElements(By.className("practiceButton")).size() <= 0) return false;
		List<WebElement> tags = driver.findElements(By.className("practiceButton"));
		List<String> tagsList = new ArrayList<>();
		for(WebElement oneTag : tags) {
			if(questionTags.contains(oneTag.findElement(By.tagName("a")).getAttribute("textContent"))) return true;
		}
		return false; 
	}
}
