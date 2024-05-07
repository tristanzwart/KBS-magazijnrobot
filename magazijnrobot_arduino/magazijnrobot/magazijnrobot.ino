
int pwmPinLinksRechts = 11;
int directionPinLinksRechts = 13;


int VRX_PIN =  A2; // Arduino pin connected to VRX pin
// Arduino pin connected to VRY pin

int knop =4;

bool laasteknopstatus= false;


void setup() {
  
  pinMode(pwmPinLinksRechts, OUTPUT);
  pinMode(directionPinLinksRechts, OUTPUT);
  Serial.begin(9600);
  pinMode(2, INPUT);
  pinMode(knop, INPUT_PULLUP);



}

void loop() {
  uitlezenJoystick(); // leest joystick uit en beweeegt hiermee de robot
  //eenmaalknopindrukken();
  eenmaalknopindrukken();
  
}


void eenmaalknopindrukken(){
  bool knop1 = knopuitlezen();
  if(knop1 != laasteknopstatus && knop1 == HIGH ){    // zorgt ervoor dat de knop inet herhaalt (een signaal)
    knopingedrukt();
  }
  laasteknopstatus = knop1;
}




bool knopuitlezen(){
  bool knopWaarde = digitalRead(knop);
  delay(50);
  return knopWaarde;

}

void knopingedrukt(){
  Serial.print('g');      //versturen van een signaal 
}



void uitlezenJoystick(){
 
  int xValue = analogRead(VRX_PIN);

  if(xValue < 500){
    stop();
    naarRechts(255);      //het naar rechts bewegen van de joystick
    
    
  }
  else if(xValue> 540){
    stop();
    
    naarLinks(255);         //het naar links bewegen van de joystick
  }
  else{
    stop();
  }


  
  
} 



// bewegen robot met pwm meegeven
void stop(){
  
  analogWrite(pwmPinLinksRechts, 0);   

}

void naarLinks(int pwm){
  digitalWrite(directionPinLinksRechts, LOW);
  analogWrite(pwmPinLinksRechts, pwm);
}

void naarRechts(int pwm){
  digitalWrite(directionPinLinksRechts, HIGH);
  analogWrite(pwmPinLinksRechts, pwm);
}