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
/*char* noRepeat (char* key){
void printLetters(char* word, char letter){
printf("The character received is: %c\nThe next character in the alphabet is: %c\n",word[0],word[0+1]+1);
}

  /* This function needs to build an array of mappings in the 'encode' array from plaintext characters 
void buildtable (char* key, char* encode){
	//printLetters("hola",'c');
  int keyLenght = strlen(key);//The length of the key
  int mod = 26; //It is the auxiliar variable in order to perform the modular operations
  int aux;
  int boole = 0; //boolean to know if the key has been copied or not
  printf("The key is %s and the length is %i\n", key, keyLenght);
  
  fixkey(key); // fix the key, i.e., uppercase and remove whitespace and punctuation
  //printf("The fixed key is %s\n", key);
  
  char* check = (char*)malloc(sizeof(char)*strlen(key));// I allocate the maximum amount of memory that the string can have
  strcpy(check, noRepeat(key));
  //printf("The fixed key without repeated letters is %s\n", check);
  
  for (int i = 0; i < 26; i++){//loop to set the plaintext letters to the cipher ones
  		if(i == keyLenght-1 ){// We have to place the key without repeated letters here
  			for (int z = 0; z < strlen(check); z++){ //I iterate all the positions of the new key
  				encode[i+z] = check[z];// I copy the key
  			}
  			//printf("Z loop has finished\n");
  			i += strlen(check) ;//I sum the length of the new key in order to know in which position of the decode string I am
  			boole = 1;//boolean to true
  		}
  		else if(boole == 1) {//The key has been copied
     	//printf("position %i\n", i);
	     	
 		for(int z = 1; z < 27; z++){
 			printf("%c ",encode[z]);
 		}
 		
     	printf("\n");
     	for(int z = 1; z < 27; z++){
     		if( (strchr(encode,encode[i-1]+z)) == NULL ){//The next letter is not in the encode string already
     				printf("The actual %i character is:%c\tand the previous one is: %c\n",i,encode[i-2],encode[i-3]);
					printf("Letter %c is introduced in the encoded table\n", encode[i-1]+1);
					encode[i-1] = encode[i-2]+z;
					z = 27;
     		}
     	}
  		}
  		else{//Before inserting the key
  			aux = (keyLenght + i) % mod; //module operation
  			encode[i] = 'A' + aux;// I add the offset to the current letter
  		}
  }
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
