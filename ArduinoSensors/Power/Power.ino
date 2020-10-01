#include <WiFi.h>

#include <FirebaseESP32.h>

#define FIREBASE_HOST "*************************/"
#define FIREBASE_AUTH "**************************************"
FirebaseData firebaseData;

#include "EmonLib.h"                    // Include Emon Library
EnergyMonitor emon1;
int Valor = 0;
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
  Serial.println("IP:");
  Serial.println((WiFi.localIP()));
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

    
  emon1.current(34,27.5);             // Current: input pin, calibration.
}

void loop()
{
  if ((Firebase.setFloat(firebaseData, "/Sensors/Power", Valor)) == true) {
    Serial.println("Sent");

  } else {
    Serial.println("Error");
  }
//delay(1000);  
double Irms = emon1.calcIrms(1480);  // Calculate Irms
Serial.print ("Watts ");
Valor= Irms*230;                    //Calculate Power
Valor = Valor - 95;
if (Valor < 0) Valor=0;
Serial.println(Valor);              // Apparent power (W)

}
