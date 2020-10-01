#include <WiFi.h>

#include <FirebaseESP32.h>

#define FIREBASE_HOST "************************************/"
#define FIREBASE_AUTH "************************************************"
FirebaseData firebaseData;

int X;
int Y;
float TIME = 0;
float FREQUENCY = 0;
float WATER = 0;
float TOTAL = 0;
float LS = 0;
const int input = 15;

void setup()
{
  Serial.begin(9600);

  WiFi.disconnect();
  delay(3000);
  Serial.println("START");
  WiFi.begin("Tavares", "*********");
    while ((!(WiFi.status() == WL_CONNECTED))) {
      delay(300);
      Serial.print("..");
    }
    
  Serial.println("Connected");
  Serial.println("Your IP is");
  Serial.println((WiFi.localIP()));
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  delay(2000);
  pinMode(input, INPUT);
}


void loop()
{

  if ((Firebase.setFloat(firebaseData, "/Sensors/WaterVolume", TOTAL)) == true) {
    Serial.println("Sent");

  } else {
    Serial.println("Error");
  }



  X = pulseIn(input, HIGH);
  Y = pulseIn(input, LOW);
  TIME = X + Y;
  FREQUENCY = 1000000 / TIME;
  WATER = FREQUENCY / 5.65;
  LS = WATER / 60;
  if (FREQUENCY >= 0)
  {
    if (isinf(FREQUENCY))
    {
      Serial.print("VOL. :0.00 ");
      Serial.print("TOTAL:");
      Serial.print( TOTAL);
      Serial.print(" L");
      Serial.println();
    }
    else
    {
      TOTAL = TOTAL + LS;
      Serial.println(FREQUENCY);
      Serial.print("VOL.: ");
      Serial.print(WATER);
      Serial.print(" L/M");
      Serial.print("TOTAL:");
      Serial.print( TOTAL);
      Serial.print(" L");
      Serial.println();
    }
  }
  delay(1000);
}
