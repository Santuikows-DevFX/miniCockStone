package main.MsgPrompts;

public class GeneralerrorPrompt extends mainPrompt {

    public String messagePrompt() {

        return super.messagePrompt() + " INVALD INPUT, TRY AGAIN!\n";
    }
    
}
