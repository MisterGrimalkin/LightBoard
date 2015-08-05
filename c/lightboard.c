#include <stdio.h>
#include <wiringPi.h>
#include <jni.h>

#define CHECK_BIT(var,pos) ((var) & (1<<(pos)))

int rows = 32;
int cols = 192;

int clock = 0;
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
                currentFrame[0][r][c] = 1.0;
                currentFrame[1][r][c] = 1.0;
                currentFrame[2][r][c] = 1.0;
            } else {
                currentFrame[0][r][c] = 0.0;
                currentFrame[1][r][c] = 0.0;
                currentFrame[2][r][c] = 0.0;
            }
        }
    }
}

void dump(double data[3][32][192]) {
    int r;
    int c;
    for ( r=0; r<rows; r++ ) {
        for ( c=0; c<cols; c++ ) {
            currentFrame[0][r][c] = data[0][r][c];
            currentFrame[1][r][c] = data[0][r][c];
            currentFrame[2][r][c] = data[0][r][c];
        }
    }
}


void sendSerialString(double red1[], double green1[], double red2[], double green2[]) {
    int col;
    for (col = 0; col < cols ; col++) {
        digitalWrite(clock, LOW);
        digitalWrite(data1R, red1[col]<0.5 );
        digitalWrite(data1G, green1[col]<0.5 );
        digitalWrite(data2R, red2[col]<0.5 );
        digitalWrite(data2G, green2[col]<0.5 );
        digitalWrite(clock, HIGH);
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
        decodeRowAddress(row);
        digitalWrite(store, LOW);
        digitalWrite(store, HIGH);
        digitalWrite(output, LOW);
    }
}

void init() {

    printf("Starting C RaspPi LightBoard....\n");

    pushTestPattern();

    wiringPiSetup() ;

    pinMode(clock, OUTPUT);
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

    for ( ;; ) {
        push();
    }

}

int main (void) {

    init();

}

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_impl_CLightBoard_init
(JNIEnv * env, jobject obj) {
    init();
}

JNIEXPORT void JNICALL Java_net_amarantha_lightboard_board_impl_CLightBoard_update
(JNIEnv * env, jobject obj, jobjectArray arr) {
    printf("update");
}

