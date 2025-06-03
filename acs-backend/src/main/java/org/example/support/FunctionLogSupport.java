package org.example.support;

public final class FunctionLogSupport {

    public static void start(String functionName) {
    	System.out.println("");
    	System.out.println(" ---------------------------------- " + functionName + " Start ----------------------------------");
    }
    
    public static void end(String functionName) {
    	System.out.println(" ---------------------------------- " + functionName + " End ----------------------------------");
    	System.out.println("");
    }

    private FunctionLogSupport() {
        super();
    }

}
