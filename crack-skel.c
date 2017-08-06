#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <string.h>
#define TEXT_SIZE 200000
#define ALEN 26         // Number of chars in ENGLISH alphabet
#define CHFREQ "ETAONRISHDLFCMUGYPWBVKJXQZ" // Characters in order of appearance in English documents.
#define ALPHABET "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

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
  for(int ii = 0; ii < strlen(string); ii++){//I check all the letters of the string
    if(string[ii] >= 'A' && string[ii] <= 'Z'){//I check that it is a letter
      aux = string[ii] - 'A' ;//I calculate what letter is actually in the string
      counter[aux]++;//I add one to the position of the letter that I have read
    }
  }
}

/*It sets all the values of an int array to 0
*/
void resetIntArray(int* counter){
  for(int zz = 0; zz < ALEN; zz++){//I initialize the values of the array to avoid problems
    counter[zz] = 0;
  }
}

/*It receives a counter with the occurrences of the letters and a string
  and it order the alphabet letters in the string in order of maximum occurrence
*/
void orderArray(int* counter, char* letterOrder){
  int value = -1, pos = 0;//aux to know which letter is the maximum
  for(int ii = 0; ii < ALEN; ii++){//I iterate through all the possitions of the counter array
    for(int jj = 0; jj < 26; jj++){//I check all the letters
      if(value < counter[jj]){//There is a new maximum
        value = counter[jj];//I assign the new maximum value
        pos = jj;//I store the position of the new maximum
      }
    }
    counter[pos] = -1;//In order to not count it again in the next iteration
    letterOrder[ii] = 'A' + pos;//I store the most seen letter in the right place
    pos = 0;//I reset the pos variable
    value = -1;
  }
}

/*It changes the letters of the original string according to the occurrences of each letter
  letterOrder is a string with the letters ordered by occurrence
*/
void changeAction(char* string, char* letterOrder){
  char* aux = (char *)malloc(sizeof(char)* strlen(string) +1);//auxiliar string
  aux[strlen(string)] = '\0';
  for(int ii = 0; ii < strlen(string); ii++){//I copy the characters that are not letters
    if(string[ii]< 'A' || string[ii] > 'Z'){
      aux[ii] = string[ii];
    }
  }
  for(int ii = 0; ii < ALEN; ii++){//I change all the letters of the alphabet`
    for(int jj = 0; jj < strlen(string); jj++){//I check all the letters of the string
      if(string[jj] == letterOrder[ii]){//It is the letter we want to change
        aux[jj] = CHFREQ[ii];//I copy the letter which is suposed to be the correct one into the aux string
      }
    }
  }
  strcpy(string,aux);//I copy the correct changed string to the original string
  free(aux);//I deallocate the memory
}

/*It changes the letters of an input string with the ones that are most common in English
  It receives an array of strings and the number of strings
*/
void change(char** array, int n){
  int counter[ALEN];//It is the counter of the occurrence of the letters
  char letterOrder[ALEN +1];//It is the string that will have the letters ordered by occurrence
  letterOrder[ALEN] = '\0';
  for(int ii = 0; ii < n; ii++){//I count the occurrences of each letter of each string
    resetIntArray(counter);//I set to 0 all the values of the counter
    count(array[ii], counter);//I call to the count method
    orderArray(counter, letterOrder);//I order the letterOrder string
    changeAction(array[ii],letterOrder);//I call to the method that actually change the letters of the string
  }
}

/*It makes the analysis of the text
*/
void decrypt (char* text, int n){
  int size = calculateSizeNeeded(text, n);//I calculate the space needed for each string
  char **array = malloc(sizeof(char*)*n);//I create the array of strings
  for (int i = 0; i < n; ++i) {
   	 array[i] = (char *)malloc(size+1);//Allocation of the needed memory for each string
	}
  split(array, text, n, size);//I split the text and store it into partial strings
  change(array, n);//I change the letters of the cipher text to the ones that should correspond in the plain text
  fprintf(stdout,"\n\t\tDecript text with %i keys\n", n);
  for(int zz = 0; zz < size; zz++){//I print the text in the correct order
    for(int ii = 0; ii < n; ii++){
      fprintf(stdout,"%c",array[ii][zz]);//I alternate the different substrings in the correct ways
    }   
  }
  free(array);//Deallocate memory
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
  for(i = 1; i <= n; i++){
    decrypt(text, i);
  }
  free(text);//Deallocate memory
}
