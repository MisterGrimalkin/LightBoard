#include <time.h>
#include <stdio.h>
#include <wiringPi.h>
#include <jni.h>
#include <stdlib.h>
#include <stdbool.h>
#include "net_amarantha_lightboard_board_CLightBoard.h"

#define CHECK_BIT(var,pos) ((var) & (1<<(pos)))

int rows = 0;
int cols = 0;

bool currentFrame[3][32][192];
bool nextFrame[3][32][192];

int clockPin = 0;
int store = 1;
int output = 2;
int data1R = 3;
int data2R = 4;
int data1G = 5;
int data2G = 6;
int addr0 = 21;
int addr1 = 22;
int addr2 = 23;
int addr3 = 24;

bool paused = false;

void pushTestPattern() {
    int r;
    int c;
    for ( r=0; r<rows; r++ ) {
        for ( c=0; c<cols; c++ ) {
            if ( c%4==0 || r%4==0 ) {
                currentFrame[0][r][c] = false;
                currentFrame[1][r][c] = false;
            } else {
                currentFrame[0][r][c] = true;
                currentFrame[1][r][c] = true;
            }
        }
    }
}

int smallDelay = 18;
int largeDelay = 16;

void doDelay(int us) {
   int i=0;
   for ( i=0; i<us; i++ ) {
       struct timespec tim, tim2;
       tim.tv_sec = 0;
       tim.tv_nsec = 1;
   }
//   nanosleep(&tim , &tim2);
//    TimeSpec ts = const struct timespec[]){{0, us}};
//    nanosleep((const struct timespec[]){{0, us}}, NULL);
}

void sendSerialString(bool red1[], bool green1[], bool red2[], bool green2[]) {
    int col;
    for (col = 0; col < cols ; col++) {

        doDelay(largeDelay);

            digitalWrite(clockPin, LOW);

        doDelay(smallDelay);

            digitalWrite(data1R, red1[col] );
        doDelay(smallDelay);
            digitalWrite(data1G, green1[col] );
        doDelay(smallDelay);
            digitalWrite(data2R, red2[col] );
        doDelay(smallDelay);
            digitalWrite(data2G, green2[col] );

        doDelay(largeDelay);

            digitalWrite(clockPin, HIGH);

        doDelay(largeDelay);
    }
}

void decodeRowAddress(int row) {
    doDelay(smallDelay);
    digitalWrite(addr0, CHECK_BIT(row, 0)!=0);
    doDelay(smallDelay);
    digitalWrite(addr1, CHECK_BIT(row, 1)!=0);
    doDelay(smallDelay);
    digitalWrite(addr2, CHECK_BIT(row, 2)!=0);
    doDelay(smallDelay);
    digitalWrite(addr3, CHECK_BIT(row, 3)!=0);
    doDelay(largeDelay);
}

void push() {
//    if ( !paused ) {
        int row;
        for (row = 0; row < rows/2; row++) {
            sendSerialString(currentFrame[0][row], currentFrame[1][row], currentFrame[0][row + rows / 2], currentFrame[1][row + rows / 2]);

                doDelay(smallDelay);
            digitalWrite(output, HIGH);
                doDelay(largeDelay);
            digitalWrite(store, LOW);
                doDelay(largeDelay);
            decodeRowAddress(row);
                doDelay(largeDelay);
            digitalWrite(store, HIGH);
                doDelay(largeDelay);
            digitalWrite(output, LOW);
                doDelay(smallDelay);
        }
        doDelay(largeDelay);
//    }
}

void init(int r, int c) {

    rows = r;
    cols = c;

    printf("Starting C RaspPi LightBoard 2....\n");
    printf("%dx%d\n", rows, cols);

    pushTestPattern();

    wiringPiSetup() ;

    pinMode(clockPin, OUTPUT);
    pinMode(store, OUTPUT);
    pinMode(output, OUTPUT);
    pinMode(data1R, OUTPUT);
    pinMode(data2R, OUTPUT);
    pinMode(data1G, OUTPUT);
    pinMode(data2G, OUTPUT);
    pinMode(addr0, OUTPUT);
    pinMode(addr1, OUTPUT);
    pinMode(addr2, OUTPUT);
    pinMode(addr3, OUTPUT);

    printf("Board Running\n");

    for ( ;; ) {
        push();
    }

}

void clearBoard() {
    int r,c;
    for ( r=0; r<rows; r++ ) {
        for ( c=0; c<cols; c++ ) {
            currentFrame[0][r][c] = true;
            currentFrame[1][r][c] = true;
        }
    }
    push();
}

/////////
// JNI //
/////////

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_initNative
  (JNIEnv *env, jobject o, jint r, jint c) {
    init(r, c);
  }

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_setPoint
  (JNIEnv *env, jobject o, jint r, jint c, jboolean red, jboolean green) {
    if ( !paused ) {
        currentFrame[0][(int)r][(int)c] = !(bool)red;
        currentFrame[1][(int)r][(int)c] = !(bool)green;
//        nextFrame[0][(int)r][(int)c] = !(bool)red;
//        nextFrame[1][(int)r][(int)c] = !(bool)green;
//        if ( r==rows-1 && c==cols-1 ) {
//            paused = true;
//            int r2 = 0;
//            int c2 = 0;
//            for ( r2=0; r2<rows; r2++ ) {
//                for ( c2=0; c2<cols; c2++ ) {
//                    currentFrame[0][r2][c2] = nextFrame[0][r2][c2];
//                    currentFrame[1][r2][c2] = nextFrame[1][r2][c2];
//                }
//            }
//            paused = false;
//        }
    }
  }

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_sleep
  (JNIEnv *env, jobject o) {
    paused = true;
//    clearBoard();
  }

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_wake
  (JNIEnv *env, jobject o) {
    paused = false;
  }

////////////////
// Test Start //
////////////////

int main (void) {
    printf("Starting LightBoard in Native Mode...\n");
    init(32, 192);
    return 0;
}
