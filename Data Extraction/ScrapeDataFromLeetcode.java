package FinalProject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

//https://leetcode.com/problemset/all/

public class ScrapeDataFromLeetcode {
	public final static String HOME_PAGE_URL = "https://leetcode.com";
	public final static String LOGIN_PAGE_URL = "https://leetcode.com/accounts/login/";
	public final static String ALL_QUESTIONS_URL ="https://leetcode.com/api/problems/all/";
	private static final String CSV_SEPARATOR = ",";
	private static String []columns = {"Question Title", "Question Description", "Examples", "Question Title Plus Description", "Similar Questions Titles", "Similar Questions Links","Array", "Hash Table", "Linked List", "Math", "Two Pointers", "String", "Binary Search", 
									"Divide and Conquer", "Dynamic Programming","Backtracking", "Stack", "Heap", "Greedy", "Sort", "Bit Manipulation", "Tree",
									"Depth-first Search", "Breadth-first Search", "Union Find", "Graph", "Design", "Topological Sort", "Trie",
									"Binary Indexed Tree", "Segment Tree", "Binary Search Tree", "Recursion", "Memoization", "Queue", "Minimax",
									"Map", "Random", "Sliding Window", "Searching"};
	private static String csrftoken = "IJzSEkQeFwQ1uy5OUnjjxEDZBR3nAfoZZaklvqJbDQJttG4ZUgU9kzarN0j8TQ1F";
	private static String leetcodeSessionID = "";
	public static void main(String[] args) {
		Workbook workbook = new XSSFWorkbook();
		CreationHelper createHelper = workbook.getCreationHelper();
		Sheet sheet = workbook.createSheet("LeetcodeQuestions");
		List<QuestionDetails> questions = new ArrayList<>();
		// Create a Row
        Row headerRow = sheet.createRow(0);
        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }
		if(login()) {
			WebDriver driver = new ChromeDriver();
			System.out.println("Login successful");
			String jsonRaw = fetchPage(ALL_QUESTIONS_URL);
			JSONParser parser = new JSONParser();
			JSONObject response = null;
			try {
				response = (JSONObject) parser.parse(jsonRaw);
			} catch (org.json.simple.parser.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JSONArray questionList = (JSONArray) response.get("stat_status_pairs");
			int i = 0;
			
			for(Object oneQuestion : questionList) {
				QuestionDetails newQuestion = new QuestionDetails();
				i++;
				if(i <= 183) continue;
				//if(i == 368) break;
				JSONObject oneQuestionJson = (JSONObject) oneQuestion;
				String isPaid = oneQuestionJson.get("paid_only").toString();
				JSONObject oneQuestionJsonDescription = (JSONObject) oneQuestionJson.get("stat");
				String questionTitle = oneQuestionJsonDescription.get("question__title_slug").toString();
				String questionTitleMain = oneQuestionJsonDescription.get("question__title").toString();
				if(isPaid.equals("true")) {
					continue;
				}
				driver.get(makeQuestionURL(questionTitle));
//				WebDriverWait wait;
//				driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//				if(driver.findElements(By.className("sign-in-page__3DVK")).size() > 0) {
//			        wait = new WebDriverWait(driver, 20);
//			        Actions actions = new Actions(driver);
//			        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("sign-in-page__3DVK")));
//					driver.findElement(By.id("username-input")).sendKeys("jai_hanuman");     
//				    driver.findElement(By.id("password-input")).sendKeys("Abc@1234");
//				    actions.moveToElement(driver.findElement(By.id("sign-in-button"))).click().perform();
//				    //driver.get(makeQuestionURL(questionTitle));
//				}
				//driver.get(makeQuestionURL(questionTitle));
				System.out.println(makeQuestionURL(questionTitle) + " " + i);
				
				WebElement wb = fluentWait(By.className("darker-content__naal"), driver);
				List<SimilarQuestion> similarQuestions = extractSimilarQuestion(driver);
				List<String> questionTags = getQuestionTags(driver);
				String questionContent = getQuestionContent(wb);
				String examples = wb.getText().indexOf("Example") != -1 ? getExampleDescription(wb) : "";
//				System.out.println("Question " + questionContent);
//				System.out.println("Examples " + examples);
//				System.out.println("Tags " + questionTags);
//				System.out.println("Similar" + similarQuestions);
				StringBuffer similarQuestionsTitles = new StringBuffer();
				StringBuffer similarQuestionsLinks = new StringBuffer();
				String titlePluDesc = "";
				
				HashSet<String> tagsSet = new HashSet<>(questionTags);
				newQuestion.setQuestionTitleMain(questionTitleMain);
				newQuestion.setQuestionContent (questionContent);
				newQuestion.setExamples(examples);
				titlePluDesc = questionTitleMain + " " + questionContent;
				newQuestion.setTitlePluDesc(titlePluDesc);
				for(SimilarQuestion singleQuestion : similarQuestions) {
					similarQuestionsTitles.append(singleQuestion.title + " | ");
					similarQuestionsLinks.append(singleQuestion.url + " | ");
				}
				newQuestion.setSimilarQuestionsTitles(similarQuestionsTitles.toString());
				newQuestion.setSimilarQuestionsLinks(similarQuestionsLinks.toString());
				makeQuestionObject(newQuestion, tagsSet);
				questions.add(newQuestion);
			}
			
			int rowNum = 1;
	        for(QuestionDetails oneQuestion: questions) {
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
					fileOut = new FileOutputStream("leetcode_questions_2.xlsx");
					workbook.write(fileOut);
		            fileOut.close();
		            // Closing the workbook
		            workbook.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
		}
	}
	private static void makeQuestionObject(QuestionDetails newQuestion, HashSet<String> tagsSet) {
		newQuestion.setArray(tagsSet.contains("Array") ? 1 : 0);
		newQuestion.setHashTable(tagsSet.contains("Hash Table") ? 1 : 0);
		newQuestion.setLinkedList(tagsSet.contains("Linked List") ? 1 : 0);
		newQuestion.setMath(tagsSet.contains("Math") ? 1 : 0);
		newQuestion.setTwoPointers(tagsSet.contains("Two Pointers") ? 1 : 0);
		newQuestion.setString(tagsSet.contains("String") ? 1 : 0);
		newQuestion.setBinarySearch(tagsSet.contains("Binary Search") ? 1 : 0);
		newQuestion.setDynamicProgramming(tagsSet.contains("Dynamic Programming") ? 1 : 0);
		newQuestion.setDivideandConquer(tagsSet.contains("Divide and Conquer") ? 1 : 0);
		newQuestion.setBacktracking(tagsSet.contains("Backtracking") ? 1 : 0);
		newQuestion.setStack(tagsSet.contains("Stack") ? 1 : 0);
		newQuestion.setHeap(tagsSet.contains("Heap") ? 1 : 0);
		newQuestion.setGreedy(tagsSet.contains("Greedy") ? 1 : 0);
		newQuestion.setSort(tagsSet.contains("Sort") ? 1 : 0);
		newQuestion.setBitManipulation(tagsSet.contains("Bit Manipulation") ? 1 : 0);
		newQuestion.setTree(tagsSet.contains("Tree") ? 1 : 0);
		newQuestion.setDepthfirstSearch(tagsSet.contains("Depth-first Search") ? 1 : 0);
		newQuestion.setBreadthfirstSearch(tagsSet.contains("Breadth-first Search") ? 1 : 0);
		newQuestion.setUnionFind(tagsSet.contains("Union Find") ? 1 : 0);
		newQuestion.setGraph(tagsSet.contains("Graph") ? 1 : 0);
		newQuestion.setDesign(tagsSet.contains("Design") ? 1 : 0);
		newQuestion.setTopologicalSort(tagsSet.contains("Topological Sort") ? 1 : 0);
		newQuestion.setTrie(tagsSet.contains("Trie") ? 1 : 0);
		newQuestion.setBinaryIndexedTree(tagsSet.contains("Binary Indexed Tree") ? 1 : 0);
		newQuestion.setSegmentTree(tagsSet.contains("Segment Tree") ? 1 : 0);
		newQuestion.setBinarySearchTree(tagsSet.contains("Binary Search Tree") ? 1 : 0);
		newQuestion.setRecursion(tagsSet.contains("Recursion") ? 1 : 0);
		newQuestion.setMemoization(tagsSet.contains("Memoization") ? 1 : 0);
		newQuestion.setQueue(tagsSet.contains("Queue") ? 1 : 0);
		newQuestion.setMinimax(tagsSet.contains("Minimax") ? 1 : 0);
		newQuestion.setMap(tagsSet.contains("Map") ? 1 : 0);
		newQuestion.setRandom(tagsSet.contains("Random") ? 1 : 0);
		newQuestion.setSlidingWindow(tagsSet.contains("Sliding Window") ? 1 : 0);
		newQuestion.setSearching(tagsSet.contains("Searching") ? 1 : 0);
		
	}
	private static class SimilarQuestion {
		String url;
		String title;
		public SimilarQuestion(String url, String title) {
			super();
			this.url = url;
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		@Override
		public String toString() {
			return "SimilarQuestion [url=" + url + ", title=" + title + "]";
		}
		
		
	}
	private static List<SimilarQuestion> extractSimilarQuestion(WebDriver driver) {
		List<WebElement> similarQuestionTags  = driver.findElements(By.className("question__3owm"));
		List<SimilarQuestion> similarQuestions = new ArrayList<>();
		for(WebElement oneTag : similarQuestionTags) {
			String url = oneTag.findElement(By.tagName("a")).getAttribute("href");
			String title = oneTag.findElement(By.tagName("a")).getAttribute("textContent");
			similarQuestions.add(new SimilarQuestion(url, title));
		}
		return similarQuestions;
	}
	private static List<String> getQuestionTags(WebDriver driver) {
		List<WebElement> multipleTage  = driver.findElements(By.className("topic-tag__Hn49"));
		List<String> tags = new ArrayList<>();
		for(WebElement oneTag : multipleTage) {
			tags.add(oneTag.getAttribute("textContent"));
		}
		return tags;
	}
	private static String getExampleDescription(WebElement wb) {
		int exampleNumber = 1;
		StringBuffer examples = new StringBuffer();
		
		while(true) {
			if(wb.getText().indexOf("Example " + exampleNumber + 1) != -1) {
				examples.append(wb.getText().substring(wb.getText().indexOf("Example " + exampleNumber, wb.getText().indexOf("Example " + exampleNumber + 1))));
			} else {
				if(wb.getText().indexOf("Example " + exampleNumber) != -1) {
					examples.append(wb.getText().substring(wb.getText().indexOf("Example " + exampleNumber)));
				} else {
					if(wb.getText().indexOf("Example:" + exampleNumber) != -1) {
						examples.append(wb.getText().substring(wb.getText().indexOf("Example:")));
					} else break;
				}
				break;
			}
		}
		return examples.toString();
	}
	private static String getQuestionContent(WebElement wb) {
		String questionContent = "";
		if(wb.getText().indexOf("Example") != -1) {
			questionContent = wb.getText().substring(0, wb.getText().indexOf("Example"));
		} else {
			questionContent = wb.getText();
		}
		return questionContent;
	}
	public static WebElement fluentWait(final By locator, WebDriver driver) {
		//login();
	    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(5, TimeUnit.SECONDS)
	            .pollingEvery(1, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class);

	    WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
	        public WebElement apply(WebDriver driver) {
	            return driver.findElement(locator);
	        }
	    });

	    return  foo;
	};
	static String makeQuestionURL(String questionTitle) {
		return HOME_PAGE_URL + "/problems/" + questionTitle;
	}
	private static String fetchPage(String url) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Cookie", "csrftoken=" + csrftoken + ";LEETCODE_SESSION=" + leetcodeSessionID);
        CloseableHttpResponse response = getWithoutAutoRedirect(url, headers);
        try {
            return fetchWebpage(response);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	public static String fetchWebpage(CloseableHttpResponse response) {
        String html = null;
        try {
            ContentType ct = ContentType.getOrDefault(response.getEntity());
            String charset = "UTF-8";
            if (ct.getCharset() != null && !charset.equals(ct.getCharset().name())) {
                charset = ct.getCharset().name();
            }
            html = EntityUtils.toString(response.getEntity(), charset);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }
	public static CloseableHttpResponse getWithoutAutoRedirect(String url, Map<String, String> headers) {
        if (url == null) {
            return null;
        }
        HttpGet get = new HttpGet(url);
        addHeaders(get, headers);
        return visit(get, false);
    }

	private static boolean login() {
		Map<String, String> headers = new HashMap<String, String>();
        headers.put("Referer", HOME_PAGE_URL);
        headers.put("Cookie", "csrftoken=" + csrftoken);
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", "jai_hanuman");
        params.put("password", "Abc@1234");
        params.put("csrfmiddlewaretoken", csrftoken);
        CloseableHttpResponse response = post(LOGIN_PAGE_URL, headers, params);
        try {
            if (response.getStatusLine().getStatusCode() == 302) {
                for (Header header : response.getHeaders("Set-Cookie")) {
                    for (HeaderElement element : header.getElements()) {
                        if (element.getName() != null && element.getName().equals("csrftoken")) {
                            csrftoken = element.getValue();
                        } else if (element.getName() != null && element.getName().equals("LEETCODE_SESSION")) {
                            leetcodeSessionID = element.getValue();
                        }
                    }
                }
    
                return true;
            }
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                
                e.printStackTrace();
            }
        }
        
        return false;
        
	}
	public static CloseableHttpResponse post(String url, Map<String, String> headers, Map<String, String> params) {
        if (url == null) {
            return null;
        }
        HttpPost post = preparePostMethod(url, params);
        addHeaders(post, headers);
        return visit(post, true);
    }
	private static HttpPost preparePostMethod(String url, Map<String, String> params) {
        HttpPost post = new HttpPost(url);
        if (params != null && params.size() > 0) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                pairs.add(new BasicNameValuePair(key, params.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(pairs, Charset.forName("UTF-8")));
        }
        return post;
    }
	private static void addHeaders(HttpUriRequest request, Map<String, String> headers) {
        if (headers == null) {
            return;
        }
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            request.addHeader(key, headers.get(key));
        }
    }
	private static CloseableHttpResponse visit(HttpUriRequest request, boolean autoRedirect) {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient client = null;
            if (autoRedirect) {
                client = HttpClients.createDefault();
            } else {
                client = HttpClients.custom().disableRedirectHandling().build();
            }
            response = client.execute(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	
}
