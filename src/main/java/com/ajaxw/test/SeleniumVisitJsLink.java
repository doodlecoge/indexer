package com.ajaxw.test;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.internal.seleniumemulation.GetEval;

public class SeleniumVisitJsLink {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WebDriver driver = new HtmlUnitDriver(true);
//		WebDriver driver = new FirefoxDriver();
		driver.navigate().to("http://scst.suda.edu.cn/File.aspx?id=17");
		List<WebElement> links = driver.findElements(By.tagName("a"));

		for (WebElement link : links) {
			if (link.getText().equalsIgnoreCase("2")) {
				link.click();
				break;
			}
		}

		links = driver.findElements(By.tagName("a"));
		for (WebElement link : links) {
			if (link.getText().equalsIgnoreCase("2")) {
				String href = link.getAttribute("href");
				href = href.substring(href.indexOf(":") + 1);
				System.out.println(href);
				break;
			}
		}
	}

}
