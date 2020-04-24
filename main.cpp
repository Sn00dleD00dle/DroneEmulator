#include <Arduino.h>
#include "WiFi.h" 
#include "AsyncUDP.h" //Importing necessary libraries to be able to excute our code 

int pinX = 36; // Assigns a pin number to the variable
int pinY = 39; // Assigns a pin number to the variable
int joystickButtonPin = 13; // Assigns pin number to the joystick button
const int PUSHBUTTON = 15; // Assigns pin number to the pushbutton
int lastPushValue = 0; // Assigns 0 to last push value.

unsigned long currentMillis; // Assigns a current value to the timer function
unsigned long previousMillis = 0; //will store the last time an interval was hit
int interval = 1000; // Assigns an interval value to be used in the timer funciton
int port = 7000; // Assigns a port to be broadcast to
int extremeMax = 4095; // Assigns maximum value for the joystick to register
int extremeMin = 0; // Assigns minimum value for the joystick to register

const char * ssid = "WaiFiFu"; //WiFi name. It varies depending on the user :)
const char * password = "2nerds&1cat"; //WiFi Password.. THIS IS SECRET DON'T LOOK

void printButton(int pushButtonValue); // Prepares method printbutton for later calling

AsyncUDP udp; // Creates AsyncUDP object

void setup() 
{   Serial.begin(9600); //The program... begins :D 
    WiFi.mode(WIFI_STA); //Connects to the WiFi
    WiFi.begin(ssid, password); // Checks the WiFi name and password 
    pinMode(pinX, INPUT); //right/left
    pinMode(pinY, INPUT); //up/down
    pinMode(joystickButtonPin, INPUT_PULLUP); //initializing digital pin 13 as an input with the internal pull up resistor enabled
    pinMode(PUSHBUTTON,INPUT); // Ini
}

void movement(String direction, String printing){ //Broadcasts the move function to the pxlserver
    udp.broadcastTo( direction.c_str(), port); //.c_str() is needed to parse the direction input into a char array, which is needed for the broadcastTo funciton to work.
    Serial.println(printing); //Prints the direction we're moving in the serial monitor
}

void movePixel(int horizontal, int vertical){
    if(horizontal == extremeMin){ //When the joystick is pushed completely to the left, the pixel will move left
       movement("moveleft", "moving left");
    } else if(horizontal == extremeMax){ //When the joystick is pushed completely to the right, the pixel will move right
        movement("moveright", "moving right"); 
    } else if(vertical == extremeMin){ //When the joystick is pushed completely up, the pixel will move up
       movement("moveup", "moving up");
    } else if(vertical == extremeMax){ //When the joystick is pushed completely down, the pixel will move down
        movement("movedown", "moving down");
    }
}

void loop(){
    currentMillis = millis();
    
    if(currentMillis - previousMillis > interval){ //By using an interval, the commands will only be read once every interval.
        if(digitalRead(joystickButtonPin) == LOW){ //LOW means pressed, HIGH means not pressed.
            udp.broadcastTo("initialize drone", port); // Initialise a drone by pressing the joystick
            Serial.println("Button pressed"); // Prints if the joystick button has been pressed
        }
        movePixel(analogRead(pinX), analogRead(pinY)); //This allows the movePixel() function to read the input from the joystick
        previousMillis = currentMillis; //This makes sure that the interval will repeat.
        int pushValue = digitalRead(PUSHBUTTON);
        if(lastPushValue != pushValue){ //check if we have a changing edge
            if(pushValue==LOW){
                Serial.println("Pushbutton pressed"); // Prints if the pushbutton has been pressed
                udp.broadcastTo("changecolor", port); // Tells the drone to change color
            } 
        }
        lastPushValue = pushValue; //set last push button to current value
    }
}
