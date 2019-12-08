package client.selenium;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class clientSeleniumTest {
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.gecko.driver","client/selenium/geckodriver");
        driver = new FirefoxDriver();
        driver.get("http://localhost:3000/");

        //Make Sure Webpage is started and available at localhost:3000
        //You also need to make sure Server is running and available for Connections
    }

    @Test
    public void test0LoginFailure() throws InterruptedException {
        //Test that you can Login and all text is properly displayed
        //Check that the title of the Application is correct
        Assert.assertEquals(driver.getTitle(), "Jungle App");

        WebElement userText = driver.findElement(By.cssSelector("input:nth-child(1)"));
        WebElement passwordText = driver.findElement(By.cssSelector("input:nth-child(2)"));
        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));

        userText.sendKeys("the_devil_himself");
        passwordText.sendKeys("WRONG_PASSWORD");

        submitButton.click();
        Thread.sleep(1500); //Give time for page to load

        //User should NOT be logged in and there should be an Error Message displayed
        Assert.assertTrue(driver.getPageSource().contains("Error: Incorrect username or password"));
    }

    @Test
    public void test1LoginSuccess() throws InterruptedException {
        //Test that you can Login and all text is properly displayed
        //Check that the title of the Application is correct
        Assert.assertEquals(driver.getTitle(), "Jungle App");

        WebElement userText = driver.findElement(By.cssSelector("input:nth-child(1)"));
        WebElement passwordText = driver.findElement(By.cssSelector("input:nth-child(2)"));
        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));

        userText.clear();
        userText.sendKeys("the_devil_himself");

        passwordText.clear();
        passwordText.sendKeys("666");

        submitButton.click();
        Thread.sleep(1500); //Give time for page to load

        //User should now be logged in and at the Welcome Page
        Assert.assertTrue(driver.getPageSource().contains("Welcome to the Online Jungle Game!"));
    }

    @Test
    public void test2GameRulesPage() {
        WebElement gameRulesTab = driver.findElement(By.id("react-tabs-4"));
        gameRulesTab.click();

        //Check that all GameRules text and Images are being displayed properly
        Assert.assertTrue(driver.getPageSource().contains("Rules of the Game of Jungle"));
        Assert.assertTrue(driver.getPageSource().contains("Object of the Game"));
        Assert.assertTrue(driver.getPageSource().contains("Movement of the Pieces"));
        Assert.assertTrue(driver.getPageSource().contains("Captures"));
        Assert.assertTrue(driver.getPageSource().contains("The Traps"));
        Assert.assertTrue(driver.getPageSource().contains("The Den"));
        //Check that all Images are being displayed properly
        List<WebElement> imageList = driver.findElements(By.cssSelector("img"));
        Assert.assertEquals(imageList.size(), 11);
    }

    @Test
    public void test2CreateGameInvite() throws InterruptedException{
        WebElement gameInviteTab = driver.findElement(By.id("react-tabs-8"));
        gameInviteTab.click();

        //Check that the Invite page is displayed properly
        Assert.assertTrue(driver.getPageSource().contains("Username of player to invite:"));

        //Cancel any pending invites so that all new invites will be successful
        List<WebElement> cancelButtonList = driver.findElements(By.xpath("//button[contains(.,'Cancel')]"));
        for(WebElement e : cancelButtonList) {
            e.click();
        }

        WebElement userToInviteTextBox = driver.findElement(By.cssSelector("input:nth-child(1)"));
        userToInviteTextBox.sendKeys("dummy_user");

        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));
        submitButton.click();

        Thread.sleep(3000); //Give time for page to update
        Assert.assertTrue(driver.getPageSource().contains("Invite was successful!"));
    }

    @Test
    public void test2GamesPage() {
        //This will test going to the Games page and loading a currently playing game
        WebElement gamesTab = driver.findElement(By.id("react-tabs-2"));
        gamesTab.click();

        Assert.assertTrue(driver.getPageSource().contains("Select a Game"));
        WebElement dropDownGameMenu = driver.findElement(By.id("dropdown-basic"));
        dropDownGameMenu.click();

        //Select a random in progress game
        WebElement randomGameToPlay = driver.findElement(By.xpath("//button[contains(.,'Game ID: ')]"));
        randomGameToPlay.click();

        Assert.assertTrue(driver.getPageSource().contains("Forfeit"));
        Assert.assertTrue(driver.getPageSource().contains("the_devil_himself"));
    }

    @Test
    public void test2GameHistoryPage() {
        WebElement gameHistoryTab = driver.findElement(By.id("react-tabs-6"));
        gameHistoryTab.click();

        //Check that the Game History page is displayed properly
        Assert.assertTrue(driver.getPageSource().contains("Game History"));
        Assert.assertTrue(driver.getPageSource().contains("Game ID"));
        Assert.assertTrue(driver.getPageSource().contains("Start time"));
        Assert.assertTrue(driver.getPageSource().contains("End time"));
        Assert.assertTrue(driver.getPageSource().contains("Player one"));
        Assert.assertTrue(driver.getPageSource().contains("Player two"));
        Assert.assertTrue(driver.getPageSource().contains("Winner"));
    }

    @Test
    public void test3UserProfilePageAndLogoutButton() throws InterruptedException {
        WebElement userProfilePage = driver.findElement(By.id("react-tabs-10"));
        userProfilePage.click();

        //Check that all GameRules text and Images are being displayed properly
        Assert.assertTrue(driver.getPageSource().contains("Welcome the_devil_himself!"));
        Assert.assertTrue(driver.getPageSource().contains("Although we'd love for you to stick around, you can manage your account here if you'd like to logout or unregister."));

        WebElement logoutButton = driver.findElement(By.xpath("//button[contains(.,'Logout')]"));
        logoutButton.click();

        Thread.sleep(1000); //Give time for page to update
        //You should now be logged out and back on Default Register page
        Assert.assertTrue(driver.getPageSource().contains("Login"));
        Assert.assertTrue(driver.getPageSource().contains("Register"));
        Assert.assertFalse(driver.getPageSource().contains("Game Rules"));
    }

    @Test
    public void test4LoginAsDummyUser() throws InterruptedException {
        WebElement loginTab = driver.findElement(By.xpath("//li[contains(.,'Login')]"));
        loginTab.click();

        WebElement userText = driver.findElement(By.cssSelector("input:nth-child(1)"));
        WebElement passwordText = driver.findElement(By.cssSelector("input:nth-child(2)"));
        WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));

        userText.clear();
        userText.sendKeys("dummy_user");

        passwordText.clear();
        passwordText.sendKeys("iforgot123");

        submitButton.click();
        Thread.sleep(2500); //Give time for page to load

        //User should now be logged in and at the Welcome Page
        Assert.assertTrue(driver.getPageSource().contains("Welcome to the Online Jungle Game!"));
    }

    @Test
    public void test5AcceptDevilHimselfInvite() throws InterruptedException{
        WebElement gameInviteTab = driver.findElement(By.id("react-tabs-8"));
        gameInviteTab.click();

        //Check that the Invite page is displayed properly
        Assert.assertTrue(driver.getPageSource().contains("Username of player to invite:"));
        //Check that devil_himself's invite has shown up
        Assert.assertTrue(driver.getPageSource().contains("the_devil_himself"));

        WebElement acceptInviteButton = driver.findElement(By.xpath("//button[contains(.,'Accept')]"));
        acceptInviteButton.click();

        Thread.sleep(5000);  //Wait for page to refresh
        //After accepting invite, the_devil_himself invite should no longer be present on this page
        Assert.assertFalse(driver.getPageSource().contains("the_devil_himself"));
    }

    @AfterClass
    public static void teardown() {
        driver.close();
    }
}