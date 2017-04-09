package compiler488.ast.stmt;

public class Loop {
    public Short startAddr;
    public boolean isLoop;
    public int arLength;

    public Loop(Short startAddr, int arLength, boolean isLoop) {
        this.startAddr = startAddr;
        this.arLength = arLength;
        this.isLoop = isLoop;
    }

    public String toString() {
        return "<" + arLength + " " + isLoop + ">";
    }
}
