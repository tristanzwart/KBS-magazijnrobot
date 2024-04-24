int pwmPinUpDown = 3;
int directionPinUpDown = 12;

int pwmPinLinksRechts = 11;
int directionPinLinksRechts = 13;

int VRX_PIN =  A2; // Arduino pin connected to VRX pin
int VRY_PIN = A3 ;// Arduino pin connected to VRY pin

int knop =4;

bool laasteknopstatus= false;


void setup() {
  pinMode(pwmPinUpDown, OUTPUT);
  pinMode(directionPinUpDown, OUTPUT);
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
  if(knop1 != laasteknopstatus && knop1 == HIGH ){
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
  Serial.print('g');
}



void uitlezenJoystick(){
  int xValue = analogRead(VRX_PIN);
  int yValue = analogRead(VRY_PIN);

  if(xValue < 500){
    stop();
    naarRechts(255);
    
    
  }
  else if(xValue> 540){
    stop();
    
    naarLinks(255);

  }
  else if (yValue < 490){
    stop();
    naarBoven(map(xValue,490, 0,  0 , 255));

  }
  else if(yValue >520){
    stop();
    naarBeneden(110);

  }
  else{
    stop();
  }


  
  
}
void stop(){
  analogWrite(pwmPinUpDown, 0);
  analogWrite(pwmPinLinksRechts, 0);

}

void naarBoven(int pwm){
  digitalWrite(directionPinUpDown, LOW);
  analogWrite(pwmPinUpDown, pwm);
}

void naarBeneden(int pwm){
  digitalWrite(directionPinUpDown, HIGH);
  analogWrite(pwmPinUpDown, pwm);
}

void naarLinks(int pwm){
  digitalWrite(directionPinLinksRechts, LOW);
  analogWrite(pwmPinLinksRechts, pwm);
}

void naarRechts(int pwm){
  digitalWrite(directionPinLinksRechts, HIGH);
  analogWrite(pwmPinLinksRechts, pwm);
}