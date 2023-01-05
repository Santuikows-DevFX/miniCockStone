package main.MsgPrompts;

public class invalidQuantityValueError extends mainPrompt {

    public String messagePrompt() {

        return super.messagePrompt() + " INVALID OPERATION, DEDUCT QNT > PRD QNT!\n";
    }
    
}
