package main.MsgPrompts;

public class idNotFoundError extends mainPrompt{
    
    public String messagePrompt() {

        return super.messagePrompt() + " INVALID INPUT, NO ID FOUND!\n";
    }

}
