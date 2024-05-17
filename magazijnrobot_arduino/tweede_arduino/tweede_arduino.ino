#define swboven 10
#define swonder 6

#define irsensor A5

bool voor = false;
int VRY_PIN = A2;

int pwmPinBovenOnder = 11;
int directionPinBovenOnder = 13;

int pwmPinVoorAchter = 3;
int directionPinVoorAchter = 12;

int ENCA = 2;
int ENCB = 7;

int pos = 0;
long prevT= 0;
float eprev = 0;
float  eintergral = 0;

int bestemming;

bool handmatigeBesturing = false;


bool handmatigelaasteknopstatus= false;
bool blockBesturingBoven = false;
bool blockBesturingBeneden = false;
int handKnop = 5;

void setup() {
  Serial.begin(9600); // Start de seriële communicatie op 9600 baud

  pinMode(swboven, INPUT_PULLUP);
  pinMode(swonder, INPUT_PULLUP);

  pinMode(ENCA,INPUT);
  pinMode(ENCB,INPUT_PULLUP);

  pinMode(pwmPinVoorAchter, OUTPUT);
  pinMode(directionPinVoorAchter, OUTPUT);

  pinMode(pwmPinBovenOnder, OUTPUT);
  pinMode(directionPinBovenOnder, OUTPUT);

  attachInterrupt(digitalPinToInterrupt(ENCA),leesEncoder,RISING);

  calibratie();

  
}
void uitlezenJoystick(){
  
  int yValue = analogRead(VRY_PIN);
  // Serial.print("y= ");
  // Serial.println(yValue);

  
  if (yValue < 410){
    stop();
    naarBoven(255);         // het naar boven bewegen van joystick

  }
  else if(yValue >440){
    stop();
    naarBeneden(110);       //het naar beneden bewegen van de joystick

  }
  else{
    stop();
  }


  
  
} 

void loop() {
  //naarbestemming(1500);
  handmatigeenmaalknopindrukken();
  checkEindebaan();
  if(handmatigeBesturing){
    uitlezenJoystick();
  }
  else{
    communicatieHMI();
  naarbestemming(bestemming);
  }


}
void handmatigeenmaalknopindrukken(){
  bool knop1 = knopuitlezen(handKnop);
  if(knop1 != handmatigelaasteknopstatus && knop1 == HIGH ){    // zorgt ervoor dat de knop inet herhaalt (een signaal)
    handmatigknopingedruk();
  }
  handmatigelaasteknopstatus = knop1;
}

bool knopuitlezen(int knoppin){
  bool knopWaarde = digitalRead(knoppin);
  delay(50);

  return knopWaarde;
}
void handmatigknopingedruk(){
  if(handmatigeBesturing){
    handmatigeBesturing= false;
  }
  else{
    handmatigeBesturing= true;
  }



}

void stop(){
  analogWrite(pwmPinBovenOnder, 0);
  analogWrite(pwmPinVoorAchter, 0);
}


void naarBoven(int pwm){
  if(!blockBesturingBoven || !handmatigeBesturing){
    digitalWrite(directionPinBovenOnder, LOW);
    analogWrite(pwmPinBovenOnder, pwm);
  }else{
    stop();
  }
}

void naarBeneden(int pwm){
  if(!blockBesturingBeneden || !handmatigeBesturing){
    digitalWrite(directionPinBovenOnder, HIGH);
    analogWrite(pwmPinBovenOnder, pwm);
  }else{
    stop();
  }
}

void naarVoren(int pwm){
  if(getVorkAfstand() <= 17){
    digitalWrite(directionPinVoorAchter, LOW);
    analogWrite(pwmPinVoorAchter, pwm);
  }else{
    analogWrite(pwmPinVoorAchter, 0);
  }
}

void naarAchteren(int pwm){
  if(getVorkAfstand() >= 7){
    digitalWrite(directionPinVoorAchter, HIGH);
    analogWrite(pwmPinVoorAchter, pwm);
  }else{
    analogWrite(pwmPinVoorAchter, 0);
  }
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
  if(pwr < 255 && pwr >0){
    pwr= 255;
  }

  // motor direction
  int dir = 1;
  if(u<0){
    dir = 0;
  }

  // signal the motor
  setMotor(dir,pwr,pwmPinBovenOnder,directionPinBovenOnder);


  // store previous error
  eprev = e;

  // Serial.print(target);
  // Serial.print(" ");
  // Serial.print(pos);
  // Serial.println();


}
void leesEncoder(){
  int b = digitalRead(ENCB);
  //Serial.println(b);
  if(b>0){
    pos--;
  }
  else{
    pos++;

  }
}


void setMotor(int dir, int pwm, int pwmpin, int dirpin){
  digitalWrite(dirpin, dir);
  analogWrite(pwmpin, pwm);
}

void calibratie(){
  //Beweeg de robot helemaal naar rechts totdat de schakelaar wordt ingedrukt
  while(!digitalRead(swonder)){
    naarBeneden(100);
    
  }
  stop();
  delay(1000);
  pos= -80;

  while(digitalRead(swonder)){
    naarBoven(255);
  }
  stop();

}

void checkEindebaan(){
  if(digitalRead(swonder)){
    if(!handmatigeBesturing){
      calibratie();
    }else{
      blockBesturingBeneden = true;
    }

  }else{
    blockBesturingBeneden = false;
  }

  if(digitalRead(swboven)){
     if(!handmatigeBesturing){
        calibratie();
     }else{
        blockBesturingBoven = true;
     }
  }else{
    blockBesturingBoven = false;
  }
}

void communicatieHMI() {
  if (Serial.available() > 0) {
    String data = Serial.readStringUntil('\n'); // Lees de binnenkomende data tot newline
//     serial.println() //om data terug te sturen naar java.
    bestemming = data.toInt();
  }
}


int getVorkAfstand(){
  // 5v
  float volts = analogRead(irsensor)*0.0048828125;  // value from sensor * (5/1024)
  int distance = 13*pow(volts, -1); // worked out from datasheet graph
  delay(1000); // slow down serial port


  return(distance);   // return de afstand in cm

}
