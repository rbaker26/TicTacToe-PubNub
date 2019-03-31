public class MoveData {
    public enum TestEnum {
        Value1, Value2
    }

    public int row;
    public int column;
    public String sender;
    public TestEnum eVal;

    @Override
    public String toString() {
        return "MoveData{" +
                "row=" + row +
                ", column=" + column +
                ", sender='" + sender + '\'' +
                ", eVal=" + eVal +
                '}';
    }
}
