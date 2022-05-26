import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.util.List;

public class MessageManagement {

    public static final int  SENT = 0,DELIVER=1;


    private boolean isSent;
    private WebElement lastMessage;
    private int messageStatus;
    private boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public boolean isSent() {
        return this.isSent;
    }

    public ChromeDriver sendMessage(ChromeDriver driver, String text) {
        WebElement footerTextBox = null;
        try {
            footerTextBox = driver.findElement(By.tagName("footer"));
            WebElement textBox = footerTextBox.findElement(By.cssSelector("div[role='textbox']"));
            textBox.sendKeys(text);
            footerTextBox.findElement(By.cssSelector("button[class='tvf2evcx oq44ahr5 lb5m6g5c svlsagor p2rjqpw5 epia9gcq']")).click();
            this.isSent=true;
        } catch (Exception e) {
            sendMessage(driver, text);
        }
        return driver;
    }

    public WebElement getLastMessage(ChromeDriver driver) {
        new Thread(() -> {
            try {

                while (!this.isSent) {

                    Thread.sleep(1000);
                }

                WebElement chat = driver.findElement(By.className("_33LGR"));
                WebElement chatBody = chat.findElement(By.cssSelector("div[tabindex='-1'][class='_3K4-L']"));
                List<WebElement> allMessage = chatBody.findElements(By.className("_22Msk"));
                this.lastMessage = allMessage.get(allMessage.size() - 1);

            } catch (Exception e) {
                getLastMessage(driver);
            }
        }).start();
        return this.lastMessage;
    }

    public void messageStatus() {
        new Thread(() -> {
            WebElement messageStatus = null;
            try {
                String status;
                while (!this.isSent) {

                    Thread.sleep(1000);
                }
                do {
                    messageStatus = this.lastMessage.findElement(By.cssSelector("span[data-testid='msg-dblcheck']"));
                    status = messageStatus.getAttribute("aria-label");
                    if (status.equals(" נמסרה ")) {
                        this.messageStatus = DELIVER;
                    }
                } while (!status.equals(" נקראה "));
                System.out.println("5");
                this.isRead=true;
            } catch (Exception e) {
                messageStatus();
            }
        }).start();

    }
}
