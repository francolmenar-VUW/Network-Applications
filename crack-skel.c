#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <string.h>
#define TEXT_SIZE 200000  // Note, the longer the text the more likely you will get a good 'decode' from the start.
#define ALEN 26         // Number of chars in ENGLISH alphabet
#define CHFREQ "ETAONRISHDLFCMUGYPWBVKJXQZ" // Characters in order of appearance in English documents.
#define ALPHABET "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

/* Program developed for NWEN243, Victoria University of Wellington
   Author: Kris Bubendorfer, this extended version (c) 2015
   LAB: 2

   This program applies a basic frequency analysis on a cyphertext.  It has been extened over the 2014 version to
   solve polyalphabetic cyphers - by brute force.  In this case, it applies the frequency analysis for different
   numbers of n keys (polyalphabetic Caeser).  Obviously it will need a cypher of about n times
   the typical length for a monoalphabetic cypher.

   Program is used like this:

   Compile:  gcc -o crack crack.c

   Test file (ctext): JWRLS, XSSH PZK JH HES BJFV, UZU (this is not a realistic length piece of cypher text)

   crack n

   Argument:

   n number of keys to try

   ---

   % cat ctext | crack 1
   ALICE, MEET YOU AT THE PARK, BOB   <-- of course it won't be this correct.  Don't worry about that for the -d option.
   AMFDE, UEET LNH AT TIE RASC, ONO   <-- this is what it really looks like, a larger sample is better, this is short.


 */

char upcase(char ch){
  if(islower(ch))
    ch -= 'a' - 'A';
  return ch;
}

/*Calculate the space needed to create n strings using an input string
  It returns the size that all the strings need at most
*/
int calculateSizeNeeded(char* sentence, int n){
  if(strlen(sentence) % n == 0) return (strlen(sentence) / n) + 1;//The +1 is for the "/0" at the end of the string
  else return (int) (strlen(sentence) / n) + 2;//The +2 is for the "/0" at the end of the string and for the extra caracter that can be at each string
}

/*It splits an input string into n substrings alternating the leters
  It is returned an n array with all the substrings
*/
//I have to check when there is more keys than the possible amount in the real world
void split(char** array, char* sentence, int n, int size){
  int aux = 0;//It is an auxiliar used to go through the string
  for(int ii = 0; ii < size-1; ii++){//size -1 because of the "\0 char. I go through the string
    for(int jj = 0; jj < n; jj++){//I iterate through all the strings
      array[jj][ii] = sentence[aux];//I copy a letter of the input string into the correct one
      aux++;
    }
  }
  for(int ii = 0; ii < n; ii++){//I copy in the last position of the strings the end of string symbol
    array[ii][size-1] = '\0';
  }
}

/*It counts the occurrences of each letter of the input string
*/
void count(char* string, int* counter){
  int aux;
  printf("\n\tCount method\n");
  for(int ii = 0; ii < strlen(string); ii++){//I check all the letters of the string
    aux = string[ii] - 'A' ;//I calculate what letter is actually in the string
    //printf("I'm going to add 1 to pos %i because of the char %c\n",aux,string[ii] );
    counter[aux]++;//I add one to the position of the letter that I have read
    //printf("Value of position %i of the counter array is %i\n",aux,counter[aux]);
  }
}

void resetIntArray(int* counter){
  for(int zz = 0; zz < 26; zz++){//I initialize the values of the array to avoid problems
  counter[zz] = 0;
  }
}

/*It receives a counter with the occurrences of the letters and a string
  and it order the alphabet letters in the string in order of maximum occurrence
*/
void orderArray(int* counter, char* letterOrder){
  int value = 0, pos = 0;//aux to know which letter is the maximum
  for(int ii = 0; ii < 26; ii++){//I iterate through all the possitions of the counter array
    for(int jj = 0; jj < 26; jj++){
      if(value < counter[jj]){//There is a new maximum
        value = counter[jj];//I assign the new maximum value
        pos = jj;//I store the position of the new maximum
      }
    }
    counter[pos] = -1;//In order to not count it again in the next iteration
    letterOrder[ii] = 'A' + pos;//I store the most seen letter in the right place
    pos = 0;//I reset the pos variable
  }
}

/*It changes the letters of the original string according to the occurrences of each letter
  letterOrder is a string with the letters ordered by occurrence
*/
void changeAction(char* string, char* letterOrder){
  char* aux = (char *)malloc(sizeof(char)* strlen(string));//auxiliar string
  for(int ii = 0; ii < 26; ii++){//I change all the letters of the alphabet`
    for(int jj = 0; jj < strlen(string); jj++){//I check all the letters of the string`
      if(string[jj] == letterOrder[ii]){//It is the letter we want to change`
        aux[jj] = CHFREQ[ii];//I copy the letter which is suposed to be the correct one into the aux string
      }
    }
  }
  strcpy(string,aux);//I copy the correct changed string to the original string
}

void intToString(int* integer){
  for(int zz = 0; zz < 26; zz++){
    printf("%i, ", integer[zz]);
  }
  printf("\n");
}

/*It changes the letters of an input string with the ones that are most common in English
  It receives an array of strings and the number of strings
*/
void change(char** array, int n){
	int counter[26];//It is the counter of the occurrence of the letters
  char letterOrder[26];//It is the string that will have the letters ordered by occurrence
  for(int ii = 0; ii < n; ii++){//I count the occurrences of each letter of each string
    resetIntArray(counter);//I set to 0 all the values of the counter
    count(array[ii], counter);//I call to the count method
    printf("Counter (after) in iteration %i is:\n",ii);
    intToString(counter);
    orderArray(counter, letterOrder);//I order the letterOrder string
    changeAction(array[ii],letterOrder);//I call to the method that actually change the letters of the string
  }
}



int main(int argc, char **argv){

  // first allocate some space for our input text (we will read from stdin).

  char* text = (char*)malloc(sizeof(char)*TEXT_SIZE+1);
  char ch;
  int n, i;//n is the number of keys4524

  if(argc > 1 && (n = atoi(argv[1])) > 0); else{ fprintf(stderr,"Malformed argument, use: crack [n], n > 0\n"); exit(-1);} // get the command line argument n

  // Now read TEXT_SIZE or feof worth of characters (whichever is smaller) and convert to uppercase as we do it.
  // Added: changed to count frequencies as we read it in

  for(i = 0, ch = fgetc(stdin); i < TEXT_SIZE && !feof(stdin); i++, ch = fgetc(stdin)){
    text[i] = (ch = (isalpha(ch)?upcase(ch):ch));
  }
  text[i] = '\0'; // terminate the string properly.

  /* At this point we have two things,
   *   1. The input cyphertext in "text"
   *   2. The maximum number of keys to try (n) - we'll be trying 1..n keys.
   *
   * What you need to do is as follows:
   *   1. create a for-loop that will check key lengths from 1..n
   *   2. for each i <= n, spit the cypher text into i sub-texts.  For i = 1, 1 subtext, for i = 2, 2 subtexts, of alternating characters etc.
   *   3. for each subtext:
   *          a. count the occurance of each letter
   *          b. then map this onto the CHFREQ, to create a map between the sub-text and english
   *          c. apply the new map to the subtext
   *   4. merge the subtexts
   *   5. output the 'possibly' partially decoded text to stdout.  This will only look OK if i was the correct number of keys
   *
   * what you need to output (sample will be provided) - exactly:
   * i maps -> stderr
   * i 'possible' translations
   *
   * You would be wise to make seperate functions that perform various sub-tasks, and test them incrementally.  Any other approach will likely
   * make your brain revolt.  This isn't a long program, mine is 160 lines, with comments (and written in a very verbose style) - if yours is
   * getting too long, double check you're on the right track.
   *
   */

  // Your code here...

  int size = calculateSizeNeeded(text, n);//I calculate the space needed for each string
  char **array = malloc(sizeof(char*)*n);//I create the array of strings
  for (i = 0; i < n; ++i) {
   	 array[i] = (char *)malloc(size+1);//Allocation of the needed memory for each string
	}
  split(array, text, n, size);//I split the text and store it into partial strings
  change(array, n);//I change the letters of the cipher text to the ones that should correspond in the plain text
}
