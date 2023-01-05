package main.MsgPrompts;

public class cannotAddSameProductError extends mainPrompt{
    
    public String messagePrompt() {

        return super.messagePrompt() + " CANNOT ADD SAME PRODUCT!\n";
    }

}
