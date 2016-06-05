#include <time.h>
#include <stdio.h>
#include <wiringPi.h>
#include <jni.h>
#include<stdlib.h>
#include "net_amarantha_lightboard_board_CLightBoard.h"

#define CHECK_BIT(var,pos) ((var) & (1<<(pos)))

int rows = 0;
int cols = 0;

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

double currentFrame[3][32][192];
double nextFrame[3][32][192];

void pushTestPattern() {
    int r;
    int c;
    for ( r=0; r<rows; r++ ) {
        for ( c=0; c<cols; c++ ) {
            if ( c%4==0 || r%4==0 ) {
//                printf("#");
                currentFrame[0][r][c] = 1.0;
                currentFrame[1][r][c] = 1.0;
                currentFrame[2][r][c] = 1.0;
            } else {
//                printf("-");
                currentFrame[0][r][c] = 0.0;
                currentFrame[1][r][c] = 0.0;
                currentFrame[2][r][c] = 0.0;
            }
        }
//        printf("\n");
    }
}

void update(double data[3][32][192]) {
    printf("DUMP");
    int r;
    int c;
    for ( r=0; r<rows; r++ ) {
        for ( c=0; c<cols; c++ ) {
            currentFrame[0][r][c] = data[0][r][c];
            currentFrame[1][r][c] = data[1][r][c];
            currentFrame[2][r][c] = data[2][r][c];
        }
    }
}


void sendSerialString(double red1[], double green1[], double red2[], double green2[]) {
    struct timespec tim, tim2;
       tim.tv_sec = 0;
       tim.tv_nsec = 500;
   int col;
    for (col = 0; col < cols ; col++) {
        digitalWrite(clockPin, LOW);
        digitalWrite(data1R, red1[col]<0.5 );
        digitalWrite(data1G, green1[col]<0.5 );
        digitalWrite(data2R, red2[col]<0.5 );
        digitalWrite(data2G, green2[col]<0.5 );
//        nanosleep(&tim, &tim2);
        digitalWrite(clockPin, HIGH);
    }
}

void decodeRowAddress(int row) {
    if (CHECK_BIT(row, 0)!=0 ) {
        digitalWrite(addr0, HIGH);
    } else {
        digitalWrite(addr0, LOW);
    }
    if (CHECK_BIT(row, 1)!=0 ) {
        digitalWrite(addr1, HIGH);
    } else {
        digitalWrite(addr1, LOW);
    }
    if (CHECK_BIT(row, 2)!=0 ) {
        digitalWrite(addr2, HIGH);
    } else {
        digitalWrite(addr2, LOW);
    }
    if (CHECK_BIT(row, 3)!=0 ) {
        digitalWrite(addr3, HIGH);
    } else {
        digitalWrite(addr3, LOW);
    }
}

void push() {
    int row;
    for (row = 0; row < rows/2; row++) {
        sendSerialString(currentFrame[0][row], currentFrame[1][row], currentFrame[0][row + rows / 2], currentFrame[1][row + rows / 2]);
        digitalWrite(output, HIGH);
        digitalWrite(store, LOW);
        decodeRowAddress(row);
        digitalWrite(store, HIGH);
        digitalWrite(output, LOW);
    }
}

void init(int r, int c) {

    rows = r;
    cols = c;

    printf("\nStarting C RaspPi LightBoard....\n");
    printf("%dx%d\n", rows, cols);

    pushTestPattern();

    wiringPiSetup() ;

    pinMode(clockPin, OUTPUT);
    pinMode(store, OUTPUT);
    pinMode(output, OUTPUT);
    pinMode(data1R, OUTPUT);
    pinMode(data2R, OUTPUT);
    pinMode(data1G, OUTPUT);
    pinMode(data1R, OUTPUT);
    pinMode(addr0, OUTPUT);
    pinMode(addr1, OUTPUT);
    pinMode(addr2, OUTPUT);
    pinMode(addr3, OUTPUT);

    printf("\nBoard Running\n");

    for ( ;; ) {
        push();
    }

}

int main (void) {

    printf("Hello\n");

    init(32, 192);

    return 0;

}

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_init
  (JNIEnv *env, jobject o, jint r, jint c) {
    init(r, c);
  }

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_update
  (JNIEnv *env, jobject o, jintArray arr) {
    int i = 0;
    jsize rowLen = (*env)->GetArrayLength(env, arr);
    jint *inputRow = (*env)->GetIntArrayElements(env, arr, 0);
//    printf(inputRow);
//    for (i=0; i<rowLen; i++) {
//        jsize colLen = (*env)->GetArrayLength(env, inputRow[i]);
//        jint *inputCol = (*env)->GetIntArrayElements(env, inputRow[i], 0);
//        int j = 0;
//        for (j=0, j<colLen; j++ ) {
////            jsize colLen = (*env)->GetArrayLength(env, inputRow[i]);
////            jint *inputCol = (*env)->GetIntArrayElements(env, inputRow[i], 0);
//            printf("#");
//        }
//        printf("-\n");
//    }
    (*env)->ReleaseIntArrayElements(env, arr, inputRow, 0);
  }

JNIEXPORT jobject JNICALL Java_net_amarantha_lightboard_board_CLightBoard_getUpdateInterval
  (JNIEnv *env, jobject o) {
    return (jobject)-1;
  }

JNIEXPORT jint JNICALL Java_net_amarantha_lightboard_board_CLightBoard_getRows
  (JNIEnv *env, jobject o) {
    return rows;
  }

JNIEXPORT jint JNICALL Java_net_amarantha_lightboard_board_CLightBoard_getCols
  (JNIEnv *env, jobject o) {
    return cols;
  }

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_sleep
  (JNIEnv *env, jobject o) {
  }

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_CLightBoard_wake
  (JNIEnv *env, jobject o) {
  }

