bool voor = false;
int VRY_PIN = A3;

int pwmPinBovenOnder = 11;
int directionPinBovenOnder = 13;


void setup() {
  Serial.begin(9600); // Start de seriële communicatie op 9600 baud
  pinMode(3, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(pwmPinBovenOnder, OUTPUT);
  pinMode(directionPinBovenOnder, OUTPUT);

  
}
void uitlezenJoystick(){
  
  int yValue = analogRead(VRY_PIN);
  Serial.print("y= ");
  Serial.println(yValue);

  
  if (yValue < 490){
    stop();
    naarBoven(255);         // het naar boven bewegen van joystick

  }
  else if(yValue >520){
    stop();
    naarBeneden(110);       //het naar beneden bewegen van de joystick

  }
  else{
    stop();
  }


  
  
} 

void loop() {
  // if (Serial.available() > 0) {
  //   String received = Serial.readString();
  //   Serial.print("Ontvangen: ");
  //   Serial.println(received);
    uitlezenJoystick();
  
  //   while (Serial.available() > 0) {    // het ontvangen van het singnaal om naar voren te gaan
  //   char c = Serial.read();  // Lees één karakter
  //   if(c == 'g'){
  //     if(voor){
  //       digitalWrite(12,HIGH);    // checken of de zas vooruit staat
  //       voor = false ;
  //     }
  //     else{
  //       digitalWrite(12,LOW);
  //       voor= true;
  //     }
  //     Serial.println(c);
      
  //     analogWrite(3, 100);     // het aan en uit zetten van de z as 
  //     delay(3000);
  //     analogWrite(3, 0);
  //     c= 'h';
  //   }         // Druk het karakter af
  // }
  }
void stop(){
  analogWrite(pwmPinBovenOnder, 0);
  
}

void naarBoven(int pwm){
  digitalWrite(directionPinBovenOnder, LOW);
  analogWrite(pwmPinBovenOnder, pwm);
}

void naarBeneden(int pwm){
  digitalWrite(directionPinBovenOnder, HIGH);
  analogWrite(pwmPinBovenOnder, pwm);
}


