package viewControler;
public class Command {
    public final Operator operator;

    public Command (String text) {
        String input = text.substring(0,2);
        switch (input) {
            case ":1":
                this.operator=Operator.INSERT;
                break;
            case ":2":
                this.operator= Operator.REMOVE;
                break;
            case ":3":
                this.operator=Operator.READ;
                break;
            case ":4":
                this.operator=Operator.changeAccessCount;
                break;
            case ":5":
                this.operator=Operator.QUIT;
                break;
            default:
                this.operator=Operator.ERROR;
                break;
        }
    }
}
