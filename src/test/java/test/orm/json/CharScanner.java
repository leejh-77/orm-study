package test.orm.json;

public class CharScanner {

    private int index;
    private String str;

    public CharScanner(String str) {
        this.str = str;
    }

    public char read() {
        return this.str.charAt(this.index++);
    }

    public String readUntil(char... until) {
        StringBuilder sb = new StringBuilder();
        loop: while (true) {
            char c = this.read();
            for (char u : until) {
                if (c == u) {
                    break loop;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
