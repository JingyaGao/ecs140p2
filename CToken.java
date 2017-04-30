public class CToken {
    public int lnum = 0;
    public int cnum = 0;
    public String toktype = "";
    public String toktoken = "";
    //constructor
    public CToken(int l, int c, String tokenType, String tokenName) {
        lnum = l;
        cnum = c;
        toktype = tokenType;
        toktoken = tokenName;
    }
    
    //copy constructor for prev CToken object
    public CToken(CToken other) {   
        this.lnum = other.lnum;
        this.cnum = other.cnum;
        this.toktype = other.toktype;
        this.toktoken = other.toktoken;
    }
    
    public void set(int l, int c, String tokenType, String tokenName){
    	lnum = l;
        cnum = c;
        toktype = tokenType;
        toktoken = tokenName;
    }
    
    public void print() {
    	System.out.printf("@%4d,%4d%14s \"%s\"%n", lnum, cnum, toktype, toktoken);
    }
    
}
