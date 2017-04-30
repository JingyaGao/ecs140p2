public class CToken {
    public int linenum = 0;
    public int charnum = 0;
    public String type = "";
    public String token = "";
    //constructor
    public CToken(int l, int c, String tokenType, String tokenName) {
        linenum = l;
        charnum = c;
        type = tokenType;
        token = tokenName;
    }
    
    public void print() {
    	System.out.printf("@%4d,%4d%14s \"%s\"%n", this.linenum, this.charnum, this.type, this.token);
    }
    
}
