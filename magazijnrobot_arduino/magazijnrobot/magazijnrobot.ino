#define swrechts 12
#define swlinks 6


int pwmPinLinksRechts = 11;
int directionPinLinksRechts = 13;

int ENCA = 2;
int ENCB =7;

int pos = 0;
long prevT= 0;
float eprev = 0;
float  eintergral = 0;

int VRX_PIN =  A2; // Arduino pin connected to VRX pin
// Arduino pin connected to VRY pin

int knop =4;

bool laasteknopstatus= false;


void setup() {
  pinMode(swrechts, INPUT_PULLUP);
  pinMode(swlinks, INPUT_PULLUP);

  pinMode(ENCA,INPUT);
  pinMode(ENCB,INPUT_PULLUP);

  attachInterrupt(digitalPinToInterrupt(ENCA),leesEncoder,RISING);
  
  pinMode(pwmPinLinksRechts, OUTPUT);
  pinMode(directionPinLinksRechts, OUTPUT);
  Serial.begin(9600);
  pinMode(2, INPUT);
  pinMode(knop, INPUT_PULLUP);

  calibratie();

}

void loop() {
  // uitlezenJoystick(); // leest joystick uit en beweeegt hiermee de robot
  
  // eenmaalknopindrukken();

  checkEindebaan();
  communicatieHMI();
  
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

void naarbestemming(int target){
 float kp = 1; //PID algoritme variabelen
 float kd= 0.025;
 float ki= 0;

 long currentTime = micros();

 float deltaT = ((float)(currentTime - prevT))/ 1.0e6;
 prevT = currentTime;

 int e = pos- target;

 float dedt = (e-eprev)/ (deltaT);
 
 eintergral = eintergral = e*deltaT;

 float u = kp*e + kd*dedt + ki*eintergral;

 float pwr = fabs(u);
  if( pwr > 255 ){
    pwr = 255;
  }
  if(pwr < 90 && pwr >0){
    pwr= 90;
  }

  // motor direction
  int dir = 1;
  if(u<0){
    dir = 0;
  }

  // signal the motor
  setMotor(dir,pwr,pwmPinLinksRechts,directionPinLinksRechts);


  // store previous error
  eprev = e;

  Serial.print(target);
  Serial.print(" ");
  Serial.print(pos);
  Serial.println();


}

void leesEncoder(){
  int b = digitalRead(ENCB);
  //Serial.println(b);
  if(b>0){
    pos++;
    
  }
  else{
    pos--;

  }
}


void setMotor(int dir, int pwm, int pwmpin, int dirpin){
  digitalWrite(dirpin, dir);
  analogWrite(pwmpin, pwm);
  }

void calibratie(){
  //Beweeg de robot helemaal naar rechts totdat de schakelaar wordt ingedrukt
  while(!digitalRead(swrechts)){
    naarLinks(255);
  }
  pos= 0;

}

void checkEindebaan(){
  if(digitalRead(swrechts)){
    naarRechts(255);
  }

  if(digitalRead(swlinks)){
    naarLinks(255);
  }
}

void communicatieHMI() {
  if (Serial.available() > 0) {
    String data = Serial.readStringUntil('\n'); // Lees de binnenkomende data tot newline
//     serial.println() //om data terug te sturen naar java.
    naarbestemming(data.toInt());
  }
}


