
import java.math.BigInteger;


/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 



public class KnockKnockProtocol {
    private static final int WAITING = 0;
    private static final int SENTKNOCKKNOCK = 1;
    private static final int SENTCLUE = 2;
    private static final int ANOTHER = 3;
    private static final int READY = 5; 

    private static final int NUMJOKES = 5;

    private int state = WAITING;
    private int currentJoke = 0;
    private String newline = System.getProperty("line.separator");

    private String[] clues = { "Turnip", "Little Old Lady", "Atch", "Who", "Who" };
    private String[] answers = { "Turnip the heat, it's cold in here!",
                                 "I didn't know you could yodel!",
                                 "Bless you!",
                                 "Is there an owl in here?",
                                 "Is there an echo in here?" };

    public String processInput(String theInput) {
        String theOutput = null;
        
        if(state == WAITING){
            theOutput = "Please enter a positive integer greater than 2";
            state = READY;
        }
        else if(state == READY && !theInput.equals("Bye")){
            BigInteger bint = null; 
            try{
               bint = new BigInteger(theInput);
            }
            catch(NumberFormatException e){
             theOutput = "BAAAD input man";    
            }
             
            if(bint != null){
                if(bint.isProbablePrime(5)){
               //theOutput = theInput + " is a prime number" +newline+"Previous prime: "+findPrev(bint)+newline+"Next prime prime: "+bint.nextProbablePrime();
               theOutput = theInput + " is a prime number" +"   "+"Previous prime: "+findPrev(bint)+"   "+"Next prime prime: "+bint.nextProbablePrime();        
                       
            }
                else{
                    //theOutput = theInput + " is not a prime number" +newline+"Previous prime: "+findPrev(bint)+newline+"Next prime prime: "+bint.nextProbablePrime();
                    theOutput = theInput + " is not a prime number" +"   "+"Previous prime: "+findPrev(bint)+"  "+"Next prime prime: "+bint.nextProbablePrime();
                } 
            }
              
            
        }
        else if(theInput.equals("Bye")){
            theOutput = "Bye.";
        }
        return theOutput;
       
    }
    
    private static BigInteger findPrev(BigInteger bint){
        BigInteger current = bint.subtract(new BigInteger("1")); 
        while(!current.isProbablePrime(5)){
            current = current.subtract(new BigInteger("1"));
                    
        }
        return current;
    }
}