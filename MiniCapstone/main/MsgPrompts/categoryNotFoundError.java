package main.MsgPrompts;

public class categoryNotFoundError extends mainPrompt{
    
    public String messagePrompt() {

        return super.messagePrompt() + " INVALID INPUT, NONE EXISTING CATEGORY!\n";
    }

}
