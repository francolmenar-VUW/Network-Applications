#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>

/* Program developed for NWEN243, Victoria University of Wellington
   Author: Kris Bubendorfer (c) 2015.
   Uses a modified caeser I & II

   Compile:  gcc -o encode encode.c

   // Make a file with a plain aplhabet in it:

   % cat > test
   ABCDEFGHIJKLMNOPQRSTUVWXYZ
   ^D

   // test it - this should be the correct result when encoding.

   % cat test | encode "i came, i saw"
   key: HJKLNOPQRTUVICAMESWXYZBDFG - 26
   HJKLNOPQRTUVICAMESWXYZBDFG

 */

char upcase(char ch){
  if(islower(ch))
    ch -= 'a' - 'A';
  return ch;
}

char* fixkey(char* s){
  int i, j;
  char plain[26]; // assume key < length of alphabet, local array on stack, will go away!

  for(i = 0, j = 0; i < strlen(s); i++){
    if(isalpha(s[i])){
      plain[j++] = upcase(s[i]);
    }
  }
  plain[j] = '\0'; 
  return strcpy(s, plain);
}

char* noRepeat (char* key){
	int boolean = 0, aux = 0;
char* check = (char*)malloc(sizeof(char)*strlen(key));// I allocate the maximun amount of memory that the aux array can have
 for(int i = 0; i < strlen(key); i++){// I go through all the key's letters
 boolean = 0;
  for(int j = 0; j < strlen(key); j++){// I check all the already checked letters
    if (key[i] == check[j]){//It is repeated
    boolean = 1;
    }
  }
  if(boolean == 0){//It is not repeated
  check[aux]=key[i]; //I assign the new letter to the correct key
  aux++;
  }
}
return strcpy(key, check);
}

void buildtable (char* key, char* encode){

  // This function needs to build an array of mappings in the 'encode' array from plaintext characters
  // to encypered characters.  The encode array will be indexed by the plaintext char.  To 
  // make this a useful 0-26 index for the array, 'A' will be stubtracted from it (yes you
  // can do this in C).  You can see this in the main(){} below.  The values in the array 
  // will be the cipher value, in the example at the top A -> H, B -> J, etc.

  // You are implementing a Caesar 1 & 2 combo Cypher as given in handout.
  // Your code here:
  
  // probably need to declare some stuff here!
  int keyLenght = strlen(key);
  int mod = 26; //It is the auxiliar variable in order to perform the modular operations
  int aux, boole = 0; //auxiliar
  printf("The key is %s and the length is %i\n", key, keyLenght);
  
  fixkey(key); // fix the key, i.e., uppercase and remove whitespace and punctuation
  printf("The fixed key is %s\n", key);
  
  noRepeat(key);
  printf("The fixed key without repeated letters is %s\n", key);
  
  for (int i = 0; i < 26; i++){//loop to set the plaintext letters to the cipher ones
  
  if(i == keyLenght-1 ){// We have to place the key here
  	for (int z = 0; z < strlen(key); z++){ //I iterate all the possitions of the new key
  	encode[i+z] = key[z];// I copy the key
  	}
  	printf("Z loop has finished\n");
  	i += strlen(key) ;
  	boole = 1;
  }
  else if(boole == 1) {//I have to check what letter are already 
     	printf("position %i\n", i);
     	int are = 0;
     	for(int z = 0; z < i; z++){
     	if(encode[i-1]+1 == encode[z]){
     	are = 1;
     	}
     	}
     	if (are == 0){
     		aux = (encode[i-1] +1);
     		printf("put after key the position %i with encode %i\n", aux, encode[i-1]);
      encode[i] = aux;// I add the offset to the current letter
     	}
  }
  else{
  aux = (keyLenght + i) % mod; //module operation
  encode[i] = 'A' + aux;// I add the offset to the current letter
  }
  }
 
 
  // Do some stuff here to make a translation between plain and cypher maps.
  
}

int main(int argc, char **argv){
  // format will be: 'program' key {encode|decode}
  // We'll be using stdin and stdout for files to encode and decode.

  // first allocate some space for our translation table.

  char* encode = (char*)malloc(sizeof(char)*26);
  char ch;

  if(argc != 2){
    printf("format is: '%s' key", argv[0]);
    exit(1);
  }

  // Build translation tables, and ensure key is upcased and alpha chars only.

  buildtable(argv[1], encode); // argv[1] is the key 

  // write the key to stderr (so it doesn't break our pipes)

  fprintf(stderr,"key: %s - %d\n", encode, strlen(encode));

  // the following code does the translations.  Characters are read 
  // one-by-one from stdin, translated and written to stdout.

  ch = fgetc(stdin);
  while (!feof(stdin)) {
    if(isalpha(ch)){        // only encrypt alpha chars
      ch = upcase(ch);      // make it uppercase
      fputc(encode[ch-'A'], stdout);
    }else 
      fputc(ch, stdout);
    ch = fgetc(stdin);      // get next char from stdin
  }
  return 0;
}
  
